package connection;

import lombok.Data;

public @Data class ConnectionSettings {
	private String connectionName;
	private String server;
	private String port;
	private String serverPassword;
	private boolean ssl;
	private String nickname;
	private String alternateNickname;
	private String nickPassword;
	
	public ConnectionSettings(String connectionName, String server,
			String port, String serverPassword, boolean ssl, String nickname,
			String alternateNickname, String nickPassword) {
		super();
		this.connectionName = connectionName;
		this.server = server;
		this.port = port;
		this.serverPassword = serverPassword;
		this.ssl = ssl;
		this.nickname = nickname;
		this.alternateNickname = alternateNickname;
		this.nickPassword = nickPassword;
	}
}
