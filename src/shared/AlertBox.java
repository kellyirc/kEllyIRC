package shared;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class AlertBox {
	static Display display = new Display();
	static Shell shell = new Shell(display);
	
	public static int alert(String title, String message, int icon, int type) {
		MessageBox messageBox = new MessageBox(shell, type|icon);

		messageBox.setText(title);
		messageBox.setMessage(message);
		
		return messageBox.open();
	}

	//sample usages
	public static void main(String[] args) {
		//SWT.ICON_ERROR, ICON_INFORMATION, ICON_QUESTION, ICON_WARNING, ICON_WORKING
		//OK, OK | CANCEL
		//YES | NO, YES | NO | CANCEL
		//RETRY | CANCEL
		//ABORT | RETRY | IGNORE
		AlertBox.alert("Warning", "Save Changes?", SWT.ABORT | SWT.RETRY, SWT.ICON_WARNING);
	}
}