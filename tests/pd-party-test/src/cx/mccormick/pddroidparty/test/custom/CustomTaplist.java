package cx.mccormick.pddroidparty.test.custom;

import android.app.Dialog;
import android.content.Context;
import cx.mccormick.pddroidparty.view.PdDroidPatchView;
import cx.mccormick.pddroidparty.widget.abs.Taplist;

public class CustomTaplist extends Taplist
{

	private static class Selector extends Dialog
	{
		public Selector(Context context) {
			super(context);
		}
		
		// TODO implements selector GUI
	}
	
	public CustomTaplist(PdDroidPatchView app, String[] atomline) {
		super(app, atomline);
	}

	@Override
	public boolean touchdown(int pid, float x, float y) 
	{
		if (dRect.contains(x, y)) 
		{
			Selector dialog = new Selector(parent.getContext());
			// TODO add dismiss listener to handle return value.
			dialog.show();
			return true;
		}
		return false;	
	}
}
