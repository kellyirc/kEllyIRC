package shared;

import java.util.HashMap;

public class Quicklinks {

	public static boolean hasQuicklink(String s) {
		return quicklinks.containsKey(s);
	}

	public static String getLink(String s) {
		return quicklinks.get(s);
	}

	private static HashMap<String, String> quicklinks = new HashMap<>();
	
}
