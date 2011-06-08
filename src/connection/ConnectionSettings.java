package connection;

import java.util.ArrayList;

import lombok.Data;

public @Data
class ConnectionSettings {
	private String connectionName;
	private String server;
	private String port;
	private String serverPassword;
	private boolean ssl;
	private boolean connectOnStart;
	private String nickname;
	private String nickPassword;
	private String ident;
	private ArrayList<String> autoJoin;

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
