package cx.mccormick.pddroidparty.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.drawable.PictureDrawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.larvalabs.svgandroid.SVGParser;

import cx.mccormick.pddroidparty.PdDroidParty;
import cx.mccormick.pddroidparty.PdDroidPartyConfig;
import cx.mccormick.pddroidparty.R;
import cx.mccormick.pddroidparty.pd.DroidPartyReceiver;
import cx.mccormick.pddroidparty.pd.PdHelper;
import cx.mccormick.pddroidparty.pd.PdPatch;
import cx.mccormick.pddroidparty.svg.SVGRenderer;
import cx.mccormick.pddroidparty.widget.Widget;
import cx.mccormick.pddroidparty.widget.abs.Display;
import cx.mccormick.pddroidparty.widget.abs.DroidNetClient;
import cx.mccormick.pddroidparty.widget.abs.DroidNetReceive;
import cx.mccormick.pddroidparty.widget.abs.DroidSystem;
import cx.mccormick.pddroidparty.widget.abs.LoadSave;
import cx.mccormick.pddroidparty.widget.abs.MenuBang;
import cx.mccormick.pddroidparty.widget.abs.Numberboxfixed;
import cx.mccormick.pddroidparty.widget.abs.Taplist;
import cx.mccormick.pddroidparty.widget.abs.Touch;
import cx.mccormick.pddroidparty.widget.abs.Wordbutton;
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
import cx.mccormick.pddroidparty.widget.ext.Knob;

public class PdDroidPatchView extends View implements OnTouchListener {
	
	private Paint paint = new Paint();
	public int patchwidth;
	public int patchheight;
	//view port :
	public int viewX = 0;
	public int viewY = 0;
	public int viewW = 1;
	public int viewH = 1;

	public int fontsize;
	private ArrayList<Widget> widgets = new ArrayList<Widget>();
	private PdDroidParty app;
	private int splash_res = 0;
	private Resources res = null;
	private Picture background = null;
	private Bitmap bgbitmap = null;
	private RectF bgrect = new RectF();
	private PdPatch patch;
	private Map<String, DroidPartyReceiver> receivemap = new HashMap<String, DroidPartyReceiver>();
	
	private PdDroidPartyConfig config;
	
	// default background color settings
	private int backgroundColor = Color.WHITE;

	
	public PdDroidPatchView(Activity activity, PdDroidParty parent, PdPatch patch, PdDroidPartyConfig config) {
		super(activity);
		
		this.patch = patch;
		this.config = config;
		
		// disable graphic acceleration to have SVG properly rendered
		// not needed prior to API level 17
		// not possible prior to API level 11
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			setSoftwareMode();
		}
		
		app = parent;

		setFocusable(true);
		setFocusableInTouchMode(true);
		
		this.setOnTouchListener(this);
		this.setId(R.id.patch_view);
		
		paint.setColor(backgroundColor);
		paint.setAntiAlias(true);
		
		res = activity.getResources();
		
		// if there is a splash image, use it
		splash_res = res.getIdentifier("splash", "raw", activity.getPackageName());
		if (splash_res != 0) {
			// Get a drawable from the parsed SVG and set it as the drawable for the ImageView
			background = SVGParser.getSVGFromResource(res, splash_res).getPicture();
		} else {
			loadBackground();
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setSoftwareMode()
	{
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	}
	
	public PdDroidPartyConfig getConfig() {
		return config;
	}
	
	private static Bitmap picture2Bitmap(Picture picture){
	    PictureDrawable pictureDrawable = new PictureDrawable(picture);
	    Bitmap bitmap = Bitmap.createBitmap(pictureDrawable.getIntrinsicWidth(), pictureDrawable.getIntrinsicHeight(), Config.ARGB_8888);
	    //Log.e(TAG, "picture size: " + pictureDrawable.getIntrinsicWidth() + " " + pictureDrawable.getIntrinsicHeight());
	    Canvas canvas = new Canvas(bitmap);
	    canvas.drawPicture(pictureDrawable.getPicture());
	    return bitmap;
	}

	private void loadBackground() {
		// if we have a splash_res or we don't have a background
		if (bgbitmap == null || splash_res != 0) {
			// load the background image
			SVGRenderer renderer = SVGRenderer.getSVGRenderer(this, "background");
			if (renderer != null) {
				background = renderer.getPicture();
				bgbitmap = picture2Bitmap(background);
				if(renderer.getInfo().getBackgroundColor() != null)
				{
					backgroundColor  = Color.parseColor(renderer.getInfo().getBackgroundColor());
				}
			}
			else {
				File f = patch.getFile("background.png");
				if (f.exists() && f.canRead() && f.isFile()) {
					bgbitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
				}
			}
		}
	}
	
	private float ratioW = 1;
	private float ratioH = 1;
	private float offsetX = 0;
	private float offsetY = 0;
	
	@Override
	public void onDraw(Canvas canvas) {
		
		paint.setColor(backgroundColor);
		
		canvas.drawPaint(paint);
		canvas.save();

		offsetX = 0;
		offsetY = 0;
		ratioW = getWidth() / (float)viewW;
		ratioH = getHeight() / (float)viewH;
		
		if(config.guiKeepAspectRatio)
		{
			float ratio = Math.min(ratioW, ratioH);
			
			if(ratioW > ratioH)
			{
				offsetX = (getWidth() - viewW * ratio)/2;
				offsetY = 0;
			}
			else
			{
				offsetX = 0;
				offsetY = (getHeight() - viewH * ratio) / 2;
			}
			ratioW = ratioH = ratio;
		}
		canvas.translate(offsetX, offsetY);
		canvas.scale(ratioW, ratioH);
		canvas.translate(-viewX, -viewY );

		if (widgets != null)
		{	
			bgrect.set(0, 0, patchwidth, patchheight);
			if (bgbitmap != null) {
				canvas.drawBitmap(bgbitmap, null, bgrect, null);
			}
			synchronized (this) {
				for (Widget widget: widgets) {
					widget.draw(canvas);
				}
			}
		}
	
		canvas.restore();
		
	}
	
	public float PointerX(float x){
		return ((x - offsetX) / ratioW + viewX);
	}
	
	public float PointerY(float y){
		return ((y - offsetY) / ratioH + viewY);
	}
	
	@Override
	public boolean performClick() 
	{
		return super.performClick();
	}
	
	public boolean onTouch(View view, MotionEvent event) {
		int index, pid, action;
		float x, y;
		
		if (widgets != null) {
			action = event.getActionMasked();
			switch(action) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_POINTER_DOWN:
					index = event.getActionIndex();
					pid = event.getPointerId(index);
					x = PointerX(event.getX(index));
					y = PointerY(event.getY(index));
					for (Widget widget: widgets) {
						if( widget.touchdown(pid,x,y)) break;
					}
					break;
				case MotionEvent.ACTION_UP:
					view.performClick();
				case MotionEvent.ACTION_POINTER_UP:
					index = event.getActionIndex();
					pid = event.getPointerId(index);
					x = PointerX(event.getX(index));
					y = PointerY(event.getY(index));
					for (Widget widget: widgets) {
						if( widget.touchup(pid,x,y)) break;
					}
					break;
				case MotionEvent.ACTION_MOVE:
					int pointerCount = event.getPointerCount();
					for (int p = 0; p < pointerCount; p++) {
						pid = event.getPointerId(p);
						x = PointerX(event.getX(p));
						y = PointerY(event.getY(p));
						for (Widget widget: widgets) {
							if( widget.touchmove(pid,x,y)) break;
						}
					}
					break;
				default:
			}
		}
		invalidate();
		return true;
	}
	
	/** Lets us invalidate this view from the audio thread */
	public void threadSafeInvalidate() 
	{
		((Activity)getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				PdDroidPatchView.this.invalidate();
			}
		});
	}
	
	/** Main patch is done loading, now we should change the background from the splash. **/
	public void loaded() {
		loadBackground();
		
		// start timer
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				synchronized (PdDroidPatchView.this) {
					for (Widget widget: widgets) {
						widget.updateData();
					}
					
				}
				threadSafeInvalidate();
			}
		};
		new Timer().scheduleAtFixedRate(task, new Date(), config.arrayRefreshTimeMS);
	}
	
	/** build a user interface using the lines of atoms found in the patch by the pd file parser */
	synchronized public void buildUI(List<String[]> atomlines) {
		//ArrayList<String> canvases = new ArrayList<String>();
		int level = 0;
		
		LinkedList<Subpatch> subpatches = new LinkedList<Subpatch>();
		
		for (String[] line: atomlines) {
			if (line.length >= 4) {
				// find canvas begin and end lines
				if (line[1].equals("canvas")) {
					level += 1;
					if (level == 1) {
						viewW = patchwidth = Integer.parseInt(line[4]);
						viewH = patchheight = Integer.parseInt(line[5]);
						fontsize = Integer.parseInt(line[6]);
					}
					else
					{
						List<Widget> widgets;
						if(subpatches.isEmpty()) widgets = this.widgets; else{
							widgets = subpatches.peekLast().widgets;
						}
						Subpatch subpatch = new Subpatch(this, line);
						subpatches.addLast(subpatch);
						widgets.add(subpatch);
					}
				} else if (line[1].equals("restore")) {
					Subpatch subpatch = subpatches.removeLast();
					subpatch.parse(line);
					level -= 1;
				// find different types of UI element in the top level patch
				} else {
					
					List<Widget> widgets;
					if(subpatches.isEmpty()) widgets = this.widgets; else{
						widgets = subpatches.peekLast().widgets;
						subpatches.peekLast().parse(line);
					}
					
					if (line.length >= 2) {
						// builtin pd things
						if (line[1].equals("text")) {
							widgets.add(new Comment(this, line));
						} else if (line[1].equals("floatatom")) {
							widgets.add(new Numberbox(this, line));
						} else if (line.length >= 5) {
							// pd objects
							if (line[4].equals("vsl")) {
								widgets.add(new Slider(this, line, false));
							} else if (line[4].equals("hsl")) {
								widgets.add(new Slider(this, line, true));
							} else if (line[4].equals("tgl")) {
								widgets.add(new Toggle(this, line));
							} else if (line[4].equals("bng")) {
								widgets.add(new Bang(this, line));
							} else if (line[4].equals("nbx")) {
								widgets.add(new Numberbox2(this, line));
							} else if (line[4].equals("cnv")) {
								widgets.add(new Canvasrect(this, line));
							} else if (line[4].equals("vradio")) {
								widgets.add(new Radio(this, line, false));
							} else if (line[4].equals("hradio")) {
								widgets.add(new Radio(this, line, true));
							} else if (line[4].equals("vu")) {
								widgets.add(new VUMeter(this, line));
							} else if (line[4].equals("mknob")) {
								widgets.add(new Knob(this, line));
							}
							// special PdDroidParty abstractions
							else if (line[4].equals("wordbutton")) {
								widgets.add(new Wordbutton(this, line));
							} else if (line[4].equals("numberbox")) {
								widgets.add(new Numberboxfixed(this, line));
							} else if (line[4].equals("taplist")) {
								widgets.add(new Taplist(this, line));
							} else if (line[4].equals("display")) {
								widgets.add(new Display(this, line));
							} else if (line[4].equals("touch")) {
								widgets.add(new Touch(this, line));
							}
						}
					}
				}
				
				// things that can be found at any depth and still work
				if (line.length >= 5) {
					if (line[4].equals("droidnetreceive")) {
						widgets.add(new DroidNetReceive(this, line));
					} else if (line[4].equals("droidnetclient")) {
						widgets.add(new DroidNetClient(this, line));
					} else if (line[4].equals("menubang")) {
						new MenuBang(this, line);
					} else if (line[4].equals("loadsave")) {
						new LoadSave(this, line);
					} else if (line[4].equals("droidsystem")) {
						new DroidSystem(this, line);
					}
				}
			}
		}
		
		// Apply theme to all widgets
		backgroundColor = config.theme.getBackgroundColor();
		applyTheme(widgets);
		
		threadSafeInvalidate();
	}
	
	private void applyTheme(List<Widget> widgets)
	{
		for(Widget widget : widgets)
		{
			widget.fgcolor = config.theme.getForegroundColor(widget);
			widget.bgcolor = config.theme.getBackgroundColor(widget);
			widget.labelcolor = config.theme.getLabelColor(widget);
			
			if(widget instanceof Subpatch)
			{
				applyTheme(((Subpatch) widget).widgets);
			}
		}
	}

	public String replaceDollarZero(String name) 
	{
		return patch.replaceDollarZero(name);
	}

	public Object getSystemService(String name) {
		return ((Activity)getContext()).getSystemService(name);
	}

	public void startActivity(Intent intent) 
	{
		((Activity)getContext()).startActivity(intent);
	}

	public void registerReceiver(String name, Widget w) {
		// do $0 replacement
		String realname = patch.replaceDollarZero(name);
		DroidPartyReceiver r = receivemap.get(realname);
		if (r == null) {
			r = new DroidPartyReceiver(this, w);
			receivemap.put(realname, r);
			PdHelper.addListener(realname, r.listener);
		} else {
			r.addWidget(w);
		}
	}

	public String getPatchRelativePath(String dir) {
		return patch.getFile(dir).getPath();
	}

	public void launchDialog(Widget which, int type) {
		app.launchDialog(which, type);
	}
	
	public PdPatch getPatch() 
	{
		return patch;
	}
}
