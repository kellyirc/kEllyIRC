package ui.room;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.pircbotx.User;


public class UserTreeItem {

	private User myUser;
	private TreeItem tree;
	private CustomChannel chan;
	
	public UserTreeItem(Tree parent, int style, User u) {
		TreeItem myItem = new TreeItem(parent, style);
		myItem.setData(false);
		setTree(myItem);
		setMyUser(u);
	}

	public UserTreeItem(TreeItem t, int none, User s, CustomChannel chan) {
		TreeItem myItem = new TreeItem(t, none);
		myItem.setData(true);
		setTree(myItem);
		setMyUser(s);
		setChan(chan);
	}

	public void setMyUser(User myUser) {
		this.myUser = myUser;
	}

	public User getMyUser() {
		return myUser;
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
