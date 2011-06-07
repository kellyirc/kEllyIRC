package shared;

import lombok.Data;
import connection.Connection;
import connection.KEllyBot;

public @Data class Message {

	private String content, sender, channel;
	private KEllyBot connection;

	public Message(KEllyBot kEllyBot, String message, String nick,
			String target) {
		this.setConnection(kEllyBot);
		this.setContent(message+(message.contains("\r\n") ? "" : "\r\n"));
		this.setSender(nick);
		this.setChannel(target);
	}
	//other things like author, timestamp, etc
	//TODO: pass in the actual IrcUser object instead of a string
	public Message(Connection nc, String content, String sender, String channel){
		this(nc.getBot(), content, sender, channel);
	}
}
