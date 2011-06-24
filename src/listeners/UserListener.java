package listeners;


import java.util.Collection;

import org.pircbotx.Channel;
import org.pircbotx.hooks.events.NickChangeEvent;
import org.pircbotx.hooks.events.QuitEvent;

import shared.Message;

import connection.Connection;
import connection.KEllyBot;

public class UserListener extends ConnectionListener{

	public UserListener(Connection nc) {
		super(nc);
	}

	@Override
	public void onNickChange(NickChangeEvent<KEllyBot> event) throws Exception {
		Collection<Channel> channels = event.getBot().getChannels();
		super.onNickChange(event);
		for(Channel c : channels){
			if(c.getUsers().contains(event.getUser())){
				updateWho(c);
				manageMessage(new Message(nc, event.getOldNick()+" is now known as "+event.getNewNick()+".", c.getName(), c, Message.CONSOLE));
			}
		}
	}
	
	@Override
	public void onQuit(QuitEvent<KEllyBot> event) throws Exception {
		for(Channel c : event.getUser().getChannels()){
			if(c.getUsers().contains(event.getBot().getUserBot())){
				updateWho(c);
				manageMessage(new Message(nc, event.getUser().getNick()+" has quit IRC. "+(event.getReason()!=null ? "("+event.getReason()+")"  : ""), c.getName(), c, Message.CONSOLE));
			}
		}
		super.onQuit(event);
	}
}
