package ui.composites;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;

import shared.Quicklinks;

public class QuicklinksComposite extends Composite {
	private Table table;
	private Text name;
	private Text url;
	
	//TODO when parsing quicklinks, replace the text in the message with the url, but don't remove the old code for quicklink parsing

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public QuicklinksComposite(Composite parent, int style) {
		super(parent, style);

		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 10, 430, 227);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnQuicklinkId = new TableColumn(table, SWT.NONE);
		tblclmnQuicklinkId.setWidth(89);
		tblclmnQuicklinkId.setText("Quicklink ID");

		TableColumn tblclmnQuicklinkUrl = new TableColumn(table, SWT.NONE);
		tblclmnQuicklinkUrl.setWidth(319);
		tblclmnQuicklinkUrl.setText("Quicklink URL");

		name = new Text(this, SWT.BORDER);
		name.setBounds(10, 269, 135, 21);

		url = new Text(this, SWT.BORDER);
		url.setBounds(151, 269, 249, 21);

		Button btnAdd = new Button(this, SWT.NONE);
		btnAdd.setBounds(406, 265, 34, 25);
		btnAdd.setText("Add");
		btnAdd.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (name.getText() != "" && url.getText() != "") {
					addQuickLink(name.getText(), url.getText());
					name.setText("");
					url.setText("");
				}
			}

			private void addQuickLink(String text, String text2) {
				Quicklinks.addLink(text, text2);
				for (TableItem ti : table.getItems()) {
					if (ti.getData().equals(text)) {
						table.remove(table.indexOf(ti));
					}
				}
				loadTable();
			}
		});

		Label lblQuicklinkPreface = new Label(this, SWT.NONE);
		lblQuicklinkPreface.setBounds(10, 248, 102, 15);
		lblQuicklinkPreface.setText("Quicklink Preface");

		Label lblUrlmarkUser = new Label(this, SWT.NONE);
		lblUrlmarkUser.setText("URL (mark user input with %INPUT%)");
		lblUrlmarkUser.setBounds(151, 248, 249, 15);

		loadTable();
	}

	private void loadTable() {
		for (String cs : Quicklinks.getQuicklinks().keySet()) {
			TableItem i = new TableItem(table, SWT.NONE);
			i.setText(new String[] { cs, Quicklinks.getQuicklinks().get(cs) });
			i.setData(cs);
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
