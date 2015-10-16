package net.mgsx.ppp.pd;

import java.io.File;
import java.io.IOException;

import org.puredata.core.PdBase;

public class PdPatch 
{
	private File patchFile;
	private int dollarzero = -1;
	
	public PdPatch(String patchPath) 
	{
		this.patchFile = new File(patchPath);
	}

	/**
	 * Open patch in PureData
	 * @throws IOException 
	 */
	public void open() throws IOException
	{
		dollarzero = PdBase.openPatch(patchFile);
	}
	
	public String replaceDollarZero(String name) {
		return name.replace("\\$0", dollarzero + "").replace("$0", dollarzero + "");
	}

	public File getFile() 
	{
		return patchFile;
	}

	/**
	 * @param path
	 * @return file relative to patch directory
	 */
	public File getFile(String path) 
	{
		return new File(patchFile.getParent(), path);
	}

	public void close() 
	{
		if (dollarzero != -1) {
			PdBase.closePatch(dollarzero);
		}
	}

	
	
}
