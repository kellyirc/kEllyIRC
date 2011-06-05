package connection;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Display;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.ChannelInfoEvent;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.FileTransferFinishedEvent;
import org.pircbotx.hooks.events.FingerEvent;
import org.pircbotx.hooks.events.FounderEvent;
import org.pircbotx.hooks.events.HalfOpEvent;
import org.pircbotx.hooks.events.IncomingChatRequestEvent;
import org.pircbotx.hooks.events.IncomingFileTransferEvent;
import org.pircbotx.hooks.events.InviteEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.ModeEvent;
import org.pircbotx.hooks.events.MotdEvent;
import org.pircbotx.hooks.events.NickChangeEvent;
import org.pircbotx.hooks.events.NoticeEvent;
import org.pircbotx.hooks.events.OpEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.QuitEvent;
import org.pircbotx.hooks.events.RemoveChannelBanEvent;
import org.pircbotx.hooks.events.RemoveChannelKeyEvent;
import org.pircbotx.hooks.events.RemoveChannelLimitEvent;
import org.pircbotx.hooks.events.RemoveInviteOnlyEvent;
import org.pircbotx.hooks.events.RemoveModeratedEvent;
import org.pircbotx.hooks.events.RemoveNoExternalMessagesEvent;
import org.pircbotx.hooks.events.RemovePrivateEvent;
import org.pircbotx.hooks.events.RemoveSecretEvent;
import org.pircbotx.hooks.events.RemoveTopicProtectionEvent;
import org.pircbotx.hooks.events.ServerPingEvent;
import org.pircbotx.hooks.events.ServerResponseEvent;
import org.pircbotx.hooks.events.SetChannelBanEvent;
import org.pircbotx.hooks.events.SetChannelKeyEvent;
import org.pircbotx.hooks.events.SetChannelLimitEvent;
import org.pircbotx.hooks.events.SetInviteOnlyEvent;
import org.pircbotx.hooks.events.SetModeratedEvent;
import org.pircbotx.hooks.events.SetNoExternalMessagesEvent;
import org.pircbotx.hooks.events.SetPrivateEvent;
import org.pircbotx.hooks.events.SetSecretEvent;
import org.pircbotx.hooks.events.SetTopicProtectionEvent;
import org.pircbotx.hooks.events.SuperOpEvent;
import org.pircbotx.hooks.events.TimeEvent;
import org.pircbotx.hooks.events.TopicEvent;
import org.pircbotx.hooks.events.UnknownEvent;
import org.pircbotx.hooks.events.UserListEvent;
import org.pircbotx.hooks.events.UserModeEvent;
import org.pircbotx.hooks.events.VersionEvent;
import org.pircbotx.hooks.events.VoiceEvent;
import org.pircbotx.hooks.managers.ListenerManager;

import shared.Message;
import ui.Room;
import ui.RoomManager;

public class Connection extends ListenerAdapter {
	
	private PircBotX bot;
	
	private CTabFolder chanList;

	private String myServer, nick;
	
				  //channel, users
	private HashMap<String, ArrayList<User>> users = new HashMap<String,ArrayList<User>>();
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
		
		bot = new PircBotX();
		ListenerManager<PircBotX> manager = bot.getListenerManager();
		manager.addListener(this);
		
		bot.setVersion("kellyIRCX v"+PircBotX.VERSION);
		
		bot.setAutoNickChange(true);
		bot.setName(nick);
		bot.setVerbose(true);
		
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
			bot.connect(server, 6667);
			//this.connect(server);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//TODO: remove this -- it is temporary
		this.joinChannel("#dgr");
		
	}
	
	public void setBot(PircBotX bot) {
		this.bot = bot;
	}
	
	public PircBotX getBot() {
		return bot;
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#ban(java.lang.String, java.lang.String)
	 */
	
	public void ban(String channel, String hostmask) {
		// TODO Auto-generated method stub
		bot.ban(channel, hostmask);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#changeNick(java.lang.String)
	 */
	
	public void changeNick(String newNick) {
		// TODO Auto-generated method stub
		bot.changeNick(newNick);
	}
	
	public void createRoom(String s, int layout) {
		createRoom(s,layout,null);
	}

	/**Create a new room based on a channel name. This does NOT need to lead to an actual channel, 
	 * as it just handles creation of the window itself. 
	 * 
	 * @param channel the name of the ctabitem that will be created
	 */
	public void createRoom(String chanstr, int layout, Channel chan) {
		RoomManager.createRoom(chanList,SWT.NONE,chanstr,this,layout,chan);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#deOp(java.lang.String, java.lang.String)
	 */
	
	public void deOp(Channel channel, User nick) {
		// TODO Auto-generated method stub
		bot.deOp(channel, nick);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#deVoice(java.lang.String, java.lang.String)
	 */
	
	public void deVoice(Channel channel, User nick) {
		// TODO Auto-generated method stub
		bot.deVoice(channel, nick);
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
			if(r.getServerConnection().equals(this) && r.getChannel().getChannelString().equals(channel)){
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

	public HashMap<String, ArrayList<User>> getUsers() {
		return users;
	}
	
	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#identify(java.lang.String)
	 */
	
	public void identify(String password) {
		// TODO Auto-generated method stub
		bot.identify(password);
	}
	
	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#joinChannel(java.lang.String)
	 */
	
	public void joinChannel(String channel) {
		Channel channelObj = bot.getChannel(channel);
		createRoom(channel, Room.IO | Room.TOPIC | Room.WHO, channelObj);
		bot.joinChannel(channel);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#joinChannel(java.lang.String, java.lang.String)
	 */
	
	public void joinChannel(String channel, String key) {
		Channel channelObj = bot.getChannel(channel);
		createRoom(channel, Room.IO | Room.TOPIC | Room.WHO, channelObj);
		bot.joinChannel(channel, key);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#kick(java.lang.String, java.lang.String)
	 */
	
	public void kick(Channel channel, User nick) {
		// TODO Auto-generated method stub
		bot.kick(channel, nick);
		updateWho(channel.getName());
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#kick(java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public void kick(Channel channel, User nick, String reason) {
		// TODO Auto-generated method stub
		bot.kick(channel, nick, reason);
		updateWho(channel.getName());
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#listChannels()
	 */
	
	public void listChannels() {
		// TODO Auto-generated method stub
		bot.listChannels();
	}

	/**
	 * @return the topics
	 */
	public HashMap<String, String> getTopics() {
		return topics;
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#partChannel(java.lang.String)
	 */
	
	public void partChannel(String channel) {
		users.remove(channel);
		bot.partChannel(channel);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#partChannel(java.lang.String, java.lang.String)
	 */
	
	public void partChannel(String channel, String reason) {
		users.remove(channel);
		bot.partChannel(channel, reason);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#quitServer()
	 */
	
	public void quitServer() {
		// TODO Auto-generated method stub
		bot.quitServer();
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#quitServer(java.lang.String)
	 */
	
	public void quitServer(String reason) {
		// TODO Auto-generated method stub
		bot.quitServer(reason);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#sendAction(java.lang.String, java.lang.String)
	 */
	
	public void sendAction(String target, String action) {
		// TODO Auto-generated method stub
		bot.sendAction(target, action);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#sendInvite(java.lang.String, java.lang.String)
	 */
	
	public void sendInvite(String nick, String channel) {
		// TODO Auto-generated method stub
		bot.sendInvite(nick, channel);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#sendMessage(java.lang.String, java.lang.String)
	 */
	
	public void sendMessage(String target, String message) {
		
		RoomManager.queue.add(new Message(this, message,getNick(),target));	
		RoomManager.manageQueue();
		
		bot.sendMessage(target, message);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#sendNotice(java.lang.String, java.lang.String)
	 */
	
	public void sendNotice(String target, String notice) {
		// TODO Auto-generated method stub
		bot.sendNotice(target, notice);
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
	
	public void setMode(Channel channel, String mode) {
		// TODO Auto-generated method stub
		bot.setMode(channel, mode);
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
	
	public void setTopic(Channel channel, String topic) {
		// TODO Auto-generated method stub
		bot.setTopic(channel, topic);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#unBan(java.lang.String, java.lang.String)
	 */
	
	public void unBan(String channel, String hostmask) {
		// TODO Auto-generated method stub
		bot.unBan(channel, hostmask);
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#updateUser(java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	
	/*protected void updateUser(String channel, String prefix, boolean add,
			String nick) {
		for(User i : users.get(channel)){
			if(i.getNick().equals(nick)){
				i.setPrefix(add ? prefix : "");
			}
		}
		bot.updateUser(channel, prefix, add, nick);
	}*/

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
	public void voice(Channel channel, User nick) {
		// TODO Auto-generated method stub
		bot.voice(channel, nick);
	}

	@Override
	public void onAction(ActionEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onAction(event);
	}

	@Override
	public void onChannelInfo(ChannelInfoEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onChannelInfo(event);
	}

	@Override
	public void onConnect(ConnectEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onConnect(event);
	}

	@Override
	public void onDisconnect(DisconnectEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onDisconnect(event);
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub
		super.onEvent(arg0);
	}

	@Override
	public void onFileTransferFinished(FileTransferFinishedEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onFileTransferFinished(event);
	}

	@Override
	public void onFinger(FingerEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onFinger(event);
	}

	@Override
	public void onFounder(FounderEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onFounder(event);
	}

	@Override
	public void onHalfOp(HalfOpEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onHalfOp(event);
		updateWho(event.getChannel().getName());
	}

	@Override
	public void onIncomingChatRequest(IncomingChatRequestEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onIncomingChatRequest(event);
	}

	@Override
	public void onIncomingFileTransfer(IncomingFileTransferEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onIncomingFileTransfer(event);
	}

	@Override
	public void onInvite(InviteEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onInvite(event);
	}

	@Override
	public void onJoin(JoinEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onJoin(event);
		
		updateWho(event.getChannel().getName());
		this.findRoom(
				event.
				getChannel().
				getName())
				.updateUserCount();
	}

	@Override
	public void onKick(KickEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onKick(event);
		updateWho(event.getChannel().getName());
		this.findRoom(event.getChannel().getName()).updateUserCount();
	}

	@Override
	public void onMessage(MessageEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onMessage(event);
		
		String message = event.getMessage();
		String sender = event.getUser().getNick();
		String channel = event.getChannel().getName();
		
		//add the message to the queue, and then make the queue clean itself out
		RoomManager.queue.add(new Message(this, message, sender, channel));	
		RoomManager.manageQueue();
		this.findRoom(channel).updateLastMessage("<" + sender + "> " + message);
		
		
	}

	@Override
	public void onMode(ModeEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onMode(event);
		updateWho(event.getChannel().getName());
	}

	@Override
	public void onMotd(MotdEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onMotd(event);
	}

	@Override
	public void onNickChange(NickChangeEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onNickChange(event);
		
		for(Channel r : bot.getChannels()){
			updateWho(r.getName());
		}
	}

	@Override
	public void onNotice(NoticeEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onNotice(event);
		if(event.getUser().getLogin().equals("")) {
			RoomManager.queue.add(new Message(this, event.getNotice(), "Notice from "+event.getUser().getNick(), "Console"));	
			RoomManager.manageQueue();
		}
	}

	@Override
	public void onOp(OpEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onOp(event);
		
		updateWho(event.getChannel().getName());
	}

	@Override
	public void onPart(PartEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onPart(event);
		
		updateWho(event.getChannel().getName());
		this.findRoom(event.getChannel().getName()).updateUserCount();
	}

	@Override
	public void onPing(PingEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onPing(event);
	}

	@Override
	public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onPrivateMessage(event);
		createRoom(event.getUser().getNick(), Room.IO);
		RoomManager.queue.add(new Message(this, event.getMessage(), event.getUser().getNick(), event.getUser().getNick()));	
		RoomManager.manageQueue();
	}

	@Override
	public void onQuit(QuitEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onQuit(event);
		
		for(String r : getUsers().keySet()){
			updateWho(r);
		}
		
		for(Channel channel: bot.getChannels())
		{
			this.findRoom(channel.getName()).updateUserCount();
		}
	}

	@Override
	public void onRemoveChannelBan(RemoveChannelBanEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveChannelBan(event);
	}

	@Override
	public void onRemoveChannelKey(RemoveChannelKeyEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveChannelKey(event);
	}

	@Override
	public void onRemoveChannelLimit(RemoveChannelLimitEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveChannelLimit(event);
	}

	@Override
	public void onRemoveInviteOnly(RemoveInviteOnlyEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveInviteOnly(event);
	}

	@Override
	public void onRemoveModerated(RemoveModeratedEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveModerated(event);
	}

	@Override
	public void onRemoveNoExternalMessages(RemoveNoExternalMessagesEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveNoExternalMessages(event);
	}

	@Override
	public void onRemovePrivate(RemovePrivateEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onRemovePrivate(event);
	}

	@Override
	public void onRemoveSecret(RemoveSecretEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveSecret(event);
	}

	@Override
	public void onRemoveTopicProtection(RemoveTopicProtectionEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveTopicProtection(event);
	}

	@Override
	public void onServerPing(ServerPingEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onServerPing(event);
	}

	@Override
	public void onServerResponse(ServerResponseEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onServerResponse(event);
		
		int code = event.getCode();
		
		//TODO: add more codes as they show up
		switch(code){
		//end of names list
		case 366:
			break;
		//topic
		case 332:
			String channel = event.getResponse().split(":")[0].trim();
			channel = channel.substring(channel.indexOf("#"));
			String topic = event.getResponse();
			//topic = topic.substring(topic.indexOf("'"));
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
			RoomManager.queue.add(new Message(this, event.getResponse(), "["+code+"] Server Response", "Console"));	
			RoomManager.manageQueue();
		}
	}

	@Override
	public void onSetChannelBan(SetChannelBanEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onSetChannelBan(event);
	}

	@Override
	public void onSetChannelKey(SetChannelKeyEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onSetChannelKey(event);
	}

	@Override
	public void onSetChannelLimit(SetChannelLimitEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onSetChannelLimit(event);
	}

	@Override
	public void onSetInviteOnly(SetInviteOnlyEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onSetInviteOnly(event);
	}

	@Override
	public void onSetModerated(SetModeratedEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onSetModerated(event);
	}

	@Override
	public void onSetNoExternalMessages(SetNoExternalMessagesEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onSetNoExternalMessages(event);
	}

	@Override
	public void onSetPrivate(SetPrivateEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onSetPrivate(event);
	}

	@Override
	public void onSetSecret(SetSecretEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onSetSecret(event);
	}

	@Override
	public void onSetTopicProtection(SetTopicProtectionEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onSetTopicProtection(event);
	}

	@Override
	public void onSuperOp(SuperOpEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onSuperOp(event);
	}

	@Override
	public void onTime(TimeEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onTime(event);
	}

	@Override
	public void onTopic(TopicEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onTopic(event);
		
		topics.put(event.getChannel().getName(), event.getTopic());
		
		for(Room c : RoomManager.getRooms()){
			if(c.getServerConnection() == this && c.getChannel().getChannelString().equals(event.getChannel().getName())){
				RoomManager.changeTopic(c, event.getTopic());
			}
		}
	}

	@Override
	public void onUnknown(UnknownEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onUnknown(event);
	}

	@Override
	public void onUserList(UserListEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onUserList(event);
		
		ArrayList<User> temp = new ArrayList<User>();
		for(User i : event.getUsers()){
			temp.add(i);
		}
		this.users.put(event.getChannel().getName(), temp);
		
		updateWho(event.getChannel().getName());
	}

	@Override
	public void onUserMode(UserModeEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onUserMode(event);
	}

	@Override
	public void onVersion(VersionEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onVersion(event);
	}

	@Override
	public void onVoice(VoiceEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onVoice(event);
		
		updateWho(event.getChannel().getName());
	}
}
