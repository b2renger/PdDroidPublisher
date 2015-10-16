package net.mgsx.ppp.test;

import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.PdDroidPartyLauncher;
import net.mgsx.ppp.theme.mono.MonochromeTheme;
import android.app.Activity;
import android.os.Bundle;


public class GUIThemeMonoLightActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.guiKeepAspectRatio = true;
        config.theme = new MonochromeTheme(MonochromeTheme.YELLOW, false);
        
        PdDroidPartyLauncher.launch(this, "GUITest/gui-test.pd", config);
    }
    



}
