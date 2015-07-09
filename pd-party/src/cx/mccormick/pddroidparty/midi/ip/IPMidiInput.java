package cx.mccormick.pddroidparty.midi.ip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

import cx.mccormick.pddroidparty.midi.MidiInput;
import cx.mccormick.pddroidparty.midi.MidiListener;

public class IPMidiInput implements MidiInput
{
	protected String ip;
	protected int port;
	protected boolean multicast;
	private MidiListener listener;
	volatile private boolean shouldRun;
	
	private static final int NUM_BUFFERS = 3;
	private byte [][] buffers;
	
	@Override
	public String getName() 
	{
		return (multicast ? "Multicast" : "Unicast") + " " + (ip == null ? "*" : ip) + ":" + String.valueOf(port);
	}

	@Override
	public void open(MidiListener listener) 
	{
		shouldRun = true;
		
		this.listener = listener;
		
		// create buffers
		buffers = new byte [NUM_BUFFERS][];
		for(int i=0 ; i<NUM_BUFFERS ; i++)
		{
			buffers[i] = new byte[i+1];
		}

		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					recvSocket();
				} catch (IOException e) {
					throw new Error(e);
				}
			}

		});
        th.setPriority(Thread.MAX_PRIORITY);
        th.start();
	}

	@Override
	public void close() {
		shouldRun = false;
	}
	
	private void recvSocket() throws IOException
	{
		DatagramSocket socket;
		
		if(multicast)
		{
			socket = new MulticastSocket(port);
			((MulticastSocket)socket).joinGroup(InetAddress.getByName(ip));
		}
		else
		{
			socket = new DatagramSocket(port);
		}
		try
		{
			socket.setSoTimeout(1000);
			
			byte[] receiveData = new byte[1024];
			
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			
			while(shouldRun)
			{
				try
				{
					socket.receive(receivePacket);
					recvMessage(receivePacket);
				}
				catch(SocketTimeoutException e)
				{
					// ignore
				}
			}
		}
		finally
		{
			if(socket instanceof MulticastSocket)
			{
				((MulticastSocket)socket).leaveGroup(InetAddress.getByName(ip));
			}
			socket.close();
		}

	}

	private void recvMessage(DatagramPacket pack)
	{
		byte [] buffer = pack.getData();
		int len = pack.getLength();
		byte [] data;
		if(len - 1 < NUM_BUFFERS)
		{
			data = buffers[len - 1];
		}
		else
		{
			data = new byte[len];
		}
		for(int i=0 ; i<len ;i++)
		{
			data[i] = buffer[i];
		}
		listener.onMidiMessage(data);

	}


}
