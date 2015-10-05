package cx.mccormick.pddroidparty.acidbass;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cx.mccormick.pddroidparty.view.MidiConfigDialog;
import cx.mccormick.pddroidparty.view.PdDroidPatchView;
import cx.mccormick.pddroidparty.widget.abs.Taplist;

public class CustomTaplist extends Taplist {

	public final static class Selector extends Dialog {
		List<String> values;
		Integer  selectedValue = null;

		public Selector(Context context, List<String> values) {
			super(context);
			this.values = values;
			
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			setTitle("Select value");

			final LinearLayout view = new LinearLayout(getContext());
			final ScrollView sview = new ScrollView(getContext());
			view.setOrientation(LinearLayout.VERTICAL);
			sview.setVerticalScrollBarEnabled(true);


			for (int i = 0; i < values.size(); i++) {
				final Button button = new Button(getContext());
				String s = values.get(i);
				button.setText(s);
				button.setId(i);

				button.setOnClickListener(new View.OnClickListener() {
					@Override	
					public void onClick(View v) {
					
						//view.setVerticalScrollbarPosition(0);
						selectedValue = (Integer) button.getId();
						Selector.this.dismiss();
					}
				});
				view.addView(button);
			}
			sview.addView(view);
			setContentView(sview);
		}
		
		public Integer getSelectedValue(){
			return selectedValue;
		}
	}

	public CustomTaplist(PdDroidPatchView app, String[] atomline) {
		super(app, atomline);
	}

	@Override
	public boolean touchdown(int pid, float x, float y) {

		if (dRect.contains(x, y)) {
			final Selector selector = new Selector(parent.getContext(), getValues());
	
			selector.setOnDismissListener(new OnDismissListener() {				
				@Override
				public void onDismiss(DialogInterface dialog) {
					Integer v = selector.getSelectedValue();
					if (v != null){
						val = v;
						doSend();
						parent.threadSafeInvalidate();
					}
					dialog.dismiss();
				}
			});	
			selector.show();
			return true;
		}
		return false;
	}
}
