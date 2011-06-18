package shared;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

public class NSAlertBox {

	private MessageBox messagebox;
	
	//this is a hack to make it so MODIFY events on the file system don't cause two boxes to appear
	private static boolean alertVisible=false;
	
	public NSAlertBox(String title, String message, int icon, int type){
		messagebox = new MessageBox(RoomManager.getMain().getShell(), type|icon);

		messagebox.setText(title);
		messagebox.setMessage(message);
	}
	
	public NSAlertBox(final String title, final String message, final int icon){
		if(alertVisible) return;
		alertVisible = true;
		RoomManager.getMain().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				messagebox = new MessageBox(RoomManager.getMain().getShell(), SWT.OK|icon);

				messagebox.setText(title);
				messagebox.setMessage(message);
				
				if(messagebox.open() != 0) {
					alertVisible = false;
				}
				
			}});
	}
	
	public int go(){
		return messagebox.open();
	}
}
