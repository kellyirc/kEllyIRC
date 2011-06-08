package connection;

import java.util.LinkedList;

import lombok.Getter;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.layout.grouplayout.GroupLayout;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.layout.grouplayout.LayoutStyle;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.managers.ListenerManager;

import ui.Room;
import ui.RoomManager;

public class Connection extends Composite {
	
	private class ConnectionData extends ListenerAdapter<KEllyBot> {
		
		private @Getter KEllyBot bot;
		
		@SuppressWarnings("unchecked")
		public ConnectionData(KEllyBot bot, String server, String nick, Connection nc) {

			this.bot = bot;
			
			bot.setVerbose(true);
			bot.changeNick(nick);
			bot.setVersion(KEllyBot.VERSION);
			bot.setAutoNickChange(true);
			bot.setName(nick);
			
			ListenerManager<PircBotX> l = bot.getListenerManager();
			l.addListener(this);
			l.addListener(new RoomListener(nc));
			l.addListener(new ServerListener(nc));
			l.addListener(new UserListener(nc));
			l.addListener(new MessageListener(nc));

			try {
				bot.connect(server, 6667);
			} catch(Exception e) {
				e.printStackTrace();
			}

			//TODO: remove this -- it is temporary
			bot.joinChannel("#idlebot");
		}
	}
	
	private @Getter ScrolledComposite scrolledComposite;
	private @Getter KEllyBot bot;
	private @Getter ConnectionData data;
	private Tree chanList;
	private LinkedList<Room> rooms = new LinkedList<Room>();
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public Connection(Composite parent, int style, String server, String nick) {
		super(parent, style);

		CTabItem c = new CTabItem((CTabFolder) parent, SWT.NONE);
		c.setText(server);
		c.setControl(this);
		
		this.bot = new KEllyBot(this);
		
		this.data = new ConnectionData(this.bot, server, nick, this);
		
		chanList = new Tree(this, SWT.BORDER);
		
		scrolledComposite = new ScrolledComposite(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(GroupLayout.LEADING)
				.add(groupLayout.createSequentialGroup()
					.add(chanList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.RELATED)
					.add(scrolledComposite, GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(GroupLayout.LEADING)
				.add(chanList, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
				.add(scrolledComposite, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
		);
		setLayout(groupLayout);
		
		chanList.addListener(SWT.MouseDown, new Listener () {
			@Override
			public void handleEvent (org.eclipse.swt.widgets.Event event) {
				Point point = new Point (event.x, event.y);
				final TreeItem item = chanList.getItem (point);

				if (item != null && item.getData()!=null) {
					switchComposite((Room) item.getData());
				}
			}
		});	
		
		createRoom("Console", Room.IO);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void switchComposite(Room c){
		scrolledComposite.setContent(c);
		c.updateTopic();
		c.updateWho();
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
		RoomManager.createRoom((Composite)scrolledComposite,chanList,SWT.NONE,chanstr,this,layout,chan);
	}
	
	public void addRoom(Room r){
		rooms.add(r);
		
	}
	
	public void updateWho(String channel) {
		Room r = findRoom(channel);
		if(r!=null){
			r.updateWho();
		}
	}
	
	public void updateTopic(String channel) {
		Room r = findRoom(channel);
		if(r!=null){
			r.updateTopic();
		}
	}
	
	public Room findRoom(String s) {
		for(Room r : rooms){
			if(r.getCChannel().getChannelString().equals(s)) {
				return r;
			}
		}
		return null;
	}
	
	public boolean canAddRoom(String s){
		for(Room r : rooms){
			if(r.getCChannel().
					getChannelString().
					equals(s)){
				return false;
			}
		}
		return true;
	}
}