package ui.composites;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class IgnoreListComposite extends Composite {
	private Table table;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public IgnoreListComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 2);
		gd_table.widthHint = 159;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNickname = new TableColumn(table, SWT.NONE);
		tblclmnNickname.setWidth(175);
		tblclmnNickname.setText("Ignored Nickname");
		
		Button btnAdd = new Button(this, SWT.NONE);
		GridData gd_btnAdd = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnAdd.widthHint = 65;
		btnAdd.setLayoutData(gd_btnAdd);
		btnAdd.setText("Add");
		
		Button btnRemove = new Button(this, SWT.NONE);
		btnRemove.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRemove.setText("Delete");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
