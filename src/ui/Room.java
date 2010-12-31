package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.grouplayout.GroupLayout;
import org.eclipse.swt.layout.grouplayout.GroupLayout.ParallelGroup;
import org.eclipse.swt.layout.grouplayout.GroupLayout.SequentialGroup;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import connection.Connection;

public class Room extends Composite{

	public static final int WHO=1, TOPIC=2, IO=4;

	private Connection serverConnection;
	
	private Channel channel;
	
	private Text output, input, topicBox;
	
	private Tree who;
	
	private String topic;
	
	private int layout;
	
	public Room(Composite parent, int style, int layout){
		super(parent, style);
		this.layout = layout;
	}
	
	public void instantiate(){
		//get the parent composite ready to roll
		Composite composite = new Composite(getServerConnection().getChanList(), SWT.NONE);
		channel.getTabRef().setControl(composite);
		
		if((layout & TOPIC)!=0){
			topicBox = new Text(composite, SWT.BORDER | SWT.WRAP);
			topicBox.setEditable(false);
		}
		if((layout & IO)!=0){
			//set up the output window
			output = new Text(composite, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP | SWT.MULTI);
			output.setEditable(false);
			
			//set up the input box and it's enter-key listener
			input = new Text(composite, SWT.BORDER);
			input.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					//CR == Carriage Return == Enter
					if(e.character == SWT.CR){
						if(input.getText().startsWith("/")){
							serverConnection.doCommand(input.getText().substring(1));
						} else {
							serverConnection.sendMessage(channel.getChannelName(), input.getText());
						}
						input.setText("");
					}
				}
			});
			
		}
		
		//TODO: make this more functional (clicky/right-clicky for individual people)
		//TODO: make this temporary measure work for testing purposes
		if((layout & WHO)!=0){
			who = new Tree(composite, SWT.BORDER);
		}
		
		//generate the anchors for the windows
		GroupLayout gl_composite = new GroupLayout(composite);
		
		SequentialGroup ss = gl_composite.createSequentialGroup();
		ParallelGroup p2 = gl_composite.createParallelGroup(GroupLayout.LEADING);

		if((layout & TOPIC)!=0){
			p2.add(topicBox, GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE);
		}
		if((layout & IO)!=0){
			p2.add(output, GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE);
			p2.add(input, GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE);
		}
		ss.add(p2);
		if((layout & WHO)!=0){
			ss.add(who, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE);
		}
        gl_composite.setHorizontalGroup(
                gl_composite.createParallelGroup(GroupLayout.LEADING)
                        .add(ss)
        );
        
		ParallelGroup p = gl_composite.createParallelGroup(GroupLayout.LEADING);
		SequentialGroup s = gl_composite.createSequentialGroup();
		
		if((layout & TOPIC)!=0){
			s.add(topicBox,GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE);
		}
		if((layout & IO)!=0){
			if((layout & TOPIC)==0){
				s.add(output, GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE);
			} else {
				s.add(output, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE);
			}
			s.add(input);
		}
		p.add(s);
		if((layout & WHO)!=0){
			p.add(who, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE);
		}
		gl_composite.setVerticalGroup(p);
		
		composite.setLayout(gl_composite);
	}
	
	/**
	 * @return the layout
	 */
	public int getIntLayout() {
		return layout;
	}

	public void setOutput(Text output) {
		this.output = output;
	}

	public Text getOutput() {
		return output;
	}

	public void setInput(Text input) {
		this.input = input;
	}

	public Text getInput() {
		return input;
	}

	public void setWho(Tree who) {
		this.who = who;
	}

	public Tree getWho() {
		return who;
	}

	public void setChannelName(String channelName) {
		this.channel.setChannelName(channelName);
	}

	public Channel getChannel() {
		return channel;
	}
	
	public void setChannel(Channel c){
		this.channel = c;
	}

	public void setServerConnection(Connection serverConnection) {
		this.serverConnection = serverConnection;
	}

	public Connection getServerConnection() {
		return serverConnection;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
		topicBox.setText(topic);
		topicBox.setToolTipText(topic);
	}
	
	public String getTopic() {
		return topic;
	}
	
}
