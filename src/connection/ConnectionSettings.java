package connection;

public class ConnectionSettings {
	public String connectionName;
	public String server;
	public String port;
	public String serverPassword;
	public boolean ssl;
	public String nickname;
	public String alternateNickname;
	public String nickPassword;
	
	public ConnectionSettings(String connectionName, String server, String port, String serverPassword, boolean ssl, String username, String alternateUsername, String nickPassword)
	{
		this.connectionName = connectionName;
		this.server = server;
		this.port = port;
		this.serverPassword = serverPassword;
		this.ssl = ssl;
		this.nickname = username;
		this.alternateNickname = alternateUsername;
		this.nickPassword = nickPassword;
	}
}
