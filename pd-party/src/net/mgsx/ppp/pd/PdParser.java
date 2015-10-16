package net.mgsx.ppp.pd;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mgsx.ppp.util.FileHelper;

public final class PdParser {
	private static final String line_re = "(#((.|\r|\n)*?)[^\\\\])\r{0,1}\n{0,1};\r{0,1}\n";
	private static final String token_re = " |\r\n?|\n";
	
	// read a file and get an array of arrays of atoms (strings)
	public static List<String[]> parsePatch(PdPatch patch) {
		return getAtomLines(readPatch(patch));
	}
	
	// read a text file
	private static String readPatch(PdPatch patch)
	{
		return FileHelper.readTextFile(patch.getFile());
	}
	
	// use a regular expression to extract rows of atoms from a given text
	public static List<String[]> getAtomLines(String patchtext) {
		Pattern pattern = Pattern.compile(line_re, Pattern.MULTILINE);
		Pattern token_pattern = Pattern.compile(token_re, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(patchtext);
		List<String[]> atomlines = new ArrayList<String[]>();
		while (matcher.find()) {
			atomlines.add(token_pattern.split(matcher.group(1)));
		}
		return atomlines;
	}
}
