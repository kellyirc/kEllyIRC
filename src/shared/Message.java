package shared;

import lombok.Data;
import connection.Connection;

public @Data class Message {
	
	private String content, sender, channel;
	private Connection connection;
	
	//other things like author, timestamp, etc
	//TODO: pass in the actual IrcUser object instead of a string
	public Message(Connection c, String content, String sender, String channel){
		this.setConnection(c);
		this.setContent(content+(content.contains("\r\n") ? "" : "\r\n"));
		this.setSender(sender);
		this.setChannel(channel);
	}
}
