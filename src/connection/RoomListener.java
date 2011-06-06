package connection;

import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.QuitEvent;

import ui.Room;

public class RoomListener extends ConnectionListener{

	public RoomListener(Connection nc) {
		super(nc);
	}

	@Override
	public void onJoin(JoinEvent event) throws Exception {
		nc.createRoom(event.getChannel().getName(), Room.IO | Room.TOPIC | Room.WHO, event.getChannel());
		super.onJoin(event);
		nc.updateWho(event.getChannel().getName());
	}

	@Override
	public void onPart(PartEvent event) throws Exception {
		//remove the channel
		super.onPart(event);
	}

	@Override
	public void onQuit(QuitEvent event) throws Exception {
		//remove all rooms from the connection
		super.onQuit(event);
	}

}
