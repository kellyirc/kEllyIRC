package shared;

import org.eclipse.swt.widgets.MessageBox;

public class NSAlertBox {

	private MessageBox messagebox;
	
	public NSAlertBox(String title, String message, int icon, int type){
		messagebox = new MessageBox(RoomManager.getMain().getShell(), type|icon);

		messagebox.setText(title);
		messagebox.setMessage(message);
	}
	
	public int go(){
		return messagebox.open();
	}
}
