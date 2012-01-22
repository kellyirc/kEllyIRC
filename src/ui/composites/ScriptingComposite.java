package ui.composites;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import scripting.Script;
import scripting.ScriptManager;
import shared.NSAlertBox;
import shared.RoomManager;
import org.eclipse.swt.widgets.Label;

//TODO show file headers in scripts otherwise line numbers will be off

public class ScriptingComposite extends Composite {

	private final class ValidFileName implements IInputValidator {
		@Override
		public String isValid(String newText) {
			if(newText == null){return "Needs a file name.";}
			
			if(!newText.replaceAll("[^a-zA-Z0-9\\_\\-\\.]", "").equals(newText)){
				return "Invalid file name.";
			}
			
			boolean isValid=false;
			for(String s : ScriptManager.validExt){
				if(newText.endsWith(s)){
					isValid=true;
					break;
				}
			}
			for(Script s : ScriptManager.scripts){
				if(s.getReference().getName().equals(newText)){
					return "File name already exists.";
				}
			}
			if(!isValid){
				return "Needs to have a valid file extension (.js, .rb, .py, .lua).";
			}
			return null;
		}
	}

	ScriptEditor curEditor;
	Script curScript;
	CTabFolder tabs;
	Tree tree;
	
	private Combo combo;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ScriptingComposite(final Composite parent, int style) {
		super(parent, style);
		System.setProperty("sun.awt.noerasebackground", "true");
		
		buildLayout(parent);
	}

	private void buildLayout(final Composite parent) {
		setLayout(new GridLayout(3,false));

		final CheckboxTreeViewer checkboxTreeViewer = new CheckboxTreeViewer(
				this, SWT.BORDER);
		Tree tree = checkboxTreeViewer.getTree();
		GridData treeGD = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 2);
		treeGD.widthHint = 124;
		tree.setLayoutData(treeGD);
//		tree.setBounds(10, 9, 124, 369);
		this.tree = tree;

		updateTreeItems();
		
		buildCombo();

		ToolBar toolBar = new ToolBar(this, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(342, 9, 306, 23);
		
		ToolItem tltmNew = new ToolItem(toolBar, SWT.NONE);
		tltmNew.setText("New");
		
		ToolItem tltmRename = new ToolItem(toolBar, SWT.NONE);
		tltmRename.setText("Rename");
		
		ToolItem tltmDelete = new ToolItem(toolBar, SWT.NONE);
		tltmDelete.setText("Delete");
		
		final ToolItem tltmSave = new ToolItem(toolBar, SWT.NONE);
		tltmSave.setEnabled(false);
		tltmSave.setText("Save");
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		final ToolItem tltmCut = new ToolItem(toolBar, SWT.NONE);
		tltmCut.setEnabled(false);
		tltmCut.setText("Cut");
		
		final ToolItem tltmCopy = new ToolItem(toolBar, SWT.NONE);
		tltmCopy.setEnabled(false);
		tltmCopy.setText("Copy");
		
		final ToolItem tltmPaste = new ToolItem(toolBar, SWT.NONE);
		tltmPaste.setEnabled(false);
		tltmPaste.setText("Paste");

		buttonListeners(parent, tltmNew, tltmSave, tltmCut, tltmCopy, tltmPaste, tltmDelete, tltmRename);
		
		buildTabFolder(tltmSave, tltmCut, tltmCopy, tltmPaste);

		checkboxTreeViewer.getTree().addListener(SWT.MouseDoubleClick,
				new Listener() {

					@Override
					public void handleEvent(Event event) {
						Point point = new Point(event.x, event.y);
						final TreeItem item = checkboxTreeViewer.getTree()
								.getItem(point);
						if (item != null) {
							Script s = (Script) item.getData();
							
							if(!createNewTab(s)) {
								return;
							}
							showTab(s);
						}
					}

					private void showTab(final Script s) {
						curEditor.setText(s.getScript());
						buildTextModifyListener(s);
					}

					private void buildTextModifyListener(final Script s) {
						//TODO modified listener for rsyntaxtextarea
						/*curTextBox.addExtendedModifyListener(new ExtendedModifyListener(){

							@Override
							public void modifyText(ExtendedModifyEvent event) {
								CTabItem currentTab = tabs.getSelection();
								if(!currentTab.getText().startsWith("*")){
									currentTab.setText("*"+currentTab.getText());
								}
								
								
							}});*/
					}

					private boolean createNewTab(Script s) {
						for(CTabItem c : tabs.getItems()){
							if(c.getData().equals(s)){
								//changeTab(c);
								return false;
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
								tltmSave.setEnabled(false);
								tltmCut.setEnabled(false);
								tltmCopy.setEnabled(false);
								tltmPaste.setEnabled(false);
								
							}});
						
						
						
						//final StyledText st = new StyledText(tabs, SWT.MULTI|SWT.V_SCROLL);
						//st.setWordWrap(true);
						//curTextBox = st;
						//newItem.setControl(st);
						
						Composite comp = new Composite(tabs, SWT.EMBEDDED | SWT.NO_BACKGROUND);
					    ScriptEditor se = new ScriptEditor(comp, s);
					    curEditor = se;
						newItem.setControl(comp);
						
						changeTab(newItem);
						//st.setFont(SWTResourceManager.getFont("Courier New", 9,	SWT.NORMAL));
						//buildTabStyler(s, st);
						enableTopBar(tltmSave, tltmCut, tltmCopy, tltmPaste);
						return true;
					}
/*
					private void buildTabStyler(Script s, final StyledText st) {
						final LineStyler styler = stylers.get(s.getScriptType());
						st.addLineStyleListener(styler);
						
						st.addKeyListener(new KeyListener() {

							@Override
							public void keyPressed(KeyEvent e) {
								styler.parseBlockComments(st.getText());
								if ((e.stateMask & SWT.CTRL) != 0) {
									switch (e.keyCode) {
									case 'a':
										curTextBox.selectAll();
										break;
									case 's':
										save();
										break;
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
					}*/
				});
		
		checkboxTreeViewer.addCheckStateListener(new ICheckStateListener() {

			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				Script s = (Script) event.getElement();
				s.setInUse(event.getChecked());

			}
		});
	}

	private void buildTabFolder(final ToolItem tltmSave,
			final ToolItem tltmCut, final ToolItem tltmCopy,
			final ToolItem tltmPaste) {
		tabs = new CTabFolder(this, SWT.BORDER);
		tabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tabs.setSimple(false);
		tabs.setBounds(140, 38, 508, 340);
		tabs.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		new Label(this, SWT.NONE);
		
		
		tabs.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				enableTopBar(tltmSave, tltmCut, tltmCopy, tltmPaste);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}});
	}

	private void buildCombo() {
		combo = new Combo(this, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		combo.setEnabled(false);
		combo.setBounds(140, 9, 196, 23);
		// TODO jump to method box
		/*
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				curTextBox.setSelection(curTextBox.getText().indexOf(
						combo.getText()));
			}
		});*/
	}

	private void buttonListeners(final Composite parent, ToolItem tltmNew,
			final ToolItem tltmSave, final ToolItem tltmCut,
			final ToolItem tltmCopy, final ToolItem tltmPaste, ToolItem tltmDelete, ToolItem tltmRename) {
		
		tltmRename.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(tree.getSelectionCount() != 1) return;
				InputDialog d = new InputDialog(parent.getShell(),
						"Rename Script File",
						"Choose the new name of the script file.",
						"modified_script_name.js",
						new ValidFileName());
				if(d.open() == Window.OK){
					for(TreeItem t : tree.getSelection()){
						for(Script s : ScriptManager.scripts){
							Script fScript = (Script)t.getData();
							if(s.getReference().equals(fScript.getReference())){
								fScript.getReference().renameTo(new File("./scripts/"+d.getValue()));
							}
						}
					}
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}});
		
		tltmDelete.addSelectionListener(new SelectionListener(){
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(tree.getSelection().length==0){return;}
				RoomManager.getMain().getDisplay().asyncExec(new Runnable(){

					@Override
					public void run() {
						NSAlertBox a = new NSAlertBox("Delete Scripts", "Are you sure you want to delete the selected files? You can always uncheck them to not use them!", SWT.ICON_QUESTION, SWT.YES|SWT.NO);
						if(a.go() == SWT.YES){
							for(TreeItem s : tree.getSelection()){
								((Script)s.getData()).getReference().delete();
							}
						}
							
					}});				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}});
		
		tltmNew.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				InputDialog d = new InputDialog(parent.getShell(), 
						"Create New Script File",
						"Choose the name of the new script file.",
						"newscript.js",
						new ValidFileName());
				if(d.open() == Window.OK){
					File f = new File("./scripts/"+d.getValue());
					try {
						if(!f.createNewFile()){
							System.err.println("Could not create file: "+f.getName());
						}
					} catch (IOException e1) {
						org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.ui.composites.script");
						fLog.error("IO Error in file creation.", e1);
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}});
		
		tltmSave.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				save();
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}});
		/*
		tltmCut.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				curEditor.cut();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {				
			}});

		tltmCopy.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				curEditor.copy();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {				
			}});
		
		tltmPaste.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				curEditor.paste();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {				
			}});*/
	}

	public void updateTreeItems() {
		
		RoomManager.getMain().getDisplay().asyncExec(new Runnable(){

			@Override
			public void run() {
				tree.clearAll(true);
				tree.removeAll();
				for (final Script s : ScriptManager.scripts) {
					TreeItem t = new TreeItem(tree, SWT.NONE);
					t.setChecked(s.isInUse());
					t.setText(s.getReference().getName());
					t.setData(s);
				}
			}});
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private void save() {
		CTabItem currentTab = tabs.getSelection();
		if(currentTab.getText().startsWith("*")){
			currentTab.setText(currentTab.getText().substring(1));
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(curScript.getReference()));
			bw.write(curEditor.getText());
			bw.close();
		} catch (IOException ex) {
			org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.ui.composites.script");
			fLog.error("IO Error in file saving.", ex);
		}
		curScript.readScript();
		updateFunctionList();
	}

	private void updateFunctionList() {
		combo.removeAll();
		for (String tag : curScript.getDescriptFunctions()) {
			combo.add(tag);
		}
	}

	private void enableTopBar(final ToolItem tltmSave, final ToolItem tltmCut,
			final ToolItem tltmCopy, final ToolItem tltmPaste) {
		//curTextBox = (StyledText) tabs.getSelection().getControl();
		combo.setEnabled(true);
		Script s = (Script)tabs.getSelection().getData();
		curScript = s;
		updateFunctionList();
		tltmSave.setEnabled(true);
		tltmCut.setEnabled(true);
		tltmCopy.setEnabled(true);
		tltmPaste.setEnabled(true);
	}

	private void changeTab(CTabItem c) {
		tabs.setSelection(c);
		
	}
}
