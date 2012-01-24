package listeners;

import java.util.LinkedList;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.DisconnectEvent;

import shared.Message;
import shared.RoomManager;
import ui.room.Room;
import connection.Connection;
import connection.KEllyBot;

public abstract class ConnectionListener extends ListenerAdapter<KEllyBot> {
	
	@Override
	public void onDisconnect(DisconnectEvent<KEllyBot> event) throws Exception {
		super.onDisconnect(event);
		//TODO: Make it so it doesn't say Disconnected. rooms.size() number of times
		LinkedList<Room> rooms = event.getBot().getConnection().getRooms();
		for(Room r : rooms)
		{
			RoomManager.enQueue(new Message(event.getBot(), "Disconnected.", KEllyBot.systemName, r.getChannelName(), Message.CONSOLE));
		}

	}

	Connection nc;
	public ConnectionListener(Connection nc){
		this.nc = nc;
	}
	
	protected void updateWho(Channel c){
		nc.updateWho(c.getName());
	}
	
	protected boolean botEqualsUser(User u){
		return u.equals(nc.getBot().getUserBot());
	}
	
	protected void queueMessage(Message m){
		RoomManager.enQueue(m);
	}
	
	protected void manageMessage(Message m){
		RoomManager.enQueue(m);
	}

}
