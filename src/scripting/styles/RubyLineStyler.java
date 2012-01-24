/*
 * @author Kyle Kemp
 */
package scripting.styles;

import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import scripting.ScriptManager;

/**
 * The Class RubyLineStyler.
 */
public class RubyLineStyler extends LineStyler {

	/**
	 * Instantiates a new ruby line styler.
	 */
	public RubyLineStyler() {
		initializeColors();
		scanner = new RubyScanner();
	}

	/* (non-Javadoc)
	 * @see scripting.styles.LineStyler#initializeColors()
	 */
	protected void initializeColors() {
		Display display = Display.getDefault();
		colors = new Color[] { 
				new Color(display, new RGB(0, 0, 0)),
				new Color(display, new RGB(125, 125, 0)),
				new Color(display, new RGB(125, 0, 125)), 
				new Color(display, new RGB(0, 0, 255)) 
		};
		tokenColors = new int[MAXIMUM_TOKEN];
		tokenColors[WORD] = 0;
		tokenColors[WHITE] = 0;
		tokenColors[KEY] = 3;
		tokenColors[COMMENT] = 1;
		tokenColors[STRING] = 2;
		tokenColors[OTHER] = 0;
		tokenColors[NUMBER] = 0;
	}
	
	/* (non-Javadoc)
	 * @see scripting.styles.LineStyler#parseBlockComments(java.lang.String)
	 */
	public void parseBlockComments(String text) {
		blockComments = new Vector<int[]>();
		boolean blkComment = false;
		String[] file = text.split("[\\n]");
		
		int count=0;
		int curVector=0;
		
		for(String s : file) {
			if(s.contains("=begin") && !blkComment){
				blockComments.add(new int[2]);
				blockComments.get(curVector)[0] = count;
				blkComment = true;
			}
			count += s.length();
			if(blkComment && s.contains("=end")){
				blockComments.get(curVector)[1] = count;
				curVector++;
				blkComment = false;
			}
			count++;
		}
	}

	/**
	 * The Class RubyScanner.
	 */
	protected class RubyScanner extends LanguageScanner{

		/**
		 * Initialize the lookup table.
		 */
		protected void initialize() {
			fgKeys = new Hashtable<String, Integer>();
			Integer k = new Integer(KEY);
			for (int i = 0; i < ScriptManager.rbKeywords.length; i++)
				fgKeys.put(ScriptManager.rbKeywords[i], k);
		}

		/* (non-Javadoc)
		 * @see scripting.styles.LanguageScanner#nextToken()
		 */
		public int nextToken() {
			int c;
			fStartToken = fPos;
			while (true) {
				switch (c = read()) {
				case EOF:
					return EOF;
				case '#': // comment
					while (true) {
						c = read();
						if ((c == EOF) || (c == EOL)) {
							unread(c);
							return COMMENT;
						}
					}
				case '\'': // char const
					while (true) {
						c = read();
						switch (c) {
						case '\'':
							return STRING;
						case EOF:
							unread(c);
							return STRING;
						case '\\':
							c = read();
							break;
						}
					}

				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					do {
						c = read();
					} while (Character.isDigit((char) c));
					unread(c);
					return NUMBER;
				default:
					if (Character.isWhitespace((char) c)) {
						do {
							c = read();
						} while (Character.isWhitespace((char) c));
						unread(c);
						return WHITE;
					}
					if (Character.isJavaIdentifierStart((char) c) && c!='=') {
						fBuffer.setLength(0);
						do {
							fBuffer.append((char) c);
							c = read();
						} while (Character.isJavaIdentifierPart((char) c));
						unread(c);
						Integer i = fgKeys.get(fBuffer.toString());
						if (i != null)
							return i.intValue();
						return WORD;
					}
					return OTHER;
				}
			}
		}
	}

}