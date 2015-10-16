package net.mgsx.ppp.widget.abs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.mgsx.ppp.LoadDialog;
import net.mgsx.ppp.PdDroidPartyLauncher;
import net.mgsx.ppp.SaveDialog;
import net.mgsx.ppp.view.PdDroidPatchView;
import net.mgsx.ppp.widget.Widget;

import org.puredata.core.PdBase;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;

public class LoadSave extends Widget {
	private PdDroidPatchView parent = null;
	private String filename = "";
	private String directory = ".";
	private String extension = "";
	private String sendreceive = null;
	
	public LoadSave(PdDroidPatchView app, String[] atomline) {
		super(app);
		parent = app;
		sendreceive = atomline[5];
		app.registerReceiver(sendreceive, this);
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getDirectory() {
		return directory;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public void receiveMessage(String symbol, Object... args) {
		directory = args.length > 0 ? (String)args[0] : ".";
		extension = args.length > 1 ? (String)args[1] : "";
		if (symbol.equals("save")) {
			final SaveDialog saveDialog = new SaveDialog(parent.getContext(), filename);
			saveDialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					String selectedFilename = saveDialog.getSelectedFilename();
					if(selectedFilename != null)
					{
						gotFilename("save", PdDroidPartyLauncher.getPersistDirectory(parent.getContext()), selectedFilename);
					}
				}
			});
			saveDialog.show();
		} else if (symbol.equals("load")) {
			final LoadDialog loadDialog = new LoadDialog(parent.getContext(), PdDroidPartyLauncher.getPersistDirectory(parent.getContext()), extension);
			loadDialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					String selectedFilename = loadDialog.getSelectedFilename();
					if(selectedFilename != null)
					{
						gotFilename("load", PdDroidPartyLauncher.getPersistDirectory(parent.getContext()), selectedFilename);
					}
				}
			});
			loadDialog.show();
		}
	}
	
	public void gotFilename(String type, File baseDirectory, String newname) {
		filename = newname;
		String directoryPath = new File(baseDirectory, directory).getAbsolutePath();
		List<Object> details = new ArrayList<Object>();
		details.add(directoryPath);
		details.add(filename);
		details.add(extension);
		Object[] ol = details.toArray();
		PdBase.sendList(sendreceive + "-" + type + "-detail", ol);
		PdBase.sendSymbol(sendreceive + "-" + type, directoryPath + "/" + filename + "." + extension);
	}
}
