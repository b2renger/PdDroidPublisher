package cx.mccormick.pddroidparty.sample;

import android.app.Activity;
import android.os.Bundle;
import cx.mccormick.pddroidparty.PdDroidPartyLauncher;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyLauncher.launch(this, "groove-box-sync/nmj_groovebox.pd");
    }
    



}
