package cx.mccormick.pddroidparty.slicer;

import org.puredata.core.PdBase;
import org.puredata.core.PdListener;

import android.graphics.Canvas;
import cx.mccormick.pddroidparty.pd.PdHelper;
import cx.mccormick.pddroidparty.widget.core.Subpatch;

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
