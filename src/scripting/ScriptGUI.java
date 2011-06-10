package scripting;

import shared.AlertBox;

public class ScriptGUI {
	
	//wrapping the thing. no biggie.
	public static int alert(String title, String message, int icon, int type) {
		return AlertBox.alert(title, message, icon, type);
	}
	
}
