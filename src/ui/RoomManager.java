package ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.jibble.pircbot.User;

import shared.Message;

public class RoomManager {

	private static MainWindow m;
	
	public static CopyOnWriteArrayList<Message> pmQueue = new CopyOnWriteArrayList<Message>();
	public static CopyOnWriteArrayList<Message> queue = new CopyOnWriteArrayList<Message>();
	
	public static CopyOnWriteArrayList<Room> rooms = new CopyOnWriteArrayList<Room>();

	public static Room createRoom(Composite c, int style) {
		if(c == null){return null;}
		Room r = new Room(c, style);
		if(canAddRoom(r)){
			rooms.add(r);
		}
		return r;
	}
	
	public static void manageQueue() {
        if(!m.getDisplay().isDisposed()){
            m.getDisplay().asyncExec (new Runnable () {
               public void run () {
            	   for(Message mes : queue) {
            		   filterMessage(mes);
            		   queue.remove(mes);
            	   }
               }
            });
        }
	}
	
	public static void changeTopic(final Room c, final String t) {
		if(c == null){return;}
        if(!m.getDisplay().isDisposed()){
            m.getDisplay().asyncExec (new Runnable () {
               public void run () {
            	   c.setTopic(t);
               }
            });
        }
	}
	
	public static void updateWho(final Room c) {
		if(c == null){return;}
        if(!m.getDisplay().isDisposed()){
            m.getDisplay().asyncExec (new Runnable () {
               public void run () {
            	   
            	   c.getWho().removeAll();
            	   //TODO: update to pircbotx -- the lattermost two are not supported via pircbot
            	   
            	   //					 none , voice, ops  , hops , owner
            	   boolean[] contains = {false, false, false, false, false};
            	   ArrayList<User> users = new ArrayList<User>();
            	   
            	   for(User u : c.getServerConnection().getUsers(c.getChannel().getChannelName())){
            		   if(u.isOp()) 			{ contains[2] = true;}
            		   else if(u.hasVoice()) 	{ contains[1] = true;}
            		   else 					{ contains[0] = true;}
            		   users.add(u);
            	   }
            	   
            	   Collections.sort(users, new Comparator<User>() {
            		   public int compare(User arg0, User arg1) {
            			   return arg0.toString().compareTo(arg1.toString());
            		   }
					});
            	   
            	   CopyOnWriteArrayList<User> moarUsers = new CopyOnWriteArrayList<User>();
            	   moarUsers.addAll(users);
            	   
            	   if(contains[2]){
                	   TreeItem t = new TreeItem(c.getWho(), SWT.NONE);
                	   t.setText("Ops");
                	   for(User s : moarUsers){
                		   if(s.isOp()){
                			   UserTreeItem i = new UserTreeItem(t, SWT.NONE, s, c.getServerConnection());
                			   i.getTree().setText(s.toString());
                			   moarUsers.remove(s);
                		   }
                	   }
                	   t.setExpanded(true);
            	   }
            	   if(contains[1]){
                	   TreeItem t = new TreeItem(c.getWho(), SWT.NONE);
                	   t.setText("Voice");
                	   for(User s : moarUsers){
                		   if(s.hasVoice()){
                			   UserTreeItem i = new UserTreeItem(t, SWT.NONE, s, c.getServerConnection());
                			   i.getTree().setText(s.toString());
                			   moarUsers.remove(s);
                		   }
                	   }
                	   t.setExpanded(true);
            	   }
            	   if(contains[0]){
                	   TreeItem t = new TreeItem(c.getWho(), SWT.NONE);
                	   t.setText("Normal");
                	   for(User s : moarUsers){
            			   UserTreeItem i = new UserTreeItem(t, SWT.NONE, s, c.getServerConnection());
            			   i.getTree().setText(s.toString());
                	   }
                	   t.setExpanded(true);
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
	
	public static boolean canAddRoom(Room r){
		for(Room n : getRooms()){
			if(n.getServerConnection().equals(r.getServerConnection()) && n.getChannel().equals(r.getChannel())){
				return false;
			}
		}
		return true;
	}
	
	public static void filterMessage(Message m){
		for(Room r : rooms){
			if(m.getConnection().equals(r.getServerConnection()) && r.getChannel().getChannelName().equals(m.getChannel())){
				r.getOutput().append(m.getSender() + ": "+m.getContent());
			}
		}
	}

	public static CopyOnWriteArrayList<Room> getRooms() {
		return rooms;
	}
	
}
