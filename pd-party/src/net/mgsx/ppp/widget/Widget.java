package net.mgsx.ppp.widget;

import java.io.File;
import java.util.ArrayList;

import net.mgsx.ppp.R;
import net.mgsx.ppp.pd.PdGUI;
import net.mgsx.ppp.pd.PdHelper;
import net.mgsx.ppp.svg.SVGRenderer;
import net.mgsx.ppp.util.FileHelper;
import net.mgsx.ppp.view.PdDroidPatchView;

import org.puredata.core.PdBase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;

abstract public class Widget {
	private static final String TAG = "Widget";
	
 	protected RectF dRect = new RectF();
	
	protected Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	protected float val = 0;
	protected int init = 0;
	protected String sendname = null;
	protected String receivename = null;
	protected String label = null;
	protected float[] labelpos = new float[2];
	protected int labelfont=0;
	protected int labelsize=10;
	protected Typeface font;
	static Typeface defaultFont;
	protected int fontsize = 0;
	float[] textoffset = new float[2];
	
	public int bgcolor=0xFFFFFFFF;

	public int fgcolor=0xFF000000;

	public int labelcolor=0xFF000000;
	
	protected PdDroidPatchView parent = null;

	/** text cache, used to compute font size once */
	private String textCache;
	
	/** the computed font size to fit in rectangle */
	private float computedSize;

	
	public Widget(PdDroidPatchView app) {
		parent = app;	
		if(defaultFont == null)
		{
			defaultFont = FileHelper.loadFontFromRaw(parent.getContext(), R.raw.dejavu_sans_mono_bold, "dejavu_sans_mono_bold.ttf");
		}
		
		fontsize = (int)((float)parent.fontsize);
		font = defaultFont;
		File f = null;

		// set an aliased font
		f = parent.getPatch().getFile("font.ttf");
		if (f.exists() && f.canRead() && f.isFile()) {
			font = Typeface.createFromFile(f);
		} else {
			// set an anti-aliased font
			f = parent.getPatch().getFile("font-antialiased.ttf");
			if (f.exists() && f.canRead() && f.isFile()) {
				font = Typeface.createFromFile(f);
				paint.setAntiAlias(true);
			}
		}
		
		paint.setColor(fgcolor);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setTypeface(font);
		paint.setTextSize(fontsize);
		textoffset[0] = 0.0f;
		textoffset[1] = 0.0f;
		
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setTextParametersFromSVG(SVGRenderer svg) {
		if (svg != null) {
			if (svg.getInfo().getTextFont() != null) {
				File f = parent.getPatch().getFile(svg.getInfo().getTextFont() + ".ttf");
				if (f.exists() && f.canRead() && f.isFile()) {
					font = Typeface.createFromFile(f);
				} else {
					Log.e("PdDroidParty", "Bad font file: " + svg.getInfo().getTextFont());
				}
				paint.setTypeface(font);
			}
			if (svg.getInfo().getTextColor() != null) {
				try {
					paint.setColor(Color.parseColor(svg.getInfo().getTextColor()));
				} catch (Exception e) {
					// badly formatted color string - who cares?
					Log.e("PdDroidParty", "Bad text color: " + svg.getInfo().getTextColor());
				}
			}
			if (svg.getInfo().getTextAntialias() != null) {
				paint.setAntiAlias(true);
			}
			if (svg.getInfo().getTextOffset() != null) {
				try {
					String[] xy = svg.getInfo().getTextOffset().split(" ");
					textoffset[0] = Float.parseFloat(xy[0]);
					textoffset[1] = Float.parseFloat(xy[1]);
				} catch (Exception e) {
					Log.e("PdDroidParty", "Bad text offset: " + svg.getInfo().getTextOffset());
					Log.e("PdDroidParty", e.toString());
				}
			}
		}
	}
	
	public void send(String msg) {
		if (isValidSymbolName(sendname)) {
			PdHelper.send(sendname, msg);
		}
	}
	
	public void sendFloat(float f) {
		if (isValidSymbolName(sendname)) {
			PdBase.sendFloat(sendname, f);
		}
	}

	public void setupreceive() {
		// listen out for floats from Pd
		if (isValidSymbolName(receivename)) {
			parent.registerReceiver(receivename, this);
		}
	}
	
	protected static boolean isValidSymbolName(String name)
	{
		return name != null && !name.isEmpty() && !name.equals("-") && !name.equals("empty");
	}
	
	public void setval(float v, float alt) {
		if (init != 0) {
			val = v;
		} else {
			val = alt;
		}
	}
	
	public void initval() {
		if (init != 0) {
			send("" + val);
		}
	}
	
	public float getval() {
		return val;
	}
	
	public void drawLabel(Canvas canvas) {
		if (label != null) {
			paint.setTextAlign(Align.LEFT);
			paint.setStrokeWidth(0);
			paint.setColor(labelcolor);
			paint.setTextSize(labelsize);
			paint.setTypeface(font);
			// convert from middle-left to baseline-left
			canvas.drawText(label, dRect.left + labelpos[0], dRect.top + labelpos[1] - paint.ascent() + paint.getFontMetrics().top/2, paint);
			paint.setTextSize(fontsize);
		}
		paint.setColor(fgcolor);
	}
	
	public void drawCenteredText(Canvas canvas, String text) {
		paint.setStrokeWidth(0);
		if (text != null) {
			if(!text.equals(textCache))
			{
				// TODO algorithm not accurate but seems to work ...
				Rect bounds = new Rect();
				paint.getTextBounds(text, 0, text.length(), bounds);
				float ratioW = bounds.width() / dRect.width();
				float ratioH = bounds.height() / dRect.height();
				float maxRatio = Math.max(ratioW, ratioH);
				if(maxRatio > 1)
					computedSize = paint.getTextSize() / maxRatio;
				else
					computedSize = paint.getTextSize();
				
				textCache = new String(text);
			}
			paint.setTextSize(computedSize);
			canvas.drawText(text, dRect.left + dRect.width() / 2 + dRect.width() * textoffset[0], (int) (dRect.top + dRect.height() * 0.75 + dRect.height() * textoffset[1]), paint);
		}
	}
	
	/* Set the label (checking for special null values) */
	public String setLabel(String incoming) {
		if (incoming.equals("-") || incoming.equals("empty")) {
			return null;
		} else {
			return incoming;
		}
	}
	
	/**
	 * Generic draw method for all widgets.
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
	}
	
	/**
	 * Generic touch method for a hit test.
	 * @param event
	 */	
	public void touch(MotionEvent event) {
	}

	/**
	 * Generic touch methods : pid=pointer id
	 **/	
	public boolean touchdown(int pid, float x, float y) {
		return false;
	}
	public boolean touchmove(int pid, float x, float y) {
		return false;
	}
	public boolean touchup(int pid, float x, float y) {
		return false;
	}

	/**
	 * Generic setval method
	 **/	
	public void setval(float v) {
		val=v;
	}

	public boolean widgetreceiveSymbol(String symbol, Object... args) {
		if( symbol.equals("label")
		&& args.length > 0 && args[0].getClass().equals(String.class)
		) {
			label = setLabel((String)args[0]);
			return true;
		}

		if( symbol.equals("label_pos")
		&& args.length > 1 && args[0].getClass().equals(Float.class)
		&& args[1].getClass().equals(Float.class)
		) {
			labelpos[0]= (Float)args[0] ;
			labelpos[1]= (Float)args[1] ;
			return true;
		}

		if( symbol.equals("pos")
		&& args.length > 1 && args[0].getClass().equals(Float.class)
		&& args[1].getClass().equals(Float.class)
		) {
			dRect.offsetTo((Float)args[0] , (Float)args[1]);
			return true;
		}

		if( symbol.equals("color")
		&& args.length > 2 && args[0].getClass().equals(Float.class)
		&& args[1].getClass().equals(Float.class)
		&& args[2].getClass().equals(Float.class)
		) {
			bgcolor = PdGUI.getColor24((int)(float)(Float)args[0]);
			fgcolor = PdGUI.getColor24((int)(float)(Float)args[1]);
			labelcolor = PdGUI.getColor24((int)(float)(Float)args[2]);
			//Log.e(TAG, "msg bgcolor = "+(int)(float)(Float)args[0]+", bgcolor = "+bgcolor);
			return true;
		} 

		if( symbol.equals("label_font")
		&& args.length > 1 && args[0].getClass().equals(Float.class)
		&& args[1].getClass().equals(Float.class)
		) {
			labelfont = (Integer)args[0];
			labelsize = (Integer)args[1];
			return true;
		}

		if( symbol.equals("set")
		&& args.length > 0 && args[0].getClass().equals(Float.class)
		) {
			setval((Float)args[0]);
			return true;
		}
		
		return false;

	}
		
	public void receiveList(Object... args) {
		Log.d(TAG, "dropped list");
	}
	
	public void receiveMessage(String symbol, Object... args) {
		Log.d(TAG, "dropped message");
	}
	
	public void receiveSymbol(String symbol) {
		Log.d(TAG, "dropped symbol");
	}
	
	public void receiveFloat(float x) {
		Log.d(TAG, "dropped float");
	}
	
	public void receiveBang() {
		Log.d(TAG, "dropped bang");
	}
	
	public void receiveAny() {
	}
	
	/***** Special SVG GUI drawing stuff *****/
	public class WImage {
		
		public SVGRenderer svg = null;
		Bitmap bitmap = null;
		
		public WImage(){
			svg = null;
			bitmap = null;
		}
		
		public boolean none(){
			return (svg == null && bitmap == null);
		}
		
		// ***** Loading :
		
		public SVGRenderer getSVG(String prefix, String suffix, Object... args) {
			// split the string into parts on underscore, so we test on e.g.
			// blah_x_y, blah, blah_x, blah_x_y
			ArrayList<String> testnames = new ArrayList<String>();
			ArrayList<String> parts = new ArrayList<String>();
			if (prefix != null)
				parts.add(prefix);
			if (suffix != null)
				parts.add(suffix);
			testnames.add(TextUtils.join("-", parts));
			
			for (int a=0; a<args.length; a++) {
				String teststring = (String)args[a];
				if (teststring != null && !teststring.equals("null") && !teststring.equals("empty") && !teststring.equals("-")) {
					String[] tries = teststring.split("_");
					ArrayList<String> buffer = new ArrayList<String>();
					for (int p=0; p<tries.length; p++) {
						parts.clear();
						buffer.add(tries[p]);
						if (prefix != null)
							parts.add(prefix);
						parts.add(TextUtils.join("_", buffer));
						if (suffix != null)
							parts.add(suffix);
						testnames.add(TextUtils.join("-", parts));
					}
					parts.clear();
				}
			}
			
			// now test every combination we have come up with
			// we want to check from most specific to least specific
			for (int s = testnames.size() - 1; s >= 0; s--) {
				SVGRenderer svg = SVGRenderer.getSVGRenderer(parent, testnames.get(s));
				if (svg != null) {
					return svg;
				}
			}
			
			return null;
		}
		
		public Bitmap getBitmap(String prefix, String suffix, Object... args) {
			// split the string into parts on underscore, so we test on e.g.
			// blah_x_y, blah, blah_x, blah_x_y
			ArrayList<String> testnames = new ArrayList<String>();
			ArrayList<String> parts = new ArrayList<String>();
			if (prefix != null)
				parts.add(prefix);
			if (suffix != null)
				parts.add(suffix);
			testnames.add(TextUtils.join("-", parts));
			
			for (int a=0; a<args.length; a++) {
				String teststring = (String)args[a];
				if (teststring != null && !teststring.equals("null") && !teststring.equals("empty") && !teststring.equals("-")) {
					String[] tries = teststring.split("_");
					ArrayList<String> buffer = new ArrayList<String>();
					for (int p=0; p<tries.length; p++) {
						parts.clear();
						buffer.add(tries[p]);
						if (prefix != null)
							parts.add(prefix);
						parts.add(TextUtils.join("_", buffer));
						if (suffix != null)
							parts.add(suffix);
						testnames.add(TextUtils.join("-", parts));
					}
					parts.clear();
				}
			}
			
			// now test every combination we have come up with
			// we want to check from most specific to least specific
			for (int s = testnames.size() - 1; s >= 0; s--) {
				File f = parent.getPatch().getFile(testnames.get(s) + ".png");
				if (f.exists() && f.canRead() && f.isFile()) {
					return BitmapFactory.decodeFile(f.getAbsolutePath() );
				}
			}
			
			return null;
		}
		
		public void load(String prefix, String suffix, Object... args) {	
			svg = getSVG(prefix, suffix, args);
			if (svg == null) bitmap = getBitmap(prefix, suffix, args);
		}
		
		// ***** Get attributes
		
		public float getWidth() {
			if(svg != null) return svg.getPicture().getWidth();
			else if(bitmap != null) return bitmap.getWidth();
			else return 0;
		}
		
		
		public float getHeight() {
			if(svg != null) return svg.getPicture().getHeight();
			else if(bitmap != null) return bitmap.getHeight();
			else return 0;
		}
		
		// ***** Drawing :
		
		public boolean draw(Canvas c) {
			return draw(c, dRect);
		}
		
		public boolean draw(Canvas c, RectF rect) {
			if(svg != null) c.drawPicture(svg.getPicture(), rect);
			else if(bitmap != null) c.drawBitmap(bitmap, null, rect, paint);
			else return true;
			
			return false;
		}	

	} // end of class WImage

	/** called to update data (arrays) */
	public void updateData() {
	}
	
}
