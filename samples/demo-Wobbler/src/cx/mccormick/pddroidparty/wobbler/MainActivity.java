package cx.mccormick.pddroidparty.wobbler;

import android.app.Activity;
import android.os.Bundle;
import cx.mccormick.pddroidparty.PdDroidPartyConfig;
import cx.mccormick.pddroidparty.PdDroidPartyLauncher;
import cx.mccormick.pddroidparty.theme.mono.MonochromeTheme;
import cx.mccormick.pddroidparty.widget.custom.PopupTaplist;


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
        config.theme = new MonochromeTheme(0xff101933, 0xff202943, 0xff004ce6);
        
        config.presetsPaths.add("Wobbler/savefiles");
        
        PdDroidPartyLauncher.launch(this, "Wobbler/wobbler.pd", config);
    }
    



}
