package listeners;

import org.pircbotx.hooks.Event;

import scripting.Script;
import scripting.ScriptManager;

import connection.Connection;
import connection.KEllyBot;

public class ScriptListener extends ConnectionListener {

	public ScriptListener(Connection nc) {
		super(nc);
	}
	
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

	private void preCall(String s, Event<KEllyBot> e) {
		for(Script script : ScriptManager.scripts){
			if(script.getFunctions().contains(s)){
				script.invoke(s, e);
			}
		}
	}

	private void postCall(String s, Event<KEllyBot> e) {
		s = "post_"+s;
		for(Script script : ScriptManager.scripts){
			if(script.getFunctions().contains(s)){
				script.invoke(s, e);
			}
		}
	}

}
