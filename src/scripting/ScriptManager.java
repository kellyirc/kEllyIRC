package scripting;

import java.io.File;
import java.util.LinkedList;

public class ScriptManager {
	public static final LinkedList<File> scripts = new LinkedList<File>();

	public static void addScript(File f) {
		scripts.add(f);
	}

	public static void removeScript(File f) {
		scripts.remove(f);
	}

	public static void modifyScript(File f) {

	}
}
