/*
 * @author Kyle Kemp
 */
package shared;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * The Class Customs.
 */
public class Customs{
	
	/** The fonts. */
	public HashMap<String, Font> fonts;
	
	/** The colors. */
	public HashMap<String, Color> colors;
	
	/** The irc colors str. */
	public HashMap<String, String> ircColorsStr;
	
	/** The irc colors. */
	public HashMap<String, Integer> ircColors;
	
	/**
	 * Instantiates a new customs.
	 */
	public Customs() {
		//initFonts();
		initColors();
	}
	
	/*
	private void initFonts(){
		FontData[] fonts = Display.getDefault().getFontList(null, true);
		for(FontData f : fonts){
			//TODO: set a font size here (in Font constructor)
			this.fonts.put(f.getName(), new Font(null, f));
		}
	}*/
	
	/**
	 * Inits the colors.
	 */
	private void initColors(){
		ircColorsStr = new HashMap<String, String>();
		this.ircColorsStr.put("white", 		"\u000300");
		this.ircColorsStr.put("black", 		"\u000301");
		this.ircColorsStr.put("dark blue", 	"\u000302");
		this.ircColorsStr.put("dark green",	"\u000303");
		this.ircColorsStr.put("red", 		"\u000304");
		this.ircColorsStr.put("brown", 		"\u000305");
		this.ircColorsStr.put("purple", 	"\u000306");
		this.ircColorsStr.put("olive", 		"\u000307");
		this.ircColorsStr.put("yellow", 	"\u000308");
		this.ircColorsStr.put("green", 		"\u000309");
		this.ircColorsStr.put("teal", 		"\u000310");
		this.ircColorsStr.put("cyan", 		"\u000311");
		this.ircColorsStr.put("blue", 		"\u000312");
		this.ircColorsStr.put("magenta", 	"\u000313");
		this.ircColorsStr.put("dark gray", 	"\u000314");
		this.ircColorsStr.put("light gray",	"\u000315");
		
		ircColors = new HashMap<String, Integer>();
		this.ircColors.put("white", 		0);
		this.ircColors.put("black", 		1);
		this.ircColors.put("dark blue", 	2);
		this.ircColors.put("dark green",	3);
		this.ircColors.put("red", 			4);
		this.ircColors.put("brown", 		5);
		this.ircColors.put("purple", 		6);
		this.ircColors.put("olive", 		7);
		this.ircColors.put("yellow", 		8);
		this.ircColors.put("green", 		9);
		this.ircColors.put("teal", 			10);
		this.ircColors.put("cyan", 			11);
		this.ircColors.put("blue", 			12);
		this.ircColors.put("magenta", 		13);
		this.ircColors.put("dark gray", 	14);
		this.ircColors.put("light gray",	15);
		
		colors = new HashMap<String, Color>();
		this.colors.put("white",		Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		this.colors.put("black",		Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		this.colors.put("dark blue",	Display.getDefault().getSystemColor(SWT.COLOR_DARK_BLUE));
		this.colors.put("dark green",	Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN));
		this.colors.put("red",			Display.getDefault().getSystemColor(SWT.COLOR_RED));
		this.colors.put("brown",		new Color(Display.getDefault(),164, 42, 42));
		this.colors.put("purple",		new Color(Display.getDefault(),160, 32, 240));
		this.colors.put("olive",		new Color(Display.getDefault(),107, 142, 35));
		this.colors.put("yellow",		Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
		this.colors.put("green",		Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		this.colors.put("teal",			new Color(Display.getDefault(),135, 206, 250));
		this.colors.put("cyan",			Display.getDefault().getSystemColor(SWT.COLOR_CYAN));
		this.colors.put("blue",			Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		this.colors.put("magenta",		Display.getDefault().getSystemColor(SWT.COLOR_MAGENTA));
		this.colors.put("dark gray",	Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
		this.colors.put("light gray",	Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
	}
	
	/**
	 * Clean up.
	 */
	public void cleanUp() {
		for(Font f : this.fonts.values()){
			f.dispose();
		}
		for(Color f : this.colors.values()){
			f.dispose();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	public void finalize() {
		cleanUp();
	}

}
