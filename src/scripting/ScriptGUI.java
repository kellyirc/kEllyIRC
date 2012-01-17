package scripting;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Color;

import hexapixel.notifier.NotificationType;
import hexapixel.notifier.NotifierDialog;
import hexapixel.notifier.NotifierSettings;
import shared.AlertBox;
import shared.RoomManager;

public final class ScriptGUI {
	
	//wrapping Alertbox.alert()
	public static int alert(String title, String message, int icon, int type) {
		return AlertBox.alert(title, message, icon, type);
	}
	
	//wrapping NotiferDialog.notify()
	public static void window(String message) {
		NotifierDialog.window("", message, null, NotifierSettings.textColor,
				NotifierSettings.titleColor,
				NotifierSettings.borderColor,
				NotifierSettings.backgroundTopGradient,
				NotifierSettings.backgroundBottomGradient);
	}
	
	public static void window(String message, String title) {
		NotifierDialog.window(message, title, null, NotifierSettings.textColor,
				NotifierSettings.titleColor,
				NotifierSettings.borderColor,
				NotifierSettings.backgroundTopGradient,
				NotifierSettings.backgroundBottomGradient);
	}
	
	public static void window(String message, String title,
			NotificationType type) {
		NotifierDialog.window(message, title, type, NotifierSettings.textColor,
				NotifierSettings.titleColor,
				NotifierSettings.borderColor,
				NotifierSettings.backgroundTopGradient,
				NotifierSettings.backgroundBottomGradient);
	}
	
	public static void window(String message, String title,
			NotificationType type, Color textColor) {
		NotifierDialog.window(message, title, type, textColor,
				NotifierSettings.titleColor,
				NotifierSettings.borderColor,
				NotifierSettings.backgroundTopGradient,
				NotifierSettings.backgroundBottomGradient);
	}
	
	public static void window(String message, String title,
			NotificationType type, Color textColor, Color titleColor) {
		NotifierDialog.window(message, title, type, textColor, titleColor,
				NotifierSettings.borderColor,
				NotifierSettings.backgroundTopGradient,
				NotifierSettings.backgroundBottomGradient);
	}
	
	public static void window(String message, String title,
			NotificationType type, Color textColor, Color titleColor, Color borderColor) {
		NotifierDialog.window(message, title, type, textColor, titleColor, borderColor, 
				NotifierSettings.backgroundTopGradient,
				NotifierSettings.backgroundBottomGradient);
	}
	
	public static void window(String message, String title,
			NotificationType type, Color textColor, Color titleColor, Color borderColor, Color topGrad, Color btmGrad) {
		NotifierDialog.window(message, title, type, textColor, titleColor, borderColor, topGrad, btmGrad);
	}

	//wrapping jface.InputDialog()
	//FIXME: fuck those fucking invalid thread accesses. find a way to make this work out
	public static String input(String title, String desc, String def, IInputValidator validator){
		InputDialog d = new InputDialog(RoomManager.getMain().getDisplay().getActiveShell(), title, desc, def, validator);
		if(d.open() == Window.OK){
			return d.getValue();
		}
		return "";
	}
}
