package net.mgsx.ppp.samples.aciddrums;

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
        config.guiKeepAspectRatio = true;
        config.theme = new MonochromeTheme(MonochromeTheme.YELLOW,true);
       
       // config.patches.put("Full Stack", "Acid_Drums/aciddrums_fullstack.pd");
        config.guiPatches.put("Sequencer", "Acid_Drums/aciddrums_sequencer.pd");
        config.guiPatches.put("Audio Controls", "Acid_Drums/aciddrums_controls.pd");
        
        config.presetsPaths.add("Acid_Drums/presets_pattern");
        config.presetsPaths.add("Acid_Drums/presets_synth");
        
        PdDroidPartyLauncher.launch(this, config);
        //PdDroidPartyLauncher.launch(this,"Acid_Drums/AcidDrums.pd", config);
    }
    



}
