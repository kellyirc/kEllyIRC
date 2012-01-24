package shared;

import java.util.Date;

import lombok.Data;

import org.pircbotx.Channel;
import org.pircbotx.User;

import connection.Connection;
import connection.KEllyBot;

@Data 
public class Message {

	public static final short CONSOLE = 0;
	public static final short MSG = 1;
	public static final short NOTICE = 2;
	public static final short ACTION = 3;
	public static final short PM = 4;
	
	public static final String NEW_LINE = System.getProperty("line.separator");

	private String content, sender, channel;
	private KEllyBot bot;
	private short type;
	private Date date;
		
	public Message(KEllyBot kEllyBot, String message, String nick,
			String target, short type) {
		this.setDate(new Date());
		this.setBot(kEllyBot);
		this.setContent(quicklinkToLink(message)+(message.contains(NEW_LINE) ? "" : NEW_LINE));
		this.setSender(nick);
		this.setChannel(target);
		this.type = type;
	}
	
	public Message(Connection nc, String content, String sender, String channel, short type){
		this(nc.getBot(), content, sender, channel, type);
	}
	
	public Message(Connection nc, String message, String nick, Channel channel, short type) {
		this(nc, message, nick, channel == null ? Connection.CONSOLE_ROOM : channel.getName(), type);
	}
	
	public Message(Connection nc, String message, User user, Channel channel, short type) {
		this(nc, message, channel == null ? user.getNick() : (getUserCode(user, channel)+user.getNick()), channel, type);
	}
	
	public static String getUserCode(User user, Channel channel) {
		if(channel.getOwners().contains(user)) return "~";
		if(channel.getSuperOps().contains(user)) return "&";
		if(channel.getOps().contains(user)) return "@";
		if(channel.getHalfOps().contains(user)) return "%";
		if(channel.getVoices().contains(user)) return "+";
		return "";
	}
	
	public static String quicklinkToLink(String string) {
		StringBuilder sb = new StringBuilder();
		String[] arr = string.split(" ");
		for(String s : arr) {
			 if(Quicklinks.hasQuicklink(s)) {
				sb.append(Quicklinks.getLink(s));
			} else sb.append(s);
			if(!s.equals(arr[arr.length-1])) sb.append(" ");
		}
		return sb.toString();
	}
	
	public static String parseForLinks(String substring) {
		StringBuilder sb = new StringBuilder();
		String[] arr = substring.split(" ");
		for(String s : arr) {
			if(s.contains("://")) {
				sb.append("<a href=\""+s+"\">"+s+"</a>");
			}else if(Quicklinks.hasQuicklink(s)) {
				sb.append("<a href=\""+Quicklinks.getLink(s)+"\">"+Quicklinks.getLink(s)+"</a>");
			} else sb.append(s);
			if(!s.equals(arr[arr.length-1])) sb.append(" ");
		}
		return sb.toString();
	}
	
}
