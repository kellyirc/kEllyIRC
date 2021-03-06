/*
 * @author Kyle Kemp
 */
package shared;

import java.util.Date;

import lombok.Data;

import org.pircbotx.Channel;
import org.pircbotx.User;

import connection.Connection;
import connection.KEllyBot;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data 
public class Message {

	/** The Constant CONSOLE. */
	public static final short CONSOLE = 0;
	
	/** The Constant MSG. */
	public static final short MSG = 1;
	
	/** The Constant NOTICE. */
	public static final short NOTICE = 2;
	
	/** The Constant ACTION. */
	public static final short ACTION = 3;
	
	/** The Constant PM. */
	public static final short PM = 4;
	
	/** The Constant NEW_LINE. */
	public static final String NEW_LINE = System.getProperty("line.separator");

	/** The channel. */
	private String content, sender, channel;
	
	/** The bot. */
	private KEllyBot bot;
	
	/** The type. */
	private short type;
	
	/** The date. */
	private Date date;
		
	/**
	 * Instantiates a new message.
	 *
	 * @param kEllyBot the k elly bot
	 * @param message the message
	 * @param nick the nick
	 * @param target the target
	 * @param type the type
	 */
	public Message(KEllyBot kEllyBot, String message, String nick,
			String target, short type) {
		this.setDate(new Date());
		this.setBot(kEllyBot);
		this.setContent(quicklinkToLink(message)+(message.contains(NEW_LINE) ? "" : NEW_LINE));
		this.setSender(nick);
		this.setChannel(target);
		this.type = type;
	}
	
	/**
	 * Instantiates a new message.
	 *
	 * @param nc the nc
	 * @param content the content
	 * @param sender the sender
	 * @param channel the channel
	 * @param type the type
	 */
	public Message(Connection nc, String content, String sender, String channel, short type){
		this(nc.getBot(), content, sender, channel, type);
	}
	
	/**
	 * Instantiates a new message.
	 *
	 * @param nc the nc
	 * @param message the message
	 * @param nick the nick
	 * @param channel the channel
	 * @param type the type
	 */
	public Message(Connection nc, String message, String nick, Channel channel, short type) {
		this(nc, message, nick, channel == null ? Connection.CONSOLE_ROOM : channel.getName(), type);
	}
	
	/**
	 * Instantiates a new message.
	 *
	 * @param nc the nc
	 * @param message the message
	 * @param user the user
	 * @param channel the channel
	 * @param type the type
	 */
	public Message(Connection nc, String message, User user, Channel channel, short type) {
		this(nc, message, channel == null ? user.getNick() : (getUserCode(user, channel)+user.getNick()), channel, type);
	}
	
	/**
	 * Gets the user code.
	 *
	 * @param user the user
	 * @param channel the channel
	 * @return the user code
	 */
	public static String getUserCode(User user, Channel channel) {
		if(channel.getOwners().contains(user)) return "~";
		if(channel.getSuperOps().contains(user)) return "&";
		if(channel.getOps().contains(user)) return "@";
		if(channel.getHalfOps().contains(user)) return "%";
		if(channel.getVoices().contains(user)) return "+";
		return "";
	}
	
	/**
	 * Quicklink to link.
	 *
	 * @param string the string
	 * @return the string
	 */
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
	
	/**
	 * Parses the substring for links.
	 *
	 * @param substring the substring
	 * @return the string
	 */
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
