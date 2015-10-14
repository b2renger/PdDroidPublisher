package cx.mccormick.pddroidparty.test;

import android.app.Activity;
import android.os.Bundle;
import cx.mccormick.pddroidparty.PdDroidPartyConfig;
import cx.mccormick.pddroidparty.PdDroidPartyLauncher;


public class PersistAbsTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.guiKeepAspectRatio = true;
        config.presetsPaths.add("abs-abs-Test/presets_abs");
        config.presetsPaths.add("abs-abs-Test/presets_top");
        
        config.patches.put("Test",  "abs-abs-Test/abs-abs.pd");
        
        
        PdDroidPartyLauncher.launch(this, config);
    }
    



}
