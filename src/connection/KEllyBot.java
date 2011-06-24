package connection;

import lombok.Getter;
import lombok.Setter;

import org.pircbotx.PircBotX;

import scripting.Script;
import scripting.ScriptManager;
import scripting.ScriptVars;
import shared.Message;
import shared.RoomManager;
import ui.room.Room;

public class KEllyBot extends PircBotX {
	
	public static final String VERSION = "kEllyIRC 0.3.98 alpha";
	
	@Getter 
	@Setter 
	private Connection connection;

	public KEllyBot(Connection c){
		this.connection = c;
	}
	
	@Override
	public void sendMessage(String target, String message) {
		if (message.startsWith("/")) {
			doCommand(message.substring(1));
		} else {
	        if(target==null || target.equals("Console")){
	        	return;
	        }
	        RoomManager.enQueue(new Message(this, message, getNick(), target, Message.MSG));  
			super.sendMessage(target, message);
		}
	}
	
	@Override
	public void sendAction(String target, String message) {
		if (message.startsWith("/")) {
			doCommand(message.substring(1));
		} else {
	        if(target==null || target.equals("Console")){
	        	return;
	        }
	        RoomManager.enQueue(new Message(this, message, getNick(), target, Message.ACTION));  
			super.sendAction(target, message);
		}
	}
	
	@Override
	public void sendNotice(String target, String notice) {
        if(target==null || target.equals("Console")){
        	return;
        }
        RoomManager.enQueue(new Message(this, "NOTICE: "+notice, getNick(), target, Message.NOTICE));
		super.sendNotice(target, notice);
	}

	public void changeNick(String nick){
		super.changeNick(nick);
		RoomManager.enQueue(new Message(connection, "You are now known as "+nick, "System", ScriptVars.curChannel, Message.CONSOLE));
	}

	public void doCommand(String line) {
		String command = line.split(" ")[0];
		
		boolean found=false;

		for(Script s : ScriptManager.scripts){
			if(s.getFunctions().contains(command)){
				if(line.split(" ").length > 1){
					s.invoke(command, this, line.substring(line.indexOf(line.split(" ")[1])));
				} else {
					s.invoke(command, this, "");
				}
				found=true;
			}
		}
		
		if(!found){
			Room r = (Room) connection.getScrolledComposite().getContent();
			RoomManager.enQueue(new Message(connection, command + " is not a valid alias. Please define it.", "System", r.getChannelName(), Message.CONSOLE));
		}
		
	}

}
