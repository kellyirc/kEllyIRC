package ui;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.MovementEvent;
import org.eclipse.swt.custom.MovementListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wb.swt.layout.grouplayout.GroupLayout;
import org.eclipse.wb.swt.layout.grouplayout.GroupLayout.ParallelGroup;
import org.eclipse.wb.swt.layout.grouplayout.GroupLayout.SequentialGroup;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.pircbotx.Channel;
import org.pircbotx.User;

import connection.Connection;

public @Data
@EqualsAndHashCode(callSuper = false)
class Room extends Composite {
	
	// TODO: make maximized window resemble minimized window, sizewise -- all of the components are absurdly large.

	// TODO: monospaced font
	
	// TODO: fake tooltips (http://dev.eclipse.org/viewcvs/viewvc.cgi/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet125.java?view=co)
	
	// TODO: change to booleans
	public static final int WHO = 1, TOPIC = 2, IO = 4;

	private CustomChannel cChannel;
	private @Getter	@Setter Connection serverConnection;

	// make clickable links by changing the style and the data of the individual
	// messages
	private StyledText output, input, topicBox;

	private Tree who;

	// private int layout;

	// variables used with the tool tip
	private int userCount;
	private String lastMessage;
	private String channelName;

	public Room(Composite c, int style, int layout, Tree tree,
			String channelstr, Connection newConnection, Channel channel, String PMUserName) {
		super(c, style);
		setServerConnection(newConnection);
		this.cChannel = new CustomChannel(tree, channelstr, newConnection,
				channel, this);
		cChannel.setChannelString(PMUserName);
		instantiate(layout);
	}

	public void instantiate(int layout) {

		if ((layout & TOPIC) != 0) {
			topicBox = new StyledText(this, SWT.BORDER | SWT.WRAP);
			topicBox.setEditable(false);
		}
		if ((layout & IO) != 0) {
			// set up the output window
			output = new StyledText(this, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP
					| SWT.MULTI);
			output.setEditable(false);
			output.addWordMovementListener(new MovementListener() {

				@Override
				public void getNextOffset(MovementEvent arg0) {
					String[] message = arg0.lineText.split(" ");
					for (String s : message) {
						if (arg0.lineText.indexOf(s) + s.length() < arg0.offset)
							continue;
						if (s.contains("://")
								&& arg0.lineText.indexOf(s) + s.length() > arg0.offset) {
							Program.launch(s);
						}
					}
				}

				@Override
				public void getPreviousOffset(MovementEvent arg0) {

				}
			});

			// set up the input box and it's enter-key listener
			input = new StyledText(this, SWT.BORDER);
			input.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					// CR == Carriage Return == Enter
					if (e.character == SWT.CR) {
						if (input.getText().startsWith("/")) {
							serverConnection.getBot().doCommand(
									input.getText().substring(1));
						} else {
							if (cChannel != null && serverConnection != null) {
								serverConnection.getBot().sendMessage(
										cChannel.getChannelString(),
										input.getText().replaceAll("\r\n", ""));
							}
						}
						input.setText("");
					}
				}
			});

		}

		if ((layout & WHO) != 0) {
			who = new Tree(this, SWT.BORDER);
			who.addListener(SWT.MouseDown, new Listener() {
				public void handleEvent(Event event) {
					Point point = new Point(event.x, event.y);
					final TreeItem item = who.getItem(point);

					if (item != null && item.getData() != null
							&& item.getData().equals(true)) {
						Menu m = new Menu(item.getParent().getShell(),
								SWT.POP_UP);
						MenuItem mitem = new MenuItem(m, SWT.PUSH);
						mitem.setText("Query");
						mitem.addSelectionListener(new SelectionListener() {

							@Override
							public void widgetDefaultSelected(
									SelectionEvent arg0) {
							}

							@Override
							public void widgetSelected(SelectionEvent arg0) {
								serverConnection.createRoom(item.getText(), IO);

							}
						});
						item.getParent().setMenu(m);
					}
				}
			});
			who.addListener(SWT.MouseDoubleClick, new Listener() {

				@Override
				public void handleEvent(Event event) {
					Point point = new Point(event.x, event.y);
					TreeItem item = who.getItem(point);
					if (item != null) {
						serverConnection.createRoom(item.getText(), IO);
					}
				}
			});

		}

		// generate the anchors for the windows
		GroupLayout gl_composite = new GroupLayout(this);

		SequentialGroup ss = gl_composite.createSequentialGroup();
		ParallelGroup p2 = gl_composite
				.createParallelGroup(GroupLayout.LEADING);

		if ((layout & TOPIC) != 0) {
			p2.add(topicBox, GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE);
		}
		if ((layout & IO) != 0) {
			p2.add(output, GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE);
			p2.add(input, GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE);
		}
		ss.add(p2);
		if ((layout & WHO) != 0) {
			ss.add(who, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE);
		}
		gl_composite.setHorizontalGroup(gl_composite.createParallelGroup(
				GroupLayout.LEADING).add(ss));

		ParallelGroup p = gl_composite.createParallelGroup(GroupLayout.LEADING);
		SequentialGroup s = gl_composite.createSequentialGroup();

		if ((layout & TOPIC) != 0) {
			s.add(topicBox, GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE);
		}
		if ((layout & IO) != 0) {
			if ((layout & TOPIC) == 0) {
				s.add(output, GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE);
			} else {
				s.add(output, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE);
			}
			s.add(input);
		}
		p.add(s);
		if ((layout & WHO) != 0) {
			p.add(who, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE);
		}
		gl_composite.setVerticalGroup(p);

		this.setLayout(gl_composite);

		// initial tab tool tip text setting
		userCount = cChannel.getChannel() != null ? cChannel.getChannel()
				.getUsers().size() : 0;
		lastMessage = "N/A";
		channelName = cChannel.getChannelString();
	}

	public String updateTopic() {
		if(topicBox==null)return null;
		final String topic = this.getCChannel().getChannel().getTopic();
		RoomManager.getMain().getDisplay().asyncExec(new Runnable() {
			public void run() {
				topicBox.setText(topic);
				topicBox.setToolTipText(topic);
				updateToolTipText();
			}
		});
		return topic;
	}

	public void updateToolTipText() {
		// channel.getItemRef().setToolTipText("Channnel: " + channelName +
		// "\nCurrent Users: " + userCount +
		// "\nLast Message: " + lastMessage);
	}

	private void updateUserCount() {
		userCount = cChannel.getChannel().getUsers().size();
	}

	public void updateLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public void updateWho() {
		if(who==null)return;
		final Room c = this;
		RoomManager.getMain().getDisplay().asyncExec(new Runnable() {
			public void run() {
				updateUserCount();
				
				getWho().removeAll();
		
				Channel chan = c.getCChannel().getChannel();
		
				if (chan.getOwners().size() > 0) {
					TreeItem t = new TreeItem(c.getWho(), SWT.NONE);
					t.setText("Founders/Owners");
					for (User u : c.getCChannel().getChannel().getOwners()) {
						UserTreeItem i = new UserTreeItem(t, SWT.NONE, u,
								c.getCChannel());
						i.getTree().setText(u.getNick());
					}
					t.setExpanded(true);
				}
				if (chan.getSuperOps().size() > 0) {
					TreeItem t = new TreeItem(c.getWho(), SWT.NONE);
					t.setText("Super-Ops");
					for (User u : c.getCChannel().getChannel().getSuperOps()) {
						UserTreeItem i = new UserTreeItem(t, SWT.NONE, u,
								c.getCChannel());
						i.getTree().setText(u.getNick());
					}
					t.setExpanded(true);
				}
				if (chan.getOps().size() > 0) {
					TreeItem t = new TreeItem(c.getWho(), SWT.NONE);
					t.setText("Ops");
					for (User u : c.getCChannel().getChannel().getOps()) {
						UserTreeItem i = new UserTreeItem(t, SWT.NONE, u,
								c.getCChannel());
						i.getTree().setText(u.getNick());
						// users.remove(u);
					}
					t.setExpanded(true);
				}
				if (chan.getHalfOps().size() > 0) {
					TreeItem t = new TreeItem(c.getWho(), SWT.NONE);
					t.setText("Half-Ops");
					for (User u : c.getCChannel().getChannel().getHalfOps()) {
						UserTreeItem i = new UserTreeItem(t, SWT.NONE, u,
								c.getCChannel());
						i.getTree().setText(u.getNick());
					}
					t.setExpanded(true);
				}
				if (chan.getVoices().size() > 0) {
					TreeItem t = new TreeItem(c.getWho(), SWT.NONE);
					t.setText("Voices");
					for (User u : c.getCChannel().getChannel().getVoices()) {
						UserTreeItem i = new UserTreeItem(t, SWT.NONE, u,
								c.getCChannel());
						i.getTree().setText(u.getNick());
					}
					t.setExpanded(true);
				}
				
				if (chan.getNormalUsers().size() > 0) {
					TreeItem t = new TreeItem(c.getWho(), SWT.NONE);
					t.setText("Normal");
					for (User u : chan.getNormalUsers()) {
						UserTreeItem i = new UserTreeItem(t, SWT.NONE, u,
								c.getCChannel());
						i.getTree().setText(u.getNick());
					}
					t.setExpanded(true);
				}
			}
		});
	}
}
