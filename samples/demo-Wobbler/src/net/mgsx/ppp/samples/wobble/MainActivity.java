package net.mgsx.ppp.samples.wobble;

import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.PdDroidPartyLauncher;
import net.mgsx.ppp.theme.mono.MonochromeTheme;
import net.mgsx.ppp.widget.core.Slider;
import net.mgsx.ppp.widget.custom.PopupTaplist;
import net.mgsx.ppp.widget.custom.RibbonSlider;
import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.factory = WobblerFactory.class;
        config.objectOverrides.put("TrackLeds", TrackLed.class);
        config.objectOverrides.put("WobbleBinary", WobbleSelector.class);
        config.objectOverrides.put("WobbleTernary", WobbleSelector.class);
        config.objectOverrides.put("TuneTaplist", PopupTaplist.class);
        config.typeOverrides.put(Slider.class, RibbonSlider.class);
        config.theme = new MonochromeTheme(0xff101933, 0xff202943, 0xff004ce6);
        
        config.presetsPaths.add("Wobbler/savefiles");
        
        PdDroidPartyLauncher.launch(this, "Wobbler/wobbler.pd", config);
    }
    



}
