package connection;

import lombok.Getter;
import lombok.Setter;

import org.pircbotx.PircBotX;

import shared.Message;
import ui.RoomManager;

public class KEllyBot extends PircBotX {
	
	public static final String VERSION = "kEllyIRC 0.1.0 alpha";
	
	private @Getter @Setter Connection connection;

	public KEllyBot(Connection c){
		this.connection = c;
	}
	
	@Override
	public void sendMessage(String target, String message) {
        if(target==null || target.equals("Console")){
        	return;
        }
        RoomManager.enQueue(new Message(this, 
        		message, 
        		getNick(), 
        		target));  
		super.sendMessage(target, message);
	}
	
	@Override
	public void sendNotice(String target, String notice) {
        if(target==null || target.equals("Console")){
        	return;
        }
        RoomManager.enQueue(new Message(this, 
        		"NOTICE: "+notice,
        		getNick(), 
        		target));
		super.sendNotice(target, notice);
	}

	public void doCommand(String command) {
		if(command.split(" ")[0].equals("join")){
			String s = command.split(" ")[1];
			if(!s.startsWith("#")){
				s = "#"+s;
			}
			joinChannel(s);
		}
	}

}
