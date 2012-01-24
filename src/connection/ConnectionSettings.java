/*
 * @author Kyle Kemp
 */
package connection;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@Data
@ToString
public class ConnectionSettings {
	
	/** The connection name. */
	private String connectionName;
	
	/** The server. */
	private String server;
	
	/** The port. */
	private String port;
	
	/** The server password. */
	private String serverPassword;
	
	/** Use SSL?. */
	private boolean ssl;
	
	/** Connect to this server on startup?. */
	private boolean connectOnStart;
	
	/** The nickname. */
	private String nickname;
	
	/** The nick password. */
	private String nickPassword;
	
	/** The ident. */
	private String ident;
	
	/** The auto join channel list. */
	private ArrayList<String> autoJoin;

	/**
	 * Instantiates a new connection settings.
	 *
	 * @param connectionName the connection name
	 * @param server the server
	 * @param port the port
	 * @param serverPassword the server password
	 * @param ssl the ssl
	 * @param connectOnStart the connect on start
	 * @param nickname the nickname
	 * @param nickPassword the nick password
	 * @param ident the ident
	 * @param autoJoin the auto join
	 */
	public ConnectionSettings(String connectionName, String server,
			String port, String serverPassword, boolean ssl,
			boolean connectOnStart, String nickname, String nickPassword,
			String ident, ArrayList<String> autoJoin) {
		super();
		this.connectionName = connectionName;
		this.server = server;
		this.port = port;
		this.serverPassword = serverPassword;
		this.ssl = ssl;
		this.connectOnStart = connectOnStart;
		this.nickname = nickname;
		this.nickPassword = nickPassword;
		this.ident = ident;
		this.autoJoin = autoJoin;
	}

}
