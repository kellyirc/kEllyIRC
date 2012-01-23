package shared;

import java.util.HashMap;

import lombok.Getter;

import connection.Settings;

public class Quicklinks {

	public static boolean hasQuicklink(String s) {
		if(!s.contains(":")) return false;
		return quicklinks.containsKey(s.split(":")[0]);
	}

	public static String getLink(String split) {
		if(!split.contains(":")) return "";
		String[] arr = split.split(":");
		return getLink(arr[0], arr[1]);
	}
	
	public static String getLink(String link, String input) {
		return quicklinks.get(link).replaceAll("%INPUT%", input);
	}

	private static @Getter HashMap<String, String> quicklinks = new HashMap<>();

	public static void addLink(String text, String text2) {
		quicklinks.put(text, text2);
		Settings.getSettings().setQuicklinks(quicklinks);
		Settings.writeToFile();
	}
	
}
