package net.mgsx.ppp.test.custom;

import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.PdDroidPartyLauncher;
import net.mgsx.ppp.widget.abs.Taplist;
import net.mgsx.ppp.widget.abs.Touch;
import net.mgsx.ppp.widget.core.Bang;
import net.mgsx.ppp.widget.core.Canvasrect;
import net.mgsx.ppp.widget.core.Comment;
import net.mgsx.ppp.widget.core.Numberbox;
import net.mgsx.ppp.widget.core.Numberbox2;
import net.mgsx.ppp.widget.core.Radio;
import net.mgsx.ppp.widget.core.Slider;
import net.mgsx.ppp.widget.core.Subpatch;
import net.mgsx.ppp.widget.core.Toggle;
import net.mgsx.ppp.widget.core.VUMeter;
import net.mgsx.ppp.widget.custom.SimpleBang;
import net.mgsx.ppp.widget.custom.SwitchToggle;
import net.mgsx.ppp.widget.custom.PopupTaplist;
import net.mgsx.ppp.widget.custom.RibbonSlider;
import android.app.Activity;
import android.os.Bundle;


public class CustomActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PdDroidPartyConfig config = new PdDroidPartyConfig();
        
        // specific override examples
        config.objectOverrides.put("hsl-custom-square", CustomSlider.class);
        config.objectOverrides.put("hsl-custom", RibbonSlider.class);
        config.objectOverrides.put("vsl-custom", RibbonSlider.class);
        config.objectOverrides.put("bang-custom", SimpleBang.class);
        config.objectOverrides.put("array-custom", CustomArray.class);
        config.objectOverrides.put("custom-taplist", PopupTaplist.class);
        config.objectOverrides.put("custom-taplist2", PopupTaplist.class);
        config.objectOverrides.put("sub-custom", CustomSubpatch.class);
        config.objectOverrides.put("custom comment", CustomComment.class);
        config.objectOverrides.put("toggle-custom", SwitchToggle.class);
        config.objectOverrides.put("vcb-custom", CustomRadio.class);
        config.objectOverrides.put("hcb-custom", CustomRadio.class);
        // TODO custom VUMeter
        // TODO custom Numberbox1&2
        // TODO custom canvasrect
        // TODO custom touch
        
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
