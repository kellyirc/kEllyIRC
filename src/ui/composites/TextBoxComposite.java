package ui.composites;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import connection.Settings;

public class TextBoxComposite extends Composite
{
	private Text timestampFormatText;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TextBoxComposite(Composite parent, int style)
	{
		super(parent, style);
		setLayout(new GridLayout(3, false));
		
		Label lblTimestamp = new Label(this, SWT.NONE);
		lblTimestamp.setText("Timestamps");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		final Button btnEnableTimestamps = new Button(this, SWT.CHECK);
		btnEnableTimestamps.setText("Enable timestamps");
		btnEnableTimestamps.setSelection(Settings.getSettings().isTimestampsEnabled());
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		Label lblTimestampFormat = new Label(this, SWT.NONE);
		lblTimestampFormat.setText("Timestamp format: ");
		
		timestampFormatText = new Text(this, SWT.BORDER);
		timestampFormatText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		timestampFormatText.setText(Settings.getSettings().getTimestampFormatPattern());
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		Button btnSave = new Button(this, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Settings.getSettings().setTimestampsEnabled(btnEnableTimestamps.getSelection());
				Settings.getSettings().setTimestampFormatPattern(timestampFormatText.getText());
			}
		});
		btnSave.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, true, 1, 1));
		btnSave.setText("Save");

	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}
