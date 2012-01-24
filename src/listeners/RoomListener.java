/*
 * @author Kyle Kemp
 */
package listeners;

import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.ModeEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.TopicEvent;

import connection.Connection;
import connection.KEllyBot;

import shared.Message;
import ui.room.Room;

/**
 * The listener interface for receiving room events.
 * The class that is interested in processing a room
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addRoomListener<code> method. When
 * the room event occurs, that object's appropriate
 * method is invoked.
 *
 * @see RoomEvent
 */
public class RoomListener extends ConnectionListener{

	/**
	 * Instantiates a new room listener.
	 *
	 * @param nc the nc
	 */
	public RoomListener(Connection nc) {
		super(nc);
	}

	/* (non-Javadoc)
	 * @see org.pircbotx.hooks.ListenerAdapter#onJoin(org.pircbotx.hooks.events.JoinEvent)
	 */
	@Override
	public void onJoin(JoinEvent<KEllyBot> event) throws Exception {
		if(botEqualsUser(event.getUser())){
			nc.createRoom(event.getChannel().getName(), Room.IO | Room.TOPIC | Room.WHO, event.getChannel());
		}
		super.onJoin(event);
		queueMessage(new Message(nc, event.getUser().getNick()+" has joined.", event.getChannel().getName(), event.getChannel().getName(), Message.CONSOLE));
		updateWho(event.getChannel());
	}

	/* (non-Javadoc)
	 * @see org.pircbotx.hooks.ListenerAdapter#onMode(org.pircbotx.hooks.events.ModeEvent)
	 */
	@Override
	public void onMode(ModeEvent<KEllyBot> event) throws Exception {
		super.onMode(event);
		updateWho(event.getChannel());
		queueMessage(new Message(nc, event.getMode()+" by "+event.getUser().getNick(), event.getChannel().getName(), event.getChannel().getName(), Message.CONSOLE));
	}

	/* (non-Javadoc)
	 * @see org.pircbotx.hooks.ListenerAdapter#onPart(org.pircbotx.hooks.events.PartEvent)
	 */
	@Override
	public void onPart(PartEvent<KEllyBot> event) throws Exception {
		super.onPart(event);
		updateWho(event.getChannel());
		queueMessage(new Message(nc, event.getUser().getNick()+" has parted.", event.getChannel().getName(), event.getChannel().getName(), Message.CONSOLE));
	}

	/* (non-Javadoc)
	 * @see org.pircbotx.hooks.ListenerAdapter#onKick(org.pircbotx.hooks.events.KickEvent)
	 */
	@Override
	public void onKick(KickEvent<KEllyBot> event) throws Exception {
		super.onKick(event);
		updateWho(event.getChannel());
		queueMessage(new Message(nc, event.getRecipient().getNick()+" was kicked by "+event.getSource().getNick()+(event.getReason() != null?" ["+event.getReason()+"]" : ""), event.getChannel().getName(), event.getChannel().getName(), Message.CONSOLE));
	}

	/* (non-Javadoc)
	 * @see org.pircbotx.hooks.ListenerAdapter#onTopic(org.pircbotx.hooks.events.TopicEvent)
	 */
	@Override
	public void onTopic(TopicEvent<KEllyBot> event) throws Exception {
		super.onTopic(event);
		nc.updateTopic(event.getChannel().getName());
		if(event.getDate() < System.currentTimeMillis()){
			//topic was set already
			queueMessage(new Message(nc, event.getTopic(), event.getChannel().getName(), event.getChannel().getName(), Message.CONSOLE));
			queueMessage(new Message(nc, "Topic set by: "+event.getUser().getNick(), event.getChannel().getName(), event.getChannel().getName(), Message.CONSOLE));
		} else {
			//topic was changed
			queueMessage(new Message(nc, event.getUser().getNick()+" changed the topic.", event.getChannel().getName(), event.getChannel().getName(), Message.CONSOLE));
		}
	}

}
