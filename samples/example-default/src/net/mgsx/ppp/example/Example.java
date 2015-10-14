package net.mgsx.ppp.example;

import android.app.Activity;
import android.os.Bundle;
import cx.mccormick.pddroidparty.PdDroidPartyLauncher;


public class Example extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PdDroidPartyLauncher.launch(this, "example/example.pd");
    }

}
