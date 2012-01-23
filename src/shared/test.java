package shared;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;

public class test {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			test window = new test();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		
		Composite composite = new Composite(shell, SWT.EMBEDDED | SWT.NO_BACKGROUND);
		composite.setBounds(0, 0, 434, 262);
		new ScriptEditor(composite, new File("aliases.js").toString());
	}

	class ScriptEditor extends JPanel {

		private static final long serialVersionUID = 1L;

		RSyntaxTextArea textArea;

		public ScriptEditor(Composite parent, String s) {

			Frame frame = SWT_AWT.new_Frame(parent);

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						String laf = UIManager.getSystemLookAndFeelClassName();
						UIManager.setLookAndFeel(laf);
					} catch (Exception e) {
					}
				}
			});

			this.setLayout(new BorderLayout());
			textArea = new RSyntaxTextArea();
			textArea.setText(s);
			// textArea.setLineWrap(true);
			// textArea.setWrapStyleWord(true);
			RTextScrollPane pane = new RTextScrollPane(textArea);
			this.add(pane);

			textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);

			JScrollPane scroll = new JScrollPane(this);
			frame.add(scroll, BorderLayout.CENTER);
		}

		public void changeSyntaxStyle(String s) {
			// textArea.setSyntaxEditingStyle(determineScriptType(s));
			CompletionProvider provider = createCompletionProvider();

			AutoCompletion ac = new AutoCompletion(provider);
			ac.install(textArea);

		}

		private CompletionProvider createCompletionProvider() {
			DefaultCompletionProvider provider = new DefaultCompletionProvider();
			String[] keywords = { "cake" };
			for (String keyword : keywords) {
				provider.addCompletion(new BasicCompletion(provider, keyword));
			}
			return provider;
		}

	}
}
