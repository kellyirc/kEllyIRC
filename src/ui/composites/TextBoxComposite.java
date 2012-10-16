/*
 * @author Kyle Kemp
 */
package ui.composites;

import hexapixel.cache.ImageCache;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import shared.NSAlertBox;
import connection.Settings;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;

/**
 * The Class TextBoxComposite.
 */
public class TextBoxComposite extends Composite
{
	
	/** The timestamp format text. */
	private Text timestampFormatText;
	
	/** The btn enable timestamps. */
	private Button btnEnableTimestamps;
	private SimpleDateFormat sdf;
	private Label lblPreview;
	private Text messageFormatText;
	private GridData gd_btnMessageHelp;

	/**
	 * Create the composite.
	 *
	 * @param parent the parent
	 * @param style the style
	 */
	public TextBoxComposite(Composite parent, int style)
	{
		super(parent, style);
		setLayout(new GridLayout(3, false));
		
		Label lblTimestamp = new Label(this, SWT.NONE);
		lblTimestamp.setText("Timestamps");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		btnEnableTimestamps = new Button(this, SWT.CHECK);
		btnEnableTimestamps.setText("Enable timestamps");
		btnEnableTimestamps.setSelection(Settings.getSettings().isTimestampsEnabled());
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		Label lblTimestampFormat = new Label(this, SWT.NONE);
		lblTimestampFormat.setText("Timestamp format: ");
		
		timestampFormatText = new Text(this, SWT.BORDER);
		timestampFormatText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(Character.isAlphabetic(e.character) && 
						!"GyYMwWDdFEuaHkKhmsSzZX".contains(""+e.character))
					e.doit = false;
			}
		});
		timestampFormatText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				sdf.applyPattern(timestampFormatText.getText());
				lblPreview.setText(sdf.format(new Date()));
			}
		});
		GridData gd_timestampFormatText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_timestampFormatText.widthHint = 97;
		timestampFormatText.setLayoutData(gd_timestampFormatText);
		timestampFormatText.setText(Settings.getSettings().getTimestampFormatPattern());
		
		Button btnTimestampHelp = new Button(this, SWT.NONE);
		btnTimestampHelp.setImage(ImageCache.getImage("info_small.png"));
		GridData gd_btnTimestampHelp = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTimestampHelp.heightHint = 28;
		gd_btnTimestampHelp.widthHint = 28;
		gd_btnTimestampHelp.verticalIndent = -1; //one pixel up because Darkblizer
		btnTimestampHelp.setLayoutData(gd_btnTimestampHelp);
		btnTimestampHelp.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				super.widgetSelected(e);
				NSAlertBox infoAlert = new NSAlertBox("Timestamp Formatting","The formatting is based on the SimpleDateFormat in Java. Here are some common pattern letters:\n\nY = Year\nM = Month in year\nd = Day in month\nE = Day name in week\na = Am/pmmarker\nH = Hour in day(0-23)\nh = Hour in am/pm(1-12)\nm = Minute in hour\ns = Second inminute\nS = Millisecond\n\nFor more information, check http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html",SWT.ICON_QUESTION|SWT.OK);
				infoAlert.go();
				
			}
		});
		
		Label lblPreview1 = new Label(this, SWT.NONE);
		lblPreview1.setText("Preview: ");
		
		lblPreview = new Label(this, SWT.NONE);
		lblPreview.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		sdf = new SimpleDateFormat(timestampFormatText.getText());
		lblPreview.setText(sdf.format(new Date()));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		Label lblAppearance = new Label(this, SWT.NONE);
		lblAppearance.setText("Appearance");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		Label lblMessageFormat = new Label(this, SWT.NONE);
		lblMessageFormat.setText("Message format:");
		
		messageFormatText = new Text(this, SWT.BORDER);
		messageFormatText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		messageFormatText.setText(Settings.getSettings().getMessageFormat());
		
		Button btnMessageHelp = new Button(this, SWT.NONE);
		btnMessageHelp.setImage(ImageCache.getImage("info_small.png"));
		GridData gd_btnMessageHelp;
		gd_btnMessageHelp = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnMessageHelp.widthHint = 28;
		gd_btnMessageHelp.heightHint = 28;
		btnMessageHelp.setLayoutData(gd_btnMessageHelp);
		btnMessageHelp.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				super.widgetSelected(e);
				NSAlertBox infoAlert = new NSAlertBox("Message Formatting","These are the components you can use in the formatting of your message output:\nChannel - %chan%\nUser Level(op, voice, etc) - %lvl%\nMessage Content- %msg%\nNickname - %nick%\nTimestamp - %time%",SWT.ICON_QUESTION|SWT.OK);
				infoAlert.go();
				
			}
		});
//		Button btnSave = new Button(this, SWT.NONE);
//		btnSave.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				
//			}
//		});
//		btnSave.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, true, 1, 1));
//		btnSave.setText("Save");

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose()
	{
		if(timestampFormatText.getText().isEmpty())
			timestampFormatText.setText("[hh:mm:ss]");
		if(messageFormatText.getText().isEmpty())
			messageFormatText.setText("%time% <%nick%> %msg%");
		
		Settings.getSettings().setTimestampsEnabled(btnEnableTimestamps.getSelection());
		Settings.getSettings().setTimestampFormatPattern(timestampFormatText.getText());
		Settings.getSettings().setMessageFormat(messageFormatText.getText());
		Settings.writeToFile();
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Composite#checkSubclass()
	 */
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}

