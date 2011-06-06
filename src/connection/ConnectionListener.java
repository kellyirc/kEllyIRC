package connection;

import org.pircbotx.hooks.ListenerAdapter;

public class ConnectionListener extends ListenerAdapter {
	Connection nc;
	public ConnectionListener(Connection nc){
		this.nc = nc;
	}
}
