/*
 * @author Kyle Kemp
 */
package ui.composites;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import scripting.ScriptWatcher;
import shared.Initializer;
import shared.RoomManager;

import connection.Connection;
import connection.ConnectionSettings;
import connection.Settings;

/**
 * The Class MainWindow.
 */
public class MainWindow extends ApplicationWindow {
	
	/**
	 * Gets the display.
	 *
	 * @return the display
	 */
	@Getter /**
  * Sets the display.
  *
  * @param display the new display
  */
 @Setter
	private Display display;
	
	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	@Getter 
 /**
  * Sets the parent.
  *
  * @param parent the new parent
  */
 @Setter
	private Composite parent;
	
	/**
	 * Gets the container.
	 *
	 * @return the container
	 */
	@Getter 
 /**
  * Sets the container.
  *
  * @param container the new container
  */
 @Setter
	private CTabFolder container;

	//TODO look into blinking http://stackoverflow.com/questions/2773364/make-jface-window-blink-in-taskbar-or-get-users-attention
	
	/**
	 * Create the application window.
	 *
	 * @param d the d
	 */
	public MainWindow(Display d) {
		super(null);

		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
		
		display = d;

		RoomManager.setMain(this);
		
		new Thread(new ScriptWatcher(),"Script Management").start();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#create()
	 */
	@Override
	public void create() {
		super.create();

		CTabItem c = new CTabItem(container, SWT.NONE);
		c.setText("Options");

		c.setControl(new OptionCompositeContainer(container, SWT.NONE));
		ArrayList<ConnectionSettings> list = Settings.getSettings().getConnSettings();
		
		for(ConnectionSettings cs:list)
		{
			if(cs.isConnectOnStart())
				new Connection(container, SWT.NONE, cs);
		}
		//RoomManager.colorset = new Customs();
	}

	/**
	 * Create contents of the application window.
	 *
	 * @param parent the parent
	 * @return the control
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
	 *
	 * @param style the style
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
	 *
	 * @param newShell the new shell
	 */
	@Override
	protected void configureShell(final Shell newShell) {
		newShell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent arg0) {
				Initializer.quit();
			}
		});
		super.configureShell(newShell);
		newShell.setText("kEllyIRC");
		newShell.setMinimumSize(getInitialSize()); 

		try {
			Image image = new Image(getDisplay(), "icon.png");
			newShell.setImage(image);
		} catch (Exception e) {
			Logger log = Logger.getLogger("logs.init");
			log.log(Level.WARNING, "icon.png not found");
		}
		newShell.addShellListener(new ShellListener(){

			@Override
			public void shellActivated(ShellEvent arg0) {}

			@Override
			public void shellClosed(ShellEvent arg0) {}

			@Override
			public void shellDeactivated(ShellEvent arg0) {}

			@Override
			public void shellDeiconified(ShellEvent arg0) {}

			@Override
			public void shellIconified(ShellEvent arg0) {
				
			}});
	}

	/**
	 * Return the initial size of the window.
	 *
	 * @return the initial size
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(850, 500);
	}

}
