package ui;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jibble.pircbot.IrcUser;

import connection.Connection;

public class UserTreeItem{

	private IrcUser myUser;
	private Connection connection;
	private TreeItem tree;
	private String chan;
	
	public UserTreeItem(Tree parent, int style, IrcUser u, Connection c) {
		TreeItem myItem = new TreeItem(parent, style);
		myItem.setData(false);
		setTree(myItem);
		setMyUser(u);
		setConnection(c);
	}

	public UserTreeItem(TreeItem t, int none, IrcUser s, Connection c, Channel chan) {
		TreeItem myItem = new TreeItem(t, none);
		myItem.setData(true);
		setTree(myItem);
		setMyUser(s);
		setConnection(c);
		setChan(chan.getChannelName());
	}

	public void setMyUser(IrcUser myUser) {
		this.myUser = myUser;
	}

	public IrcUser getMyUser() {
		return myUser;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setTree(TreeItem tree) {
		this.tree = tree;
	}

	public TreeItem getTree() {
		return tree;
	}

	
	public void setChan(String chan) {
		this.chan = chan;
	}

	public String getChan() {
		return chan;
	}

}
