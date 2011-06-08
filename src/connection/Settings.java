package connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

import com.thoughtworks.xstream.XStream;

public class Settings {
	
	//stuff to read and write files with
	static String filename = "settings.xml";
	static XStream xstream = new XStream();
	
	//the sole Settings instance
	static @Getter Settings settings = Settings.readFromFile();
	
	//This keeps the list of servers the user has saved.
	@Getter @Setter ArrayList<ConnectionSettings> connSettings;
	
	//Other variables/objects to hold settings/preferences go here
	
	//creates default settings
	public Settings()
	{
		connSettings = new ArrayList<ConnectionSettings>();
	}
	
	public static void writeToFile()
	{
		try {
			xstream.toXML(settings,new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	//if a settings file exists, returns that
	//else returns default settings
	public static Settings readFromFile() 
	{
		File f = new File(filename);
		if(f.exists())
		{
			try {
				return (Settings)xstream.fromXML(new FileInputStream(filename));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return new Settings();
	}

}
