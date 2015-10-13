package cx.mccormick.pddroidparty.slicer;

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
        config.theme = new MonochromeTheme(180, false);
        config.objectOverrides.put("Bend", PitchBend.class);
        config.objectOverrides.put("smp1", WavePreview.class);
        config.objectOverrides.put("smp2", WavePreview.class);
        config.objectOverrides.put("smp3", WavePreview.class);
        config.objectOverrides.put("smp4", WavePreview.class);
        
        config.presetsPaths.add("savefiles");
        
        PdDroidPartyLauncher.launch(this, "Slicer/slicer2.pd", config);
    }
    



}
