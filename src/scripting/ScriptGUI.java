package scripting;

import java.util.concurrent.CountDownLatch;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import hexapixel.notifier.NotificationType;
import hexapixel.notifier.NotifierDialog;
import hexapixel.notifier.NotifierSettings;
import shared.AlertBox;
import shared.RoomManager;

public final class ScriptGUI {

	// wrapping Alertbox.alert()
	public static int alert(String title, String message, int icon, int type) {
		return AlertBox.alert(title, message, icon, type);
	}

	// wrapping NotiferDialog.notify()
	public static void window(String message) {
		NotifierDialog.window("", message, null, NotifierSettings.textColor,
				NotifierSettings.titleColor, NotifierSettings.borderColor,
				NotifierSettings.backgroundTopGradient,
				NotifierSettings.backgroundBottomGradient);
	}

	public static void window(String message, String title) {
		NotifierDialog.window(message, title, null, NotifierSettings.textColor,
				NotifierSettings.titleColor, NotifierSettings.borderColor,
				NotifierSettings.backgroundTopGradient,
				NotifierSettings.backgroundBottomGradient);
	}

	public static void window(String message, String title,
			NotificationType type) {
		NotifierDialog.window(message, title, type, NotifierSettings.textColor,
				NotifierSettings.titleColor, NotifierSettings.borderColor,
				NotifierSettings.backgroundTopGradient,
				NotifierSettings.backgroundBottomGradient);
	}

	public static void window(String message, String title,
			NotificationType type, Color textColor) {
		NotifierDialog.window(message, title, type, textColor,
				NotifierSettings.titleColor, NotifierSettings.borderColor,
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
			NotificationType type, Color textColor, Color titleColor,
			Color borderColor) {
		NotifierDialog.window(message, title, type, textColor, titleColor,
				borderColor, NotifierSettings.backgroundTopGradient,
				NotifierSettings.backgroundBottomGradient);
	}

	public static void window(String message, String title,
			NotificationType type, Color textColor, Color titleColor,
			Color borderColor, Color topGrad, Color btmGrad) {
		NotifierDialog.window(message, title, type, textColor, titleColor,
				borderColor, topGrad, btmGrad);
	}

	// wrapping jface.InputDialog()
	public static String input(final String title, final String desc,
			final String def, final IInputValidator validator) {
		CountDownLatch latch = new CountDownLatch(1);

		InputDialogThread t = new InputDialogThread(latch, title, desc, def,
				validator);
		Display.getCurrent().asyncExec(t);

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return t.getReturnValue();
	}

	public static class InputDialogThread extends Thread {

		String returnValue = "";
		String title, desc, def;
		IInputValidator validator;
		CountDownLatch latch;

		public InputDialogThread(CountDownLatch latch, String title,
				String desc, String def, IInputValidator validator) {
			this.latch = latch;
			this.title = title;
			this.desc = desc;
			this.def = def;
			this.validator = validator;
		}

		public void run() {
			InputDialog d = new InputDialog(RoomManager.getMain().getDisplay()
					.getActiveShell(), title, desc, def, validator);
			if (d.open() == Window.OK) {
				returnValue = d.getValue();
			}
			latch.countDown();
		}

		public String getReturnValue() {
			return returnValue;
		}

	}
}
