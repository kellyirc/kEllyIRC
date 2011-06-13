package scripting;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import hexapixel.notifier.NotificationType;
import hexapixel.notifier.NotifierDialog;
import hexapixel.notifier.NotifierSettings;
import shared.AlertBox;

public class ScriptGUI {
	
	//wrapping Alertbox.alert()
	public static int alert(String title, String message, int icon, int type) {
		return AlertBox.alert(title, message, icon, type);
	}
	
	//wrapping NotiferDialog.notify()
	public static void window(String title, String message) {
		NotifierDialog.window(title, message, null, NotifierSettings.getTextColor(),
				NotifierSettings.getTitleColor(),
				NotifierSettings.getBorderColor(),
				NotifierSettings.getBackgroundTopGradient(),
				NotifierSettings.getBackgroundBottomGradient());
	}
	
	public static void window(String title, String message,
			NotificationType type) {
		NotifierDialog.window(title, message, type, NotifierSettings.getTextColor(),
				NotifierSettings.getTitleColor(),
				NotifierSettings.getBorderColor(),
				NotifierSettings.getBackgroundTopGradient(),
				NotifierSettings.getBackgroundBottomGradient());
	}
	
	public static void window(String title, String message,
			NotificationType type, Color textColor) {
		NotifierDialog.window(title, message, type, textColor,
				NotifierSettings.getTitleColor(),
				NotifierSettings.getBorderColor(),
				NotifierSettings.getBackgroundTopGradient(),
				NotifierSettings.getBackgroundBottomGradient());
	}
	
	public static void window(String title, String message,
			NotificationType type, Color textColor, Color titleColor) {
		NotifierDialog.window(title, message, type, textColor, titleColor,
				NotifierSettings.getBorderColor(),
				NotifierSettings.getBackgroundTopGradient(),
				NotifierSettings.getBackgroundBottomGradient());
	}
	
	public static void window(String title, String message,
			NotificationType type, Color textColor, Color titleColor, Color borderColor) {
		NotifierDialog.window(title, message, type, textColor, titleColor, borderColor, 
				NotifierSettings.getBackgroundTopGradient(),
				NotifierSettings.getBackgroundBottomGradient());
	}
	
	public static void window(String title, String message,
			NotificationType type, Color textColor, Color titleColor, Color borderColor, Color topGrad, Color btmGrad) {
		NotifierDialog.window(title, message, type, textColor, titleColor, borderColor, topGrad, btmGrad);
	}

	//wrapping jface.InputDialog()
	//TODO: fuck those fucking invalid thread accesses. this and another function need to work and return values but can't because the thread is being accessed incorrectly :(
	public static String input(String title, String desc, String def, IInputValidator validator){
		InputDialog d = new InputDialog(Display.getDefault().getActiveShell(), title, desc, def, validator);
		if(d.open() == Window.OK){
			return d.getValue();
		}
		return "";
	}
}
