package connection;

import lombok.Getter;
import lombok.Setter;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;

import shared.Message;
import ui.RoomManager;

public class KEllyBot extends PircBotX {
	
	private @Getter @Setter Connection connection;

	public KEllyBot(Connection c){
		this.connection = c;
	}
	
	@Override
	public void sendMessage(Channel target, String message) {
        if(target==null || target.getName().equals("Console")){
        	return;
        }
        RoomManager.enQueue(new Message(this, 
        		message, 
        		getNick(), 
        		target.getName()));  
		super.sendMessage(target, message);
	}

	public void doCommand(String command) {
		if(command.split(" ")[0].equals("join")){
			joinChannel(command.split(" ")[1]);
		}
	}

}
