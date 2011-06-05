package connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;

public class Settings {
	
	//stuff to read and write files with
	static String filename = "settings.xml";
	static XStream xstream = new XStream();
	
	//This keeps the list of servers the user has saved.
	ArrayList<ConnectionSettings> connSettings;
	
	//Other variables/objects to hold settings/preferences go here
	
	public Settings()
	{
		connSettings = new ArrayList<ConnectionSettings>();
		connSettings.add(new ConnectionSettings("EsperNet","irc.esper.net","6666","pass",true,"FireFreek","FireFreeek","pass"));
	}
	
	public static void writeToFile(Settings s)
	{
		try {
			xstream.toXML(s,new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Settings readFromFile()
	{
		try {
			return (Settings)xstream.fromXML(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String toString()
	{
		return connSettings.toString();
	}
}
