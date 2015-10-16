package net.mgsx.ppp.test;

import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.PdDroidPartyLauncher;
import net.mgsx.ppp.theme.mono.MonochromeTheme;
import android.app.Activity;
import android.os.Bundle;


public class GUIThemeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.theme = new MonochromeTheme(0xff101933, 0xff202943, 0xff004ce6);
        config.guiKeepAspectRatio = true;

        PdDroidPartyLauncher.launch(this, "SVGTheme/gui-svg-test.pd", config);
    }
    



}
