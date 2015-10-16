package net.mgsx.ppp.test;

import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.PdDroidPartyLauncher;
import android.app.Activity;
import android.os.Bundle;


public class PersistTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.guiKeepAspectRatio = true;
        config.presetsPaths.add("PersistTest/presets");
        PdDroidPartyLauncher.launch(this, "PersistTest/PersistTestAndroid.pd", config);
    }
    



}
