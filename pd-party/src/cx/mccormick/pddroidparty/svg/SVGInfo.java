package cx.mccormick.pddroidparty.svg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class SVGInfo {

	public SVGInfo(File f) 
	{
		try
		{
			XmlPullParser parser = Xml.newPullParser();
	        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
	        parser.setInput(new FileInputStream(f), null);
	        while (parser.next() != XmlPullParser.END_TAG) {
	            if (parser.getEventType() != XmlPullParser.START_TAG) {
	                continue;
	            }
	            String name = parser.getName();
	            if (name.equals("svg")) {
	            	textFont = parser.getAttributeValue(null, "textFont");
	            	textColor = parser.getAttributeValue(null, "textColor");
	            	textAntialias = parser.getAttributeValue(null, "textAntialias");
	            	textOffset = parser.getAttributeValue(null, "textOffset");
	            }
	        }  
		} catch (XmlPullParserException e) {
			throw new Error(e);
		} catch (FileNotFoundException e) {
			throw new Error(e);
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	private String textFont, textColor, textAntialias, textOffset;
	
	public String getTextFont() {
		return textFont;
	}

	public String getTextColor() {
		return textColor;
	}

	public String getTextAntialias() {
		return textAntialias;
	}

	public String getTextOffset() {
		return textOffset;
	}

}
