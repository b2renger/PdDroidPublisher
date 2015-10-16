package net.mgsx.ppp.samples.glitcher;

import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.PdDroidPartyLauncher;
import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        
        PdDroidPartyLauncher.launch(this, "Glitcher/Glitcher.pd", config);
    }
    



}
