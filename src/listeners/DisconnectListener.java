package listeners;

import org.pircbotx.Channel;
import org.pircbotx.hooks.Event;

import shared.Message;

import connection.Connection;
import connection.KEllyBot;

public class DisconnectListener extends ConnectionListener {

	public DisconnectListener(Connection nc) {
		super(nc);
	}
	
	//TODO: try a few times to reconnect
	public void onDisconnect(Event<KEllyBot> event){
		for(Channel c : event.getBot().getChannels()){
			manageMessage(new Message(nc, "You have been disconnected.. attempting to reconnect.", c.getName(), c, Message.CONSOLE));
		}
	}

}
