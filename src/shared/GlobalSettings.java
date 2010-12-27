package shared;

import java.util.concurrent.CopyOnWriteArrayList;

import ui.MainWindow;

public class GlobalSettings {

	private MainWindow main;
	
	public CopyOnWriteArrayList<Message> pmQueue = new CopyOnWriteArrayList<Message>();
	public CopyOnWriteArrayList<Message> queue = new CopyOnWriteArrayList<Message>();

	public void manageQueue(final MainWindow m) {
        if(!m.getDisplay().isDisposed()){
            m.getDisplay().asyncExec (new Runnable () {
               public void run () {
            	   for(Message mes : queue) {
            		   m.getTO().append(mes.getContent());
            		   queue.remove(mes);
            	   }
               }
            });
        }
	}

	public void setMain(MainWindow main) {
		this.main = main;
	}

	public MainWindow getMain() {
		return main;
	}
}
