/*
 * @author Kyle Kemp
 */
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

/**
 * The Class ScriptGUI.
 */
public final class ScriptGUI {

	// wrapping Alertbox.alert()
	/**
	 * Alert.
	 *
	 * @param title the title
	 * @param message the message
	 * @param icon the icon
	 * @param type the type
	 * @return the int
	 */
	public static int alert(String title, String message, int icon, int type) {
		return AlertBox.alert(title, message, icon, type);
	}

	// wrapping NotiferDialog.notify()
	/**
	 * Window.
	 *
	 * @param message the message
	 */
	public static void window(String message) {
		NotifierDialog.window("", message, null, NotifierSettings.textColor,
				NotifierSettings.titleColor, NotifierSettings.borderColor,
				NotifierSettings.backgroundTopGradient,
				NotifierSettings.backgroundBottomGradient);
	}

	/**
	 * Window.
	 *
	 * @param message the message
	 * @param title the title
	 */
	public static void window(String message, String title) {
		NotifierDialog.window(message, title, null, NotifierSettings.textColor,
				NotifierSettings.titleColor, NotifierSettings.borderColor,
				NotifierSettings.backgroundTopGradient,
				NotifierSettings.backgroundBottomGradient);
	}

	/**
	 * Window.
	 *
	 * @param message the message
	 * @param title the title
	 * @param type the type
	 */
	public static void window(String message, String title,
			NotificationType type) {
		NotifierDialog.window(message, title, type, NotifierSettings.textColor,
				NotifierSettings.titleColor, NotifierSettings.borderColor,
				NotifierSettings.backgroundTopGradient,
				NotifierSettings.backgroundBottomGradient);
	}

	/**
	 * Window.
	 *
	 * @param message the message
	 * @param title the title
	 * @param type the type
	 * @param textColor the text color
	 */
	public static void window(String message, String title,
			NotificationType type, Color textColor) {
		NotifierDialog.window(message, title, type, textColor,
				NotifierSettings.titleColor, NotifierSettings.borderColor,
				NotifierSettings.backgroundTopGradient,
				NotifierSettings.backgroundBottomGradient);
	}

	/**
	 * Window.
	 *
	 * @param message the message
	 * @param title the title
	 * @param type the type
	 * @param textColor the text color
	 * @param titleColor the title color
	 */
	public static void window(String message, String title,
			NotificationType type, Color textColor, Color titleColor) {
		NotifierDialog.window(message, title, type, textColor, titleColor,
				NotifierSettings.borderColor,
				NotifierSettings.backgroundTopGradient,
				NotifierSettings.backgroundBottomGradient);
	}

	/**
	 * Window.
	 *
	 * @param message the message
	 * @param title the title
	 * @param type the type
	 * @param textColor the text color
	 * @param titleColor the title color
	 * @param borderColor the border color
	 */
	public static void window(String message, String title,
			NotificationType type, Color textColor, Color titleColor,
			Color borderColor) {
		NotifierDialog.window(message, title, type, textColor, titleColor,
				borderColor, NotifierSettings.backgroundTopGradient,
				NotifierSettings.backgroundBottomGradient);
	}

	/**
	 * Window.
	 *
	 * @param message the message
	 * @param title the title
	 * @param type the type
	 * @param textColor the text color
	 * @param titleColor the title color
	 * @param borderColor the border color
	 * @param topGrad the top grad
	 * @param btmGrad the btm grad
	 */
	public static void window(String message, String title,
			NotificationType type, Color textColor, Color titleColor,
			Color borderColor, Color topGrad, Color btmGrad) {
		NotifierDialog.window(message, title, type, textColor, titleColor,
				borderColor, topGrad, btmGrad);
	}

	// wrapping jface.InputDialog()
	/**
	 * Input.
	 *
	 * @param title the title
	 * @param desc the desc
	 * @param def the def
	 * @param validator the validator
	 * @return the string
	 */
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

	/**
	 * The Class InputDialogThread.
	 */
	public static class InputDialogThread extends Thread {

		/** The return value. */
		String returnValue = "";
		
		/** The def. */
		String title, desc, def;
		
		/** The validator. */
		IInputValidator validator;
		
		/** The latch. */
		CountDownLatch latch;

		/**
		 * Instantiates a new input dialog thread.
		 *
		 * @param latch the latch
		 * @param title the title
		 * @param desc the desc
		 * @param def the def
		 * @param validator the validator
		 */
		public InputDialogThread(CountDownLatch latch, String title,
				String desc, String def, IInputValidator validator) {
			this.latch = latch;
			this.title = title;
			this.desc = desc;
			this.def = def;
			this.validator = validator;
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			InputDialog d = new InputDialog(RoomManager.getMain().getDisplay()
					.getActiveShell(), title, desc, def, validator);
			if (d.open() == Window.OK) {
				returnValue = d.getValue();
			}
			latch.countDown();
		}

		/**
		 * Gets the return value.
		 *
		 * @return the return value
		 */
		public String getReturnValue() {
			return returnValue;
		}

	}
}
