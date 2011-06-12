package ui.composites;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;

import scripting.Script;
import scripting.ScriptManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;


public class ScriptComposite extends Composite {

	StyledText curTextBox;
	Script curScript;
	CTabFolder tabs;
	JavaLineStyler lineStyler = new JavaLineStyler();
	private Combo combo;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ScriptComposite(final Composite parent, int style) {
		super(parent, style);

		combo = new Combo(this, SWT.NONE);
		combo.setEnabled(false);
		combo.setBounds(140, 9, 196, 23);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				curTextBox.setSelection(curTextBox.getText().indexOf(
						combo.getText()));
			}
		});

		final CheckboxTreeViewer checkboxTreeViewer = new CheckboxTreeViewer(
				this, SWT.BORDER);
		Tree tree = checkboxTreeViewer.getTree();
		tree.setBounds(10, 9, 124, 369);

		addTreeItems(tree);
		
		tabs = new CTabFolder(this, SWT.BORDER);
		tabs.setSimple(false);
		tabs.setBounds(140, 38, 508, 340);
		tabs.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		ToolBar toolBar = new ToolBar(this, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(342, 9, 306, 23);
		
		ToolItem tltmNew = new ToolItem(toolBar, SWT.NONE);
		tltmNew.setText("New");
		
		ToolItem tltmSave = new ToolItem(toolBar, SWT.NONE);
		tltmSave.setText("Save");
		tltmSave.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					BufferedWriter bw = new BufferedWriter(new FileWriter(curScript.getReference()));
					bw.write(curTextBox.getText());
					bw.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}});
		
		
		tabs.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				enableTopBar();
			}

			private void enableTopBar() {
				curTextBox = (StyledText) tabs.getSelection().getControl();
				combo.setEnabled(true);
				combo.removeAll();
				Script s = (Script)tabs.getSelection().getData();
				for (String tag : s.getDescriptFunctions()) {
					combo.add(tag);
				}
				curScript = s;
				//TODO enable buttons
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}});

		checkboxTreeViewer.getTree().addListener(SWT.MouseDoubleClick,
				new Listener() {

					@Override
					public void handleEvent(Event event) {
						Point point = new Point(event.x, event.y);
						final TreeItem item = checkboxTreeViewer.getTree()
								.getItem(point);
						if (item != null) {
							Script s = (Script) item.getData();
							
							createNewTab(s);
							curTextBox.setText(s.getScript());
							
							lineStyler.parseBlockComments(s.getScript());
						}
					}

					private void createNewTab(Script s) {
						for(CTabItem c : tabs.getItems()){
							if(c.getData().equals(s)){
								tabs.setSelection(c);
								return;
							}
						}
						CTabItem newItem = new CTabItem(tabs, SWT.CLOSE);
						newItem.setData(s);
						newItem.setText(s.getReference().getName());
						newItem.addDisposeListener(new DisposeListener(){

							@Override
							public void widgetDisposed(DisposeEvent e) {
								if(tabs.getItemCount() == 0){
									disableTopBar();
								}
								
							}

							private void disableTopBar() {
								combo.setEnabled(false);
								//TODO add buttons and stuff
								
							}});
						StyledText st = new StyledText(tabs, SWT.MULTI|SWT.V_SCROLL);
						curTextBox = st;
						newItem.setControl(st);
						st.setFont(SWTResourceManager.getFont("Courier New", 9,
								SWT.NORMAL));
						st.addLineStyleListener(lineStyler);
						
						st.addKeyListener(new KeyListener() {

							@Override
							public void keyPressed(KeyEvent e) {
								if ((e.stateMask & SWT.CTRL) != 0) {
									switch (e.keyCode) {
									case 's':
									case 'z':
										//undo();
										break;
									case 'y':
										//redo();
										break;
									}
								}
							}

							@Override
							public void keyReleased(KeyEvent e) {

							}
						});
					}
				});
		checkboxTreeViewer.addCheckStateListener(new ICheckStateListener() {

			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				Script s = (Script) event.getElement();
				s.setInUse(event.getChecked());

			}
		});
	}

	private void addTreeItems(final Tree tree) {

		for (final Script s : ScriptManager.scripts) {
			TreeItem t = new TreeItem(tree, SWT.NONE);
			t.setChecked(s.isInUse());
			t.setText(s.getReference().getName());
			t.setData(s);
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
