package cx.mccormick.pddroidparty.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;

public class FileHelper {

	public static String readTextFile(File file)
	{
		StringBuffer contents = new StringBuffer();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;

			// repeat until all lines is read
			while ((text = reader.readLine()) != null) {
				contents.append(text)
					.append(System.getProperty(
						"line.separator"));
			}
		} catch (FileNotFoundException e) {
			throw new Error(e);
		} catch (IOException e) {
			throw new Error(e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				throw new Error(e);
			}
		}
		
		// show file contents here
		return contents.toString();
	}
	
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
