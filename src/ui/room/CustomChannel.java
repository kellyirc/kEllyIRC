package ui.room;

import lombok.Data;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.pircbotx.Channel;


import connection.Connection;

@Data
public class CustomChannel {
	
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
