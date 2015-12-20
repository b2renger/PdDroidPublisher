package net.mgsx.ppp.samples.arpeggio;

import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.PdDroidPartyLauncher;
import net.mgsx.ppp.theme.mono.MonochromeTheme;
import net.mgsx.ppp.widget.core.Slider;
import net.mgsx.ppp.widget.custom.RibbonSlider;
import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.objectOverrides.put("led", TrackLed.class);
        config.typeOverrides.put(Slider.class, RibbonSlider.class);
        config.theme = new MonochromeTheme(180, true);
        
        config.presetsPaths.add("Arpeggio/presets");
        
        PdDroidPartyLauncher.launch(this, "Arpeggio/arpeggio.pd", config);
    }
    



}
