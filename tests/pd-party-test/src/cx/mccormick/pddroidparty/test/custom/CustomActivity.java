package cx.mccormick.pddroidparty.test.custom;

import android.app.Activity;
import android.os.Bundle;
import cx.mccormick.pddroidparty.PdDroidPartyConfig;
import cx.mccormick.pddroidparty.PdDroidPartyLauncher;
import cx.mccormick.pddroidparty.widget.abs.Taplist;
import cx.mccormick.pddroidparty.widget.abs.Touch;
import cx.mccormick.pddroidparty.widget.core.Bang;
import cx.mccormick.pddroidparty.widget.core.Canvasrect;
import cx.mccormick.pddroidparty.widget.core.Comment;
import cx.mccormick.pddroidparty.widget.core.Numberbox;
import cx.mccormick.pddroidparty.widget.core.Numberbox2;
import cx.mccormick.pddroidparty.widget.core.Radio;
import cx.mccormick.pddroidparty.widget.core.Slider;
import cx.mccormick.pddroidparty.widget.core.Subpatch;
import cx.mccormick.pddroidparty.widget.core.Toggle;
import cx.mccormick.pddroidparty.widget.core.VUMeter;


public class CustomActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        config.midiClockMaxBPM = 480;
        
        // specific override examples
        config.objectOverrides.put("hsl-custom-square", CustomSlider.class);
        config.objectOverrides.put("hsl-custom", RibbonSlider.class);
        config.objectOverrides.put("vsl-custom", RibbonSlider.class);
        config.objectOverrides.put("bang-custom", CustomBang.class);
        config.objectOverrides.put("array-custom", CustomArray.class);
        config.objectOverrides.put("custom-taplist", CustomTaplist.class);
        config.objectOverrides.put("sub-custom", CustomSubpatch.class);
        config.objectOverrides.put("custom comment", CustomComment.class);
        
        // Global override (all types hidden)
        config.typeOverrides.put(Bang.class, HiddenWidget.class);
        config.typeOverrides.put(Canvasrect.class, HiddenWidget.class);
        config.typeOverrides.put(Comment.class, HiddenWidget.class);
        config.typeOverrides.put(Numberbox.class, HiddenWidget.class);
        config.typeOverrides.put(Numberbox2.class, HiddenWidget.class);
        config.typeOverrides.put(Radio.class, HiddenWidget.class);
        config.typeOverrides.put(Slider.class, HiddenWidget.class);
        config.typeOverrides.put(Subpatch.class, HiddenSubpatch.class);
        config.typeOverrides.put(Toggle.class, HiddenWidget.class);
        config.typeOverrides.put(VUMeter.class, HiddenWidget.class);
        
        config.typeOverrides.put(Taplist.class, HiddenWidget.class);
        config.typeOverrides.put(Touch.class, HiddenWidget.class);
        
        PdDroidPartyLauncher.launch(this, "custom/custom.pd", config);
    }
    



}
