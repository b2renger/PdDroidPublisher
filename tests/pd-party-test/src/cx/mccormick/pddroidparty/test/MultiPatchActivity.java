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
        config.patches.put("Main", "MultiPatch/main.pd");
        config.patches.put("Control", "MultiPatch/control.pd");
        PdDroidPartyLauncher.launch(this, config);
    }
    



}
