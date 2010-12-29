package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.grouplayout.GroupLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import connection.Connection;

public class Room extends Composite{

	private Connection serverConnection;
	
	private Channel channel;
	
	private Text output, input, who;
	
	public Room(Composite parent, int style){
		super(parent, style);
	}
	
	public void instantiate(){

		//get the parent composite ready to roll
		Composite composite = new Composite(getServerConnection().getChanList(), SWT.NONE);
		channel.getTabRef().setControl(composite);
		
		//set up the output window
		output = new Text(composite, SWT.BORDER | SWT.V_SCROLL);
		output.setEditable(false);
		
		//set up the input box and it's enter-key listener
		input = new Text(composite, SWT.BORDER);
		input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				//CR == Carriage Return == Enter
				if(e.character == SWT.CR){
					serverConnection.sendMessage(channel.getChannelName(), input.getText());
					input.setText("");
				}
			}
		});
		
		//TODO: make this more functional (clicky/right-clicky for individual people)
		//TODO: make this temporary measure work for testing purposes
		who = new Text(composite, SWT.BORDER);
		who.setEditable(false);
		
		//generate the anchors for the windows
		GroupLayout gl_composite = new GroupLayout(composite);
		gl_composite.setHorizontalGroup(
			gl_composite.createParallelGroup(GroupLayout.LEADING)
				.add(gl_composite.createSequentialGroup()
					.add(gl_composite.createParallelGroup(GroupLayout.LEADING)
						.add(output, GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
						.add(input, GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE))
					.add(who, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
		);
		gl_composite.setVerticalGroup(
			gl_composite.createParallelGroup(GroupLayout.LEADING)
				.add(gl_composite.createSequentialGroup()
					.add(output, GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
					.add(input))
				.add(who, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
		);
		composite.setLayout(gl_composite);
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

	public void setWho(Text who) {
		this.who = who;
	}

	public Text getWho() {
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
	
}
