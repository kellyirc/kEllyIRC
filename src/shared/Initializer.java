package shared;

import org.eclipse.swt.widgets.Display;

import ui.MainWindow;

public class Initializer {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		
		//TODO: first time setup stuff if properties file not found
		
		final Display disp = Display.getDefault();
		MainWindow window = null;
		try {
			window = new MainWindow(disp);
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
