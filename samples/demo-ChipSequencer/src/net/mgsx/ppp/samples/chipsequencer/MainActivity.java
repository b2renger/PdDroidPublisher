package net.mgsx.ppp.samples.chipsequencer;

import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.PdDroidPartyLauncher;
import net.mgsx.ppp.theme.mono.MonochromeTheme;
import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.guiKeepAspectRatio = false;
        config.theme = new MonochromeTheme(MonochromeTheme.ORANGE, true);
       
        config.presetsPaths.add("Chip_sequencer/presets_pattern");
        config.presetsPaths.add("Chip_sequencer/presets_synth");
        
       
        config.guiPatches.put("Sequencer", "Chip_sequencer/chip_sequencer_sequencer.pd");
        config.guiPatches.put("Audio Controls", "Chip_sequencer/chip_sequencer_controls.pd");
        
        PdDroidPartyLauncher.launch(this, config);
    }
    



}
