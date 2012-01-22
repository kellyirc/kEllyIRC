package ui.composites;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;

public class QuicklinksComposite extends Composite {
	private Table table;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public QuicklinksComposite(Composite parent, int style) {
		super(parent, style);
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 10, 203, 280);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnQuicklinkId = new TableColumn(table, SWT.NONE);
		tblclmnQuicklinkId.setWidth(100);
		tblclmnQuicklinkId.setText("Quicklink ID");
		
		TableColumn tblclmnQuicklinkUrl = new TableColumn(table, SWT.NONE);
		tblclmnQuicklinkUrl.setWidth(100);
		tblclmnQuicklinkUrl.setText("Quicklink URL");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
