package net.mgsx.ppp.samples.chorder;

import android.app.Activity;
import android.os.Bundle;
import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.PdDroidPartyLauncher;
import net.mgsx.ppp.theme.mono.MonochromeTheme;
import net.mgsx.ppp.widget.abs.Taplist;
import net.mgsx.ppp.widget.core.Bang;
import net.mgsx.ppp.widget.core.Slider;
import net.mgsx.ppp.widget.core.Toggle;
import net.mgsx.ppp.widget.core.Radio;
import net.mgsx.ppp.widget.custom.SwitchToggle;
import net.mgsx.ppp.widget.custom.PopupTaplist;
import net.mgsx.ppp.widget.custom.RibbonSlider;
import net.mgsx.ppp.widget.custom.SimpleBang;
import net.mgsx.ppp.widget.custom.FilledToggle;
import net.mgsx.ppp.widget.custom.SimpleRadio;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.theme = new MonochromeTheme(MonochromeTheme.ORANGE, true);
        
        config.guiKeepAspectRatio = true;
        
        config.typeOverrides.put(Taplist.class, PopupTaplist.class);
        config.typeOverrides.put(Slider.class, RibbonSlider.class);
        config.typeOverrides.put(Bang.class, SimpleBang.class);
        config.typeOverrides.put(Toggle.class, SwitchToggle.class);
        config.typeOverrides.put(Radio.class, SimpleRadio.class);
        
        config.presetsPaths.add("Chorder/presets_chords");
        config.presetsPaths.add("Chorder/presets_synth");
        
        config.guiPatches.put("Play Board", "Chorder/Chorder-playBoard.pd");
        config.guiPatches.put("Chords selection", "Chorder/Chorder-gui.pd");
        config.guiPatches.put("Audio Controls", "Chorder/Chorder-controls.pd");
        config.corePatches.add("Chorder/Chorder-core.pd");
        
        PdDroidPartyLauncher.launch(this, config);
    }
        
    

}
