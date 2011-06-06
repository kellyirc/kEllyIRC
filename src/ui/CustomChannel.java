package ui;

import lombok.Data;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.pircbotx.Channel;

import connection.Connection;
import connection.Connection;

public @Data class CustomChannel {
	
	private Channel channel;
	private TreeItem itemRef;
	private String channelString;
	
	public CustomChannel(Tree c, String s, final Connection newConnection, Channel chan, Room r){
		setChannel(chan);
		setChannelString(s);
		itemRef = new TreeItem(c, SWT.NONE);
		itemRef.setText(s);
		itemRef.setData(r);
	}
	
}
