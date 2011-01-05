package shared;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

public class Customs{
	
	public HashMap<String, Font> fonts;
	
	public HashMap<String, Color> colors;
	
	public HashMap<String, String> ircColors;
	
	public Customs() {
		initFonts();
		initColors();
	}
	
	private void initFonts(){
		FontData[] fonts = Display.getDefault().getFontList(null, true);
		for(FontData f : fonts){
			//TODO: set a font size here (in Font constructor)
			this.fonts.put(f.getName(), new Font(null, f));
		}
	}
	
	private void initColors(){
		this.ircColors.put("white", 	"\u000300");
		this.ircColors.put("black", 	"\u000301");
		this.ircColors.put("dark blue", "\u000302");
		this.ircColors.put("dark green","\u000303");
		this.ircColors.put("red", 		"\u000304");
		this.ircColors.put("brown", 	"\u000305");
		this.ircColors.put("purple", 	"\u000306");
		this.ircColors.put("olive", 	"\u000307");
		this.ircColors.put("yellow", 	"\u000308");
		this.ircColors.put("green", 	"\u000309");
		this.ircColors.put("teal", 		"\u000310");
		this.ircColors.put("cyan", 		"\u000311");
		this.ircColors.put("blue", 		"\u000312");
		this.ircColors.put("magenta", 	"\u000313");
		this.ircColors.put("dark gray", "\u000314");
		this.ircColors.put("light gray","\u000315");
		
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
	
	public void cleanUp() {
		for(Font f : this.fonts.values()){
			f.dispose();
		}
		for(Color f : this.colors.values()){
			f.dispose();
		}
	}
	
	public void finalize() {
		cleanUp();
	}

}
