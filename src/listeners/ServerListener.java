package listeners;


import org.pircbotx.hooks.events.ServerResponseEvent;

import connection.Connection;
import connection.KEllyBot;

import shared.Message;
import shared.RoomManager;

public class ServerListener extends ConnectionListener {

	public ServerListener(Connection nc) {
		super(nc);
	}

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
			break;	
			
		default: 
			RoomManager.enQueue(new Message(nc, event.getResponse(), "["+event.getCode()+"] Server Response", "Console"));    
		}
		super.onServerResponse(event);
	}
	
	

}
