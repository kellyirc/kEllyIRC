package shared;

import connection.Connection;

public class Message {
	
	private String content, sender, channel;
	private Connection connection;
	
	//other things like author, timestamp, etc
	public Message(Connection c, String content, String sender, String channel){
		this.setConnection(c);
		this.setContent(content+"\n");
		this.setSender(sender);
		this.setChannel(channel);
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSender() {
		return sender;
	}

	
	public void setChannel(String channel) {
		this.channel = channel;
	}

	
	public String getChannel() {
		return channel;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}
}
