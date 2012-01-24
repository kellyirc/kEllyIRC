/*
 * @author Kyle Kemp
 */
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

/**
 * The Class Script.
 */
public final class Script implements Comparable<Script> {
	
	/** The Constant JAVASCRIPT. */
	public static final int JAVASCRIPT = 1;
	
	/** The Constant RUBY. */
	public static final int RUBY = 2;
	
	//XXX not supported yet
	/** The Constant PYTHON. */
	public static final int PYTHON = 3;
	
	/**
	 * Gets the script type.
	 *
	 * @return the script type
	 */
	@Getter
	private int scriptType=0;
	
	/**
	 * Checks if is in use.
	 *
	 * @return true, if is in use
	 */
	@Getter /**
  * Sets the in use.
  *
  * @param inUse the new in use
  */
 @Setter
	private boolean inUse = true;
	
	/**
	 * Gets the script.
	 *
	 * @return the script
	 */
	@Getter
	private String script;
	
	/**
	 * Gets the file reference.
	 *
	 * @return the reference
	 */
	@Getter
	private File reference;
	
	/**
	 * Gets the function list associated with this script.
	 *
	 * @return the functions
	 */
	@Getter
	private ConcurrentSkipListSet<String> functions = new ConcurrentSkipListSet<String>();
	
	/**
	 * Gets the descript version of the functions associated with this script.
	 *
	 * @return the descript functions
	 */
	@Getter
	private ConcurrentSkipListSet<String> descriptFunctions = new ConcurrentSkipListSet<String>();
	
	/**
	 * Gets the name of the script.
	 *
	 * @return the name
	 */
	@Getter
	private String name;
	
	/** The newline constant. */
	private static final String nl = System.getProperty("line.separator");
	
	/** The manager. */
	private static ScriptEngineManager manager = new ScriptEngineManager();
	
	/** The js engine. */
	private static ScriptEngine jsEngine = manager.getEngineByName("JavaScript");
	
	/** The rb engine. */
	private static ScriptEngine rbEngine = manager.getEngineByName("jruby");
	
	/** The js invocable. */
	private static Invocable jsInvocable = (Invocable) jsEngine;
	
	/** The rb invocable. */
	private static Invocable rbInvocable = (Invocable) rbEngine;
	
	/** The bindings. */
	private static Bindings bindings = jsEngine.getBindings(ScriptContext.ENGINE_SCOPE);
	
	/** The rb base script. */
	private String rbBase = "require \"java\""+nl;
	
	/** The js base script. */
	private String jsBase = "importClass(org.eclipse.swt.SWT);" +nl+
							"importClass(Packages.hexapixel.notifier.NotificationType);" +nl+
							"importClass(Packages.org.pircbotx.Colors);" +nl+
							"importClass(java.lang.Thread);" +nl+
							"importClass(Packages.connection.KEllyBot);" +nl+
							"importPackage(org.pircbotx);" +nl+
							"importPackage(java.util);" +nl+
							"importPackage(java.lang);" +nl;
	
	/**
	 * Instantiates a new script.
	 *
	 * @param f the f
	 */
	public Script(File f) {
		this.reference = f;
		this.name = f.getName();
		
		String s = reference.getName().substring(reference.getName().indexOf('.'));

		if(s.equals(".js")){
			scriptType=JAVASCRIPT;
		} else if(s.equals(".rb")){
			scriptType=RUBY;
		}
		
		reset();
	}

	/**
	 * Reset the script.
	 */
	public void reset() {
		readScript();
		
		initBindings();
		
		initialize();
	}

	/**
	 * Initialize.
	 */
	private void initialize() {
		try {
			switch(scriptType){
			case JAVASCRIPT:
				jsEngine.eval(jsBase+script);
				break;
				//TODO fix ruby scripts breaking on windows
			case RUBY:
		        rbEngine.eval(rbBase+script);
		        break;
			}
		} catch (ScriptException e) {

			new NSAlertBox("Script Read Error", reference.getName()+" has an error. Due to error reporting methods, I can not help you narrow down the issue. Here is a stack trace:\n"+e.getMessage(), SWT.ICON_ERROR);

			org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.script.scripts");
			fLog.error("Script initialization failed: "+reference.getName()+" at line #"+e.getLineNumber());
		} catch(Exception e) {
			org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.script.scripts");
			fLog.error("Script initialization failed: "+e.getStackTrace());
		}
		
	}

	/**
	 * Initialize the bindings.
	 */
	private void initBindings() {
		bindings.put("global", new ScriptVars());
		bindings.put("util", new ScriptFunctions());
		bindings.put("gui", new ScriptGUI());
		bindings.put("sound", new SoundData());
	}

	/**
	 * Read script.
	 */
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
	/**
	 * Parses the function.
	 *
	 * @param text the text
	 */
	private void parseFunction(String text) {
		if(text.toLowerCase().contains("meta")) {
			parseMeta(text);
		}
		switch(scriptType){
		case JAVASCRIPT:
			if(!text.contains("function ")) return;
			String elFunction = text.substring(9).split("[{]")[0].trim();
			descriptFunctions.add(elFunction);
			String[] array = text.replaceAll("[(]", " ").split(" ");
			//function, onFunctionName, event), {
			functions.add(array[1].trim());
			break;
		case RUBY:
			if(!text.contains("def ")) return;
			String rbFunction = text.substring(4);
			descriptFunctions.add(rbFunction);
			String[] rbarray = text.replaceAll("[(]", " ").split(" ");
			//def function(var) 
			functions.add(rbarray[1].trim());
			break;
		}
	}
	
	/**
	 * Parses the metadata of a script.
	 *
	 * @param text the text
	 */
	private void parseMeta(String text){
		//META<inuse=false>
		String[] cleanMeta = text.replaceAll("[<>]", " ").split(" ")[1].split("=");
		if(cleanMeta[0].equals("inuse")){
			this.inUse = Boolean.parseBoolean(cleanMeta[1]);
		}
	}
	
	//event invocation
	/**
	 * Invoke a function on a script, using a specified invocation schema
	 *
	 * @param function the function
	 * @param e the e
	 */
	public void invoke(String function, Event<KEllyBot> e){
		if(!inUse)return;
		try {
			switch(scriptType){
			case JAVASCRIPT:
				jsInvocable.invokeFunction(function, e);
				break;
			case RUBY:
				rbInvocable.invokeFunction(function, e);
				break;
			}
		} catch (NoSuchMethodException e1) {
			org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.script.scripts");
			fLog.error("Script invocation failed.", e1);
		} catch (ScriptException e1) {
			org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.script.scripts");
			fLog.error("Script invocation failed.", e1);
		}
	}

	//open-ended invocation
	/**
	 * Invoke with no expectation of returning a vlaue.
	 *
	 * @param command the command
	 * @param args the args
	 */
	public void invoke(String command, Object... args) { 
		if(!inUse)return;
		try {
			switch(scriptType){
			case JAVASCRIPT:
				jsInvocable.invokeFunction(command, args);
				break;
			case RUBY:
				rbInvocable.invokeFunction(command, args);
				break;
			}
		} catch (NoSuchMethodException e1) {
			org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.script.scripts");
			fLog.error("Script invocation failed.", e1);
		} catch (ScriptException e1) {
			org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.script.scripts");
			fLog.error("Script invocation failed.", e1);
		}
	}
	
	/**
	 * Invoke, and turn objects back into Java-accessible objects.
	 *
	 * @param command the command
	 * @return the object[]
	 */
	public Object[] invoke(String command) {
		Object[] rv = null;
		try {
			switch(scriptType){
			case JAVASCRIPT:
				NativeArray arr = (NativeArray) jsInvocable.invokeFunction(command);
				rv = new Object[(int)arr.getLength()];
				
				//get as object
				for(Object o : arr.getIds()) {
					int index = (Integer) o;
					rv[index] = arr.get(index, null);
				}
				break;
			case RUBY:
				rv = (Object[]) rbInvocable.invokeFunction(command);
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
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Script o) {
		return reference.getName().compareTo(o.reference.getName());
	}
}
