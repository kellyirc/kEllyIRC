package connection;

import lombok.Data;

public @Data class ConnectionSettings {
	private String connectionName;
	private String server;
	private String port;
	private String serverPassword;
	private boolean ssl;
	private boolean connectOnStart;
	private String nickname;
	private String alternateNickname;
	private String nickPassword;
	private String ident;
	
	public ConnectionSettings(String connectionName, String server,
			String port, String serverPassword, boolean ssl,
			boolean connectOnStart, String nickname, String alternateNickname,
			String nickPassword, String ident) {
		super();
		this.connectionName = connectionName;
		this.server = server;
		this.port = port;
		this.serverPassword = serverPassword;
		this.ssl = ssl;
		this.connectOnStart = connectOnStart;
		this.nickname = nickname;
		this.alternateNickname = alternateNickname;
		this.nickPassword = nickPassword;
		this.ident = ident;
	}
	
	
}
