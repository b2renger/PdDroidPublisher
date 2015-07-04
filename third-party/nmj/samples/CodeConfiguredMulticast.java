import de.humatic.nmj.*;

public class CodeConfiguredMulticast implements NetworkMidiListener {

	/**
	Simple example for configuring nmj channels in code. This configures two "RAW" channels to join
	the same multicast group, opens the ports, sends a MIDI message from one to the other and exits.
	The sample uses nmj's public api, but the initial configuration would be done just as shown here
	if one would replace everything from "NetworkMidiSystem nwms.." onwards with javax.sound. midi based code.
	The configuration will be stored and recalled on the next run, so there is no need to do it over and over
	again unless something is to be changed.
	**/

	private boolean showDialog;

	public CodeConfiguredMulticast(){

		try{

			NMJConfig.setMode(0, NMJConfig.RAW);
			NMJConfig.setMode(1, NMJConfig.RAW);

			NMJConfig.setIO(0, NMJConfig.IN);
			NMJConfig.setIO(1, NMJConfig.OUT);

			NMJConfig.setIP(0, "225.0.0.37");
			NMJConfig.setIP(1, "225.0.0.37");

			NMJConfig.setPort(0, 21928);
			NMJConfig.setPort(1, 21928);

			NetworkMidiSystem nwms = NetworkMidiSystem.get();

			NetworkMidiInput in = nwms.openInput(0, this);

			NetworkMidiOutput out = nwms.openOutput(1, this);

			out.sendMidi(new byte[]{(byte)0x80, 7, 0});

			if (showDialog) NMJConfig.showConfigDialog(null, null);

			in.close(this);

			out.close(this);

		} catch (Exception x){ x.printStackTrace(); }

		System.exit(0);

	}

	public void midiReceived(int channel, int ssrc, byte[] data, long timeStamp){

		System.out.println("midi received on: ");

		System.out.println("nmj channel: " + channel);

		System.out.println("timestamp: "+new java.util.Date(timeStamp));

		System.out.print("data: ");

		for (int i = 0; i < data.length; i++) {

			System.out.print(data[i]+" ");

		}

		System.out.println("");

	}

	public static void main(String[] args){

		new CodeConfiguredMulticast();

	}

}