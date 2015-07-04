package example.humatic.de;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;

public class USBListener extends Activity {
	
	/** Utility activity that does nothing but receive USB_DEVICE_ATTACHED events from the
	 * system and trigger a full MIDI interface query.<br>
	 * This is a workaround for the inconvenient way Android implements device attachment
	 * notification. It is not currently possible to set up notification listeners in code
	 * alone. Instead you need to explicitely have the system start some activity, that 
	 * needs to be declared in the application's AndroidManifest.xml file along with
	 * intent filters and metadata, see AndroidManifest.xml and res/xml/devices.xml.
	 * The sideeffect of this is that given your app is running while an interface is
	 * attached, Android will terminate and relaunch it, so it may be easier to not make use 
	 * of this (in fact it is outcommented in the manifest). nmj will detect USB interfaces 
	 * during normal launch without it. 
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{ 
			if (Build.VERSION.SDK_INT < 12) {
				/* No USB host mode before sdk 12 */
				finish();
				return;
			}   
			/* If we're not running, launch */
			Intent intent = new Intent(this, nmjSample.class);
		    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} catch (Exception e){ e.printStackTrace(); }
		
		try{
			/* Trigger an interface scan in nmj */
			PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent("de.humatic.nmj.USB_DEVICE_ATTACHED"), 0);
			pi.send();
		} catch (Exception ex){ex.printStackTrace();}
			
		
		finish();
	}
}
  