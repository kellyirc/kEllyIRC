package ui;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import connection.Connection;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DisposeEvent;

import shared.GlobalSettings;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class MainWindow extends ApplicationWindow {
	
	GlobalSettings globals;
	
	private Text temp_output;
	private Text temp_input;
	private Text temp_who;
	
	private Display display;

	/**
	 * Create the application window.
	 */
	public MainWindow(Display d, GlobalSettings g) {
		super(null);
		
		display = d;
		globals = g;
		globals.setMain(this);
		
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
		
		new Connection(globals, "irc.esper.net","SlaveOfElly");
		globals.manageQueue(this);
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		setStatus("");
		Composite container = new Composite(parent, SWT.NONE);
		
		temp_output = new Text(container, SWT.BORDER | SWT.V_SCROLL);
		temp_output.setEditable(false);
		temp_output.setBounds(10, 10, 464, 332);
		
		temp_input = new Text(container, SWT.BORDER);
		temp_input.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.character == SWT.CR){
					//TODO:
					//send to message queue
					//also send to the server
					//somehow, the connection needs to be passed into this method, or needs to be gottem
					//	or something.. so it knows what connection to use when sending the message to the server
					//perhaps 
					//THATS IT -- a new Text class must exist. It must be extended to have a connection variable passed to it
					//	then it can store the connection so it knows what one to use. 
				}
			}
		});
		temp_input.setBounds(10, 348, 464, 21);
		
		temp_who = new Text(container, SWT.BORDER);
		temp_who.setEditable(false);
		temp_who.setBounds(480, 10, 134, 359);

		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		newShell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent arg0) {
				System.exit(0);
			}
		});
		super.configureShell(newShell);
		newShell.setText("kellyIRC");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(640, 480);
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	public Display getDisplay() {
		return display;
	}
	
	public Text getTO() {
		return temp_output;
	}
}
