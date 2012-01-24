/*
 * @author Kyle Kemp
 */
package shared;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;


/**
 * THIS CLASS IS NOT MEANT FOR INTERNAL USE.
 * USE NSAlertBox INSTEAD!
 */
public class AlertBox {
	
	/** The display. */
	private static Display display = new Display();
	
	/** The shell. */
	private static Shell shell = new Shell(display);
	
	/**
	 * Alert.
	 *
	 * @param title the title
	 * @param message the message
	 * @param icon the icon
	 * @param type the type
	 * @return the int
	 */
	public static int alert(String title, String message, int icon, int type) {
		MessageBox messageBox = new MessageBox(shell, type|icon);

		messageBox.setText(title);
		messageBox.setMessage(message);
		
		return messageBox.open();
	}

	//sample usages
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		//SWT.ICON_ERROR, ICON_INFORMATION, ICON_QUESTION, ICON_WARNING, ICON_WORKING
		//OK, OK | CANCEL
		//YES | NO, YES | NO | CANCEL
		//RETRY | CANCEL
		//ABORT | RETRY | IGNORE
		AlertBox.alert("Warning", "Save Changes?", SWT.ABORT | SWT.RETRY, SWT.ICON_WARNING);
	}
}