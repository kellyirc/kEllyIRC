/*
 * @author Kyle Kemp
 */
package scripting;

import java.io.File;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * The Class ScriptManager.
 */
public final class ScriptManager {
	
	/** The global list of scripts. */
	public static final ConcurrentSkipListSet<Script> scripts = new ConcurrentSkipListSet<Script>();

	/**
	 * Adds the script.
	 *
	 * @param f the f
	 */
	public static void addScript(File f) {
		scripts.add(new Script(f));
	}

	/**
	 * Removes the script from the list of scripts.
	 *
	 * @param f the f
	 */
	public static void removeScript(File f) {
		for(Script s : scripts){
			if(s.getReference().equals(f)){
				scripts.remove(s);
				s.getFunctions().clear();
			}
		}
	}

	/**
	 * A script was modified on the file system, so this is called as a result.
	 *
	 * @param f the f
	 */
	public static void modifyScript(File f) {
		for(Script s : scripts){
			if(s.getReference().equals(f)){
				s.reset();
			}
		}
	}

	/** The Constant validExt. */
	public static final String[] validExt = {".js",".rb"}; // ".py", ".lua"
	
	/** The Constant globalKeywords. */
	public static final String[] globalKeywords = {"gui","util","in","global","print","println","sound"};
	
	/** The Constant jsKeywords. */
	public static final String[] jsKeywords = {"break","case","catch","continue","default","do",
		"else","false","finally","for","if","new","null","return","switch",
		"true","try","while","function","var",
		
		"importPackage","importClass"
	};
	
	/** The Constant rbKeywords. */
	public static final String[] rbKeywords = {"alias","and","BEGIN","break","case","class","def","defined",
		"do","else","elsif","END","end","ensure","false","for","if","in","module","next","nil","not","or",
		"redo","rescue","retry","return","self","super","then","true","undef","unless","until","when","while",
		"yield","require",
		
		"java_import","import"
	};
	
	/** The Constant pyKeywords. */
	public static final String[] pyKeywords = {"and","as","assert","break","class","continue","def","del","elif",
		"else","except","exec","finally","for","from","global","if","import","in","is","lambda","not","or","pass",
		"print","raise","return","try","while","with","yield"
	};
	
	/** The Constant luaKeywords. */
	public static final String[] luaKeywords = {"break","do","end","else","elseif","function","if",
		"local","nil","not","or","repeat","return","then","until","while"
	};
}
