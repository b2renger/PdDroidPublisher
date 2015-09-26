package cx.mccormick.pddroidparty.wobbler;

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
        config.theme = new MonochromeTheme(MonochromeTheme.BLUE, true);
        
        PdDroidPartyLauncher.launch(this, "Wobbler/wobbler.pd", config);
    }
    



}
