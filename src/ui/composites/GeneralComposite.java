package ui.composites;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import shared.RoomManager;

import connection.Settings;

public class GeneralComposite extends Composite {

	Button btnMinimizeToSystem;
	Button btnGenerateChatLogs;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public GeneralComposite(Composite parent, int style) {
		super(parent, style);

		btnMinimizeToSystem = new Button(this, SWT.CHECK);
		btnMinimizeToSystem.setBounds(10, 10, 151, 16);
		btnMinimizeToSystem.setText("Minimize to System Tray");
		btnMinimizeToSystem.setSelection(Settings.getSettings()
				.isMinimizeTray());
		if (RoomManager.getMain().getDisplay().getSystemTray() == null) {
			btnMinimizeToSystem.setEnabled(false);
			btnMinimizeToSystem.setSelection(false);
		}
		btnMinimizeToSystem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Settings.getSettings().setMinimizeTray(
						btnMinimizeToSystem.getSelection());
			}
		});

		btnGenerateChatLogs = new Button(this, SWT.CHECK);
		btnGenerateChatLogs.setBounds(10, 32, 151, 16);
		btnGenerateChatLogs.setText("Generate Chat Logs");
		 btnGenerateChatLogs.setSelection(Settings.getSettings().isChatLogs());
		btnGenerateChatLogs.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Settings.getSettings().setChatLogs(
						btnGenerateChatLogs.getSelection());
			}
		});

	}

	@Override
	public void dispose()
	{
		Settings.getSettings().setMinimizeTray(
				btnMinimizeToSystem.getSelection());
		Settings.getSettings().setChatLogs(
				btnGenerateChatLogs.getSelection());
		Settings.writeToFile();
		super.dispose();
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
