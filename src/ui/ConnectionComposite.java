package ui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import connection.Connection;
import connection.ConnectionSettings;
import connection.Settings;

public class ConnectionComposite extends Composite {
	private Table table;
	private Text textConnName;
	private Text textServer;
	private Text textServPass;
	private Text textNick;
	private Text textNickPass;
	private Text textPort;
	private Text textIdent;
	private Button btnUseSsl;
	private Button btnConnectOnStartup;
	private Button btnConnect; 
	private Label incompleteAlert; 
	private Text textAutoJoin;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ConnectionComposite(Composite parent, int style) {
		super(parent, style);
		
		Group grpConnectionInfo = new Group(this, SWT.NONE);
		grpConnectionInfo.setText("Connection Info");
		grpConnectionInfo.setBounds(10, 130, 430, 211);
		
		Label lblName = new Label(grpConnectionInfo, SWT.NONE);
		lblName.setBounds(10, 22, 95, 19);
		lblName.setText("Name:");
		
		textConnName = new Text(grpConnectionInfo, SWT.BORDER);
		textConnName.setBounds(111, 19, 76, 19);
		
		Label lblServer = new Label(grpConnectionInfo, SWT.NONE);
		lblServer.setText("Server:");
		lblServer.setBounds(10, 50, 95, 19);
		
		textServer = new Text(grpConnectionInfo, SWT.BORDER);
		textServer.setBounds(111, 47, 76, 19);
		
		Label lblPort = new Label(grpConnectionInfo, SWT.NONE);
		lblPort.setText("Port:");
		lblPort.setBounds(10, 75, 95, 19);
		
		textPort = new Text(grpConnectionInfo, SWT.BORDER);
		textPort.setBounds(111, 72, 76, 19);
		
		Label lblServerPassword = new Label(grpConnectionInfo, SWT.NONE);
		lblServerPassword.setText("Server Password:");
		lblServerPassword.setBounds(10, 103, 95, 19);
		
		textServPass = new Text(grpConnectionInfo, SWT.BORDER);
		textServPass.setBounds(111, 100, 76, 19);
		
		btnUseSsl = new Button(grpConnectionInfo, SWT.CHECK);
		btnUseSsl.setBounds(10, 128, 59, 16);
		btnUseSsl.setText("Use SSL");
		
		btnConnectOnStartup = new Button(grpConnectionInfo, SWT.CHECK);
		btnConnectOnStartup.setText("Connect On Startup");
		btnConnectOnStartup.setBounds(75, 128, 112, 16);
		
		Label lblNickname = new Label(grpConnectionInfo, SWT.NONE);
		lblNickname.setText("Nickname:");
		lblNickname.setBounds(243, 22, 95, 19);
		
		textNick = new Text(grpConnectionInfo, SWT.BORDER);
		textNick.setBounds(344, 19, 76, 19);
		
		Label lblNickservPassword = new Label(grpConnectionInfo, SWT.NONE);
		lblNickservPassword.setText("Nickserv Password:");
		lblNickservPassword.setBounds(243, 47, 95, 19);
		
		textNickPass = new Text(grpConnectionInfo, SWT.BORDER);
		textNickPass.setBounds(344, 44, 76, 19);
		
		Label lblIdent = new Label(grpConnectionInfo, SWT.NONE);
		lblIdent.setText("Ident:");
		lblIdent.setBounds(243, 72, 95, 19);
		
		textIdent = new Text(grpConnectionInfo, SWT.BORDER);
		textIdent.setBounds(344, 69, 76, 19);
		
		Label lblAutojoin = new Label(grpConnectionInfo, SWT.NONE);
		lblAutojoin.setBounds(243, 97, 49, 13);
		lblAutojoin.setText("Auto-Join:");
		
		textAutoJoin = new Text(grpConnectionInfo, SWT.BORDER);
		textAutoJoin.setBounds(344, 97, 76, 19);
		
		Button btnSave = new Button(grpConnectionInfo, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(checkIfCompleted())
				{
					//save stuff
					ArrayList<String> autoJoin = new ArrayList<String>();
					for(String ch:textAutoJoin.getText().split(","))
						autoJoin.add(ch);
					ConnectionSettings newCS = new ConnectionSettings(
							textConnName.getText(), textServer.getText(),
							textPort.getText(), textServPass.getText(),
							btnUseSsl.getSelection(), btnConnectOnStartup
									.getSelection(), textNick.getText(),
							textNickPass.getText(), textIdent.getText(),
							autoJoin);
					table.getItem(table.getSelectionIndex()).setData(newCS);
					table.getItem(table.getSelectionIndex()).setText(new String[] {newCS.getConnectionName(),newCS.getServer(),newCS.getNickname()});
					saveTable();
					Settings.writeToFile();
				}
			}

		});
		btnSave.setBounds(352, 178, 68, 23);
		btnSave.setText("Save");
		
		incompleteAlert = new Label(grpConnectionInfo, SWT.NONE);
		incompleteAlert.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		incompleteAlert.setBounds(243, 137, 150, 13);
		incompleteAlert.setText("Some fields missing");
		incompleteAlert.setVisible(false);
		
		
		
		Label lblSeparateChannelsBy = new Label(grpConnectionInfo, SWT.NONE);
		lblSeparateChannelsBy.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		lblSeparateChannelsBy.setBounds(275, 116, 145, 13);
		lblSeparateChannelsBy.setText("Separate channels by commas");
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableFields();
				loadForms((ConnectionSettings)table.getItem(table.getSelectionIndex()).getData());
				//get rid of any blank items
				clearBlanks();
				
			}
		});
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(10, 10, 430, 114);
		
		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(113);
		tblclmnName.setText("Name");
		
		TableColumn tblclmnServer = new TableColumn(table, SWT.NONE);
		tblclmnServer.setWidth(176);
		tblclmnServer.setText("Server");
		
		TableColumn tblclmnNickname = new TableColumn(table, SWT.NONE);
		tblclmnNickname.setWidth(133);
		tblclmnNickname.setText("Nickname");
		
		Button btnNew = new Button(this, SWT.NONE);
		btnNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearBlanks();
				TableItem i = new TableItem(table,SWT.NONE);
				i.setText(new String[] {"[blank]","[blank]","[blank]"});
				table.setSelection(i);
				defaultFields();
				enableFields();
			}
		});
		btnNew.setText("New");
		btnNew.setBounds(451, 10, 68, 23);
		
		Button btnDel = new Button(this, SWT.NONE);
		btnDel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int[] indices = table.getSelectionIndices();
				if(indices.length!=0)
				{
					table.remove(indices);
					saveTable();
					Settings.writeToFile();
					clearFields();
					disableFields();
				}
			}
		});
		btnDel.setText("Delete");
		btnDel.setBounds(451, 39, 68, 23);
		
		btnConnect = new Button(this, SWT.NONE);
		btnConnect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(table.getSelectionCount()==1)
				{
					ConnectionSettings selected = (ConnectionSettings) table.getItem(table.getSelectionIndex()).getData();
					
					Connection c = new Connection(RoomManager.getMain().getContainer(), SWT.NONE, selected);
				}
			}
		});
		btnConnect.setBounds(451, 68, 68, 23);
		btnConnect.setText("Connect");


		loadTable();
		disableFields();
	}
	//load table from connSettings from Settings
	private void loadTable()
	{
		ArrayList<ConnectionSettings> list = Settings.getSettings().getConnSettings();
		for(ConnectionSettings cs:list)
		{
			TableItem i = new TableItem(table,SWT.NONE);
			i.setData(cs);
			i.setText(new String[] {cs.getConnectionName(),cs.getServer(),cs.getNickname()});
		}
	}
	
	private void saveTable()
	{
		ArrayList<ConnectionSettings> list = new ArrayList<ConnectionSettings>();
		for(TableItem i : table.getItems())
		{
			list.add((ConnectionSettings)i.getData());
		}
		Settings.getSettings().setConnSettings(list);
	}
	
	private void loadForms(ConnectionSettings cs)
	{
		textConnName.setText(cs.getConnectionName());
		textServer.setText(cs.getServer());
		textPort.setText(cs.getPort());
		textServPass.setText(cs.getServerPassword());
		btnUseSsl.setSelection(cs.isSsl());
		btnConnectOnStartup.setSelection(cs.isConnectOnStart());
		textNick.setText(cs.getNickname());
		textNickPass.setText(cs.getNickPassword());
		textIdent.setText(cs.getIdent());
		String autoJoin = "";
		for(String ch:cs.getAutoJoin())
		{
			autoJoin+=ch+",";
		}
		textAutoJoin.setText(autoJoin.substring(0,autoJoin.length()-1));
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	private boolean checkIfCompleted() {
		if(textServer.getText().equals(""))
		{
			incompleteAlert.setText("Missing server.");
			incompleteAlert.setVisible(true);
			return false;
		}
		if(textConnName.getText().equals(""))
		{
			textConnName.setText(textServer.getText());
		}
		if(textPort.getText().equals(""))
		{
			textPort.setText("6667");
		}
		if(textNick.getText().equals(""))
		{
			incompleteAlert.setText("Missing nickname.");
			incompleteAlert.setVisible(true);
			return false;
		}
		if(textIdent.getText().equals(""))
		{
			textIdent.setText(textNick.getText());
		}
		incompleteAlert.setVisible(false);
		return true;
	}
	
	private void clearBlanks()
	{
		TableItem[] items = table.getItems();
		for(int k=items.length-1;k>=0;k--)
		{
			if(items[k].getText(0).equals("[blank]"))
			{
				table.remove(k);
			}	
		}
	}
	
	private void enableFields()
	{
		textConnName.setEnabled(true);
		textServer.setEnabled(true);
		textPort.setEnabled(true);
		textServPass.setEnabled(true);
		textNick.setEnabled(true);
		textNickPass.setEnabled(true);
		textIdent.setEnabled(true);
		textAutoJoin.setEnabled(true);
		btnUseSsl.setEnabled(true);
		btnConnectOnStartup.setEnabled(true);
		btnConnect.setEnabled(true);
	}
	
	private void disableFields()
	{
		textConnName.setEnabled(false);
		textServer.setEnabled(false);
		textPort.setEnabled(false);
		textServPass.setEnabled(false);
		textNick.setEnabled(false);
		textNickPass.setEnabled(false);
		textIdent.setEnabled(false);
		textAutoJoin.setEnabled(false);
		btnUseSsl.setEnabled(false);
		btnConnectOnStartup.setEnabled(false);
		btnConnect.setEnabled(false);
	}
	private void clearFields()
	{
		textConnName.setText("");
		textServer.setText("");
		textPort.setText("");
		textServPass.setText("");
		btnUseSsl.setSelection(false);
		btnConnectOnStartup.setSelection(false);
		textNick.setText("");
		textNickPass.setText("");
		textIdent.setText("");
		textAutoJoin.setText("");
	}
	private void defaultFields()
	{
		textConnName.setText("");
		textServer.setText("");
		textPort.setText("6667");
		textServPass.setText("");
		btnUseSsl.setSelection(false);
		btnConnectOnStartup.setSelection(false);
		textNick.setText("");
		textNickPass.setText("");
		textIdent.setText("");
		textAutoJoin.setText("");
	}
}
