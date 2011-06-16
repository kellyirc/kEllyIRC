package scripting;

import java.io.File;
import java.util.concurrent.ConcurrentSkipListSet;

public final class ScriptManager {
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
	
	public static final String[] validKeywords = {"break","case","catch","continue","default","do",
			"else","false","finally","for","if","new","null","return","switch",
			"true","try","while","function","var","gui","util","in","global","print","println"
};
}
