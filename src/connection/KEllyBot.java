package connection;

import lombok.Getter;
import lombok.Setter;

import org.pircbotx.PircBotX;

import scripting.Script;
import scripting.ScriptManager;
import shared.Message;
import shared.RoomManager;
import ui.room.Room;

public class KEllyBot extends PircBotX {
	
	public static final String VERSION = "kEllyIRC 0.2.1 alpha";
	
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
	        RoomManager.enQueue(new Message(this, message, getNick(), target));  
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
	        RoomManager.enQueue(new Message(this, "***"+message, getNick(), target));  
			super.sendAction(target, message);
		}
	}
	
	@Override
	public void sendNotice(String target, String notice) {
        if(target==null || target.equals("Console")){
        	return;
        }
        RoomManager.enQueue(new Message(this, "NOTICE: "+notice, getNick(), target));
		super.sendNotice(target, notice);
	}

	public void doCommand(String line) {
		String command = line.split(" ")[0];
		
		boolean found=false;
		
		for(Script s : ScriptManager.scripts){
			if(s.getFunctions().contains(command)){
				s.invoke(command, this, line.substring(line.indexOf(line.split(" ")[1])));
				found=true;
			}
		}
		
		if(!found){
			Room r = (Room) connection.getScrolledComposite().getContent();
			RoomManager.enQueue(new Message(connection, command + " is not a valid alias. Please define it.", "System", r.getChannelName()));
		}
		
	}

}
