package ui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import connection.ConnectionSettings;

public class NewConnectionDialog extends Dialog {

	protected Object result;
	protected Shell shlNewConnection;
	private Text connNameField;
	private Text serverField;
	private Text portField;
	private Text serverPasswordField;
	private Text nickField;
	private Text altNickField;
	private Text nickPasswordField;
	

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public NewConnectionDialog(Shell parent) {
		super(parent);
		setText("SWT Dialog");
	}
	

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open(ConnectionSettings settings) {
		createContents(settings);
		shlNewConnection.open();
		shlNewConnection.layout();
		Display display = getParent().getDisplay();
		while (!shlNewConnection.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents(ConnectionSettings settings) {
		shlNewConnection = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		shlNewConnection.setSize(289, 370);
		shlNewConnection.setText("New Connection");
		
		Group group = new Group(shlNewConnection, SWT.NONE);
		group.setText("Connection Settings");
		group.setBounds(10, 10, 264, 154);
		
		CLabel label = new CLabel(group, SWT.NONE);
		label.setText("Connection Name:");
		label.setAlignment(SWT.RIGHT);
		label.setBounds(10, 20, 121, 21);
		
		connNameField = new Text(group, SWT.BORDER);
		connNameField.setBounds(137, 20, 117, 21);
		
		serverField = new Text(group, SWT.BORDER);
		serverField.setBounds(137, 47, 76, 21);
		
		portField = new Text(group, SWT.BORDER);
		if(settings != null)
			portField.setText("6667");
		portField.setBounds(137, 74, 60, 21);
		
		CLabel label_1 = new CLabel(group, SWT.NONE);
		label_1.setText("Server:");
		label_1.setAlignment(SWT.RIGHT);
		label_1.setBounds(10, 47, 121, 21);
		
		CLabel label_2 = new CLabel(group, SWT.NONE);
		label_2.setText("Port:");
		label_2.setAlignment(SWT.RIGHT);
		label_2.setBounds(10, 74, 121, 21);
		
		CLabel label_3 = new CLabel(group, SWT.NONE);
		label_3.setText("Password:");
		label_3.setAlignment(SWT.RIGHT);
		label_3.setBounds(10, 102, 121, 21);
		
		serverPasswordField = new Text(group, SWT.BORDER | SWT.PASSWORD);
		serverPasswordField.setBounds(137, 101, 117, 21);
		
		Label label_4 = new Label(group, SWT.NONE);
		label_4.setText("New Label");
		label_4.setBounds(20, 47, 55, 15);
		
		final Button ssl = new Button(group, SWT.CHECK);
		ssl.setText("Use SSL");
		ssl.setBounds(137, 128, 93, 16);
		
		Group group_1 = new Group(shlNewConnection, SWT.NONE);
		group_1.setText("Nick");
		group_1.setBounds(10, 170, 264, 134);
		
		CLabel label_5 = new CLabel(group_1, SWT.NONE);
		label_5.setText("Nickname:");
		label_5.setAlignment(SWT.RIGHT);
		label_5.setBounds(10, 21, 123, 21);
		
		nickField = new Text(group_1, SWT.BORDER);
		nickField.setBounds(139, 21, 115, 21);
		
		CLabel label_6 = new CLabel(group_1, SWT.NONE);
		label_6.setText("Alternate Nickname:");
		label_6.setAlignment(SWT.RIGHT);
		label_6.setBounds(10, 48, 123, 21);
		
		altNickField = new Text(group_1, SWT.BORDER);
		altNickField.setBounds(139, 48, 115, 21);
		
		CLabel label_7 = new CLabel(group_1, SWT.NONE);
		label_7.setText("Password:");
		label_7.setAlignment(SWT.RIGHT);
		label_7.setBounds(10, 75, 123, 21);
		
		nickPasswordField = new Text(group_1, SWT.BORDER | SWT.PASSWORD);
		nickPasswordField.setBounds(139, 75, 115, 21);
		
		Button ok = new Button(shlNewConnection, SWT.NONE);
		ok.addSelectionListener(new SelectionAdapter() {	//Checks if all required fields are filled in, if so, create a ConnectionSettings object and add it to the table.
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!connNameField.getText().equals("") && !serverField.getText().equals("") && !portField.getText().equals("") && !nickField.getText().equals("") && !altNickField.getText().equals(""))
				{
					ConnectionSettings cs = new ConnectionSettings(connNameField.getText(), serverField.getText(), portField.getText(), serverPasswordField.getText(), ssl.getSelection(), nickField.getText(), altNickField.getText(), nickPasswordField.getText());
					
					((ConnectionWindow) shlNewConnection.getParent()).addEntry(cs);
					shlNewConnection.dispose();
				}
			}
		});	
		ok.setText("OK");
		ok.setBounds(118, 310, 75, 25);
		
		Button cancel = new Button(shlNewConnection, SWT.NONE);
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlNewConnection.dispose();
			}
		});
		cancel.setText("Cancel");
		cancel.setBounds(199, 310, 75, 25);

		
		//Fill in the fields if this was constructed with a ConnectionSettings
		if(settings != null)
		{
			connNameField.setText(settings.connectionName);
			serverField.setText(settings.server);
			portField.setText(settings.port);
			serverPasswordField.setText(settings.serverPassword);
			ssl.setSelection(settings.ssl);
			nickField.setText(settings.nickname);
			altNickField.setText(settings.alternateNickname);
			nickPasswordField.setText(settings.nickPassword);
		}
	}

}
