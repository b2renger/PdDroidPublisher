package cx.mccormick.pddroidparty.slicer;

import android.app.Activity;
import android.os.Bundle;
import cx.mccormick.pddroidparty.PdDroidPartyConfig;
import cx.mccormick.pddroidparty.PdDroidPartyLauncher;
import cx.mccormick.pddroidparty.theme.mono.MonochromeTheme;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.midiClockMaxBPM = 480;
        config.theme = new MonochromeTheme(300, false);
        config.objectOverrides.put("Bend", PitchBend.class);
        PdDroidPartyLauncher.launch(this, "Slicer/slicer2.pd", config);
    }
    



}
