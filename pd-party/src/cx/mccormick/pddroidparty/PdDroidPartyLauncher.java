package cx.mccormick.pddroidparty;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import cx.mccormick.pddroidparty.util.FileHelper;


public class PdDroidPartyLauncher extends Activity
{
	/**
	 * Launch a Pure Data Activity with default configuration.
	 * Convenient method to call {@link #launch(Activity, String, PdDroidPartyConfig))} with
	 * a new PdDroidPartyConfig().
	 * @param activity calling activity (usually "this" when launched from another activity)
	 * @param patchPath path to the pure data patch file relative to assets directory.
	 */
	public static void launch(Activity activity, String patchPath)
	{
		launch(activity, patchPath, new PdDroidPartyConfig());
	}
	/**
	 * Launch a Pure Data Activity
	 * @param activity calling activity (usually "this" when launched from another activity)
	 * @param patchPath path to the pure data patch file relative to assets directory.
	 */
	public static void launch(Activity activity, String patchPath, PdDroidPartyConfig config)
	{
		// copy patch folder to cache directory
		// needed for java.io.File manipulations
		// clear cache to ensure synchronized folders
		
		FileHelper.trimCache(activity);

        String patchFolder = new File(patchPath).getParent();
		File cachePatchFolder = new File(activity.getCacheDir(), patchFolder);
        try {
        	FileHelper.copyAssetFolder(activity.getAssets(), patchFolder, 
					cachePatchFolder.getAbsolutePath());
		} catch (IOException e) {
			throw new Error(e);
		}
        File cachePatchFile = new File(activity.getCacheDir(), patchPath);
		
        // start the intent
		Intent intent = new Intent(activity, PdDroidParty.class);
		intent.putExtra(PdDroidParty.INTENT_EXTRA_PATCH_PATH, cachePatchFile.getAbsolutePath());
		intent.putExtra(PdDroidPartyConfig.class.getName(), config);
		activity.startActivity(intent);
		activity.finish();
	}

}
