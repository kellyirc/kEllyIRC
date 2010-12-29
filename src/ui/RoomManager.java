package ui;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.swt.widgets.Composite;

import shared.Message;

public class RoomManager {

	private static MainWindow m;
	
	public static CopyOnWriteArrayList<Message> pmQueue = new CopyOnWriteArrayList<Message>();
	public static CopyOnWriteArrayList<Message> queue = new CopyOnWriteArrayList<Message>();
	
	private static ArrayList<Room> rooms = new ArrayList<Room>();

	public static Room createRoom(Composite c, int style) {
		Room r = new Room(c, style);
		rooms.add(r);
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
	
	public static void setMain(MainWindow w){
		m = w;
	}
	
	public static void addRoom(Room r){
		rooms.add(r);
	}
	
	public static void filterMessage(Message m){
		for(Room r : rooms){
			if(m.getConnection().equals(r.getServerConnection()) && r.getChannel().getChannelName().equals(m.getChannel())){
				r.getOutput().append(m.getSender() + ": "+m.getContent());
			}
		}
	}

	public ArrayList<Room> getRooms() {
		return rooms;
	}
	
	
}
