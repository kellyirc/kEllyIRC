/*
 * @author Kyle Kemp
 */
package shared;

import java.util.HashMap;

import lombok.Getter;

import connection.Settings;

/**
 * The Class Quicklinks.
 */
public class Quicklinks {

	/**
	 * Checks for quicklink.
	 *
	 * @param s the s
	 * @return true, if successful
	 */
	public static boolean hasQuicklink(String s) {
		if(!s.contains(":")) return false;
		return quicklinks.containsKey(s.split(":")[0]);
	}

	/**
	 * Gets the link.
	 *
	 * @param split the split
	 * @return the link
	 */
	public static String getLink(String split) {
		if(!split.contains(":")) return "";
		String[] arr = split.split(":");
		return getLink(arr[0], arr[1]);
	}
	
	/**
	 * Gets the link.
	 *
	 * @param link the link
	 * @param input the input
	 * @return the link
	 */
	public static String getLink(String link, String input) {
		return quicklinks.get(link).replaceAll("%INPUT%", input);
	}

	/** The quicklinks. */
	private static /**
  * Gets the quicklinks.
  *
  * @return the quicklinks
  */
 @Getter HashMap<String, String> quicklinks = new HashMap<>();

	/**
	 * Adds the link.
	 *
	 * @param text the text
	 * @param text2 the text2
	 */
	public static void addLink(String text, String text2) {
		quicklinks.put(text, text2);
		Settings.getSettings().setQuicklinks(quicklinks);
		Settings.writeToFile();
	}
	
}
