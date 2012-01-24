/*
 * @author Kyle Kemp
 */
package ui.room;

import lombok.Data;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.pircbotx.Channel;


import connection.Connection;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data
public class CustomChannel {
	
	/** The channel. */
	private Channel channel;
	
	/** The item ref. */
	private TreeItem itemRef;
	
	/** The channel string. */
	private String channelString;
	
	/**
	 * Instantiates a new custom channel.
	 *
	 * @param c the c
	 * @param s the s
	 * @param newConnection the new connection
	 * @param chan the chan
	 * @param r the r
	 */
	public CustomChannel(Tree c, String s, final Connection newConnection, Channel chan, Room r){
		setChannel(chan);
		setChannelString(s);
		itemRef = new TreeItem(c, SWT.NONE);
		itemRef.setText(s);
		itemRef.setData(r);
	}
	
}
