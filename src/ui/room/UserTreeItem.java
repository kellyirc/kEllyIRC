package ui.room;

import lombok.Data;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.pircbotx.User;

@Data
public class UserTreeItem {

	private User myUser;
	private TreeItem tree;
	private CustomChannel chan;
	
	//born to a tree - a "header" item
	public UserTreeItem(Tree parent, int style, User u) {
		TreeItem myItem = new TreeItem(parent, style);
		myItem.setData(false);
		setTree(myItem);
		setMyUser(u);
	}

	//sub-header items
	public UserTreeItem(TreeItem t, int none, User s, CustomChannel chan) {
		TreeItem myItem = new TreeItem(t, none);
		myItem.setData(s);
		setTree(myItem);
		setMyUser(s);
		setChan(chan);
	}

}
