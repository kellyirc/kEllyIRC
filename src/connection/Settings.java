package connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.eclipse.swt.SWT;

import shared.NSAlertBox;

import lombok.Getter;
import lombok.Setter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Settings {
	
	//stuff to read and write files with
	static String filename = "settings.xml";
	static XStream xstream = new XStream(new Sun14ReflectionProvider( ), new DomDriver( ));
	
	//the sole Settings instance
	@Getter 
	static Settings settings = Settings.readFromFile();
	
	//This keeps the list of servers the user has saved.
	@Getter 
	@Setter 
	ArrayList<ConnectionSettings> connSettings;
	
	//Other variables/objects to hold settings/preferences go here 
	@Getter
	@Setter
	ArrayList<String> nicksIgnored;
	
	
	//creates default settings
	public Settings()
	{
		connSettings = new ArrayList<ConnectionSettings>();
		nicksIgnored = new ArrayList<String>();
	}
	
	public static void writeToFile()
	{
		try {
			xstream.toXML(settings,new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.settings");
			fLog.error("Unable to save settings.", e);
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
			} catch (Exception e) {
				org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.settings");
				fLog.error("Unable to load settings, resetting to default.", e);
				NSAlertBox alert = new NSAlertBox("Unable to load settings","The file settings.xml was corrupted or was not formatted correctly. Settings have reverted to their defaults.",SWT.OK,SWT.ICON_WARNING);
				alert.go();
				Settings defaultSettings = new Settings();
				settings = defaultSettings;
				writeToFile();
				return defaultSettings;
			}
		}
		return new Settings();
	}

}
