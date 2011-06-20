package ui.composites;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import connection.Settings;

public class IgnoreListComposite extends Composite {
	private Table table;
	private TableEditor editor;
	Text nickEditor;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public IgnoreListComposite(Composite parent, int style) {
		super(parent, style);
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setBounds(10, 10, 111, 280);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNickname = new TableColumn(table, SWT.NONE);
		tblclmnNickname.setWidth(107);
		tblclmnNickname.setText("Nickname");
		
		editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		editor.minimumWidth = 50;
		
		Button btnAdd = new Button(this, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(nickEditor != null && !nickEditor.isDisposed())
				{
					nickEditor.dispose();
					editor.getItem().dispose();
				}
				TableItem i = new TableItem(table,SWT.NONE);
				nickEditor = new Text(table, SWT.NONE);
				nickEditor.addKeyListener(new KeyListener(){
					@Override
					public void keyPressed(KeyEvent e) {}
					@Override
					public void keyReleased(KeyEvent e) {
						if(e.character == SWT.CR) //when user presses enter
						{
							//if the field is empty, get rid of the item
							if(nickEditor.getText().equals(""))
							{
								editor.getItem().dispose();
								nickEditor.dispose();
							}
							//set item to text of editor
							else
							{
							editor.getItem().setText(0,nickEditor.getText());
							table.setSelection(editor.getItem());
							nickEditor.dispose();
							saveTable();
							}
						}
					}
				});
				nickEditor.addFocusListener(new FocusListener(){
					public void focusGained(FocusEvent e) {}
					//if the editor loses focus, DELETE EVERYTHING
					public void focusLost(FocusEvent e) {
						nickEditor.dispose();
						editor.getItem().dispose();
					}
					
				});
				nickEditor.selectAll();
				nickEditor.setFocus();
				editor.setEditor(nickEditor, i, 0);
			}
		});
		btnAdd.setBounds(127, 10, 75, 25);
		btnAdd.setText("Add");
		
		Button btnRemove = new Button(this, SWT.NONE);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.remove(table.getSelectionIndices());
				saveTable();
			}
		});
		btnRemove.setBounds(127, 41, 75, 25);
		btnRemove.setText("Remove");
		
		loadTable();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	private void loadTable()
	{
		ArrayList<String> nicksIgnored = Settings.getSettings().getNicksIgnored();
		for(String nick:nicksIgnored)
		{
			TableItem i = new TableItem(table, SWT.NONE);
			i.setText(0,nick);
		}
	}
	
	private void saveTable()
	{
		ArrayList<String> nicksIgnored = new ArrayList<String>();
		for(TableItem i:table.getItems())
		{
			nicksIgnored.add(i.getText(0));
		}
		Settings.getSettings().setNicksIgnored(nicksIgnored);
		Settings.writeToFile();
	}
	
}
