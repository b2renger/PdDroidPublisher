package net.mgsx.ppp.test;

import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.PdDroidPartyLauncher;
import android.app.Activity;
import android.os.Bundle;


public class GUIWidgetActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.guiKeepAspectRatio = true;
        PdDroidPartyLauncher.launch(this, "GUITest/gui-test.pd", config);

//      PdDroidPartyLauncher.launch(this, "GUITest/gui-high.pd", config);
//      PdDroidPartyLauncher.launch(this, "GUITest/gui-wide.pd", config);
//      PdDroidPartyLauncher.launch(this, "GUITest/gui-ultra-high.pd", config);
//      PdDroidPartyLauncher.launch(this, "GUITest/gui-ultra-wide.pd", config);
    }
    



}
