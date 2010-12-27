package shared;

import org.eclipse.swt.widgets.Display;

import ui.MainWindow;

public class Initializer {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		//first time setup stuff if properties file not found
		GlobalSettings g = new GlobalSettings();
		final Display disp = Display.getDefault();
		try {
			MainWindow window = new MainWindow(disp, g);
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
