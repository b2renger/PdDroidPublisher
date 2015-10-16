package net.mgsx.ppp.test.custom;

import net.mgsx.ppp.widget.core.Subpatch;
import android.graphics.Canvas;

public class HiddenSubpatch extends Subpatch
{

	public HiddenSubpatch(Subpatch subpatch) {
		super(subpatch);
	}
	
	@Override
	public void draw(Canvas canvas) {
	}

}
