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

import org.pircbotx.hooks.Event;

import connection.KEllyBot;

import lombok.Getter;
import lombok.Setter;

import sun.org.mozilla.javascript.internal.NativeArray;

public class Script implements Comparable<Script> {
	
	@Getter @Setter
	private boolean inUse = true;
	String language;
	@Getter
	private String script;
	@Getter
	private File reference;
	@Getter
	private ArrayList<String> functions = new ArrayList<String>();
	@Getter
	private ArrayList<String> descriptFunctions = new ArrayList<String>();
	
	private ScriptEngineManager manager = new ScriptEngineManager();
	private ScriptEngine jsEngine = manager.getEngineByName("JavaScript");
	private Invocable engine = (Invocable) jsEngine;
	private Bindings bindings = jsEngine.getBindings(ScriptContext.ENGINE_SCOPE);
	
	private String base = 	"importClass(org.eclipse.swt.SWT);\n" +
							"importClass(Packages.hexapixel.notifier.NotificationType);\n";
	
	public Script(File f) {
		
		this.reference = f;
		
		reset();
	}

	public void reset() {
		readScript();
		
		initBindings();
		
		initialize();
	}

	private void initialize() {
		try {
			jsEngine.eval(base+script);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	private void initBindings() {
		bindings.put("util", new ScriptFunctions());
		bindings.put("gui", new ScriptGUI());
	}

	private void readScript() {

		//reset the functions list
		functions.clear();
		
		//re-parse the script
		StringBuffer contents = new StringBuffer();
		
		BufferedReader reader = null;
		if(!reference.exists()) return;
		try {
			reader = new BufferedReader(new FileReader(reference));
			String text = null;
			while((text = reader.readLine())!=null){
				contents.append(text).append(System.getProperty("line.separator"));
				parseFunction(text);
			}
			reader.close();
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
		String elFunction = text.substring(9).split("[{]")[0].trim();
		descriptFunctions.add(elFunction);
		String[] array = text.replaceAll("[(]", " ").split(" ");
		//function, onFunctionName, event), {
		functions.add(array[1].trim());
	}
	
	//event invocation
	public void invoke(String function, Event<KEllyBot> e){
		if(!inUse)return;
		try {
			engine.invokeFunction(function, e);
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		} catch (ScriptException e1) {
			e1.printStackTrace();
		}
	}

	//open-ended invocation
	public void invoke(String command, Object... args) {
		try {
			engine.invokeFunction(command, args);
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		} catch (ScriptException e1) {
			e1.printStackTrace();
		}
	}
	
	public Object[] invoke(String command) {
		Object[] rv = null;
		try {
			NativeArray arr = (NativeArray) engine.invokeFunction(command);
			rv = new Object[(int)arr.getLength()];
			
			//get as object
			for(Object o : arr.getIds()) {
				int index = (Integer) o;
				rv[index] = arr.get(index, null);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return rv;
	}
	
	@Override
	public int compareTo(Script o) {
		return reference.getName().compareTo(o.reference.getName());
	}
}
