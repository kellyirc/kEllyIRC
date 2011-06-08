package connection;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;

import shared.Message;
import ui.RoomManager;

public abstract class ConnectionListener extends ListenerAdapter<KEllyBot> {
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
}
