package connection;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Display;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.TrustingSSLSocketFactory;
import org.jibble.pircbot.IrcUser;

import shared.Message;
import ui.Room;
import ui.RoomManager;

public class Connection extends PircBot{
	
	private CTabFolder chanList;

	private String myServer, nick;
	
				  //channel, users
	private HashMap<String, ArrayList<IrcUser>> users = new HashMap<String,ArrayList<IrcUser>>();
	private HashMap<String, String> topics = new HashMap<String, String>();

	/**
	 * TODO: Document me!
	 * @param parent
	 * @param server
	 * @param nick
	 */
	public Connection(CTabFolder parent, String server, String nick){

		this.myServer = server;
		this.nick = nick;
		
		setVersion("kellyIRC v"+VERSION);
		
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
		createRoom("Console", Room.IO);
		
		//tell the channel list that it has a tab, and that it needs to be drawn
		c.setControl(chanList);

		try {
			this.connect(server, 6697, new TrustingSSLSocketFactory());
			//this.connect(server);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//TODO: remove this -- it is temporary
		this.joinChannel("#dgr");
		
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#ban(java.lang.String, java.lang.String)
	 */
	@Override
	public void ban(String channel, String hostmask) {
		// TODO Auto-generated method stub
		super.ban(channel, hostmask);
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
	public void createRoom(String channel, int layout) {
		RoomManager.createRoom(chanList,SWT.NONE,channel,this,layout);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#deHalfop(java.lang.String, java.lang.String)
	 */
	@Override
	public void deHalfop(String channel, String nick) {
		// TODO Auto-generated method stub
		super.deHalfop(channel, nick);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#deOp(java.lang.String, java.lang.String)
	 */
	@Override
	public void deOp(String channel, String nick) {
		// TODO Auto-generated method stub
		super.deOp(channel, nick);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#deVoice(java.lang.String, java.lang.String)
	 */
	@Override
	public void deVoice(String channel, String nick) {
		// TODO Auto-generated method stub
		super.deVoice(channel, nick);
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

	public HashMap<String, ArrayList<IrcUser>> getUsers() {
		return users;
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#halfop(java.lang.String, java.lang.String)
	 */
	@Override
	public void halfop(String channel, String nick) {
		// TODO Auto-generated method stub
		super.halfop(channel, nick);
	}
	
	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#identify(java.lang.String)
	 */
	@Override
	public void identify(String password) {
		// TODO Auto-generated method stub
		super.identify(password);
	}
	
	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#joinChannel(java.lang.String)
	 */
	@Override
	public void joinChannel(String channel) {
		createRoom(channel, Room.IO | Room.TOPIC | Room.WHO);		
		
		super.joinChannel(channel);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#joinChannel(java.lang.String, java.lang.String)
	 */
	@Override
	public void joinChannel(String channel, String key) {
		createRoom(channel, Room.IO | Room.TOPIC | Room.WHO);			
		super.joinChannel(channel, key);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#kick(java.lang.String, java.lang.String)
	 */
	@Override
	public void kick(String channel, String nick) {
		// TODO Auto-generated method stub
		super.kick(channel, nick);
		updateWho(channel);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#kick(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void kick(String channel, String nick, String reason) {
		// TODO Auto-generated method stub
		super.kick(channel, nick, reason);
		updateWho(channel);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#listChannels()
	 */
	@Override
	public void listChannels() {
		// TODO Auto-generated method stub
		super.listChannels();
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
	 * @see org.jibble.pircbot.PircBot#onAdmin(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onAdmin(String channel, String sourceNick,
			String sourceLogin, String sourceHostname, String string) {
		updateWho(channel);
		super.onAdmin(channel, sourceNick, sourceLogin, sourceHostname, string);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onBeforeQuit(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onBeforeQuit(String sourceNick, String sourceLogin,
			String sourceHostname, String reason) {
		for(String s : getUsers().keySet()){
			updateWho(s);
		}
		super.onBeforeQuit(sourceNick, sourceLogin, sourceHostname, reason);
	}
	
	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onChannelInfo(java.lang.String, int, java.lang.String)
	 */
	@Override
	protected void onChannelInfo(String channel, int userCount, String topic) {
		
		super.onChannelInfo(channel, userCount, topic);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onConnect()
	 */
	@Override
	protected void onConnect() {
		// TODO Auto-generated method stub
		super.onConnect();
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onDeAdmin(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onDeAdmin(String channel, String sourceNick,
			String sourceLogin, String sourceHostname, String string) {
		updateWho(channel);
		super.onDeAdmin(channel, sourceNick, sourceLogin, sourceHostname, string);
	}
	
	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onDeHalfop(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onDeHalfop(String channel, String sourceNick,
			String sourceLogin, String sourceHostname, String recipient) {
		updateWho(channel);
		super.onDeHalfop(channel, sourceNick, sourceLogin, sourceHostname, recipient);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onDeop(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onDeop(String channel, String sourceNick,
			String sourceLogin, String sourceHostname, String recipient) {
		updateWho(channel);
		super.onDeop(channel, sourceNick, sourceLogin, sourceHostname, recipient);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onDeOwner(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onDeOwner(String channel, String sourceNick,
			String sourceLogin, String sourceHostname, String string) {
		updateWho(channel);
		super.onDeOwner(channel, sourceNick, sourceLogin, sourceHostname, string);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onDeVoice(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onDeVoice(String channel, String sourceNick,
			String sourceLogin, String sourceHostname, String recipient) {
		updateWho(channel);
		super.onDeVoice(channel, sourceNick, sourceLogin, sourceHostname, recipient);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onDisconnect()
	 */
	@Override
	protected void onDisconnect() {
		// TODO Auto-generated method stub
		super.onDisconnect();
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onHalfop(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onHalfop(String channel, String sourceNick,
			String sourceLogin, String sourceHostname, String recipient) {
		updateWho(channel);
		super.onHalfop(channel, sourceNick, sourceLogin, sourceHostname, recipient);
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
		for(String r : getUsers().keySet()){
			updateWho(r);
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
		updateWho(channel);
		super.onOp(channel, sourceNick, sourceLogin, sourceHostname, recipient);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onOwner(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onOwner(String channel, String sourceNick,
			String sourceLogin, String sourceHostname, String string) {
		updateWho(channel);
		super.onOwner(channel, sourceNick, sourceLogin, sourceHostname, string);
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

		createRoom(sender, Room.IO);
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
		
		for(String r : getUsers().keySet()){
			updateWho(r);
		}
		
		super.onQuit(sourceNick, sourceLogin, sourceHostname, reason);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onServerResponse(int, java.lang.String)
	 */
	@Override
	protected void onServerResponse(int code, String response) {
		//TODO: add more codes as they show up
		switch(code){
		//end of names list
		case 366:
			break;
		//topic
		case 332:
			String channel = response.split(":")[0].trim();
			channel = channel.substring(channel.indexOf("#"));
			String topic = response;
			topic = topic.substring(topic.indexOf("'"));
			//for(Room r : RoomManager.getRooms()){
				//Initializer.Debug("serverresponse - "+r.getChannel().getChannelName());
			//	if(r.getChannel().getChannelName().equals(channel)){
					//RoomManager.changeTopic(r, topic);
					topics.put(channel, topic);
			//		break;
			//	}
			//}
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

	/**
	 * @return the topics
	 */
	public HashMap<String, String> getTopics() {
		return topics;
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onTopic(java.lang.String, java.lang.String, java.lang.String, long, boolean)
	 */
	@Override
	protected void onTopic(String channel, String topic, String setBy, String setByLogin, String setByHost,
			long date, boolean changed) {
		for(Room c : RoomManager.getRooms()){
			if(c.getServerConnection().equals(this) && c.getChannel().getChannelName().equals(channel)){
				RoomManager.changeTopic(c, topic);
			}
		}
		super.onTopic(channel, topic, setBy, setByLogin, setByHost, date, changed);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onUserList(java.lang.String, org.jibble.pircbot.User[])
	 */
	@Override
	protected void onUserList(String channel, IrcUser[] users) {

		ArrayList<IrcUser> temp = new ArrayList<IrcUser>();
		for(IrcUser i : users){
			temp.add(i);
		}
		this.users.put(channel, temp);
		
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
		users.remove(channel);
		super.partChannel(channel);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#partChannel(java.lang.String, java.lang.String)
	 */
	@Override
	public void partChannel(String channel, String reason) {
		users.remove(channel);
		super.partChannel(channel, reason);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#quitServer()
	 */
	@Override
	public void quitServer() {
		// TODO Auto-generated method stub
		super.quitServer();
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#quitServer(java.lang.String)
	 */
	@Override
	public void quitServer(String reason) {
		// TODO Auto-generated method stub
		super.quitServer(reason);
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
	 * @see org.jibble.pircbot.PircBot#sendInvite(java.lang.String, java.lang.String)
	 */
	@Override
	public void sendInvite(String nick, String channel) {
		// TODO Auto-generated method stub
		super.sendInvite(nick, channel);
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

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#setMode(java.lang.String, java.lang.String)
	 */
	@Override
	public void setMode(String channel, String mode) {
		// TODO Auto-generated method stub
		super.setMode(channel, mode);
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

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#setTopic(java.lang.String, java.lang.String)
	 */
	@Override
	public void setTopic(String channel, String topic) {
		// TODO Auto-generated method stub
		super.setTopic(channel, topic);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#unBan(java.lang.String, java.lang.String)
	 */
	@Override
	public void unBan(String channel, String hostmask) {
		// TODO Auto-generated method stub
		super.unBan(channel, hostmask);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#updateUser(java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	@Override
	protected void updateUser(String channel, String prefix, boolean add,
			String nick) {
		for(IrcUser i : users.get(channel)){
			if(i.getNick().equals(nick)){
				i.setPrefix(add ? prefix : "");
			}
		}
		super.updateUser(channel, prefix, add, nick);
	}

	/**
	 * Update the who list for a given channel.
	 * @param channel the channel to update the who list on
	 */
	private void updateWho(String channel) {
		Room r = findRoom(channel);
		RoomManager.updateWho(r);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#voice(java.lang.String, java.lang.String)
	 */
	@Override
	public void voice(String channel, String nick) {
		// TODO Auto-generated method stub
		super.voice(channel, nick);
	}
}
