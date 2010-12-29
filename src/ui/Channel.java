package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

public class Channel {
	
	private String channelName;
	private CTabItem tabRef;
	
	public Channel(CTabFolder c, String s){
		setChannelName(s);
		tabRef = new CTabItem(c, SWT.NONE);
		tabRef.setText(s);
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
}
