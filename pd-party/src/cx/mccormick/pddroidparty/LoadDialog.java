package cx.mccormick.pddroidparty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.puredata.core.utils.IoUtils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class LoadDialog extends Dialog {
	
	private File directory;
	private String extension;
	private String selectedFilename;
	
	public LoadDialog(Context context, File directory, String extension) {
		super(context);
		this.directory = directory;
		this.extension = extension;
	}
	
	public String getSelectedFilename() {
		return selectedFilename;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load_dialog);
		ArrayList<String> filenames = new ArrayList<String>();
		final ListView filelist = (ListView)findViewById(R.id.filelist);
		List<File> list = IoUtils.find(directory, ".*" + (extension == null || extension.isEmpty() ? "" : "\\." + extension) + "$");
		for (File f: list) {
			String fn = f.getName();
			int i = fn.lastIndexOf('.');
			if (i > 0 && i < fn.length() - 1) {
				filenames.add(fn.substring(0, i));
			}
		}
		if (filenames.size() > 0) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, filenames);
			filelist.setAdapter(adapter);
		}
		
		Button cancel = (Button)findViewById(R.id.cancel);
		cancel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedFilename = null;
				dismiss();
			}
		});
		
		filelist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				TextView item = (TextView) v;
				selectedFilename = item.getText().toString();
				dismiss();
			}
		});
	}
}
