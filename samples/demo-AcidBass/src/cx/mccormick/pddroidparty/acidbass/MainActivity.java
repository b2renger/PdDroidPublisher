package cx.mccormick.pddroidparty.acidbass;

import android.app.Activity;
import android.os.Bundle;
import cx.mccormick.pddroidparty.PdDroidPartyConfig;
import cx.mccormick.pddroidparty.PdDroidPartyLauncher;
import cx.mccormick.pddroidparty.theme.mono.MonochromeTheme;
import cx.mccormick.pddroidparty.widget.abs.Taplist;
import cx.mccormick.pddroidparty.widget.custom.PopupTaplist;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();

        config.theme = new MonochromeTheme(MonochromeTheme.RED, true);
       
        config.guiKeepAspectRatio = true;
        
        config.typeOverrides.put(Taplist.class, PopupTaplist.class);
        
        config.presetsPaths.add("Chip_sequencer/presets_pattern");
        config.presetsPaths.add("Chip_sequencer/presets_synth");
        
        config.patches.put("Sequencer", "Acid_Bass/AcidBass_sequencer.pd");
        config.patches.put("Audio Controls", "Acid_Bass/AcidBass_controls.pd");
        
        PdDroidPartyLauncher.launch(this, config);
        
    }
    



}
