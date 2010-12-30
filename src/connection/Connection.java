package connection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Display;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.TrustingSSLSocketFactory;
import org.jibble.pircbot.User;

import shared.Message;
import ui.Channel;
import ui.Room;
import ui.RoomManager;

/*
 * TODO:
 * 		features for each connection: 
 * 			ssl support [checkbox]
 * 			server password [text]
 * 			specify port to use (default 6669) [text]
 * 			nickname [text]
 * 			quit message [text]
 * 			"ident" (pircbot is a crappy ident to have) [text]
 * 			real name [text]
 */

public class Connection extends PircBot{
	
	private CTabFolder chanList;
	
	private String myServer;

	private String nick;

	public Connection(CTabFolder parent, String server, String nick){
		
		this.myServer = server;
		this.nick = nick;
		
		setAutoNickChange(true);
		setName(nick);
		setVerbose(true);
		
		CTabItem c = new CTabItem(parent, SWT.NONE);
		c.setText(server);
		
		//this handles the settings of anchors on the parent
		chanList = new CTabFolder(parent, SWT.BORDER);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.right = new FormAttachment(100);
		fd_tabFolder.bottom = new FormAttachment(100);
		fd_tabFolder.top = new FormAttachment(0);
		fd_tabFolder.left = new FormAttachment(0);
		chanList.setLayoutData(fd_tabFolder);
		chanList.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		chanList.setSimple(false);

		//every connection needs a console
		createRoom("Console");
		
		//tell the channel list that it has a tab, and that it needs to be drawn
		c.setControl(chanList);

		try {
			this.connect(server, 6697, new TrustingSSLSocketFactory());
			//TODO: This can be modified for specific server-settings (including SSL)
			//this.connect(server);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//TODO: remove this -- it is temporary
		this.joinChannel("#dgr");
		
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#changeNick(java.lang.String)
	 */
	@Override
	public void changeNick(String newNick) {
		// TODO Auto-generated method stub
		super.changeNick(newNick);
	}

	/**Create a new room based on a channel name. This does NOT need to lead to an actual channel, 
	 * as it just handles creation of the window itself. 
	 * 
	 * @param channel the name of the ctabitem that will be created
	 */
	private void createRoom(String channel) {
		Room r = RoomManager.createRoom(chanList,SWT.NONE);

		r.setChannel(new Channel(getChanList(), channel, this));
		r.setServerConnection(this);
		r.instantiate();
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onTopic(java.lang.String, java.lang.String, java.lang.String, long, boolean)
	 */
	@Override
	protected void onTopic(String channel, String topic, String setBy,
			long date, boolean changed) {
		for(Room c : RoomManager.getRooms()){
			if(c.getServerConnection().equals(this) && c.getChannel().getChannelName().equals(channel)){
				RoomManager.changeTopic(c, topic);
			}
		}
		super.onTopic(channel, topic, setBy, date, changed);
	}

	//TODO: fix this and make a proper command class and aliases function with javascript
	public void doCommand(String command){
		if(command.split(" ")[0].equals("join")){
			joinChannel(command.split(" ")[1]);
		}
	}
	
	/**
	 * Find a Room in RoomManager.rooms given a name to look for
	 * @param channel the name of the channel to look for on this connection
	 * @return a Room with the given channel name on this connection, or null
	 */
	private Room findRoom(String channel) {
		for(Room r : RoomManager.getRooms()){
			if(r.getServerConnection().equals(this) && r.getChannel().getChannelName().equals(channel)){
				return r;
			}
		}
		return null;
	}

	/**
	 * @return the chanList
	 */
	public CTabFolder getChanList() {
		return chanList;
	}

	/**
	 * @return the server
	 */
	public String getMyServer() {
		return this.myServer;
	}
	
	/**
	 * @return the nick
	 */
	public String getNick() {
		return nick;
	}
	
	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#joinChannel(java.lang.String)
	 */
	@Override
	public void joinChannel(String channel) {
		createRoom(channel);		
		
		super.joinChannel(channel);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#joinChannel(java.lang.String, java.lang.String)
	 */
	@Override
	public void joinChannel(String channel, String key) {
		createRoom(channel);			
		super.joinChannel(channel, key);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onAction(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onAction(String sender, String login, String hostname,
			String target, String action) {
		// TODO Auto-generated method stub
		super.onAction(sender, login, hostname, target, action);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onChannelInfo(java.lang.String, int, java.lang.String)
	 */
	@Override
	protected void onChannelInfo(String channel, int userCount, String topic) {
		
		System.out.println(channel+userCount+topic);
		
		super.onChannelInfo(channel, userCount, topic);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onDeop(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onDeop(String channel, String sourceNick,
			String sourceLogin, String sourceHostname, String recipient) {
		//updateWho(channel);
		super.onDeop(channel, sourceNick, sourceLogin, sourceHostname, recipient);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onDeVoice(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onDeVoice(String channel, String sourceNick,
			String sourceLogin, String sourceHostname, String recipient) {
		//updateWho(channel);
		super.onDeVoice(channel, sourceNick, sourceLogin, sourceHostname, recipient);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onJoin(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onJoin(String channel, String sender, String login,
			String hostname) {
		updateWho(channel);
		super.onJoin(channel, sender, login, hostname);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onKick(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onKick(String channel, String kickerNick,
			String kickerLogin, String kickerHostname, String recipientNick,
			String reason) {
		updateWho(channel);
		super.onKick(channel, kickerNick, kickerLogin, kickerHostname, recipientNick,
				reason);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onMessage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		
		//add the message to the queue, and then make the queue clean itself out
		RoomManager.queue.add(new Message(this, message, sender, channel));	
		RoomManager.manageQueue();
		
		super.onMessage(channel, sender, login, hostname, message);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onMode(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onMode(String channel, String sourceNick,
			String sourceLogin, String sourceHostname, String mode) {
		updateWho(channel);
		super.onMode(channel, sourceNick, sourceLogin, sourceHostname, mode);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onNickChange(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onNickChange(String oldNick, String login, String hostname,
			String newNick) {
		for(Room r : RoomManager.getRooms()){
			if(r.getServerConnection().equals(this)){
				RoomManager.updateWho(r);
			}
		}
		super.onNickChange(oldNick, login, hostname, newNick);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onNotice(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onNotice(String sourceNick, String sourceLogin,
			String sourceHostname, String target, String notice) {

		if(sourceLogin.equals("")) {
			RoomManager.queue.add(new Message(this, notice, "Notice from "+sourceNick, "Console"));	
			RoomManager.manageQueue();
		}
		
		super.onNotice(sourceNick, sourceLogin, sourceHostname, target, notice);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onOp(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onOp(String channel, String sourceNick, String sourceLogin,
			String sourceHostname, String recipient) {
		//updateWho(channel);
		super.onOp(channel, sourceNick, sourceLogin, sourceHostname, recipient);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onPart(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onPart(String channel, String sender, String login,
			String hostname) {
		updateWho(channel);
		super.onPart(channel, sender, login, hostname);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onPrivateMessage(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onPrivateMessage(String sender, String login,
			String hostname, String message) {

		createRoom(sender);
		RoomManager.queue.add(new Message(this, message, sender, sender));	
		RoomManager.manageQueue();
		
		super.onPrivateMessage(sender, login, hostname, message);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onQuit(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onQuit(String sourceNick, String sourceLogin,
			String sourceHostname, String reason) {
		
		for(Room r : RoomManager.getRooms()){
			if(r.getServerConnection().equals(this)){
				RoomManager.updateWho(r);
			}
		}
		
		super.onQuit(sourceNick, sourceLogin, sourceHostname, reason);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onServerResponse(int, java.lang.String)
	 */
	@Override
	protected void onServerResponse(int code, String response) {
		switch(code){
		//topic
		case 332:
			String channel = response.split(":")[0].trim();
			channel = channel.substring(channel.indexOf("#"));
			String topic = response;
			topic = topic.substring(topic.indexOf("'"));
			for(Room r : RoomManager.getRooms()){
				if(r.getChannel().getChannelName().equals(channel)){
					RoomManager.changeTopic(r, topic);
					break;
				}
			}
			break;
			
		//no idea?
		case 333:
			break;
			
		//who list - taken care of with onUserList	
		case 353:
			break;
			
		default:
			RoomManager.queue.add(new Message(this, response, "["+code+"] Server Response", "Console"));	
			RoomManager.manageQueue();
		}
		super.onServerResponse(code, response);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onUserList(java.lang.String, org.jibble.pircbot.User[])
	 */
	@Override
	protected void onUserList(String channel, User[] users) {

		updateWho(channel);
		
		super.onUserList(channel, users);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onUserMode(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onUserMode(String targetNick, String sourceNick,
			String sourceLogin, String sourceHostname, String mode) {
		super.onUserMode(targetNick, sourceNick, sourceLogin, sourceHostname, mode);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onVoice(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onVoice(String channel, String sourceNick,
			String sourceLogin, String sourceHostname, String recipient) {
		//updateWho(channel);
		super.onVoice(channel, sourceNick, sourceLogin, sourceHostname, recipient);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#partChannel(java.lang.String)
	 */
	@Override
	public void partChannel(String channel) {
		// TODO Auto-generated method stub
		super.partChannel(channel);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#partChannel(java.lang.String, java.lang.String)
	 */
	@Override
	public void partChannel(String channel, String reason) {
		// TODO Auto-generated method stub
		super.partChannel(channel, reason);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#sendAction(java.lang.String, java.lang.String)
	 */
	@Override
	public void sendAction(String target, String action) {
		// TODO Auto-generated method stub
		super.sendAction(target, action);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#sendMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMessage(String target, String message) {
		
		RoomManager.queue.add(new Message(this, message,getNick(),target));	
		RoomManager.manageQueue();
		
		super.sendMessage(target, message);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#sendNotice(java.lang.String, java.lang.String)
	 */
	@Override
	public void sendNotice(String target, String notice) {
		// TODO Auto-generated method stub
		super.sendNotice(target, notice);
	}

	/**
	 * @param chanList the chanList to set
	 */
	public void setChanList(CTabFolder chanList) {
		this.chanList = chanList;
	}

	/**
	 * @param server the server to set
	 */
	public void setMyServer(String server) {
		this.myServer = server;
	}

	/**
	 * @param nick the nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	private void updateWho(String channel) {
		Room r = findRoom(channel);
		RoomManager.updateWho(r);
	}
}
