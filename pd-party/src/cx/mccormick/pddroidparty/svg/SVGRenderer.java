package cx.mccormick.pddroidparty.svg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Picture;
import android.util.Log;

import com.larvalabs.svgandroid.SVGParseException;
import com.larvalabs.svgandroid.SVGParser;

import cx.mccormick.pddroidparty.view.PdDroidPatchView;

public class SVGRenderer {
	private static final String TAG = "SVGRenderer";
	// cached image so we don't keep regenerating it every frame
	Picture cached = null;
	// my SVG filename
	String svgfile = null;
	// class shared static hashmap of all cached SVG images
	private static HashMap<String, Picture> cache = new HashMap<String, Picture>();
	
	private static Map<String, SVGInfo> infoCache = new HashMap<String, SVGInfo>();
	
	private SVGInfo info;
	
	public SVGRenderer(File f) {
		svgfile = f.toString();
		Log.d(TAG, "Loading: " + svgfile);
		// cache it the first time
		if (!cache.containsKey(svgfile)) {
			try {
				cache.put(svgfile, SVGParser.getSVGFromInputStream(new FileInputStream(f)).getPicture());
			} catch (SVGParseException e) {
				throw new Error(e);
			} catch (FileNotFoundException e) {
				throw new Error(e);
			}
			Log.d(TAG, "(cache store)");
		}
		info = infoCache.get(svgfile);
		if(info == null)
		{
			info = new SVGInfo(f);
			infoCache.put(svgfile, info);
		}
		
	}
	
	// only create an SVGRenderer if we can load the file name asked for
	public static SVGRenderer getSVGRenderer(PdDroidPatchView parent, String name) {
		// reads the SVG string from a file if the file with with name exists
		// returns null if the file with name does not exist	
		File f = new File(parent.getPatchFile().getParent() + "/" + name + ".svg");
		if (f.exists() && f.canRead() && f.isFile()) {
			return new SVGRenderer(f);
		}
		return null;
	}
	
	// get a rendered version of the svg
	public Picture getPicture() {
		if (cached != null) {
			return cached;
		}
		return cache.get(svgfile);
	}

	public SVGInfo getInfo() {
		return info;
	}
	
}
