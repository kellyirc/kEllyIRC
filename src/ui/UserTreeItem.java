package ui;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jibble.pircbot.User;

import connection.Connection;

public class UserTreeItem{

	private User myUser;
	private Connection connection;
	private TreeItem tree;
	
	public UserTreeItem(Tree parent, int style, User u, Connection c) {
		setTree(new TreeItem(parent, style));
		setMyUser(u);
		setConnection(c);
	}

	public UserTreeItem(TreeItem t, int none, User s, Connection c) {
		setTree(new TreeItem(t, none));
		setMyUser(s);
		setConnection(c);
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

}
