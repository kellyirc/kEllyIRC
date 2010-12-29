package connection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Display;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.TrustingSSLSocketFactory;

import shared.Message;
import ui.Channel;
import ui.Room;
import ui.RoomManager;

/*
 * TODO:
 * 		features for each connection: 
 * 			ssl support
 * 			server password
 * 			specify port to use (default 6669)
 * 			nickname
 * 			quit message
 * 			"ident" (pircbot is a crappy ident to have)
 * 			real name
 */

public class Connection extends PircBot{
	
	private CTabFolder chanList;
	
	private String nick;

	private String myServer;

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

	/**
	 * @return the chanList
	 */
	public CTabFolder getChanList() {
		return chanList;
	}

	/**
	 * @return the nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @return the server
	 */
	public String getMyServer() {
		return this.myServer;
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#joinChannel(java.lang.String)
	 */
	@Override
	public void joinChannel(String channel) {
		createRoom(channel);		
		
		super.joinChannel(channel);
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
	 * @see org.jibble.pircbot.PircBot#joinChannel(java.lang.String, java.lang.String)
	 */
	@Override
	public void joinChannel(String channel, String key) {
		//TODO: not auto generated, just needs to be modified to function like joinChannel(String channel)	
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
	 * @see org.jibble.pircbot.PircBot#onServerResponse(int, java.lang.String)
	 */
	@Override
	protected void onServerResponse(int code, String response) {
		switch(code){
		//topic
		case 332:
			String channel = response.split(":")[0].trim();
			channel = channel.substring(channel.indexOf("#"));
			String topic = response;//.split(":")[1];
			topic = topic.substring(topic.indexOf("'"));//.substring(channel.indexOf("'"));
			for(Room r : RoomManager.getRooms()){
				if(r.getChannel().getChannelName().equals(channel)){
					RoomManager.changeTopic(r, topic);
					break;
				}
			}
			break;
		//
		case 333:
		//who list
		case 353:
			
		default:
			RoomManager.queue.add(new Message(this, response, "["+code+"] Server Response", "Console"));	
			RoomManager.manageQueue();
		}
		super.onServerResponse(code, response);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onDeop(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onDeop(String channel, String sourceNick,
			String sourceLogin, String sourceHostname, String recipient) {
		// TODO Auto-generated method stub
		super.onDeop(channel, sourceNick, sourceLogin, sourceHostname, recipient);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onDeVoice(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onDeVoice(String channel, String sourceNick,
			String sourceLogin, String sourceHostname, String recipient) {
		// TODO Auto-generated method stub
		super.onDeVoice(channel, sourceNick, sourceLogin, sourceHostname, recipient);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onJoin(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onJoin(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub
		super.onJoin(channel, sender, login, hostname);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onKick(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onKick(String channel, String kickerNick,
			String kickerLogin, String kickerHostname, String recipientNick,
			String reason) {
		// TODO Auto-generated method stub
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
	 * @see org.jibble.pircbot.PircBot#onNickChange(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onNickChange(String oldNick, String login, String hostname,
			String newNick) {
		// TODO Auto-generated method stub
		super.onNickChange(oldNick, login, hostname, newNick);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onOp(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onOp(String channel, String sourceNick, String sourceLogin,
			String sourceHostname, String recipient) {
		// TODO Auto-generated method stub
		super.onOp(channel, sourceNick, sourceLogin, sourceHostname, recipient);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onPart(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onPart(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub
		super.onPart(channel, sender, login, hostname);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onPrivateMessage(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onPrivateMessage(String sender, String login,
			String hostname, String message) {

		// TODO: check for a pm tab, if not, create one
		
		// TODO Auto-generated method stub
		super.onPrivateMessage(sender, login, hostname, message);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onQuit(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onQuit(String sourceNick, String sourceLogin,
			String sourceHostname, String reason) {
		// TODO Auto-generated method stub
		super.onQuit(sourceNick, sourceLogin, sourceHostname, reason);
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
	 * @see org.jibble.pircbot.PircBot#sendMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMessage(String target, String message) {
		
		RoomManager.queue.add(new Message(this, message,getNick(),target));	
		RoomManager.manageQueue();
		
		super.sendMessage(target, message);
	}

	/**
	 * @param chanList the chanList to set
	 */
	public void setChanList(CTabFolder chanList) {
		this.chanList = chanList;
	}

	/**
	 * @param nick the nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @param server the server to set
	 */
	public void setMyServer(String server) {
		this.myServer = server;
	}
}
