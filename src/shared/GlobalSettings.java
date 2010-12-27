package shared;

import java.util.ArrayList;
import java.util.HashMap;

public class GlobalSettings {
	// 			   server,         channel,     messages
	public HashMap<String, HashMap<String, ArrayList<Message>>> messages;

	public GlobalSettings() {
		messages = new HashMap<String, HashMap<String, ArrayList<Message>>>();
	}

}
