/*
 * @author Kyle Kemp
 */
package ui.room;

import lombok.Data;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.pircbotx.User;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data
public class UserTreeItem {

	/** The user represented by this object. */
	private User myUser;
	
	/** The tree. */
	private TreeItem tree;
	
	/** The chan. */
	private CustomChannel chan;
	
	//born to a tree - a "header" item
	/**
	 * Instantiates a new user tree item.
	 *
	 * @param parent the parent
	 * @param style the style
	 * @param u the u
	 */
	public UserTreeItem(Tree parent, int style, User u) {
		TreeItem myItem = new TreeItem(parent, style);
		myItem.setData(false);
		setTree(myItem);
		setMyUser(u);
	}

	//sub-header items
	/**
	 * Instantiates a new user tree item.
	 *
	 * @param t the t
	 * @param none the none
	 * @param s the s
	 * @param chan the chan
	 */
	public UserTreeItem(TreeItem t, int none, User s, CustomChannel chan) {
		TreeItem myItem = new TreeItem(t, none);
		myItem.setData(s);
		setTree(myItem);
		setMyUser(s);
		setChan(chan);
	}

}
