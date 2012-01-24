/*
 * @author Kyle Kemp
 */
package listeners;

import org.pircbotx.hooks.events.ServerResponseEvent;

import connection.Connection;
import connection.KEllyBot;

import shared.Message;
import shared.RoomManager;

/**
 * The listener interface for receiving server events.
 * The class that is interested in processing a server
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addServerListener<code> method. When
 * the server event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ServerEvent
 */
public class ServerListener extends ConnectionListener {

	/**
	 * Instantiates a new server listener.
	 *
	 * @param nc the nc
	 */
	public ServerListener(Connection nc) {
		super(nc);
	}

	/* (non-Javadoc)
	 * @see org.pircbotx.hooks.ListenerAdapter#onServerResponse(org.pircbotx.hooks.events.ServerResponseEvent)
	 */
	@Override
	public void onServerResponse(ServerResponseEvent<KEllyBot> event) throws Exception {
		switch(event.getCode()){
		case 5:
		case 332:
		case 366:
		case 352:
		case 315:
		case 324:
		case 329:
		case 353:
		case 333:
			break;	
			
		default: 
			RoomManager.enQueue(new Message(nc, event.getResponse(), "["+event.getCode()+"] Server Response", Connection.CONSOLE_ROOM, Message.CONSOLE));    
		}
		super.onServerResponse(event);
	}
	
	

}
