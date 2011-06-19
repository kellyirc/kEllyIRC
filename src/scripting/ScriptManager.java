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
	
	public static final String[] jsKeywords = {"break","case","catch","continue","default","do",
			"else","false","finally","for","if","new","null","return","switch",
			"true","try","while","function","var","gui","util","in","global","print","println","sound"
	};
	
	public static final String[] rbKeywords = {"alias","and","BEGIN","break","case","class","def","defined",
		"do","else","elsif","END","end","ensure","false","for","if","in","module","next","nil","not","or",
		"redo","rescue","retry","return","self","super","then","true","undef","unless","until","when","while",
		"yield"
	};
	
	public static final String[] pyKeywords = {"and","as","assert","break","class","continue","def","del","elif",
		"else","except","exec","finally","for","from","global","if","import","in","is","lambda","not","or","pass",
		"print","raise","return","try","while","with","yield"
	};
	
	public static final String[] luaKeywords = {"break","do","end","else","elseif","function","if",
		"local","nil","not","or","repeat","return","then","until","while"
	};
}
