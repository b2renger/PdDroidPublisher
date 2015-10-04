package cx.mccormick.pddroidparty.test.custom;

import android.graphics.Canvas;
import cx.mccormick.pddroidparty.widget.core.Subpatch;

public class HiddenSubpatch extends Subpatch
{

	public HiddenSubpatch(Subpatch subpatch) {
		super(subpatch);
	}
	
	@Override
	public void draw(Canvas canvas) {
	}

}
