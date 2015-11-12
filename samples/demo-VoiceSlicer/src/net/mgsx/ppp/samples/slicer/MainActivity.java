package net.mgsx.ppp.samples.slicer;

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
        
        // enable microphone.
        config.audioInputs = 1;
        
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
