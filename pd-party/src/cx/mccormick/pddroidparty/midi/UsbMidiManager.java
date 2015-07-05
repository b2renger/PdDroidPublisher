package cx.mccormick.pddroidparty.midi;

import java.util.List;

import org.puredata.android.midi.MidiToPdAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.noisepages.nettoyeur.usb.ConnectionFailedException;
import com.noisepages.nettoyeur.usb.DeviceNotConnectedException;
import com.noisepages.nettoyeur.usb.InterfaceNotAvailableException;
import com.noisepages.nettoyeur.usb.UsbBroadcastHandler;
import com.noisepages.nettoyeur.usb.midi.UsbMidiDevice;
import com.noisepages.nettoyeur.usb.midi.UsbMidiDevice.UsbMidiInput;
import com.noisepages.nettoyeur.usb.midi.UsbMidiDevice.UsbMidiOutput;
import com.noisepages.nettoyeur.usb.midi.util.UsbMidiInputSelector;
import com.noisepages.nettoyeur.usb.midi.util.UsbMidiOutputSelector;
import com.noisepages.nettoyeur.usb.util.AsyncDeviceInfoLookup;

public class UsbMidiManager 
{
	private UsbMidiDevice midiDevice = null;
	private MidiToPdAdapter receiver = new MidiToPdAdapter();
	private Activity activity;
	private UsbMidiHandler handler;
	
	public UsbMidiManager(Activity activity, UsbMidiHandler handler)
	{
		this.activity = activity;
		this.handler = handler;
	}
	
	public boolean isUsbAvailable()
	{
		try {
			UsbManager manager = (UsbManager) activity.getSystemService(Context.USB_SERVICE);
			if(manager != null)
			{
				return true;
			}
		} catch(NoClassDefFoundError e) {
		}
		return false;
	}
	
	public void create()
	{
		UsbMidiDevice.installBroadcastHandler(activity, new UsbBroadcastHandler() {
			@Override
			public void onPermissionGranted(UsbDevice device) {
				if (midiDevice == null || !midiDevice.matches(device)) return;
				try {
					midiDevice.open(activity);
				} catch (ConnectionFailedException e) {
					handler.onStatusMessage("USB connection failed");
					midiDevice = null;
					return;
				}
				final UsbMidiOutputSelector outputSelector = new UsbMidiOutputSelector(midiDevice) {

					@Override
					protected void onOutputSelected(UsbMidiOutput output, UsbMidiDevice device, int iface, int index) {
						handler.onStatusMessage("Output selection: Interface " + iface + ", Output " + index);
						try {
							output.getMidiOut();
						} catch (DeviceNotConnectedException e) {
							handler.onStatusMessage("MIDI device has been disconnected");
						} catch (InterfaceNotAvailableException e) {
							handler.onStatusMessage("MIDI interface is unavailable");
						}
					}

					@Override
					protected void onNoSelection(UsbMidiDevice device) {
						handler.onStatusMessage("No output selected");
					}
				};
				new UsbMidiInputSelector(midiDevice) {

					@Override
					protected void onInputSelected(UsbMidiInput input, UsbMidiDevice device, int iface,
							int index) {
						handler.onStatusMessage("Input selection: Interface " + iface + ", Input " + index);
						input.setReceiver(receiver);
						try {
							input.start();
						} catch (DeviceNotConnectedException e) {
							handler.onStatusMessage("MIDI device has been disconnected");
							return;
						} catch (InterfaceNotAvailableException e) {
							handler.onStatusMessage("MIDI interface is unavailable");
							return;
						}
						outputSelector.show(getFragmentManager(), null);
					}

					@Override
					protected void onNoSelection(UsbMidiDevice device) {
						handler.onStatusMessage("No input selected");
						outputSelector.show(getFragmentManager(), null);
					}
				}.show(activity.getFragmentManager(), null);
			}

			@Override
			public void onPermissionDenied(UsbDevice device) {
				if (midiDevice == null || !midiDevice.matches(device)) return;
				handler.onStatusMessage("Permission denied for device " + midiDevice.getCurrentDeviceInfo());
				midiDevice = null;
			}

			@Override
			public void onDeviceDetached(UsbDevice device) {
				if (midiDevice == null || !midiDevice.matches(device)) return;
				midiDevice.close();
				midiDevice = null;
				handler.onStatusMessage("MIDI device disconnected");
			}
		});
	}

	public void destroy() 
	{
		closeDevice();
		
		UsbMidiDevice.uninstallBroadcastHandler(activity);
	}
	
	public void chooseMidiDevice() 
	{
		// set a progress dialog running
		final ProgressDialog progress = new ProgressDialog(activity);
		progress.setMessage("Waiting for USB midi");
		progress.setCancelable(false);
		progress.setIndeterminate(true);
		progress.show();
		final List<UsbMidiDevice> devices = UsbMidiDevice.getMidiDevices(activity);
		new AsyncDeviceInfoLookup() {
			@Override
			protected void onLookupComplete() {
				// ok we are done
				progress.dismiss();
				if (!devices.isEmpty()) {
					String devicenames[] = new String[devices.size()];
					// loop through the devices and get their names
					for (int i = 0; i < devices.size(); ++i) {
						devicenames[i] = devices.get(i).getCurrentDeviceInfo().toString();
					}
					// construct the alert we will show
					new AlertDialog.Builder(activity)
					// make the alert and show it
					.setTitle("Midi device")
					.setItems(devicenames, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// The 'which' argument contains the index position
							// of the selected item
							midiDevice = devices.get(which);
							midiDevice.requestPermission(activity);
						}
					})
					.show();
				} else {
					handler.onStatusMessage("No midi devices found.");
				}
			}
		}.execute(devices.toArray(new UsbMidiDevice[devices.size()]));
	}

	public void closeDevice() 
	{
		if(deviceOpened())
		{
			midiDevice.close();
			midiDevice = null;
			handler.onStatusMessage("USB MIDI connection closed");
		}
	}

	public boolean deviceOpened() 
	{
		return midiDevice != null;
	}

}
