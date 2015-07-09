package cx.mccormick.pddroidparty.midi.nmj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;
import cx.mccormick.pddroidparty.midi.MidiDevice;
import cx.mccormick.pddroidparty.midi.MidiInput;
import cx.mccormick.pddroidparty.midi.MidiOutput;
import cx.mccormick.pddroidparty.view.PdPartyClockControl;
import de.humatic.nmj.NMJConfig;
import de.humatic.nmj.NMJSystemListener;
import de.humatic.nmj.NetworkMidiClient;
import de.humatic.nmj.NetworkMidiSystem;

public class NMJMidiDevice implements MidiDevice, NetworkMidiClient, NMJSystemListener
{
	protected NetworkMidiSystem nmjs;
	private Activity ctx;
	private List<MidiInput> inputs = new ArrayList<MidiInput>();
	private List<MidiOutput> outputs = new ArrayList<MidiOutput>();
	
	private boolean validatedIn = false;
	private boolean validatedOut = false;
	
	@Override
	public void init(Context ctx) 
	{
		this.ctx = (Activity)ctx; // XXX
		
		try {
			nmjs = NetworkMidiSystem.get(ctx);
		} catch (Exception e) {
			throw new Error(e);
		}
		
		/** 
		 * Set a port base that local rtp sessions will have their ports calculated from (long as no
		 * preference has been "hard-set" with setLocalPort(..))
		 * This is to avoid collisions when multiple apps deploying the library on a per app basis
		 * run at the same time. Before publishing anything please request a portbase for your project!
		 */
		
		NMJConfig.setBasePort(NMJConfig.RTP, 6700);
	}

	@Override
	public List<MidiInput> getInputs() 
	{
		if(!validatedIn)
		{
			List<MidiInput> inputs = new ArrayList<MidiInput>();
			for(int i=0 ; i<NMJConfig.getNumChannels() ; i++)
			{
				int io = NMJConfig.getIO(i);
				String name = NMJConfig.getName(i);
				// input
				if(io == 0 || io == -1)
				{
					inputs.add(new NMJMidiInput(this, i, name));
				}
			}
			mergeIn(this.inputs, inputs);
			validatedIn = true;
		}
		return inputs;
	}

	private static SparseArray<MidiInput> asMapIn(List<MidiInput> lst)
	{
		SparseArray<MidiInput> map = new SparseArray<MidiInput>();
		for(MidiInput port : lst)
		{
			map.put(((NMJMidiInput)port).port, port);
		}
		return map;
	}
	
	private static void mergeIn(List<MidiInput> dst, List<MidiInput> src) 
	{
		SparseArray<MidiInput> dstMap = asMapIn(dst);
		SparseArray<MidiInput> srcMap = asMapIn(src);
		for(MidiInput port : src)
		{
			NMJMidiInput nmjport = (NMJMidiInput)port;
			NMJMidiInput d = (NMJMidiInput)dstMap.get(nmjport.port);
			if(d != null)
			{
				d.port = nmjport.port;
				d.name = nmjport.name;
			}
			else
			{
				dst.add(port);
			}
		}
		for(Iterator<MidiInput> i = dst.iterator() ; i.hasNext() ; )
		{
			if(srcMap.get(((NMJMidiInput)i.next()).port) == null)
			{
				i.remove();
			}
		}
	}
	private static SparseArray<MidiOutput> asMapOut(List<MidiOutput> lst)
	{
		SparseArray<MidiOutput> map = new SparseArray<MidiOutput>();
		for(MidiOutput port : lst)
		{
			map.put(((NMJMidiOutput)port).port, port);
		}
		return map;
	}
	
	private static void mergeOut(List<MidiOutput> dst, List<MidiOutput> src) 
	{
		SparseArray<MidiOutput> dstMap = asMapOut(dst);
		SparseArray<MidiOutput> srcMap = asMapOut(src);
		for(MidiOutput port : src)
		{
			NMJMidiOutput nmjport = (NMJMidiOutput)port;
			NMJMidiOutput d = (NMJMidiOutput)dstMap.get(nmjport.port);
			if(d != null)
			{
				d.port = nmjport.port;
				d.name = nmjport.name;
			}
			else
			{
				dst.add(port);
			}
		}
		for(Iterator<MidiOutput> i = dst.iterator() ; i.hasNext() ; )
		{
			if(srcMap.get(((NMJMidiOutput)i.next()).port) == null)
			{
				i.remove();
			}
		}
	}

	@Override
	public List<MidiOutput> getOutputs() {
		if(!validatedOut)
		{
			List<MidiOutput> outputs = new ArrayList<MidiOutput>();
			for(int i=0 ; i<NMJConfig.getNumChannels() ; i++)
			{
				int io = NMJConfig.getIO(i);
				String name = NMJConfig.getName(i);
				// output
				if(io == 1 || io == -1)
				{
					outputs.add(new NMJMidiOutput(this, i, name));
				}
			}
			mergeOut(this.outputs, outputs);
			validatedOut = true;
		}
		return outputs;
	}

	@Override
	public String getName() 
	{
		return "NMJ";
	}
	
	@Override
	public void systemChanged(int channel, int property, int value) {

		Log.i("nmjSample", " System changed "+channel+" "+property+" "+value);

		if (property == NMJConfig.CH_STATE && value == NMJConfig.RTPA_CH_DISCOVERED) {
			/**
			 * Given multicast works on your device then DNS might 
			 * uncover more RTP and MWS channels and call this for notification. 
			 * Newly found USB host & ADB channels will also be announced here.
			 * Time to update the spinner, which is not done in this sample 
			 * (added channels can be seen in the control panel activity, though). 
			 */
		}

	} 

	@Override
	public void systemError(int channel, int err, String description) {
		
		/** 
		 * Make sure to override this method to be informed about eventual errors on
		 * lower levels. Much of what the library does runs on port specific threads that
		 * nmj can not throw exception that would be catchable in application code 
		 * from.    
		 */
		Log.e("nmjSample", " Error on ch "+channel+", code: "+err+", desc: "+description);
		
	}

	public void openConfigPanel() 
	{
		validatedIn = false;
		validatedOut = false;
		
		final Intent si = new Intent(ctx, de.humatic.nmj.NMJConfigDialog.class);
		ctx.startActivityForResult(si, PdPartyClockControl.SETUP_ACTIVITY_CODE);
	}


}
