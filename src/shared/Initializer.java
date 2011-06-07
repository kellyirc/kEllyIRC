package shared;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import lombok.Cleanup;

import org.eclipse.swt.SWT;
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
		
		checkVersion();
		
		//TODO: load basic UI separate from connection so the UI shows right away
		//TODO: first time setup stuff if properties file not found
		setUpStreams();
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
	
	private static void checkVersion() {
		String s = System.getProperty("java.version");
		int version = Integer.parseInt(s.split("[.]")[1]);
		if(version < 7){
			AlertBox.alert("JRE Warning", "You are not currently using JRE 1.7.0 or higher, kEllyIRC may not function properly for you. Please update your Java installation.", SWT.ICON_WARNING, SWT.OK);
		}
		
	}

	// redirect to a file when we have errors for when we distribute as a jar
	private static void setUpStreams() {
		if(!DEBUG_MODE){
			@Cleanup PrintStream exceptionCatch = null;
			try {
				exceptionCatch = new PrintStream(new FileOutputStream("err.log"));
				System.setErr(exceptionCatch);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
}
