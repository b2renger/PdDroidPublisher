package com.larvalabs.svgandroid;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.Log;

class SVGHandler extends DefaultHandler {

    Picture picture;
    Canvas canvas;
    Paint paint;
    // Scratch rect (so we aren't constantly making new ones)
    RectF rect = new RectF();
    RectF bounds = null;
    RectF limits = new RectF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

    Integer searchColor = null;
    Integer replaceColor = null;

    boolean whiteMode = false;

    boolean pushed = false;

    HashMap<String, Shader> gradientMap = new HashMap<String, Shader>();
    HashMap<String, Gradient> gradientRefMap = new HashMap<String, Gradient>();
    Gradient gradient = null;

    SVGHandler(Picture picture) {
        this.picture = picture;
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void setColorSwap(Integer searchColor, Integer replaceColor) {
        this.searchColor = searchColor;
        this.replaceColor = replaceColor;
    }

    public void setWhiteMode(boolean whiteMode) {
        this.whiteMode = whiteMode;
    }

    @Override
    public void startDocument() throws SAXException {
        // Set up prior to parsing a doc
    }

    @Override
    public void endDocument() throws SAXException {
        // Clean up after parsing a doc
    }

    private boolean doFill(Properties atts, HashMap<String, Shader> gradients) {
        if ("none".equals(atts.getString("display"))) {
            return false;
        }
        if (whiteMode) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xFFFFFFFF);
            return true;
        }
        String fillString = atts.getString("fill");
        if (fillString != null && fillString.startsWith("url(#")) {
            // It's a gradient fill, look it up in our map
            String id = fillString.substring("url(#".length(), fillString.length() - 1);
            Shader shader = gradients.get(id); 
            if (shader != null) {
                //Util.debug("Found shader!");
                paint.setShader(shader);
                paint.setStyle(Paint.Style.FILL);
                return true;
            } else {
                //Util.debug("Didn't find shader!");
                return false;
            }
        } else {
            paint.setShader(null);
            Integer color = atts.getHex("fill");
            if (color != null) {
                doColor(atts, color, true);
                paint.setStyle(Paint.Style.FILL);
                return true;
            } else if (atts.getString("fill") == null && atts.getString("stroke") == null) {
                // Default is black fill
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(0xFF000000);
                return true;
            }
        }
        return false;
    }

    private boolean doStroke(Properties atts) {
        if (whiteMode) {
            // Never stroke in white mode
            return false;
        }
        if ("none".equals(atts.getString("display"))) {
            return false;
        }
        Integer color = atts.getHex("stroke");
        if (color != null) {
            doColor(atts, color, false);
            // Check for other stroke attributes
            Float width = atts.getFloat("stroke-width");
            // Set defaults

            if (width != null) {
                paint.setStrokeWidth(width);
            }
            String linecap = atts.getString("stroke-linecap");
            if ("round".equals(linecap)) {
                paint.setStrokeCap(Paint.Cap.ROUND);
            } else if ("square".equals(linecap)) {
                paint.setStrokeCap(Paint.Cap.SQUARE);
            } else if ("butt".equals(linecap)) {
                paint.setStrokeCap(Paint.Cap.BUTT);
            }
            String linejoin = atts.getString("stroke-linejoin");
            if ("miter".equals(linejoin)) {
                paint.setStrokeJoin(Paint.Join.MITER);
            } else if ("round".equals(linejoin)) {
                paint.setStrokeJoin(Paint.Join.ROUND);
            } else if ("bevel".equals(linejoin)) {
                paint.setStrokeJoin(Paint.Join.BEVEL);
            }
            paint.setStyle(Paint.Style.STROKE);
            paint.setShader(null);
            return true;
        }
        return false;
    }

    private Gradient doGradient(boolean isLinear, Attributes atts) {
        Gradient gradient = new Gradient();
        gradient.id = SVGParser.getStringAttr("id", atts);
        gradient.spread = SVGParser.getStringAttr("spreadMethod", atts);
        gradient.isLinear = isLinear;
        if (isLinear) {
            gradient.x1 = SVGParser.getFloatAttr("x1", atts, 0f);
            gradient.x2 = SVGParser.getFloatAttr("x2", atts, 0f);
            gradient.y1 = SVGParser.getFloatAttr("y1", atts, 0f);
            gradient.y2 = SVGParser.getFloatAttr("y2", atts, 0f);
        } else {
            gradient.x = SVGParser.getFloatAttr("cx", atts, 0f);
            gradient.y = SVGParser.getFloatAttr("cy", atts, 0f);
            gradient.radius = SVGParser.getFloatAttr("r", atts, 0f);
        }
        String transform = SVGParser.getStringAttr("gradientTransform", atts);
        if (transform != null) {
            gradient.matrix = SVGParser.parseTransform(transform);
        }
        String xlink = SVGParser.getStringAttr("href", atts);
        if (xlink != null) {
            if (xlink.startsWith("#")) {
                xlink = xlink.substring(1);
            }
            gradient.xlink = xlink;
        }
        return gradient;
    }

    private void doColor(Properties atts, Integer color, boolean fillMode) {
        int c = (0xFFFFFF & color) | 0xFF000000;
        if (searchColor != null && searchColor.intValue() == c) {
            c = replaceColor;
        }
        paint.setColor(c);
        Float opacity = atts.getFloat("opacity");
        if (opacity == null) {
            opacity = atts.getFloat(fillMode ? "fill-opacity" : "stroke-opacity");
        }
        if (opacity == null) {
            paint.setAlpha(255);
        } else {
            paint.setAlpha((int) (255 * opacity));
        }
    }

    private boolean hidden = false;
    private int hiddenLevel = 0;
    private boolean boundsMode = false;

    private void doLimits(float x, float y) {
        if (x < limits.left) {
            limits.left = x;
        }
        if (x > limits.right) {
            limits.right = x;
        }
        if (y < limits.top) {
            limits.top = y;
        }
        if (y > limits.bottom) {
            limits.bottom = y;
        }
    }

    private void doLimits(float x, float y, float width, float height) {
        doLimits(x, y);
        doLimits(x + width, y + height);
    }

    private void doLimits(Path path) {
        path.computeBounds(rect, false);
        doLimits(rect.left, rect.top);
        doLimits(rect.right, rect.bottom);
    }

    private void pushTransform(Attributes atts) {
        final String transform = SVGParser.getStringAttr("transform", atts);
        pushed = transform != null;
        if (pushed) {
            final Matrix matrix = SVGParser.parseTransform(transform);
            canvas.save();
            canvas.concat(matrix);
        }
    }

    private void popTransform() {
        if (pushed) {
            canvas.restore();
        }
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        // Reset paint opacity
        paint.setAlpha(255);
        // Ignore everything but rectangles in bounds mode
        if (boundsMode) {
            if (localName.equals("rect")) {
                Float x = SVGParser.getFloatAttr("x", atts);
                if (x == null) {
                    x = 0f;
                }
                Float y = SVGParser.getFloatAttr("y", atts);
                if (y == null) {
                    y = 0f;
                }
                Float width = SVGParser.getFloatAttr("width", atts);
                Float height = SVGParser.getFloatAttr("height", atts);
                bounds = new RectF(x, y, x + width, y + height);
            }
            return;
        }
        if (localName.equals("svg")) {
            int width = (int) Math.ceil(SVGParser.getFloatAttr("width", atts));
            int height = (int) Math.ceil(SVGParser.getFloatAttr("height", atts));
            canvas = picture.beginRecording(width, height);
        } else if (localName.equals("defs")) {
            // Ignore
        } else if (localName.equals("linearGradient")) {
            gradient = doGradient(true, atts);
        } else if (localName.equals("radialGradient")) {
            gradient = doGradient(false, atts);
        } else if (localName.equals("stop")) {
            if (gradient != null) {
                float offset = SVGParser.getFloatAttr("offset", atts);
                String styles = SVGParser.getStringAttr("style", atts);
                StyleSet styleSet = new StyleSet(styles);
                String colorStyle = styleSet.getStyle("stop-color");
                int color = Color.BLACK;
                if (colorStyle != null) {
                    if (colorStyle.startsWith("#")) {
                        color = Integer.parseInt(colorStyle.substring(1), 16);
                    } else {
                        color = Integer.parseInt(colorStyle, 16);
                    }
                }
                String opacityStyle = styleSet.getStyle("stop-opacity");
                if (opacityStyle != null) {
                    float alpha = Float.parseFloat(opacityStyle);
                    int alphaInt = Math.round(255 * alpha);
                    color |= (alphaInt << 24);
                } else {
                    color |= 0xFF000000;
                }
                gradient.positions.add(offset);
                gradient.colors.add(color);
            }
        } else if (localName.equals("g")) {
        	Properties props = new Properties(atts);
            // Check to see if this is the "bounds" layer
            if ("bounds".equalsIgnoreCase(SVGParser.getStringAttr("id", atts))) {
                boundsMode = true;
            }
            if (hidden) {
                hiddenLevel++;
                //Util.debug("Hidden up: " + hiddenLevel);
            }
            // Go in to hidden mode if display is "none"
            if ("none".equals(props.getString("display"))) {
                if (!hidden) {
                    hidden = true;
                    hiddenLevel = 1;
                    //Util.debug("Hidden up: " + hiddenLevel);
                }
            }
            else
            {
            	 pushTransform(atts);
            }
        } else if (!hidden && localName.equals("rect")) {
            Float x = SVGParser.getFloatAttr("x", atts);
            if (x == null) {
                x = 0f;
            }
            Float y = SVGParser.getFloatAttr("y", atts);
            if (y == null) {
                y = 0f;
            }
            Float width = SVGParser.getFloatAttr("width", atts);
            Float height = SVGParser.getFloatAttr("height", atts);
            Float radiusX = SVGParser.getFloatAttr("rx", atts);
            Float radiusY = SVGParser.getFloatAttr("ry", atts);
            if(radiusX == null) radiusX = radiusY;
            if(radiusY == null) radiusY = radiusX;
            if(radiusX == null) radiusX = 0f;
            if(radiusY == null) radiusY = 0f;
            pushTransform(atts);
            Properties props = new Properties(atts);
            if (doFill(props, gradientMap)) {
                doLimits(x, y, width, height);
                canvas.drawRoundRect(new RectF(x, y, x+width, y+height), radiusX, radiusY, paint);
            }
            if (doStroke(props)) {
            	canvas.drawRoundRect(new RectF(x, y, x+width, y+height), radiusX, radiusY, paint);
            }
            popTransform();
        } else if (!hidden && localName.equals("line")) {
            Float x1 = SVGParser.getFloatAttr("x1", atts);
            Float x2 = SVGParser.getFloatAttr("x2", atts);
            Float y1 = SVGParser.getFloatAttr("y1", atts);
            Float y2 = SVGParser.getFloatAttr("y2", atts);
            Properties props = new Properties(atts);
            if (doStroke(props)) {
                pushTransform(atts);
                doLimits(x1, y1);
                doLimits(x2, y2);
                canvas.drawLine(x1, y1, x2, y2, paint);
                popTransform();
            }
        } else if (!hidden && localName.equals("circle")) {
            Float centerX = SVGParser.getFloatAttr("cx", atts);
            Float centerY = SVGParser.getFloatAttr("cy", atts);
            Float radius = SVGParser.getFloatAttr("r", atts);
            if (centerX != null && centerY != null && radius != null) {
                pushTransform(atts);
                Properties props = new Properties(atts);
                if (doFill(props, gradientMap)) {
                    doLimits(centerX - radius, centerY - radius);
                    doLimits(centerX + radius, centerY + radius);
                    canvas.drawCircle(centerX, centerY, radius, paint);
                }
                if (doStroke(props)) {
                    canvas.drawCircle(centerX, centerY, radius, paint);
                }
                popTransform();
            }
        } else if (!hidden && localName.equals("ellipse")) {
            Float centerX = SVGParser.getFloatAttr("cx", atts);
            Float centerY = SVGParser.getFloatAttr("cy", atts);
            Float radiusX = SVGParser.getFloatAttr("rx", atts);
            Float radiusY = SVGParser.getFloatAttr("ry", atts);
            if (centerX != null && centerY != null && radiusX != null && radiusY != null) {
                pushTransform(atts);
                Properties props = new Properties(atts);
                rect.set(centerX - radiusX, centerY - radiusY, centerX + radiusX, centerY + radiusY);
                if (doFill(props, gradientMap)) {
                    doLimits(centerX - radiusX, centerY - radiusY);
                    doLimits(centerX + radiusX, centerY + radiusY);
                    canvas.drawOval(rect, paint);
                }
                if (doStroke(props)) {
                    canvas.drawOval(rect, paint);
                }
                popTransform();
            }
        } else if (!hidden && (localName.equals("polygon") || localName.equals("polyline"))) {
            NumberParse numbers = SVGParser.getNumberParseAttr("points", atts);
            if (numbers != null) {
                Path p = new Path();
                ArrayList<Float> points = numbers.numbers;
                if (points.size() > 1) {
                    pushTransform(atts);
                    Properties props = new Properties(atts);
                    p.moveTo(points.get(0), points.get(1));
                    for (int i = 2; i < points.size(); i += 2) {
                        float x = points.get(i);
                        float y = points.get(i + 1);
                        p.lineTo(x, y);
                    }
                    // Don't close a polyline
                    if (localName.equals("polygon")) {
                        p.close();
                    }
                    if (doFill(props, gradientMap)) {
                        doLimits(p);
                        canvas.drawPath(p, paint);
                    }
                    if (doStroke(props)) {
                        canvas.drawPath(p, paint);
                    }
                    popTransform();
                }
            }
        } else if (!hidden && localName.equals("path")) {
            Path p = SVGParser.doPath(SVGParser.getStringAttr("d", atts));
            pushTransform(atts);
            Properties props = new Properties(atts);
            if (doFill(props, gradientMap)) {
                doLimits(p);
                canvas.drawPath(p, paint);
            }
            if (doStroke(props)) {
                canvas.drawPath(p, paint);
            }
            popTransform();
        } else if (!hidden) {
            Log.d(SVGParser.TAG, "UNRECOGNIZED SVG COMMAND: " + localName);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) {
        // no-op
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {
        if (localName.equals("svg")) {
            picture.endRecording();
        } else if (localName.equals("linearGradient")) {
            if (gradient.id != null) {
                if (gradient.xlink != null) {
                    Gradient parent = gradientRefMap.get(gradient.xlink);
                    if (parent != null) {
                        gradient = parent.createChild(gradient);
                    }
                }
                int[] colors = new int[gradient.colors.size()];
                for (int i = 0; i < colors.length; i++) {
                    colors[i] = gradient.colors.get(i);
                }
                float[] positions = new float[gradient.positions.size()];
                for (int i = 0; i < positions.length; i++) {
                    positions[i] = gradient.positions.get(i);
                }
                if (colors.length == 0) {
                    Log.d("BAD", "BAD");
                }
                TileMode mode = TileMode.CLAMP;
                if("reflect".equals(gradient.spread))
                {
                	mode = TileMode.MIRROR;
                }
                else if(("pad").equals(gradient.spread))
                {
                	mode = TileMode.CLAMP;
                }
                else if(("repeat").equals(gradient.spread))
                {
                	mode = TileMode.REPEAT;
                }
                LinearGradient g = new LinearGradient(gradient.x1, gradient.y1, gradient.x2, gradient.y2, colors, positions, mode);
                if (gradient.matrix != null) {
                    g.setLocalMatrix(gradient.matrix);
                }
                gradientMap.put(gradient.id, g);
                gradientRefMap.put(gradient.id, gradient);
            }
        } else if (localName.equals("radialGradient")) {
            if (gradient.id != null) {
                if (gradient.xlink != null) {
                    Gradient parent = gradientRefMap.get(gradient.xlink);
                    if (parent != null) {
                        gradient = parent.createChild(gradient);
                    }
                }
                int[] colors = new int[gradient.colors.size()];
                for (int i = 0; i < colors.length; i++) {
                    colors[i] = gradient.colors.get(i);
                }
                float[] positions = new float[gradient.positions.size()];
                for (int i = 0; i < positions.length; i++) {
                    positions[i] = gradient.positions.get(i);
                }
                if (gradient.xlink != null) {
                    Gradient parent = gradientRefMap.get(gradient.xlink);
                    if (parent != null) {
                        gradient = parent.createChild(gradient);
                    }
                }
                RadialGradient g = new RadialGradient(gradient.x, gradient.y, gradient.radius, colors, positions, Shader.TileMode.CLAMP);
                if (gradient.matrix != null) {
                    g.setLocalMatrix(gradient.matrix);
                }
                gradientMap.put(gradient.id, g);
                gradientRefMap.put(gradient.id, gradient);
            }
        } else if (localName.equals("g")) {
            if (boundsMode) {
                boundsMode = false;
            }
            // Break out of hidden mode
            if (hidden) {
                hiddenLevel--;
                //Util.debug("Hidden down: " + hiddenLevel);
                if (hiddenLevel == 0) {
                    hidden = false;
                }
            }
            else
            {
            	popTransform();
            }
            // Clear gradient map
           // XXX gradientMap.clear();
        }
    }
}