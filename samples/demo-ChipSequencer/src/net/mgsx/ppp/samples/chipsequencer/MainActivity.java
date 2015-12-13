package net.mgsx.ppp.samples.chipsequencer;

import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.PdDroidPartyLauncher;
import net.mgsx.ppp.theme.mono.MonochromeTheme;
import net.mgsx.ppp.widget.abs.Taplist;
import net.mgsx.ppp.widget.core.Bang;
import net.mgsx.ppp.widget.core.Radio;
import net.mgsx.ppp.widget.core.Slider;
import net.mgsx.ppp.widget.core.Toggle;
import net.mgsx.ppp.widget.custom.FilledToggle;
import net.mgsx.ppp.widget.custom.PopupTaplist;
import net.mgsx.ppp.widget.custom.RibbonSlider;
import net.mgsx.ppp.widget.custom.SimpleBang;
import net.mgsx.ppp.widget.custom.SimpleRadio;
import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.guiKeepAspectRatio = true;
        
        config.theme = new MonochromeTheme(MonochromeTheme.YELLOW, true);
        
        config.typeOverrides.put(Taplist.class, PopupTaplist.class);
        config.typeOverrides.put(Slider.class, RibbonSlider.class);
        config.typeOverrides.put(Bang.class, SimpleBang.class);
        config.typeOverrides.put(Toggle.class, FilledToggle.class);
        config.typeOverrides.put(Radio.class, SimpleRadio.class);
       
        config.presetsPaths.add("Chip_sequencer/presets_pattern");
        config.presetsPaths.add("Chip_sequencer/presets_synth");
        
       
        config.guiPatches.put("Sequencer", "Chip_sequencer/chip_sequencer_sequencer.pd");
        config.guiPatches.put("Audio Controls", "Chip_sequencer/chip_sequencer_controls.pd");
        config.corePatches.add("Chip_sequencer/chip_sequencer_audiocore.pd");
        
        PdDroidPartyLauncher.launch(this, config);
    }
    



}
