package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

import connection.Connection;

public class Channel {
	
	private String channelName;
	private CTabItem tabRef;
	private Connection conn;

	
	public Channel(CTabFolder c, String s, final Connection conn){
		this.setConn(conn);
		setChannelName(s.trim());
		tabRef = new CTabItem(c, SWT.NONE | (s.equals("Console") ? 0 : SWT.CLOSE));
		tabRef.setText(s);
		
		
		
		tabRef.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				conn.partChannel(channelName);
			}});
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelName() {
		return channelName;
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
