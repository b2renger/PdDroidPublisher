package cx.mccormick.pddroidparty.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;

public class FileHelper {

	public static void trimCache(Context context) {
		File dir = context.getCacheDir();
		if (dir != null && dir.isDirectory()) {
			deleteDir(dir);
		}
	}

	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	public static void copyAssetFolder(AssetManager assetManager,
			String fromAssetPath, String toPath) throws IOException {
		String[] files = assetManager.list(fromAssetPath);
		new File(toPath).mkdirs();
		for (String file : files)
			if (file.contains("."))
				copyAsset(assetManager, fromAssetPath + "/" + file, toPath
						+ "/" + file);
			else
				copyAssetFolder(assetManager, fromAssetPath + "/" + file,
						toPath + "/" + file);
	}

	private static void copyAsset(AssetManager assetManager,
			String fromAssetPath, String toPath) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		in = assetManager.open(fromAssetPath);
		new File(toPath).createNewFile();
		out = new FileOutputStream(toPath);
		copyFile(in, out);
		in.close();
		in = null;
		out.flush();
		out.close();
		out = null;
	}

	private static void copyFile(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

}
