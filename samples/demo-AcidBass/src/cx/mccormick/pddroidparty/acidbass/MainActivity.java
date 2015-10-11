package cx.mccormick.pddroidparty.acidbass;

import android.app.Activity;
import android.os.Bundle;
import cx.mccormick.pddroidparty.PdDroidPartyConfig;
import cx.mccormick.pddroidparty.PdDroidPartyLauncher;
import cx.mccormick.pddroidparty.theme.mono.MonochromeTheme;
import cx.mccormick.pddroidparty.widget.abs.Taplist;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
       
        config.midiClockMaxBPM = 480;
        config.guiKeepAspectRatio = true;
        config.theme = new MonochromeTheme(MonochromeTheme.RED, true);
        config.typeOverrides.put(Taplist.class, CustomTaplist.class);
        
        config.patches.put("Sequencer", "Acid_Bass/AcidBass_sequencer.pd");
        config.patches.put("Audio Controls", "Acid_Bass/AcidBass_controls.pd");
        
        PdDroidPartyLauncher.launch(this, config);
        
       // PdDroidPartyLauncher.launch(this, "Acid_Bass/AcidBass.pd", config);
    }
    



}
