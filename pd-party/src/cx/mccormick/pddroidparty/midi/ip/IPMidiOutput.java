package cx.mccormick.pddroidparty.midi.ip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import cx.mccormick.pddroidparty.midi.MidiCode;
import cx.mccormick.pddroidparty.midi.MidiOutput;

public class IPMidiOutput implements MidiOutput
{
	protected String ip;
	protected int port;
	protected boolean multicast;
	
	private DatagramSocket clientSocket;
	private DatagramPacket tickPacket;
	private InetAddress IPAddress;
	
	@Override
	public String getName() 
	{
		return (multicast ? "Multicast" : "Unicast") + " " + ip + ":" + String.valueOf(port);
	}

	@Override
	public void open() {
		try
		{
			IPAddress = InetAddress.getByName(ip);
			if(multicast)
			{
				clientSocket = new MulticastSocket();
			}
			else
			{
				clientSocket = new DatagramSocket();
			}
			tickPacket = new DatagramPacket(new byte[]{(byte)0xF8}, 1, IPAddress, port);
		}
		catch(IOException e){
			throw new Error(e);
		}

	}

	@Override
	public void close() 
	{
		if(clientSocket != null)
		{
			clientSocket.close();
			clientSocket = null;
		}
	}

	@Override
	public void send(byte[] message) 
	{
		DatagramPacket packet;
		if(message.length == 1 && message[0] == (byte)MidiCode.MIDI_REALTIME_CLOCK_TICK)
		{
			packet = tickPacket;
		}
		else
		{
			packet = new DatagramPacket(message, message.length, IPAddress, port);
		}
		try {
			clientSocket.send(packet);
		} catch (IOException e) {
			throw new Error(e);
		}
	}

}
