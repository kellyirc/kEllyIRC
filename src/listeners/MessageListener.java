/*
 * @author Kyle Kemp
 */
package listeners;

import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.NoticeEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import connection.Connection;
import connection.KEllyBot;

import shared.Message;

/**
 * The listener interface for receiving message events.
 * The class that is interested in processing a message
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addMessageListener<code> method. When
 * the message event occurs, that object's appropriate
 * method is invoked.
 *
 * @see MessageEvent
 */
public class MessageListener extends ConnectionListener{

	/**
	 * Instantiates a new message listener.
	 *
	 * @param nc the nc
	 */
	public MessageListener(Connection nc) {
		super(nc);
	}

	/* (non-Javadoc)
	 * @see org.pircbotx.hooks.ListenerAdapter#onMessage(org.pircbotx.hooks.events.MessageEvent)
	 */
	@Override
	public void onMessage(MessageEvent<KEllyBot> event) throws Exception {
		super.onMessage(event);
		manageMessage(new Message(nc, event.getMessage(), event.getUser(), event.getChannel(), Message.MSG));
	}

	/* (non-Javadoc)
	 * @see org.pircbotx.hooks.ListenerAdapter#onAction(org.pircbotx.hooks.events.ActionEvent)
	 */
	@Override
	public void onAction(ActionEvent<KEllyBot> event) throws Exception {
		super.onAction(event);
		manageMessage(new Message(nc, event.getAction(), event.getUser(), event.getChannel(), Message.ACTION));	
	}

	/* (non-Javadoc)
	 * @see org.pircbotx.hooks.ListenerAdapter#onNotice(org.pircbotx.hooks.events.NoticeEvent)
	 */
	@Override
	public void onNotice(NoticeEvent<KEllyBot> event) throws Exception {	
		super.onNotice(event);
		manageMessage(new Message(nc, "NOTICE: "+event.getNotice(), event.getUser(), null, Message.NOTICE));
	}

	/* (non-Javadoc)
	 * @see org.pircbotx.hooks.ListenerAdapter#onPrivateMessage(org.pircbotx.hooks.events.PrivateMessageEvent)
	 */
	@Override
	public void onPrivateMessage(PrivateMessageEvent<KEllyBot> event)
			throws Exception {	
		super.onPrivateMessage(event);
		manageMessage(new Message(nc, event.getMessage(), event.getUser().getNick(), event.getUser().getNick(), Message.PM
				));
	} 
}
