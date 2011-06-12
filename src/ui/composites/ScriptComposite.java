package ui.composites;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;

import scripting.Script;
import scripting.ScriptManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;


public class ScriptComposite extends Composite {

	
	JavaLineStyler lineStyler = new JavaLineStyler();
	private StyledText styledText;
	private Combo combo;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ScriptComposite(Composite parent, int style) {
		super(parent, style);

		combo = new Combo(this, SWT.NONE);
		combo.setEnabled(false);
		combo.setBounds(140, 9, 196, 23);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				styledText.setSelection(styledText.getText().indexOf(
						combo.getText()));
			}
		});

		Button btnSave = new Button(this, SWT.NONE);
		btnSave.setEnabled(false);
		btnSave.setBounds(573, 7, 75, 25);
		btnSave.setText("Save");

		styledText = new StyledText(this, SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);
		styledText.setFont(SWTResourceManager.getFont("Courier New", 9,
				SWT.NORMAL));
		styledText.setBounds(140, 38, 508, 340);

		final CheckboxTreeViewer checkboxTreeViewer = new CheckboxTreeViewer(
				this, SWT.BORDER);
		Tree tree = checkboxTreeViewer.getTree();
		tree.setBounds(10, 9, 124, 369);

		addTreeItems(tree);

		styledText.addLineStyleListener(lineStyler);
		
		styledText.addKeyListener(new KeyListener() {

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

		checkboxTreeViewer.getTree().addListener(SWT.MouseDoubleClick,
				new Listener() {

					@Override
					public void handleEvent(Event event) {
						Point point = new Point(event.x, event.y);
						final TreeItem item = checkboxTreeViewer.getTree()
								.getItem(point);
						if (item != null) {
							Script s = (Script) item.getData();
							styledText.setText(s.getScript());
							
							combo.setEnabled(true);
							combo.removeAll();
							for (String tag : s.getDescriptFunctions()) {
								combo.add(tag);
							}
							
							lineStyler.parseBlockComments(s.getScript());
						}
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
