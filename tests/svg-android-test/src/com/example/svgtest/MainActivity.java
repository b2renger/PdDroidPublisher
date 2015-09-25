package com.example.svgtest;

import java.io.IOException;

import android.app.Activity;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParseException;
import com.larvalabs.svgandroid.SVGParser;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		
		try {
			final SVG svg = SVGParser.getSVGFromInputStream(this.getAssets().open("test.svg"));
			// svg.getPicture().
			final PictureDrawable img = svg.createPictureDrawable();
			img.setBounds(0,0,200, 200);
			
			ImageView view = new ImageView(this);
			view.setImageDrawable(img);
			
			view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			
			setContentView(view);
			
			
		} catch (SVGParseException e) {
			throw new Error(e);
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
