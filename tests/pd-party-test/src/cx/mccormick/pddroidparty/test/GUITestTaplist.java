package cx.mccormick.pddroidparty.test;

import android.app.Activity;
import android.os.Bundle;
import cx.mccormick.pddroidparty.PdDroidPartyConfig;
import cx.mccormick.pddroidparty.PdDroidPartyLauncher;
import cx.mccormick.pddroidparty.theme.mono.MonochromeTheme;


public class GUITestTaplist extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.theme = new MonochromeTheme(0xff101933, 0xff202943, 0xff004ce6);
        config.guiKeepAspectRatio = true;

        PdDroidPartyLauncher.launch(this, "GUITestTaplist/GUITestTaplist.pd", config);
    }
    



}
