/*
 * @author Kyle Kemp
 */
package shared;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import connection.Connection;
import connection.KEllyBot;

import ui.composites.MainWindow;

/**
 * The Class Initializer.
 */
public class Initializer {

	/** The DEBU g_ mode. */
	public static boolean DEBUG_MODE = true;
	
	/**
	 * Launch the application.
	 *
	 * @param args the arguments
	 */
	public static void main(String args[]) {
		checkVersion();
		//TODO: load basic UI separate from connection so the UI shows right away
		//TODO: first time setup stuff if properties file not found (download scripts from repository, and basic_settings file that hasn't  been made yet)
		//the basic settings file should contain basic quicklinks (google, others), and other presets that are deemed important

		Display.setAppName("kEllyIRC");
		final Display disp = Display.getDefault();
		MainWindow window = null;
		
		try {
			HTMLLayout h = new HTMLLayout();
			h.setTitle(KEllyBot.VERSION+ " Error Log");
			h.setLocationInfo(true);
			BasicConfigurator.configure(new FileAppender(h, "error_log.html",true));
			
			window = new MainWindow(disp);
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
			RoomManager.colorset.cleanUp();
		} catch (Exception e) {
			org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.init");
			fLog.error("Initialization failed.", e);
		}
	}

	/**
	 * Check version.
	 */
	private static void checkVersion() {
		String s = System.getProperty("java.version");
		int version = Integer.parseInt(s.split("[.]")[1]);
		if(version < 7){
			Logger vLog = Logger.getLogger("log.version");
			vLog.fatal("JRE version out of date. Requires upgrade to fully use kEllyIRC.");
			AlertBox.alert("JRE Warning", "You are not currently using JRE 1.7.0 or higher, kEllyIRC may not function properly for you. Please update your Java installation.", SWT.ICON_WARNING, SWT.OK);
		}
		
	}

	// called when the program exits
	/**
	 * Quit.
	 */
	public static void quit() {
		RoomManager.getMain().getDisplay().getSystemTray().dispose();

		for(Connection.ConnectionData c : Connection.connections) {
			c.getBot().quitServer();
		}
		
		System.exit(0);
	}

}
