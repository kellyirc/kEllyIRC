package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.pircbotx.Channel;

import connection.Connection;

public class CustomChannel {
	
	private Channel channel;
	private CTabItem tabRef;
	private Connection conn;
	private String channelString;
	
	
	
	public CustomChannel(CTabFolder c, String s, final Connection conn, Channel chan){
		this.setConn(conn);
		setChannel(chan);
		setChannelString(s);
		tabRef = new CTabItem(c, SWT.NONE | (s.equals("Console") ? 0 : SWT.CLOSE));
		tabRef.setText(s);
		
		
		
		tabRef.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				conn.partChannel(channel.getName());
			}});
	}
	
	public void setChannelString(String channelString) {
		this.channelString = channelString;
	}
	
	public String getChannelString() {
		return channelString;
	}
	
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setTabRef(CTabItem tabRef) {
		this.tabRef = tabRef;
	}

	public CTabItem getTabRef() {
		return tabRef;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public Connection getConn() {
		return conn;
	}
	
	
}
