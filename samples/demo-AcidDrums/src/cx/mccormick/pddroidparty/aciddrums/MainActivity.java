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
        config.midiClockMaxBPM = 480;
        config.guiKeepAspectRatio = true;
        config.theme = new MonochromeTheme(MonochromeTheme.YELLOW,true);
        
       
        
        config.patches.put("Sequencer", "Acid_Drums/main.pd");
        config.patches.put("Controls", "Acid_Drums/controls.pd");
        
        PdDroidPartyLauncher.launch(this, config);
        //PdDroidPartyLauncher.launch(this,"Acid_Drums/AcidDrums.pd", config);
    }
    



}
