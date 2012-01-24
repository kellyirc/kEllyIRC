/*
 * @author Kyle Kemp
 */
package listeners;

import org.pircbotx.hooks.Event;

import scripting.Script;
import scripting.ScriptManager;

import connection.Connection;
import connection.KEllyBot;

/**
 * The listener interface for receiving script events.
 * The class that is interested in processing a script
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addScriptListener<code> method. When
 * the script event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ScriptEvent
 */
public class ScriptListener extends ConnectionListener {

	/**
	 * Instantiates a new script listener.
	 *
	 * @param nc the nc
	 */
	public ScriptListener(Connection nc) {
		super(nc);
	}
	
	/* (non-Javadoc)
	 * @see org.pircbotx.hooks.ListenerAdapter#onEvent(org.pircbotx.hooks.Event)
	 */
	@Override
	public void onEvent(Event<KEllyBot> arg0) throws Exception {
		
		//org.pircbotx.hooks.events.eventtype
		String s = arg0.getClass().getName().split("[.]")[4];
		s = "on"+s.substring(0,s.indexOf("Event"));
		
		//find scripts, invoke scripts with the arg0 event if it matches
		preCall(s, arg0);
		
		super.onEvent(arg0);
		
		postCall(s, arg0);
	}

	/**
	 * Pre call.
	 *
	 * @param s the s
	 * @param e the e
	 */
	private void preCall(String s, Event<KEllyBot> e) {
		for(Script script : ScriptManager.scripts){
			if(script.getFunctions().contains(s)){
				script.invoke(s, e);
			}
		}
	}

	/**
	 * Post call.
	 *
	 * @param s the s
	 * @param e the e
	 */
	private void postCall(String s, Event<KEllyBot> e) {
		s = "post_"+s;
		for(Script script : ScriptManager.scripts){
			if(script.getFunctions().contains(s)){
				script.invoke(s, e);
			}
		}
	}

}
