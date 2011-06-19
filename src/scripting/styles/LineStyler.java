package scripting.styles;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;

public abstract class LineStyler implements LineStyleListener {

	protected LanguageScanner scanner;
	
	protected int[] tokenColors;
	protected Color[] colors;
	protected Vector<int[]> blockComments = new Vector<int[]>();

	public static final int EOF = -1;
	public static final int EOL = 10;

	public static final int WORD = 0;
	public static final int WHITE = 1;
	public static final int KEY = 2;
	public static final int COMMENT = 3;
	public static final int STRING = 5;
	public static final int OTHER = 6;
	public static final int NUMBER = 7;

	public static final int MAXIMUM_TOKEN = 8;
	
	@Override
	public void lineGetStyle(LineStyleEvent event) {
		Vector<StyleRange> styles = new Vector<StyleRange>();
		int token;
		StyleRange lastStyle;
		// If the line is part of a block comment, create one style for the
		// entire line.
		if (inBlockComment(event.lineOffset,
				event.lineOffset + event.lineText.length())) {
			styles.addElement(new StyleRange(event.lineOffset, event.lineText
					.length(), getColor(COMMENT), null));
			event.styles = new StyleRange[styles.size()];
			styles.copyInto(event.styles);
			return;
		}
		Color defaultFgColor = ((Control) event.widget).getForeground();
		scanner.setRange(event.lineText);
		token = scanner.nextToken();
		while (token != EOF) {
			if (token == OTHER) {
				// do nothing for non-colored tokens
			} else if (token != WHITE) {
				Color color = getColor(token);
				// Only create a style if the token color is different than the
				// widget's default foreground color and the token's style is
				// not
				// bold. Keywords are bolded.
				if ((!color.equals(defaultFgColor)) || (token == KEY)) {
					StyleRange style = new StyleRange(scanner.getStartOffset()
							+ event.lineOffset, scanner.getLength(), color,
							null);
					if (token == KEY) {
						style.fontStyle = SWT.BOLD;
					}
					if (styles.isEmpty()) {
						styles.addElement(style);
					} else {
						// Merge similar styles. Doing so will improve
						// performance.
						lastStyle = styles.lastElement();
						if (lastStyle.similarTo(style)
								&& (lastStyle.start + lastStyle.length == style.start)) {
							lastStyle.length += style.length;
						} else {
							styles.addElement(style);
						}
					}
				}
			} else if ((!styles.isEmpty())
					&& ((lastStyle = styles.lastElement()).fontStyle == SWT.BOLD)) {
				int start = scanner.getStartOffset() + event.lineOffset;
				lastStyle = styles.lastElement();
				// A font style of SWT.BOLD implies that the last style
				// represents a java keyword.
				if (lastStyle.start + lastStyle.length == start) {
					// Have the white space take on the style before it to
					// minimize the number of style ranges created and the
					// number of font style changes during rendering.
					lastStyle.length += scanner.getLength();
				}
			}
			token = scanner.nextToken();
		}
		event.styles = new StyleRange[styles.size()];
		styles.copyInto(event.styles);
	}
	
	protected abstract void initializeColors();

	protected void disposeColors() {
		for (int i = 0; i < colors.length; i++) {
			colors[i].dispose();
		}
	}

	protected final Color getColor(int type) {
		if (type < 0 || type >= tokenColors.length) {
			return null;
		}
		return colors[tokenColors[type]];
	}

	protected final boolean inBlockComment(int start, int end) {
		for (int i = 0; i < blockComments.size(); i++) {
			int[] offsets = blockComments.elementAt(i);
			// start of comment in the line
			if ((offsets[0] >= start) && (offsets[0] <= end)){
				return true;
			}
			// end of comment in the line
			if ((offsets[1] >= start) && (offsets[1] <= end)){
				return true;
			}
			if ((offsets[0] <= start) && (offsets[1] >= end)){
				return true;
			}
		}
		return false;
	}

	
	public abstract void parseBlockComments(String script);
}
