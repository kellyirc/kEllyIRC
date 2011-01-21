package shared;

//DELETE THIS FILE AFTER INTEGRATING THE CONNECTION 
//MANAGER INTO THE APPLICATION ITSELF
import org.eclipse.swt.widgets.Display;

import ui.ConnectionWindow;

public class ConnectionManagerInitializer {

	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			ConnectionWindow shell = new ConnectionWindow(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
