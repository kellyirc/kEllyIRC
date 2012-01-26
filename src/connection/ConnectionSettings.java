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
	
	public static final int DEFAULT_PORT = 6667;
	public static final boolean DEFAULT_SSL = false;
	public static final boolean DEFAULT_CONNECT_ON_START = false;
	
	
	/** The connection name. */
	private String connectionName;
	
	/** The server address. */
	private String server;
	
	/** The port to connect to. */
	private int port;
	
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
	
	/** The commands to perform on connection */
	private ArrayList<String> performOnConnect;
	
	/**
	 * Instantiates a new connection settings. The connection
	 * name will use the server value. port, ssl, and
	 * connectOnStart will have their default values. The
	 * passwords will be blank. The ident will use the 
	 * nickname value 
	 *
	 * @param server the server
	 * @param nickname the nickname
	 */
	public ConnectionSettings(String server, String nickname)
	{
		this.connectionName = server;
		this.server = server;
		this.port = DEFAULT_PORT;
		this.serverPassword = "";
		this.ssl = DEFAULT_SSL;
		this.connectOnStart = DEFAULT_CONNECT_ON_START;
		this.nickname = nickname;
		this.nickPassword = "";
		this.ident = nickname;
		this.autoJoin = new ArrayList<String>();
		this.performOnConnect= new ArrayList<String>();
	}

	
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
	 * @param performOnConnect the commands to perform on start
	 */
	public ConnectionSettings(String connectionName, String server, int port,
			String serverPassword, boolean ssl, boolean connectOnStart,
			String nickname, String nickPassword, String ident,
			ArrayList<String> autoJoin, ArrayList<String> performOnConnect)
	{
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
		this.performOnConnect = performOnConnect;
	}
}