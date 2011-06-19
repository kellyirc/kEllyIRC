package scripting.styles;

import java.util.Hashtable;

public abstract class LanguageScanner {

	protected Hashtable<String, Integer> fgKeys = null;
	protected StringBuffer fBuffer = new StringBuffer();
	protected String fDoc;
	protected int fPos;
	protected int fEnd;
	protected int fStartToken;
	protected boolean fEofSeen = false;	
	
	public LanguageScanner() {
		initialize();
	}
	
	protected abstract void initialize();

	public final int getLength() {
		return fPos - fStartToken;
	}
	
	public final int getStartOffset() {
		return fStartToken;
	}
	
	public abstract int nextToken();

	protected int read() {
		if (fPos <= fEnd) {
			return fDoc.charAt(fPos++);
		}
		return LineStyler.EOF;
	}

	public void setRange(String text) {
		fDoc = text;
		fPos = 0;
		fEnd = fDoc.length() - 1;
	}

	protected void unread(int c) {
		if (c != LineStyler.EOF)
			fPos--;
	}
}
