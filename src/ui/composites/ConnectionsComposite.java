/*
 * @author Kyle Kemp
 */
package ui.composites;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import shared.Message;
import shared.NSAlertBox;
import shared.RoomManager;
import shared.SWTResourceManager;
import connection.Connection;
import connection.ConnectionSettings;
import connection.Settings;

/**
 * The Class ConnectionsComposite.
 */
public class ConnectionsComposite extends Composite {
	
	/** The table. */
	private Table table;
	
	/** The text conn name. */
	private Text textConnName;
	
	/** The text server. */
	private Text textServer;
	
	/** The text serv pass. */
	private Text textServPass;
	
	/** The text nick. */
	private Text textNick;
	
	/** The text nick pass. */
	private Text textNickPass;
	
	/** The text port. */
	private Text textPort;
	
	/** The text ident. */
	private Text textIdent;
	
	/** The btn use ssl. */
	private Button btnUseSsl;
	
	/** The btn connect on startup. */
	private Button btnConnectOnStartup;
	
	/** The btn connect. */
	private Button btnConnect;
	
	/** The incomplete alert. */
	private Label incompleteAlert;
	
	/** The text auto join. */
	private Text textAutoJoin;

	/**
	 * Create the composite.
	 *
	 * @param parent the parent
	 * @param style the style
	 */
	public ConnectionsComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//if the CS data isn't null, load dat up
				if (!(table.getItem(table.getSelectionIndex()).getData() == null)) {
					incompleteAlert.setVisible(false);
					lblServer.setForeground(SWTResourceManager
							.getColor(SWT.COLOR_BLACK));
					lblNickname.setForeground(SWTResourceManager
							.getColor(SWT.COLOR_BLACK));
					enableFields();
					loadForms((ConnectionSettings) table.getItem(
							table.getSelectionIndex()).getData());
					// get rid of any blank items
					clearBlanks();
				}
			}
		});
		// connect the connection on double click
		table.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				TableItem item = table.getItem(new Point(e.x, e.y));
				if (item != null) {
					saveAndConnect();
				}
			}
		});
		// connect the connection when user presses enter
		table.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.character == SWT.CR && table.getSelectionCount() == 1) {
					saveAndConnect();
				}
			}
		});

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(139);
		tblclmnName.setText("Name");

		TableColumn tblclmnServer = new TableColumn(table, SWT.NONE);
		tblclmnServer.setWidth(176);
		tblclmnServer.setText("Server");

		TableColumn tblclmnNickname = new TableColumn(table, SWT.NONE);
		tblclmnNickname.setWidth(148);
		tblclmnNickname.setText("Nickname");

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,
				1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		composite.setLayout(gl_composite);

		Button btnNew = new Button(composite, SWT.NONE);
		GridData gd_btnNew = new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1);
		gd_btnNew.widthHint = 65;
		btnNew.setLayoutData(gd_btnNew);
		btnNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				incompleteAlert.setVisible(false);
				clearBlanks();
				TableItem i = new TableItem(table, SWT.NONE);
				i.setText(new String[] { "", "", "" });
				table.setSelection(i);
				defaultFields();
				enableFields();
			}
		});
		btnNew.setText("New");

		Button btnDel = new Button(composite, SWT.NONE);
		btnDel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));
		btnDel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int[] indices = table.getSelectionIndices();
				if (indices.length != 0) {
					table.remove(indices);
					saveTable();
					clearFields();
					disableFields();
				}
			}
		});
		btnDel.setText("Delete");

		btnConnect = new Button(composite, SWT.NONE);
		btnConnect.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false,
				1, 1));
		btnConnect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveAndConnect();
			}
		});
		btnConnect.setText("Connect");

		Group grpConnectionInfo = new Group(this, SWT.NONE);
		GridData gd_grpConnectionInfo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_grpConnectionInfo.widthHint = 609;
		grpConnectionInfo.setLayoutData(gd_grpConnectionInfo);
		grpConnectionInfo.setText("Connection Info");

		lblName = new Label(grpConnectionInfo, SWT.NONE);
		lblName.setBounds(10, 22, 95, 19);
		lblName.setText("Name:");

		textConnName = new Text(grpConnectionInfo, SWT.BORDER);
		textConnName.setBounds(111, 19, 76, 19);
		textConnName.addFocusListener(fieldSaver);

		lblServer = new Label(grpConnectionInfo, SWT.NONE);
		lblServer.setText("Server:");
		lblServer.setBounds(10, 50, 95, 19);

		textServer = new Text(grpConnectionInfo, SWT.BORDER);
		textServer.setBounds(111, 47, 76, 19);
		textServer.addFocusListener(fieldSaver);

		lblPort = new Label(grpConnectionInfo, SWT.NONE);
		lblPort.setText("Port:");
		lblPort.setBounds(10, 75, 95, 19);

		textPort = new Text(grpConnectionInfo, SWT.BORDER);
		textPort.setBounds(111, 72, 76, 19);
		textPort.addFocusListener(fieldSaver);

		lblServerPassword = new Label(grpConnectionInfo, SWT.NONE);
		lblServerPassword.setText("Server Password:");
		lblServerPassword.setBounds(10, 103, 95, 19);

		textServPass = new Text(grpConnectionInfo, SWT.BORDER | SWT.PASSWORD);
		textServPass.setBounds(111, 100, 76, 19);
		textServPass.addFocusListener(fieldSaver);

		btnUseSsl = new Button(grpConnectionInfo, SWT.CHECK);
		btnUseSsl.setBounds(10, 128, 59, 16);
		btnUseSsl.setText("Use SSL");
		btnUseSsl.addFocusListener(fieldSaver);

		btnConnectOnStartup = new Button(grpConnectionInfo, SWT.CHECK);
		btnConnectOnStartup.setText("Connect On Startup");
		btnConnectOnStartup.setBounds(75, 128, 162, 16);
		btnConnectOnStartup.addFocusListener(fieldSaver);

		lblNickname = new Label(grpConnectionInfo, SWT.NONE);
		lblNickname.setText("Nickname:");
		lblNickname.setBounds(211, 25, 95, 19);

		textNick = new Text(grpConnectionInfo, SWT.BORDER);
		textNick.setBounds(341, 22, 76, 19);
		textNick.addFocusListener(fieldSaver);

		lblNickservPassword = new Label(grpConnectionInfo, SWT.NONE);
		lblNickservPassword.setText("Nickserv Password:");
		lblNickservPassword.setBounds(211, 50, 112, 19);

		textNickPass = new Text(grpConnectionInfo, SWT.BORDER | SWT.PASSWORD);
		textNickPass.setBounds(341, 50, 76, 19);
		textNickPass.addFocusListener(fieldSaver);

		lblIdent = new Label(grpConnectionInfo, SWT.NONE);
		lblIdent.setText("Ident:");
		lblIdent.setBounds(211, 75, 95, 19);

		textIdent = new Text(grpConnectionInfo, SWT.BORDER);
		textIdent.setBounds(341, 75, 76, 19);
		textIdent.addFocusListener(fieldSaver);

		lblAutojoin = new Label(grpConnectionInfo, SWT.NONE);
		lblAutojoin.setBounds(211, 100, 81, 13);
		lblAutojoin.setText("Auto-Join:");

		textAutoJoin = new Text(grpConnectionInfo, SWT.BORDER);
		textAutoJoin.setBounds(341, 103, 76, 19);
		textAutoJoin.addFocusListener(fieldSaver);

		incompleteAlert = new Label(grpConnectionInfo, SWT.NONE);
		incompleteAlert.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_RED));
		incompleteAlert.setBounds(211, 159, 220, 13);
		incompleteAlert.setText("Some fields missing");
		incompleteAlert.setVisible(false);

		lblSeparateChannelsBy = new Label(grpConnectionInfo, SWT.NONE);
		lblSeparateChannelsBy.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		lblSeparateChannelsBy.setBounds(240, 131, 174, 14);
		lblSeparateChannelsBy.setText("Separate channels by commas");
		
		textPerform = new Text(grpConnectionInfo, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textPerform.setBounds(434, 50, 162, 72);
		textPerform.addFocusListener(fieldSaver);

		lblPerformOnConnect = new Label(grpConnectionInfo, SWT.NONE);
		lblPerformOnConnect.setBounds(434, 26, 173, 15);
		lblPerformOnConnect.setText("Perform on connect:");
		new Label(this, SWT.NONE);

		loadTable();
		disableFields();
	}

	/**
	 * Load table.
	 */
	private void loadTable() {
		ArrayList<ConnectionSettings> list = Settings.getSettings()
				.getConnSettings();
		for (ConnectionSettings cs : list) {
			TableItem i = new TableItem(table, SWT.NONE);
			i.setData(cs);
			i.setText(new String[] { cs.getConnectionName(), cs.getServer(),
					cs.getNickname() });
		}
	}

	/**
	 * Save table.
	 */
	private void saveTable() {
		ArrayList<ConnectionSettings> list = new ArrayList<ConnectionSettings>();
		for (TableItem i : table.getItems()) {
			list.add((ConnectionSettings) i.getData());
		}
		Settings.getSettings().setConnSettings(list);
		Settings.writeToFile();
	}

	/**
	 * Save selected table entry.
	 */
	private void saveEntry()
	{
		//do nothing if nothing selected
		if(table.getSelectionCount()==0)
			return;
		ArrayList<String> autoJoin = new ArrayList<>();
		for (String ch : textAutoJoin.getText().split(","))
			autoJoin.add(ch);
		
		ArrayList<String> performOnConnect = new ArrayList<>();
		for (String cmd : textPerform.getText().split(Message.NEW_LINE))
			performOnConnect.add(cmd);
		
		ConnectionSettings newCS = new ConnectionSettings(textServer.getText(),
										textNick.getText());
		
		if(!textConnName.getText().isEmpty())
			newCS.setConnectionName(textConnName.getText());
		
		newCS.setPort(Integer.parseInt(textPort.getText()));
		newCS.setServerPassword(textServPass.getText());
		newCS.setSsl(btnUseSsl.getSelection());
		newCS.setConnectOnStart(btnConnectOnStartup.getSelection());
		newCS.setNickPassword(textNickPass.getText());
		if(!textIdent.getText().isEmpty())	
			newCS.setIdent(textIdent.getText());
		newCS.setAutoJoin(autoJoin);
		newCS.setPerformOnConnect(performOnConnect);
		
		table.getItem(table.getSelectionIndex()).setData(newCS);
		table.getItem(table.getSelectionIndex()).setText(
				new String[] { newCS.getConnectionName(),
						newCS.getServer(), newCS.getNickname() });
		saveTable();
	}
	
	/**
	 * Load forms.
	 *
	 * @param cs the cs
	 */
	private void loadForms(ConnectionSettings cs) {
		textConnName.setText(cs.getConnectionName());
		textServer.setText(cs.getServer());
		textPort.setText(String.valueOf(cs.getPort()));
		textServPass.setText(cs.getServerPassword());
		btnUseSsl.setSelection(cs.isSsl());
		btnConnectOnStartup.setSelection(cs.isConnectOnStart());
		textNick.setText(cs.getNickname());
		textNickPass.setText(cs.getNickPassword());
		textIdent.setText(cs.getIdent());
		
		String autoJoin = "";
		for (String ch : cs.getAutoJoin()) {
			if(!autoJoin.isEmpty())
				autoJoin += ",";
			autoJoin += ch;
		}
		textAutoJoin.setText(autoJoin.substring(0, autoJoin.length()));
		
		String perform = "";
		for (String cmd : cs.getPerformOnConnect())
		{
			if(!perform.isEmpty())
				perform += Message.NEW_LINE;
			perform += cmd;
		}
		textPerform.setText(perform);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Composite#checkSubclass()
	 */
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * Check if completed.
	 *
	 * @return true, if successful
	 */
	private boolean checkIfCompleted() {
		
		boolean complete = true;
		//check if server text empty
		if (textServer.getText().isEmpty()) {
			incompleteAlert.setText("Missing server.");
			lblServer.setForeground(SWTResourceManager
					.getColor(SWT.COLOR_RED));
			complete = false;
		}
		else
			lblServer.setForeground(SWTResourceManager
					.getColor(SWT.COLOR_BLACK));
		//check if nick text is empty
		if (textNick.getText().isEmpty()) {
			incompleteAlert.setText("Missing nickname.");
			lblNickname.setForeground(SWTResourceManager
					.getColor(SWT.COLOR_RED));
			complete = false;
		}
		else
			lblNickname.setForeground(SWTResourceManager
					.getColor(SWT.COLOR_BLACK));
		incompleteAlert.setVisible(!complete);
		
		//try to fill empty ident and conn name and port fields
		if(textPort.getText().isEmpty())
			textPort.setText(String.valueOf(ConnectionSettings.DEFAULT_PORT));
		if(textConnName.getText().isEmpty())
			textConnName.setText(textServer.getText());
		if(textIdent.getText().isEmpty())
			textIdent.setText(textNick.getText());
		
		return complete;
	}

	/**
	 * Clear blanks.
	 */
	private void clearBlanks() {
		TableItem[] items = table.getItems();
		for (int k = items.length - 1; k >= 0; k--) {
			ConnectionSettings cs = ((ConnectionSettings)items[k].getData());
			if (cs == null || cs.getNickname().isEmpty() && 
					cs.getServer().isEmpty() && cs.getConnectionName().isEmpty())
				table.remove(k);
		}
	}

	/**
	 * Enable fields.
	 */
	private void enableFields() {
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
		textPerform.setEnabled(true);
	}

	/**
	 * Disable fields.
	 */
	private void disableFields() {
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
		textPerform.setEnabled(false);
	}

	/**
	 * Clear fields.
	 */
	private void clearFields() {
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
		textPerform.setText("");
	}

	/**
	 * Default fields.
	 */
	private void defaultFields() {
		textConnName.setText("");
		textServer.setText("");
		textPort.setText(String.valueOf(ConnectionSettings.DEFAULT_PORT));
		textServPass.setText("");
		btnUseSsl.setSelection(ConnectionSettings.DEFAULT_SSL);
		btnConnectOnStartup.setSelection(ConnectionSettings.DEFAULT_CONNECT_ON_START);
		textNick.setText("");
		textNickPass.setText("");
		textIdent.setText("");
		textAutoJoin.setText("");
		textPerform.setText("");
	}

	/**
	 * Save and connect.
	 */
	private void saveAndConnect() {
		boolean completed = checkIfCompleted();
		if (table.getSelectionCount() == 1 && completed) {
			// save stuff
			saveEntry();
			//connect
			ConnectionSettings selected = (ConnectionSettings) table.getItem(
					table.getSelectionIndex()).getData();
			new Connection(RoomManager.getMain().getContainer(), SWT.NONE,
					selected);
		}
		else if(!completed)
			new NSAlertBox("Unable to connect", "The selected connection does not have enough information.", SWT.OK).go();
	}
	
	/** The field saver. */
	private FocusAdapter fieldSaver = new FocusAdapter() {

		@Override
		public void focusLost(FocusEvent e)
		{
			checkIfCompleted();
			saveEntry();
			table.getItem(table.getSelectionIndex()).setText(
					new String[] { textConnName.getText(),
							textServer.getText(), textNick.getText() });
			super.focusLost(e);
		}
		
	};
	
	/** The lbl name. */
	private Label lblName;
	
	/** The lbl server. */
	private Label lblServer;
	
	/** The lbl port. */
	private Label lblPort;
	
	/** The lbl server password. */
	private Label lblServerPassword;
	
	/** The lbl nickname. */
	private Label lblNickname;
	
	/** The lbl nickserv password. */
	private Label lblNickservPassword;
	
	/** The lbl ident. */
	private Label lblIdent;
	
	/** The lbl autojoin. */
	private Label lblAutojoin;
	
	/** The lbl separate channels by. */
	private Label lblSeparateChannelsBy;
	private Text textPerform;
	private Label lblPerformOnConnect;
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose()
	{
		clearBlanks();
		saveEntry();
		super.dispose();
	}
}