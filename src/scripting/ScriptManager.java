package scripting;

import java.io.File;
import java.util.concurrent.ConcurrentSkipListSet;

public class ScriptManager {
	public static final ConcurrentSkipListSet<Script> scripts = new ConcurrentSkipListSet<Script>();

	public static void addScript(File f) {
		scripts.add(new Script(f));
	}

	public static void removeScript(File f) {
		for(Script s : scripts){
			if(s.getReference().equals(f)){
				scripts.remove(s);
				s.getFunctions().clear();
			}
		}
	}

	public static void modifyScript(File f) {
		for(Script s : scripts){
			if(s.getReference().equals(f)){
				s.reset();
			}
		}
	}

	public static final String[] validExt = {".js",".rb",".py"};
}
