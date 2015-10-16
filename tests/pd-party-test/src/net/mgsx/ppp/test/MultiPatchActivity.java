package net.mgsx.ppp.test;

import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.PdDroidPartyLauncher;
import android.app.Activity;
import android.os.Bundle;


public class MultiPatchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.guiKeepAspectRatio = true;
        config.presetsPaths.add("MultiPatch/presets");
        config.guiPatches.put("Main", "MultiPatch/main.pd");
        config.guiPatches.put("Control", "MultiPatch/control.pd");
        config.corePatches.add("MultiPatch/core.pd");
        PdDroidPartyLauncher.launch(this, config);
    }
    



}
