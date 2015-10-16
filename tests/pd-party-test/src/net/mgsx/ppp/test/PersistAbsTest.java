package net.mgsx.ppp.test;

import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.PdDroidPartyLauncher;
import android.app.Activity;
import android.os.Bundle;


public class PersistAbsTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.guiKeepAspectRatio = true;
        config.presetsPaths.add("abs-abs-Test/presets_abs");
        config.presetsPaths.add("abs-abs-Test/presets_top");
        
        config.guiPatches.put("Test",  "abs-abs-Test/abs-abs.pd");
        
        
        PdDroidPartyLauncher.launch(this, config);
    }
    



}
