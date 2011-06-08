package connection;

import org.pircbotx.Channel;
import org.pircbotx.hooks.events.NickChangeEvent;
import org.pircbotx.hooks.events.QuitEvent;

public class UserListener extends ConnectionListener{

	public UserListener(Connection nc) {
		super(nc);
	}

	@Override
	public void onNickChange(NickChangeEvent<KEllyBot> event) throws Exception {
		super.onNickChange(event);
		for(Channel c : event.getUser().getChannels()){
			updateWho(c);
		}
	}

	@Override
	public void onQuit(QuitEvent<KEllyBot> event) throws Exception {
		super.onQuit(event);
		for(Channel c : event.getUser().getChannels()){
			updateWho(c);
		}
	}
}
