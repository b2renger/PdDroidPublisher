package net.mgsx.ppp.net;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;

public class NetworkHelper 
{
	private static MulticastLock wifiMulticastLock;
	
	public static boolean aquireWifiMulticast(Context context)
	{
		boolean result = false;
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		if(wifiManager != null)
		{
			wifiMulticastLock = wifiManager.createMulticastLock("PdDroidPartyMulticastLock");
			wifiMulticastLock.acquire();
			result = wifiMulticastLock.isHeld();
		}
		return result;
	}
	
	public static void releaseWifiMulticast() 
	{
		if (wifiMulticastLock != null && wifiMulticastLock.isHeld())
		{
			wifiMulticastLock.release();
		}
	}
}
