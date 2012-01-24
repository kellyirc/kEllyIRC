package shared;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

public class NSAlertBox {

	private MessageBox messagebox;
	/*FIXME: You get NullPointerExceptions when using this,
	 * but it still works. Still annoying so fix it.
	 * Here's an example error
	 * java.lang.NullPointerException
	at shared.NSAlertBox.go(NSAlertBox.java:40)
	at ui.composites.TextBoxComposite$1.widgetSelected(TextBoxComposite.java:67)
	at org.eclipse.swt.widgets.TypedListener.handleEvent(TypedListener.java:240)
	at org.eclipse.swt.widgets.EventTable.sendEvent(EventTable.java:84)
	at org.eclipse.swt.widgets.Widget.sendEvent(Widget.java:1053)
	at org.eclipse.swt.widgets.Display.runDeferredEvents(Display.java:4165)
	at org.eclipse.swt.widgets.Display.readAndDispatch(Display.java:3754)
	at org.eclipse.jface.window.Window.runEventLoop(Window.java:825)
	at org.eclipse.jface.window.Window.open(Window.java:801)
	at shared.Initializer.main(Initializer.java:41)
	 */
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
