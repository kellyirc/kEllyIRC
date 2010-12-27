package shared;

import org.eclipse.swt.widgets.Display;

import ui.MainWindow;

public class Initializer {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		GlobalSettings g = new GlobalSettings();
		try {
			MainWindow window = new MainWindow(g);
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
