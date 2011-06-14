package scripting;

import org.pircbotx.Channel;

import connection.KEllyBot;

public final class ScriptVars {
	public static Channel curChannel = null;
	public static KEllyBot curConnection = null;
	
	public Channel currentChannel() { return curChannel;}
	public KEllyBot currentConnection() { return curConnection;}
	
}
