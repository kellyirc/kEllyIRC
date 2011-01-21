package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.thoughtworks.xstream.XStream;

import connection.ConnectionSettings;

public class ConnectionSettingsUtil 
{
	static String filename = "connections.xml";
	static XStream xstream = new XStream();
	public static void writeToFile(ConnectionSettings[] settings)
	{
		try
		{
			
			String xml = "";
			for(ConnectionSettings cs:settings)
			{
				xml += xstream.toXML(cs)+"\n";
				
			}
			
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			out.write(xml);
			out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static ConnectionSettings[] readFromFile()  //returns null if there are no settings
	{
		ArrayList<String> xml = new ArrayList<String>();
		String temp = "";
		try {
			Scanner read = new Scanner(new File(filename));
			while(read.hasNextLine())
			{
				String nextLine = read.nextLine();
				temp += nextLine;
				if(nextLine.equals("</connection.ConnectionSettings>"))
				{
					xml.add(temp);
					temp = "";
				}
			}
		} catch (FileNotFoundException e) { //If it doesn't exist
			File f = new File(filename); 
			try {
				f.createNewFile();  //Create an empty file there
			} catch (IOException e1) {
				System.out.println("Error creating empty connections file");
			}
		}
		
		if(xml.equals("")) //No settings?
			return null;  //return null
		else  //create the ConnectionSettings
		{
			ConnectionSettings[] settings = new ConnectionSettings[xml.size()];
			for(int k=0;k<settings.length;k++)				
			{
				settings[k] = (ConnectionSettings)xstream.fromXML(xml.get(k));
			}
			return settings;
		}
	}
	
	
	
}
