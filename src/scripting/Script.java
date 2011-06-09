package scripting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import lombok.Cleanup;

public class Script {
	
	String language;
	private String script;
	private File reference;
	private ArrayList<String> functions = new ArrayList<String>();
	
	private ScriptEngineManager manager = new ScriptEngineManager();
	private ScriptEngine jsEngine = manager.getEngineByName("JavaScript");
	private Invocable engine = (Invocable) jsEngine;
	private Bindings bindings = jsEngine.getBindings(ScriptContext.ENGINE_SCOPE);
	
	public Script(File f) {
		
		this.reference = f;
		
		readScript();
		
		initBindings();
		
		initialize();
	}

	private void initialize() {
		try {
			jsEngine.eval(script);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	private void initBindings() {
		bindings.put("util", new ScriptFunctions());
		//bindings.put("con", new ScriptFunctions());
		//TODO more bindings
	}

	private void readScript() {

		//reset the functions list
		functions.clear();
		
		//re-parse the script
		StringBuffer contents = new StringBuffer();
		
		try {
			@Cleanup BufferedReader reader = new BufferedReader(new FileReader(reference));
			String text = null;
			while((text = reader.readLine())!=null){
				contents.append(text).append(System.getProperty("line.separator"));
				parseFunction(text);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		script = contents.toString();
	}
	
	//check line-by-line for a function name
	private void parseFunction(String text) {
		if(!text.contains("function")) return;
		String[] array = text.replaceAll("[(]", " ").split(" ");
		for(String s : array){
			//TODO test this
			System.out.println(s);
		}
	}
}
