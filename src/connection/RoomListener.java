package connection;

import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.ModeEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.TopicEvent;

import shared.Message;
import ui.Room;

public class RoomListener extends ConnectionListener{

	public RoomListener(Connection nc) {
		super(nc);
	}

	@Override
	public void onJoin(JoinEvent<KEllyBot> event) throws Exception {
		if(botEqualsUser(event.getUser())){
			nc.createRoom(event.getChannel().getName(), Room.IO | Room.TOPIC | Room.WHO, event.getChannel());
			queueMessage(new Message(nc, event.getChannel().getTopic(), event.getChannel().getName(), event.getChannel().getName()));
		}
		super.onJoin(event);
		queueMessage(new Message(nc, event.getUser().getNick()+" has joined.", event.getChannel().getName(), event.getChannel().getName()));
		updateWho(event.getChannel());
	}

	@Override
	public void onMode(ModeEvent<KEllyBot> event) throws Exception {
		super.onMode(event);
		updateWho(event.getChannel());
		queueMessage(new Message(nc, event.getMode()+" by "+event.getUser().getNick(), event.getChannel().getName(), event.getChannel().getName()));
	}

	@Override
	public void onPart(PartEvent<KEllyBot> event) throws Exception {
		super.onPart(event);
		updateWho(event.getChannel());
		queueMessage(new Message(nc, event.getUser().getNick()+" has parted.", event.getChannel().getName(), event.getChannel().getName()));
	}

	@Override
	public void onKick(KickEvent<KEllyBot> event) throws Exception {
		super.onKick(event);
		updateWho(event.getChannel());
		queueMessage(new Message(nc, event.getRecipient()+" was kicked by "+event.getSource()+(event.getReason() != null?" ["+event.getReason()+"]" : ""), event.getChannel().getName(), event.getChannel().getName()));
	}

	@Override
	public void onTopic(TopicEvent<KEllyBot> event) throws Exception {
		super.onTopic(event);
		//queueMessage(new Message(nc, event.getUser().getNick()+" changed the topic.", event.getChannel().getName(), event.getChannel().getName()));
		nc.updateTopic(event.getChannel().getName());
	}

}
