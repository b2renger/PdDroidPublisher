package cx.mccormick.pddroidparty.widget;

import cx.mccormick.pddroidparty.view.PdDroidPatchView;

abstract public class OrientedWidget extends Widget
{
	protected boolean horizontal;
	
	public OrientedWidget(PdDroidPatchView app, boolean horizontal) {
		super(app);
		this.horizontal = horizontal;
	}

	public boolean isHorizontal() {
		return horizontal;
	}
}
