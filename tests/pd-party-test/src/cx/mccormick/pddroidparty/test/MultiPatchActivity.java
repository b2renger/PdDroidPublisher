package cx.mccormick.pddroidparty.test;

import android.app.Activity;
import android.os.Bundle;
import cx.mccormick.pddroidparty.PdDroidPartyConfig;
import cx.mccormick.pddroidparty.PdDroidPartyLauncher;


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
