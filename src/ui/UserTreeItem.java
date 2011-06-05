package ui;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.pircbotx.Channel;
import org.pircbotx.User;

import connection.Connection;

public class UserTreeItem{

	private User myUser;
	private Connection connection;
	private TreeItem tree;
	private CustomChannel chan;
	
	public UserTreeItem(Tree parent, int style, User u, Connection c) {
		TreeItem myItem = new TreeItem(parent, style);
		myItem.setData(false);
		setTree(myItem);
		setMyUser(u);
		setConnection(c);
	}

	public UserTreeItem(TreeItem t, int none, User s, Connection c, CustomChannel chan) {
		TreeItem myItem = new TreeItem(t, none);
		myItem.setData(true);
		setTree(myItem);
		setMyUser(s);
		setConnection(c);
		setChan(chan);
	}

	public void setMyUser(User myUser) {
		this.myUser = myUser;
	}

	public User getMyUser() {
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

	
	public void setChan(CustomChannel chan) {
		this.chan = chan;
	}

	public CustomChannel getChan() {
		return chan;
	}

}
