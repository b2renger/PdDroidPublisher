package cx.mccormick.pddroidparty.chipsequencer;

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
        config.guiKeepAspectRatio = true;
        config.theme = new MonochromeTheme(MonochromeTheme.ORANGE, true);

      
        
        PdDroidPartyLauncher.launch(this, "Chip_sequencer/chip_sequencer.pd", config);
    }
    



}
