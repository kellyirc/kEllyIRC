/*
 * @author Kyle Kemp
 */
package scripting.styles;

import java.util.Hashtable;

/**
 * The Class LanguageScanner.
 */
public abstract class LanguageScanner {

	/** The fg keys. */
	protected Hashtable<String, Integer> fgKeys = null;
	
	/** The buffer. */
	protected StringBuffer fBuffer = new StringBuffer();
	
	/** The doc. */
	protected String fDoc;
	
	/** The pos. */
	protected int fPos;
	
	/** The end. */
	protected int fEnd;
	
	/** The start token. */
	protected int fStartToken;
	
	/** The eof seen. */
	protected boolean fEofSeen = false;	
	
	/**
	 * Instantiates a new language scanner.
	 */
	public LanguageScanner() {
		initialize();
	}
	
	/**
	 * Initialize.
	 */
	protected abstract void initialize();

	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public final int getLength() {
		return fPos - fStartToken;
	}
	
	/**
	 * Gets the start offset.
	 *
	 * @return the start offset
	 */
	public final int getStartOffset() {
		return fStartToken;
	}
	
	/**
	 * Next token.
	 *
	 * @return the int
	 */
	public abstract int nextToken();

	/**
	 * Read.
	 *
	 * @return the int
	 */
	protected int read() {
		if (fPos <= fEnd) {
			return fDoc.charAt(fPos++);
		}
		return LineStyler.EOF;
	}

	/**
	 * Sets the range.
	 *
	 * @param text the new range
	 */
	public void setRange(String text) {
		fDoc = text;
		fPos = 0;
		fEnd = fDoc.length() - 1;
	}

	/**
	 * Unread.
	 *
	 * @param c the c
	 */
	protected void unread(int c) {
		if (c != LineStyler.EOF)
			fPos--;
	}
}
