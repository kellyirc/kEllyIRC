package listeners;


import java.util.Collection;

import org.pircbotx.Channel;
import org.pircbotx.User;
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
				manageMessage(new Message(nc, event.getOldNick()+" is now known as "+event.getNewNick()+".", c.getName(), c));
			}
		}
	}

	//FIXME this does not work right at all
	
	@Override
	public void onQuit(QuitEvent<KEllyBot> event) throws Exception {
		Collection<Channel> channels = event.getBot().getChannels();
		//System.out.println(event.getUser());
		super.onQuit(event);
		for(Channel c : channels){
			//System.out.println(event.getUser().getNick());
			for(User u : c.getUsers()){
				if(u.getNick().equals(event.getUser().getNick())){
					System.out.println("matched");
				}
			}
			if(c.getUsers().contains(event.getUser())){
				System.out.println("found user");
				updateWho(c);
				manageMessage(new Message(nc, event.getUser().getNick()+" has quit "+(event.getReason()!=null ? event.getReason()  : "."), c.getName(), c));
			}
		}
	}
}
