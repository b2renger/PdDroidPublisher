package net.mgsx.ppp.test;

import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.PdDroidPartyLauncher;
import net.mgsx.ppp.widget.custom.PopupTaplist;
import net.mgsx.ppp.widget.custom.RibbonSlider;
import net.mgsx.ppp.widget.custom.SimpleBang;
import net.mgsx.ppp.widget.custom.SwitchToggle;
import net.mgsx.ppp.widget.custom.FilledToggle;
import net.mgsx.ppp.widget.custom.SimpleRadio;
import android.app.Activity;
import android.os.Bundle;


public class GuiOriginalVsCustom extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.guiKeepAspectRatio = true;
        
        config.objectOverrides.put("A", SimpleBang.class);
        config.objectOverrides.put("Switch", SwitchToggle.class);
        config.objectOverrides.put("Filled", FilledToggle.class);
        config.objectOverrides.put("custom-taplist", PopupTaplist.class);
        config.objectOverrides.put("ribbon", RibbonSlider.class);
        config.objectOverrides.put("radio", SimpleRadio.class);
        
        
        
        PdDroidPartyLauncher.launch(this, "OriginalVsCustom-Factory/custom.pd", config);
    }
    



}
