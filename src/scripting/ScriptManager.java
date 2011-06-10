package scripting;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ScriptManager {
	public static final ConcurrentLinkedQueue<Script> scripts = new ConcurrentLinkedQueue<Script>();

	public static void addScript(File f) {
		scripts.add(new Script(f));
	}

	public static void removeScript(File f) {
		for(Script s : scripts){
			if(s.getReference().equals(f)){
				scripts.remove(f);
				s.getFunctions().clear();
			}
		}
	}

	public static void modifyScript(File f) {
		for(Script s : scripts){
			if(s.getReference().equals(f)){
				removeScript(f);
				addScript(f);
			}
		}
	}
}
