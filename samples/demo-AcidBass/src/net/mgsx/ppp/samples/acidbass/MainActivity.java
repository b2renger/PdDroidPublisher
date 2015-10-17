package net.mgsx.ppp.samples.acidbass;

import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.PdDroidPartyLauncher;
import net.mgsx.ppp.theme.mono.MonochromeTheme;
import net.mgsx.ppp.widget.abs.Taplist;
import net.mgsx.ppp.widget.core.Slider;
import net.mgsx.ppp.widget.custom.PopupTaplist;
import net.mgsx.ppp.widget.custom.RibbonSlider;
import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();

        config.theme = new MonochromeTheme(MonochromeTheme.RED, true);
       
        config.guiKeepAspectRatio = true;
        
        config.typeOverrides.put(Taplist.class, PopupTaplist.class);
        config.typeOverrides.put(Slider.class, RibbonSlider.class);
        
        config.presetsPaths.add("Chip_sequencer/presets_pattern");
        config.presetsPaths.add("Chip_sequencer/presets_synth");
        
        
        config.guiPatches.put("Harmony","Acid_Bass/AcidBass_noteSelector.pd");
        config.guiPatches.put("Notes Tweaks","Acid_Bass/AcidBass_noteOptions.pd");
        config.guiPatches.put("Audio Controls", "Acid_Bass/AcidBass_audioControls.pd");
        config.corePatches.add("Acid_Bass/AcidBass_audioCore.pd");
        
        PdDroidPartyLauncher.launch(this, config);
        
    }
    



}
