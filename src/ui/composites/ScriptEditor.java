package ui.composites;

import java.awt.BorderLayout;
import java.awt.Frame;

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

import scripting.ScriptManager;

public class ScriptEditor extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	RSyntaxTextArea textArea;
	
	public ScriptEditor(Composite parent) {
		
	    Frame frame = SWT_AWT.new_Frame(parent);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					String laf = UIManager.getSystemLookAndFeelClassName();
					UIManager.setLookAndFeel(laf);
				} catch (Exception e) {}
			}
		});

		this.setLayout(new BorderLayout());
		textArea = new RSyntaxTextArea();
		this.add(new RTextScrollPane(textArea));

		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
	    
	    JScrollPane scroll = new JScrollPane(this);
	    frame.add(scroll, BorderLayout.CENTER);
	}
	
	public void changeSyntaxStyle(String s) {
		textArea.setSyntaxEditingStyle(s);
		CompletionProvider provider = createCompletionProvider(s);

		AutoCompletion ac = new AutoCompletion(provider);
		ac.install(textArea);
	}

	private CompletionProvider createCompletionProvider(String s) {
		DefaultCompletionProvider provider = new DefaultCompletionProvider();
		for(String keyword : ScriptManager.globalKeywords) {
			provider.addCompletion(new BasicCompletion(provider, keyword));
		}
		switch(s) {
		case SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT:
			for(String keyword : ScriptManager.jsKeywords) {
				provider.addCompletion(new BasicCompletion(provider, keyword));
			}
			break;
		case SyntaxConstants.SYNTAX_STYLE_RUBY:
			for(String keyword : ScriptManager.rbKeywords) {
				provider.addCompletion(new BasicCompletion(provider, keyword));
			}
			break;
		case SyntaxConstants.SYNTAX_STYLE_PYTHON:
			for(String keyword : ScriptManager.pyKeywords) {
				provider.addCompletion(new BasicCompletion(provider, keyword));
			}
			break;
		}
		return provider;
	}

	public void setText(String script) {
		this.textArea.setText(script);
		
	}

	public String getText() {
		return textArea.getText();
	}
	
}
