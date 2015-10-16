package net.mgsx.ppp.samples.slicer;

import net.mgsx.ppp.pd.PdHelper;
import net.mgsx.ppp.widget.core.Subpatch;

import org.puredata.core.PdBase;
import org.puredata.core.PdListener;

import android.graphics.Canvas;

public class WavePreview extends Subpatch
{

	public WavePreview(Subpatch subpatch) {
		super(subpatch);
		PdHelper.addListener(array.name + ".change", new PdListener.Adapter(){
			@Override
			public void receiveBang(String source) {
				PdBase.readArray(array.buffer, 0, array.name, 0, array.buffer.length);
				parent.threadSafeInvalidate();
			}
		});
	}
	
	@Override
	public void updateData() {
		// cut off auto update.
	}
	
	@Override
	public void drawLabel(Canvas canvas) {
		// do not display label.
	}

}
