package shared;

import org.eclipse.swt.widgets.Display;

import ui.MainWindow;
import ui.RoomManager;

public class Initializer {

	public static boolean DEBUG_MODE = true;
	
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
			RoomManager.colorset.cleanUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void Debug(String s){
		if(DEBUG_MODE){
			System.err.println(s);
		}
	}
}
