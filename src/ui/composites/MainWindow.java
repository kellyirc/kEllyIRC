package ui.composites;

import java.util.ArrayList;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import shared.RoomManager;

import connection.Connection;
import connection.ConnectionSettings;
import connection.Settings;

public class MainWindow extends ApplicationWindow {
	
	private Display display;
	private Composite parent;
	private CTabFolder container;

	/**
	 * Create the application window.
	 */
	public MainWindow(Display d) {
		super(null);

		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
		
		display = d;
		RoomManager.setMain(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#open()
	 */
	@Override
	public int open() {

		int i = super.open();
		
		return i;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#create()
	 */
	@Override
	public void create() {
		super.create();

		CTabItem c = new CTabItem(container, SWT.NONE);
		c.setText("Options");

		c.setControl(new OptionComposite(container, SWT.NONE));
		ArrayList<ConnectionSettings> list = Settings.getSettings().getConnSettings();
		
		//TODO: Make use of other settings in ConnectionSettings
		for(ConnectionSettings cs:list)
		{
			if(cs.isConnectOnStart())
				new Connection(container, SWT.NONE, cs);
		}
		//RoomManager.colorset = new Customs();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.ApplicationWindow#close()
	 */
	@Override
	public boolean close() {
		return super.close();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		setStatus("");
		this.parent = parent;
		
		CTabFolder container = new CTabFolder(parent, SWT.NONE);
		container.setSimple(false);
		container.setLayout(new FormLayout());
		this.container = container;

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
				//TODO: do quitting stuff here
				System.exit(0);
			}
		});
		super.configureShell(newShell);
		newShell.setText("kEllyIRC");
		newShell.setMinimumSize(800, 480); 
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(800, 480);
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	public Display getDisplay() {
		return display;
	}

	public void setParent(Composite parent) {
		this.parent = parent;
	}

	public Composite getParent() {
		return parent;
	}

	public Composite getContainer() {
		return container;
	}
}
