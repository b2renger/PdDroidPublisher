package cx.mccormick.pddroidparty.aciddrums;

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
        config.guiKeepAspectRatio = true;
        config.theme = new MonochromeTheme(MonochromeTheme.YELLOW,true);
       
       // config.patches.put("Full Stack", "Acid_Drums/aciddrums_fullstack.pd");
        config.patches.put("Sequencer", "Acid_Drums/aciddrums_sequencer.pd");
        config.patches.put("Audio Controls", "Acid_Drums/aciddrums_controls.pd");
        
        config.presetsPaths.add("Acid_Drums/presets_pattern");
        config.presetsPaths.add("Acid_Drums/presets_synth");
        
        PdDroidPartyLauncher.launch(this, config);
        //PdDroidPartyLauncher.launch(this,"Acid_Drums/AcidDrums.pd", config);
    }
    



}
