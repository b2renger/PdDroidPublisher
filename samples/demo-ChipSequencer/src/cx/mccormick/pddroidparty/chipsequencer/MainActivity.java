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
        config.guiKeepAspectRatio = false;
        config.theme = new MonochromeTheme(MonochromeTheme.ORANGE, true);
       
        config.presetsPaths.add("Chip_sequencer/presets_pattern");
        config.presetsPaths.add("Chip_sequencer/presets_synth");
        
       
        config.patches.put("Sequencer", "Chip_sequencer/chip_sequencer_sequencer.pd");
        config.patches.put("Audio Controls", "Chip_sequencer/chip_sequencer_controls.pd");
        
        PdDroidPartyLauncher.launch(this, config);
    }
    



}
