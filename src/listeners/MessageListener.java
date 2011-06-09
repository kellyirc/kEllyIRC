package listeners;

import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.NoticeEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import connection.Connection;
import connection.KEllyBot;

import shared.Message;
import shared.RoomManager;

public class MessageListener extends ConnectionListener{

	public MessageListener(Connection nc) {
		super(nc);
	}

	@Override
	public void onMessage(MessageEvent<KEllyBot> event) throws Exception {
		RoomManager.enQueue(new Message(nc, event.getMessage(), event.getUser().getNick(), event.getChannel()));	
		super.onMessage(event);
	}

	@Override
	public void onAction(ActionEvent<KEllyBot> event) throws Exception {
		RoomManager.enQueue(new Message(nc, "***"+event.getAction(), event.getUser().getNick(), event.getChannel()));	
		super.onAction(event);
	}

	@Override
	public void onNotice(NoticeEvent<KEllyBot> event) throws Exception {
		RoomManager.enQueue(
				new Message(nc, 
						"NOTICE: "+event.getNotice(), 
						event.getUser().getNick(), 
						event.getChannel()));	
		super.onNotice(event);
	}

	@Override
	public void onPrivateMessage(PrivateMessageEvent<KEllyBot> event)
			throws Exception {
		RoomManager.enQueue(new Message(nc, event.getMessage(), event.getUser().getNick(), event.getUser().getNick()));	
		super.onPrivateMessage(event);
	} 

}
