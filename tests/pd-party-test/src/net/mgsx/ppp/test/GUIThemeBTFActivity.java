package net.mgsx.ppp.test;

import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.PdDroidPartyLauncher;
import net.mgsx.ppp.theme.mono.MonochromeTheme;
import android.app.Activity;
import android.os.Bundle;


public class GUIThemeBTFActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.theme = new MonochromeTheme(0xff191919, 0xff4B4B4B, 0xffB4B4B4);
        config.guiKeepAspectRatio = true;

        PdDroidPartyLauncher.launch(this, "SVGTheme-BTF/gui-svg-test.pd", config);
    }
    



}
