package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import util.ConnectionSettingsUtil;
import connection.ConnectionSettings;

public class ConnectionWindow extends Shell {
	private Table table;
	ConnectionWindow shell;

	/**
	 * Create the shell.
	 * @param display
	 */
	public ConnectionWindow(Display display) {
		
		//todo: Read saved connections from a file here
		super(display, SWT.CLOSE | SWT.MIN | SWT.TITLE);
		
		final NewConnectionDialog ncd = new NewConnectionDialog(this);
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		
		table.setBounds(10, 10, 375, 189);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnConnectionName = new TableColumn(table, SWT.NONE);
		tblclmnConnectionName.setWidth(107);
		tblclmnConnectionName.setText("Conn. Name");
		
		TableColumn tblclmnServer = new TableColumn(table, SWT.NONE);
		tblclmnServer.setWidth(151);
		tblclmnServer.setText("Server");
		
		TableColumn tblclmnUsername = new TableColumn(table, SWT.NONE);
		tblclmnUsername.setWidth(113);
		tblclmnUsername.setText("Username");
		
		
		loadFile();
		
		
		
		Button btnAdd = new Button(this, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {						
				ncd.open(null);
				updateFile();
			}
		});
		btnAdd.setBounds(391, 10, 75, 25);
		btnAdd.setText("Add");
		
		Button btnEdit = new Button(this, SWT.NONE);
		btnEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(table.getSelectionIndex()!=-1)
				ncd.open((ConnectionSettings)table.getItem(table.getSelectionIndex()).getData());
				table.remove(table.getSelectionIndex());
				updateFile();
			}
		});
		btnEdit.setBounds(391, 41, 75, 25);
		btnEdit.setText("Edit");
		
		Button btnRemove = new Button(this, SWT.NONE);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(table.getSelectionIndex()!=-1)
				table.remove(table.getSelectionIndex());
				updateFile();
			}
		});
		btnRemove.setBounds(391, 72, 75, 25);
		btnRemove.setText("Remove");
		
		Button btnClose = new Button(this, SWT.NONE);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//todo: Make this button close the Connection Manager
				shell.close(); //Why does this error?
			}
		});
		btnClose.setBounds(391, 174, 75, 25);
		btnClose.setText("Close");
		
		Button btnConnect = new Button(this, SWT.NONE);  //todo: Make this button connect to the connection selected
		btnConnect.setBounds(391, 143, 75, 25);
		btnConnect.setText("Connect");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Connection Manager");
		setSize(481, 237);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void addEntry(ConnectionSettings cs)
	{
		TableItem tableItem = new TableItem(table, SWT.NONE);
		String[] strings = {cs.connectionName,cs.server,cs.nickname};
		tableItem.setText(strings);
		tableItem.setData(cs);
	}
	
	private void updateFile()
	{
		TableItem[] items = table.getItems();
		ConnectionSettings[] settings = new ConnectionSettings[items.length];
		for(int k=0;k<items.length;k++)
			settings[k] = (ConnectionSettings) items[k].getData();
		ConnectionSettingsUtil.writeToFile(settings);
	}
	
	private void loadFile()
	{
		ConnectionSettings[] settings = ConnectionSettingsUtil.readFromFile();
		for(ConnectionSettings cs : settings)
		{
			addEntry(cs);
		}
	}
}
