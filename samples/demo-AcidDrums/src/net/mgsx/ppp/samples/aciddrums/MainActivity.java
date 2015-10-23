package net.mgsx.ppp.samples.aciddrums;

import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.PdDroidPartyLauncher;
import net.mgsx.ppp.theme.mono.MonochromeTheme;
import net.mgsx.ppp.widget.core.Bang;
import net.mgsx.ppp.widget.core.Slider;
import net.mgsx.ppp.widget.core.Toggle;
import net.mgsx.ppp.widget.core.Radio;
import net.mgsx.ppp.widget.custom.RibbonSlider;
import net.mgsx.ppp.widget.custom.SimpleBang;
import net.mgsx.ppp.widget.custom.SwitchToggle;
import net.mgsx.ppp.widget.custom.FilledToggle;
import net.mgsx.ppp.widget.custom.SimpleRadio;
import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.guiKeepAspectRatio = true;
        config.theme = new MonochromeTheme(MonochromeTheme.GREEN,true);
        
       
        config.typeOverrides.put(Slider.class, RibbonSlider.class);
        config.typeOverrides.put(Bang.class, SimpleBang.class);
        config.typeOverrides.put(Toggle.class, FilledToggle.class);
        config.typeOverrides.put(Radio.class, SimpleRadio.class);
       
        config.objectOverrides.put("Kick_Mute", SwitchToggle.class);
        config.objectOverrides.put("Snare_Mute", SwitchToggle.class);
        config.objectOverrides.put("Clap_Mute", SwitchToggle.class);
        config.objectOverrides.put("Hihat_Mute", SwitchToggle.class);
        config.objectOverrides.put("Hihat2_Mute", SwitchToggle.class);
       
       // config.patches.put("Full Stack", "Acid_Drums/aciddrums_fullstack.pd");
        config.guiPatches.put("Sequencer", "Acid_Drums/aciddrums_sequencer.pd");
        config.guiPatches.put("Audio Controls", "Acid_Drums/aciddrums_controls.pd");
        config.corePatches.add("Acid_Drums/aciddrums_audiocore.pd");
        
        config.presetsPaths.add("Acid_Drums/presets_pattern");
        config.presetsPaths.add("Acid_Drums/presets_synth");
        
        PdDroidPartyLauncher.launch(this, config);
        //PdDroidPartyLauncher.launch(this,"Acid_Drums/AcidDrums.pd", config);
    }
    



}
