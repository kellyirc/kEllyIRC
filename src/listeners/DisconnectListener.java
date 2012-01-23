package listeners;

import java.io.IOException;

import org.pircbotx.Channel;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.Event;

import shared.Message;

import connection.Connection;
import connection.KEllyBot;

public class DisconnectListener extends ConnectionListener {

	public DisconnectListener(Connection nc) {
		super(nc);
	}
	
	public void onDisconnect(Event<KEllyBot> event){
		for(Channel c : event.getBot().getChannels()){
			manageMessage(new Message(nc, "You have been disconnected, attempting to reconnect.", c.getName(), c, Message.CONSOLE));
		}
		int tries=0;
		while(!event.getBot().isConnected() && tries++<10){
			try {
				Thread.sleep(3000);
				//TODO getconsole function to get the console room of an active connection
				//manageMessage(new Message(nc, "Reconnect attempt #"+tries+"...", event.getBot().getConnection());
				event.getBot().reconnect();
			} catch (IOException | IrcException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		//TODO get all channels previously connected to
		/*
		if(event.getBot().isConnected()) {
			for(Channel c : event.getBot().getc)
		} else {
			manageMessage(new Message(nc, "You were unable to be reconnected.", c.getName(), c, Message.CONSOLE));
		}*/
	}

}
