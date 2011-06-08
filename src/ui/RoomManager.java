package ui;

import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.pircbotx.Channel;

import shared.Customs;
import shared.Message;
import connection.Connection;

public class RoomManager {

	private static MainWindow m;
	
	public static Customs colorset;
	
	public static CopyOnWriteArrayList<Message> pmQueue = new CopyOnWriteArrayList<Message>();
	private static CopyOnWriteArrayList<Message> queue = new CopyOnWriteArrayList<Message>();
	
	public static void enQueue(Message m){
		queue.add(m);
		manageQueue();
	}
	
	//public static CopyOnWriteArrayList<Room> rooms = new CopyOnWriteArrayList<Room>();

	public static void createRoom(final Composite c, final Tree tree, final int style, final String channelstr, final Connection newConnection, final int layout, final Channel channel) {
		if(!m.getDisplay().isDisposed()){
        	if(canAddRoom(newConnection, channelstr)){
        		getMain().getDisplay().asyncExec(new Runnable() {
        			public void run() {
                		Room r = new Room(c, style, layout, tree, channelstr, newConnection, channel);
                		newConnection.addRoom(r);
        			}
        		});
        	}
        }
	}
	
	private static void manageQueue() {
        if(!m.getDisplay().isDisposed()){
            m.getDisplay().asyncExec (new Runnable () {
               public void run () {
            	   for(Message mes : queue) {
            		   if(mes.getBot() == null || mes.getChannel() == null || mes.getContent() == null || mes.getSender() == null) {
            			   queue.remove(mes);
            			   continue;
            		   }
            		   filterMessage(mes);
            	   }
               }
            });
        }
	}
	
	public static void setMain(MainWindow w){
		m = w;
	}
	
	public static MainWindow getMain(){
		return m;
	}
	
	public static boolean canAddRoom(Connection newConnection, String s){
		return newConnection.canAddRoom(s);
	}
	
	public static String stripControlCodes(String original) {
		String allowedChars = "0123456789";
		for(int i = 0; i<original.length()-1; i++) {
			String curchar;
			if(i+1 < original.length()) curchar = original.substring(i,i+1);
			else curchar = original.substring(i);
			
			if(curchar.equals("\u0003")) {
				String numStr = "";
				int commaPos = -1;
				
				for(int j = i+1; j<original.length()-1; j++) {
					if(j+1 < original.length()) curchar = original.substring(j,j+1);
					else curchar = original.substring(j);
					
					if(numStr.length() > 0 && curchar.equals(",")) commaPos = j;
					
					if(!allowedChars.contains(curchar)) break;
					
					numStr += curchar;
				}
				
				if(commaPos != -1) {
					numStr += ",";
					for(int j = commaPos+1; j<original.length()-1; j++) {
						if(j+1 < original.length()) curchar = original.substring(j,j+1);
						else curchar = original.substring(j);
						
						if(!allowedChars.contains(curchar)) break;
						
						numStr += curchar;
					}
				}
				System.out.println("BEGONE " + numStr);
				original = original.replaceFirst("\u0003" + numStr, "");
			}
			//TODO: Add bold, underline and italics
			original = original.replaceAll("\u0002","");
		}
		
		return original;
	}
	
	public static void filterMessage(Message m){
		Room r = m.getBot().getConnection().findRoom(m.getChannel());
		if(r == null) return;
		
		String strippedLine = stripControlCodes(m.getContent());
		
		if(r.getOutput()!=null){
			r.getOutput().append("<"+m.getSender() + "> "+strippedLine);
			r.getOutput().setSelection(r.getOutput().getText().length()); //scroll the output control down
		}
		
		//START OF KR-CODE
		
		Customs c = new Customs(); //Create a Customs object, used for color-checking
		String allowedChars = "0123456789"; 	//Create a string, used to check for valid colors after
												//an IRC color control code
		int totalDispos = 0;
		boolean applied[] = {false,false	,false	,false};
							//Bold,Underline,Reverse,Italic
		
		for(int i = 0; i<m.getContent().length()-1; i++) {
			String curchar;
			if(i+1 < m.getContent().length()) curchar = m.getContent().substring(i,i+1);
			else curchar = m.getContent().substring(i);
			
			if(curchar.equals("\u0003")) { //Color control code
				String numStr = "";
				String bgNumStr = "";
				int commaPos = -1;
				
				for(int j = i+1; j<m.getContent().length()-1; j++) {
					if(j+1 < m.getContent().length()) curchar = m.getContent().substring(j,j+1);
					else curchar = m.getContent().substring(j);
					
					if(numStr.length() > 0 && curchar.equals(",")) commaPos = j;
					
					if(!allowedChars.contains(curchar)) break;
					
					numStr += curchar;
				}
				
				if(commaPos != -1) {
					for(int j = commaPos+1; j<m.getContent().length()-1; j++) {
						if(j+1 < m.getContent().length()) curchar = m.getContent().substring(j,j+1);
						else curchar = m.getContent().substring(j);
						
						if(!allowedChars.contains(curchar)) break;
						
						bgNumStr += curchar;
					}
				}
				
				if(numStr.length() <= 0) totalDispos -= numStr.length()-1;
				
				Color foregroundCol = null;
				int fgColNum = -1;
				Color backgroundCol = null;
				int bgColNum = -1;
				
				if(numStr.length() > 0) fgColNum = Integer.parseInt(numStr);
				else continue;
				
				if(bgNumStr.length() > 0) bgColNum = Integer.parseInt(bgNumStr);
				
				for(String key : c.ircColors.keySet()) {
					if(c.ircColors.get(key).equals(fgColNum)) {
						foregroundCol = c.colors.get(key);
					}
					if(c.ircColors.get(key).equals(bgColNum)) {
						backgroundCol = c.colors.get(key);
					}
				}
				
				int nextColCode = m.getContent().indexOf("\u0003",i+1);
				
				int totalNumStrLens = numStr.length();
				if(commaPos != -1) totalNumStrLens += bgNumStr.length() + 1;
				
				StyleRange styleRange = new StyleRange();
				styleRange.start = r.getOutput().getCharCount() - strippedLine.length() + i - totalDispos;
				styleRange.length = nextColCode - i - totalNumStrLens - 1;
				if(styleRange.length < 0) styleRange.length = strippedLine.length() - i + totalDispos;
				styleRange.foreground = foregroundCol;
				styleRange.background = backgroundCol;
				r.getOutput().setStyleRange(styleRange);
				totalDispos += totalNumStrLens + 1;
			}
			//TODO: Add bold, underline and italics
			if(curchar.equals("\u0002")) { //Bold control code
				if(!applied[0]) {
					int nextBoldCode = m.getContent().indexOf("\u0002",i+1);
					int nextNormalCode = m.getContent().indexOf("\u000F",i+1);
					
					int nextCtrlCode = Math.min(nextBoldCode,nextNormalCode);
					
					if(nextBoldCode == -1) nextCtrlCode = nextNormalCode;
					if(nextNormalCode == -1) nextCtrlCode = nextBoldCode;
					
					StyleRange styleRange = new StyleRange();
					styleRange.start = r.getOutput().getCharCount() - strippedLine.length() + i - totalDispos;
					styleRange.length = nextCtrlCode - i - 1;
					if(styleRange.length < 0) styleRange.length = strippedLine.length() - i + totalDispos;
					styleRange.fontStyle = SWT.BOLD;
					r.getOutput().setStyleRange(styleRange);
				}
				applied[0] = !applied[0];
			}
		}
		
		//END OF KR-CODE
		
		for(String s : m.getContent().split(" ")){
			if(s.contains("://")){
				//TODO: make this conform to the global list, and make the global list work
				Color blue = new Color(r.getOutput().getDisplay(),0,0,255);
				StyleRange styleRange = new StyleRange();
				styleRange.start = r.getOutput().getCharCount() - m.getContent().length() + m.getContent().indexOf(s)-1;
				styleRange.length = s.length();
				styleRange.foreground = blue;
				styleRange.data = s;
				styleRange.underline=true;
				styleRange.underlineStyle = SWT.UNDERLINE_LINK;
				r.getOutput().setStyleRange(styleRange);
			}
		}
		queue.remove(m);
	}
	
}
