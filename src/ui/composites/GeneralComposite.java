package ui.composites;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import shared.RoomManager;

import connection.Settings;

public class GeneralComposite extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public GeneralComposite(Composite parent, int style) {
		super(parent, style);
		
		
		final Button btnMinimizeToSystem = new Button(this, SWT.CHECK);
		btnMinimizeToSystem.setBounds(0, 0, 151, 16);
		btnMinimizeToSystem.setText("Minimize to System Tray");
		if(RoomManager.getMain().getDisplay().getSystemTray() == null) btnMinimizeToSystem.setEnabled(false);
		btnMinimizeToSystem.setSelection(Settings.getSettings().getMinimizeTray());
		btnMinimizeToSystem.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Settings.getSettings().setMinimizeTray(btnMinimizeToSystem.getSelection());
			}});
		
		final Button btnGenerateChatLogs = new Button(this, SWT.CHECK);
		btnGenerateChatLogs.setBounds(0, 22, 151, 16);
		btnGenerateChatLogs.setText("Generate Chat Logs");
		btnMinimizeToSystem.setSelection(Settings.getSettings().getChatLogs());
		btnGenerateChatLogs.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Settings.getSettings().setMinimizeTray(btnGenerateChatLogs.getSelection());
			}});

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
