package shared;

import org.pircbotx.Channel;

import lombok.Data;
import connection.Connection;
import connection.KEllyBot;

@Data 
public class Message {

	private String content, sender, channel;
	private KEllyBot bot;

	public Message(KEllyBot kEllyBot, String message, String nick,
			String target) {
		this.setBot(kEllyBot);
		this.setContent(message+(message.contains("\r\n") ? "" : "\r\n"));
		this.setSender(nick);
		this.setChannel(target);
	}
	//other things like author, timestamp, etc
	//TODO: pass in the actual IrcUser object instead of a string
	public Message(Connection nc, String content, String sender, String channel){
		this(nc.getBot(), content, sender, channel);
		System.err.println(nc);
	}
	public Message(Connection nc, String message, String nick, Channel channel) {
		this(nc, message, nick, channel == null ? "Console" : channel.getName());
	}
}
