package cx.mccormick.pddroidparty.test;

import android.app.Activity;
import android.os.Bundle;
import cx.mccormick.pddroidparty.PdDroidPartyConfig;
import cx.mccormick.pddroidparty.PdDroidPartyLauncher;


public class GUIThemeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        
        config.guiKeepAspectRatio = true;

        config.foregroundColor = 0xff004ce6;
        config.backgroundColor = 0xff101933;
        
        PdDroidPartyLauncher.launch(this, "SVGTheme/gui-svg-test.pd", config);
    }
    



}
