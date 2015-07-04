package cx.mccormick.pddroidparty;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;


public class PdDroidPartyLauncher extends Activity
{
	/**
	 * Launch a Pure Data Activity
	 * @param activity calling activity (usually "this" when launched from another activity)
	 * @param patchPath path to the pure data patch file relative to assets directory.
	 */
	public static void launch(Activity activity, String patchPath)
	{
		// copy patch folder to cache directory
		// needed for java.io.File manipulations
		// clear cache to ensure synchronized folders
		
		trimCache(activity);

        String patchFolder = new File(patchPath).getParent();
		File cachePatchFolder = new File(activity.getCacheDir(), patchFolder);
        try {
			copyAssetFolder(activity.getAssets(), patchFolder, 
					cachePatchFolder.getAbsolutePath());
		} catch (IOException e) {
			throw new Error(e);
		}
        File cachePatchFile = new File(activity.getCacheDir(), patchPath);
		
        // start the intent
		Intent intent = new Intent(activity, PdDroidParty.class);
		intent.putExtra(PdDroidParty.PATCH, cachePatchFile.getAbsolutePath());
		activity.startActivity(intent);
		activity.finish();
	}
	
	private static void trimCache(Context context) {
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
    
    private static void copyAssetFolder(AssetManager assetManager,
            String fromAssetPath, String toPath) throws IOException {
        String[] files = assetManager.list(fromAssetPath);
        new File(toPath).mkdirs();
        for (String file : files)
            if (file.contains("."))
                copyAsset(assetManager, 
                        fromAssetPath + "/" + file,
                        toPath + "/" + file);
            else 
                copyAssetFolder(assetManager, 
                        fromAssetPath + "/" + file,
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

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
          out.write(buffer, 0, read);
        }
    }

}
