/*
 * @author Kyle Kemp
 */
package connection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import javax.net.ssl.SSLSocketFactory;

import listeners.DisconnectListener;
import listeners.MessageListener;
import listeners.RoomListener;
import listeners.ScriptListener;
import listeners.ServerListener;
import listeners.UserListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.layout.grouplayout.GroupLayout;
import org.eclipse.wb.swt.layout.grouplayout.LayoutStyle;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.exception.NickAlreadyInUseException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.managers.ListenerManager;

import scripting.ScriptGUI;
import scripting.ScriptVars;
import shared.NSAlertBox;
import shared.RoomManager;
import ui.room.Room;

/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@EqualsAndHashCode(callSuper = false)
public class Connection extends Composite {
	
	/** The console room name. */
	public static final String CONSOLE_ROOM = "Console";

	/** The list of connections used so no duplicate connections are made. */
	public static LinkedList<ConnectionData> connections = new LinkedList<>();

	/**
	 * The Class ConnectionData.
	 */
	public class ConnectionData extends ListenerAdapter<KEllyBot> {

		/**
		 * Gets the bot representing this connection.
		 *
		 * @return the bot
		 */
		@Getter
		private KEllyBot bot;
		
		/** The connection settings represented by this data. */
		private ConnectionSettings cs;

		/**
		 * Instantiates a new connection data.
		 *
		 * @param bot the bot
		 * @param cs the cs
		 * @param nc the nc
		 */
		public ConnectionData(final KEllyBot bot, final ConnectionSettings cs,
				final Connection nc) {

			// TODO check internet connection
			// http://docs.oracle.com/javase/6/docs/api/java/net/InetAddress.html#isReachable%28int%29

			this.bot = bot;
			this.cs = cs;

			bot.setVersion(KEllyBot.VERSION);
			bot.setVerbose(false);
			bot.changeNick(cs.getNickname());
			bot.setAutoNickChange(true);
			bot.setName(cs.getNickname());
			bot.setLogin(cs.getIdent());
			bot.setMessageDelay(0);

			final ConnectionData conndat = this;
			new Thread(new Runnable() {

				@Override
				public void run() {
					// fix special characters not showing up
					try {
						bot.setEncoding("UTF-8");
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}

					ListenerManager<? extends PircBotX> l = bot.getListenerManager();
					l.addListener(conndat);
					l.addListener(new RoomListener(nc));
					l.addListener(new ServerListener(nc));
					l.addListener(new UserListener(nc));
					l.addListener(new MessageListener(nc));
					l.addListener(new ScriptListener(nc));
					l.addListener(new DisconnectListener(nc));
					// connecting to server
					try {
						attemptToConnect(cs);
					} catch (Exception e) {
						Logger.getLogger("log.error").error(
								"Error during the connection process.", e);
					}
					// identify nick
					if (!cs.getNickPassword().equals("")) {
						bot.identify(cs.getNickPassword());
					}

					// auto joining channels
					for (String channel : cs.getAutoJoin()) {
						bot.joinChannel(channel);
					}

				}
			}).start();
		}

		/**
		 * Attempt to connect.
		 *
		 * @param cs the cs
		 */
		private void attemptToConnect(ConnectionSettings cs) {
			final ConnectionSettings CS = cs;
			new Thread(new Runnable() {

				public void run() {
					int tries = 0;
					while (!bot.isConnected() && tries < 3)
						try {
							tries++;
							if (!CS.getServerPassword().equals(""))
								bot.connect(CS.getServer(), CS.getPort(),
										CS.getServerPassword());
							else if (CS.isSsl())
								bot.connect(CS.getServer(), CS.getPort(),
										SSLSocketFactory.getDefault());
							else
								bot.connect(CS.getServer(), CS.getPort());
						} catch (NumberFormatException e) {
							Logger dLog = Logger.getLogger("log.error");
							dLog.error("Improper port, not a number", e);
							new NSAlertBox(
									"Connection failed",
									"The port was not a number. Please use a number 0-65535",
									SWT.ICON_ERROR, SWT.OK);
						} catch (NickAlreadyInUseException e) {
						} catch (IOException e) {
							Logger dLog = Logger.getLogger("log.error");
							dLog.error("IOException while trying to connect", e);
						} catch (IrcException e) {
							Logger dLog = Logger.getLogger("log.error");
							dLog.error("IrcException while trying to connect",
									e);
						}
					if(bot.isConnected())
					{
						for(String cmd:CS.getPerformOnConnect())
						{
							bot.doCommand(cmd);
						}
					}
				}

			}).run();

		}
	}

	/**
	 * Gets the scrolled composite.
	 *
	 * @return the scrolled composite
	 */
	@Getter
	private ScrolledComposite scrolledComposite;
	
	/**
	 * Gets the bot.
	 *
	 * @return the bot
	 */
	@Getter
	private KEllyBot bot;
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#getData()
	 */
	@Getter
	private ConnectionData data;
	
	/**
	 * Gets the connection settings represented solely by this connection.
	 *
	 * @return the cs
	 */
	@Getter
	private ConnectionSettings cs;
	
	/** The channel list. */
	private Tree chanList;
	
	/**
	 * Gets the rooms available to this connection.
	 *
	 * @return the rooms
	 */
	@Getter
	private LinkedList<Room> rooms = new LinkedList<Room>();


	/**
	 * Check if the connection in question already exists.
	 *
	 * @param cs the cs
	 * @return true, if successful
	 */
	private boolean alreadyExists(ConnectionSettings cs) {
		for (ConnectionData c : connections) {
			if (c.cs.equals(cs)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Create the composite and connect to the server.
	 *
	 * @param parent the parent
	 * @param style the style
	 * @param cs the cs
	 */
	public Connection(final Composite parent, int style,
			final ConnectionSettings cs) {
		super(parent, style);

		final Connection conn = this;

		Display.getCurrent().asyncExec(new Runnable() {

			@Override
			public void run() {


				if (alreadyExists(cs)) {
					ScriptGUI
							.window("Please be patient:\n This connection is already running.");
					return;
				} else {
					ScriptGUI.window("Connecting to " + cs.getServer() + "..."
							+ "\nNick: " + cs.getNickname());
				}
				
				conn.cs = cs;
				bot = new KEllyBot(conn);
				data = new ConnectionData(bot, cs, conn);
				connections.add(data);

				CTabItem c = buildLayout(parent, cs);

				chanList.addListener(SWT.MouseDown, new Listener() {
					@Override
					public void handleEvent(org.eclipse.swt.widgets.Event event) {
						Point point = new Point(event.x, event.y);
						final TreeItem item = chanList.getItem(point);

						if (item != null && item.getData() != null) {
							switchComposite((Room) item.getData());
						}
					}
				});

				addTooltipListener();

				createRoom(CONSOLE_ROOM, Room.IO);
				RoomManager.getMain().getContainer().setSelection(c);
			}
		});

	}

	/**
	 * Adds the tooltip listener.
	 */
	private void addTooltipListener() {
		Listener treeListener = buildTooltip();
		chanList.addListener(SWT.Dispose, treeListener);
		chanList.addListener(SWT.KeyDown, treeListener);
		chanList.addListener(SWT.MouseMove, treeListener);
		chanList.addListener(SWT.MouseHover, treeListener);
	}

	/**
	 * Builds the tooltip.
	 *
	 * @return the listener
	 */
	private Listener buildTooltip() {
		final Listener labelListener = new Listener() {
			public void handleEvent(Event event) {
				Label label = (Label) event.widget;
				Shell shell = label.getShell();
				switch (event.type) {
				case SWT.MouseDown:
					Event e = new Event();
					e.item = (TreeItem) label.getData("_TREEITEM");
					chanList.setSelection((TreeItem) e.item);
					if (e.item != null && e.item.getData() != null)
						switchComposite((Room) e.item.getData());
					shell.dispose();
					chanList.setFocus();
					break;
				case SWT.MouseExit:
					shell.dispose();
					break;
				}
			}
		};

		Listener treeListener = new Listener() {
			Shell tip = null;
			Label label = null;

			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Dispose:
				case SWT.KeyDown:
				case SWT.MouseMove: {
					if (tip == null)
						break;
					tip.dispose();
					tip = null;
					label = null;
					break;
				}
				case SWT.MouseHover: {
					TreeItem item = chanList
							.getItem(new Point(event.x, event.y));
					if (item != null) {
						genToolTip(labelListener, item);
					}
				}
				}
			}

			private void genToolTip(final Listener labelListener, TreeItem item) {
				if (tip != null && !tip.isDisposed())
					tip.dispose();
				tip = new Shell(RoomManager.getMain().getShell(),
						SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
				tip.setBackground(RoomManager.getMain().getDisplay()
						.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
				FillLayout layout = new FillLayout();
				layout.marginWidth = 2;
				tip.setLayout(layout);
				label = new Label(tip, SWT.NONE);
				label.setForeground(RoomManager.getMain().getDisplay()
						.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
				label.setBackground(RoomManager.getMain().getDisplay()
						.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
				label.setData("_TREEITEM", item);
				label.setText(findRoom(item.getText()).getToolTipText());
				label.addListener(SWT.MouseExit, labelListener);
				label.addListener(SWT.MouseDown, labelListener);
				Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				Rectangle rect = item.getBounds(0);
				Point pt = chanList.toDisplay(rect.x, rect.y);
				tip.setBounds(pt.x, pt.y, size.x, size.y);
				tip.setVisible(true);
			}
		};
		return treeListener;
	}

	/**
	 * Builds the layout.
	 *
	 * @param parent the parent
	 * @param cs the cs
	 * @return the c tab item
	 */
	private CTabItem buildLayout(Composite parent, ConnectionSettings cs) {
		CTabItem c = new CTabItem((CTabFolder) parent, SWT.NONE);
		c.setText(cs.getConnectionName());
		c.setControl(this);

		chanList = new Tree(this, SWT.BORDER);

		scrolledComposite = new ScrolledComposite(this, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
				GroupLayout.LEADING).add(
				groupLayout
						.createSequentialGroup()
						.add(chanList, 150, 150, 150)
						.addPreferredGap(LayoutStyle.RELATED)
						.add(scrolledComposite, GroupLayout.DEFAULT_SIZE, 359,
								Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout
				.createParallelGroup(GroupLayout.LEADING)
				.add(chanList, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
				.add(scrolledComposite, GroupLayout.DEFAULT_SIZE, 300,
						Short.MAX_VALUE));
		setLayout(groupLayout);
		return c;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Composite#checkSubclass()
	 */
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * Switch rooms.
	 *
	 * @param c the c
	 */
	public void switchComposite(Room c) {
		scrolledComposite.setContent(c);
		ScriptVars.curChannel = c.getCChannel().getChannel();
		ScriptVars.curConnection = c.getBot();
		c.changeStatus(Room.NORMAL);
		c.updateTopic();
		c.updateWho();
		c.getInput().setFocus();
	}

	/**
	 * Creates the room.
	 *
	 * @param s the s
	 * @param layout the layout
	 */
	public void createRoom(String s, int layout) {
		createRoom(s, layout, null);
	}

	/**
	 * Create a new room based on a channel name. This does NOT need to lead to
	 * an actual channel, as it just handles creation of the window itself.
	 *
	 * @param chanstr the chanstr
	 * @param layout the layout
	 * @param chan the chan
	 */
	public void createRoom(String chanstr, int layout, Channel chan) {
		RoomManager.createRoom((Composite) scrolledComposite, chanList,
				SWT.NONE, chanstr, this, layout, chan);
	}

	/**
	 * Adds the room.
	 *
	 * @param r the r
	 */
	public void addRoom(Room r) {
		rooms.add(r);

	}

	/**
	 * Update who list.
	 *
	 * @param channel the channel
	 */
	public void updateWho(String channel) {
		Room r = findRoom(channel);
		if (r != null) {
			r.updateWho();
		}
	}

	/**
	 * Update topic.
	 *
	 * @param channel the channel
	 */
	public void updateTopic(String channel) {
		Room r = findRoom(channel);
		if (r != null) {
			r.updateTopic();
		}
	}

	/**
	 * Find room.
	 *
	 * @param channel the channel
	 * @return the room
	 */
	public Room findRoom(String channel) {
		for (Room r : rooms) {
			if (r.getCChannel().getChannelString().equals(channel)) {
				return r;
			}
		}
		return null;
	}

	/**
	 * Can add room.
	 *
	 * @param s the s
	 * @return true, if successful
	 */
	public boolean canAddRoom(String s) {
		for (Room r : rooms) {
			if (r.getCChannel().getChannelString().equals(s)) {
				return false;
			}
		}
		return true;
	}
}