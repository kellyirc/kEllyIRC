package scripting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.eclipse.swt.SWT;
import org.pircbotx.hooks.Event;

import connection.KEllyBot;

import lombok.Getter;
import lombok.Setter;

import shared.NSAlertBox;
import sun.org.mozilla.javascript.internal.NativeArray;

public final class Script implements Comparable<Script> {
	
	@Getter @Setter
	private boolean inUse = true;
	String language;
	@Getter
	private String script;
	@Getter
	private File reference;
	@Getter
	private ConcurrentSkipListSet<String> functions = new ConcurrentSkipListSet<String>();
	@Getter
	private ConcurrentSkipListSet<String> descriptFunctions = new ConcurrentSkipListSet<String>();
	
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

			new NSAlertBox("Script Read Error", reference.getName()+" has an error. Due to error reporting methods, I can not help you narrow down the issue.", SWT.ICON_ERROR);

			org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.script.scripts");
			fLog.error("Script initialization failed: "+reference.getName()+" at line #"+e.getLineNumber());
		}
	}

	private void initBindings() {
		bindings.put("global", new ScriptVars());
		bindings.put("util", new ScriptFunctions());
		bindings.put("gui", new ScriptGUI());
	}

	public void readScript() {

		//reset the functions list
		functions.clear();
		descriptFunctions.clear();
		
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
		} catch (Exception e) {
			org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.script.scripts");
			fLog.error("Script reading failed.", e);
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
			org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.script.scripts");
			fLog.error("Script invocation failed.", e1);
		} catch (ScriptException e1) {
			org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.script.scripts");
			fLog.error("Script invocation failed.", e1);
		}
	}

	//open-ended invocation
	public void invoke(String command, Object... args) { 
		if(!inUse)return;
		try {
			engine.invokeFunction(command, args);
		} catch (NoSuchMethodException e1) {
			org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.script.scripts");
			fLog.error("Script invocation failed.", e1);
		} catch (ScriptException e1) {
			org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.script.scripts");
			fLog.error("Script invocation failed.", e1);
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
			org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.script.scripts");
			fLog.error("Script invocation failed.", e);
		} catch (ScriptException e) {
			org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.script.scripts");
			fLog.error("Script invocation failed.", e);
		}
		return rv;
	}
	
	@Override
	public int compareTo(Script o) {
		return reference.getName().compareTo(o.reference.getName());
	}
}
