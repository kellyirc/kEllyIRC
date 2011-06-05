package ui;

import lombok.Data;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.pircbotx.Channel;

import connection.Connection;

public @Data class CustomChannel {
	
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
	
}
