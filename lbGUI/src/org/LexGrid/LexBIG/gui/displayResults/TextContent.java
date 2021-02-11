
package org.LexGrid.LexBIG.gui.displayResults;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * This class makes it easier to display formatted text in a SWT StyledText. You
 * can mark up your text with tags like <b> </b> - basic html tags - and it will
 * create the necessary style ranges for the StyledText object.
 * 
 * Supported Tags: (pre is not supported - it is just here so you can read this
 * from the javadocs)
 * 
 * <pre>
 * &lt;b&gt; - bold
 * &lt;i&gt; - italic
 * &lt;u&gt; - underline
 * &lt;so&gt; - strikeout
 * &lt;red&gt; - red font color
 * &lt;green&gt; - green font color
 * &lt;blue&gt; - blue font color
 * </pre>
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class TextContent {

	protected String content;

	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
     * 
     */
	public TextContent(Display display) {
		// these don't have to be disposed because they are system colors.
		red = display.getSystemColor(SWT.COLOR_RED);
		green = display.getSystemColor(SWT.COLOR_GREEN);
		blue = display.getSystemColor(SWT.COLOR_BLUE);
		black = display.getSystemColor(SWT.COLOR_BLACK);
		white = display.getSystemColor(SWT.COLOR_WHITE);
	}

	protected int nesting = 0;

	protected List<StyleRange> styleRanges = new ArrayList<StyleRange>();

	boolean inItalics = false, inBold = false, inUnderline = false,
			inStrikeout = false;
	boolean inRed = false, inBlue = false, inGreen = false;

	protected Color black, white, red, green, blue;

	protected StyleRange curRange;
	protected int rangeStyle = SWT.NORMAL;

	protected void startRange(int posn) {
		if (curRange != null) {
			endRange(posn);
		}
		curRange = new StyleRange();
		curRange.start = posn;
	}

	protected void endRange(int posn) {
		if (curRange != null) {
			curRange.length = posn - curRange.start;

			if (inItalics) {
				rangeStyle |= SWT.ITALIC;
			} else {
				rangeStyle &= ~SWT.ITALIC;
			}
			if (inBold) {
				rangeStyle |= SWT.BOLD;
			} else {
				rangeStyle &= ~SWT.BOLD;
			}
			curRange.fontStyle = rangeStyle;
			curRange.underline = inUnderline;
			curRange.strikeout = inStrikeout;
			if (inRed) {
				curRange.foreground = red;
			} else if (inGreen) {
				curRange.foreground = green;
			} else if (inBlue) {
				curRange.foreground = blue;
			} else {
				curRange.foreground = black;
			}
			if (curRange.background == null) {
				curRange.background = white;
			}
			if (curRange.length > 0) {
				styleRanges.add(curRange);
			}
		}
		curRange = null;
		if (nesting > 0) {
			startRange(posn);
		}
	}

	public String toPlainText() {
		int posn = 0;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < content.length();) {
			char c = content.charAt(i);
			if (c == '<') {
				if ("<>".equals(content.substring(i, i + 2))) {
					sb.append('<');
					i += 2;
					nesting++;
				} else if ("<i>".equals(content.substring(i, i + 3))) {
					startRange(posn);
					inItalics = true;
					i += 3;
					nesting++;
				} else if ("<b>".equals(content.substring(i, i + 3))) {
					startRange(posn);
					inBold = true;
					i += 3;
					nesting++;
				} else if ("<u>".equals(content.substring(i, i + 3))) {
					startRange(posn);
					inUnderline = true;
					i += 3;
					nesting++;
				} else if ("<so>".equals(content.substring(i, i + 4))) {
					startRange(posn);
					inStrikeout = true;
					i += 4;
					nesting++;
				} else if ("<red>".equals(content.substring(i, i + 5))) {
					startRange(posn);
					inRed = true;
					i += 5;
					nesting++;
				} else if ("<green>".equals(content.substring(i, i + 7))) {
					startRange(posn);
					inGreen = true;
					i += 7;
					nesting++;
				} else if ("<blue>".equals(content.substring(i, i + 6))) {
					startRange(posn);
					inBlue = true;
					i += 6;
					nesting++;
				} else if ("</i>".equals(content.substring(i, i + 4))) {
					endRange(posn);
					inItalics = false;
					i += 4;
					nesting--;
				} else if ("</b>".equals(content.substring(i, i + 4))) {
					endRange(posn);
					inBold = false;
					i += 4;
					nesting--;
				} else if ("</u>".equals(content.substring(i, i + 4))) {
					endRange(posn);
					inUnderline = false;
					i += 4;
					nesting--;
				} else if ("</so>".equals(content.substring(i, i + 5))) {
					endRange(posn);
					inStrikeout = false;
					i += 5;
					nesting--;
				} else if ("</red>".equals(content.substring(i, i + 6))) {
					endRange(posn);
					inRed = false;
					i += 6;
					nesting--;
				} else if ("</green>".equals(content.substring(i, i + 8))) {
					endRange(posn);
					inGreen = false;
					i += 8;
					nesting--;
				} else if ("</blue>".equals(content.substring(i, i + 7))) {
					endRange(posn);
					inBlue = false;
					i += 7;
					nesting--;
				} else {
					while (true) {
						if ((c = content.charAt(++i)) == '>') {
							break;
						}
					}
				}
			} else {
				sb.append(c);
				i++;
				posn++;
			}
		}
		endRange(posn);
		return sb.toString();
	}

	public StyleRange[] getStyleRanges() {
		return (StyleRange[]) styleRanges.toArray(new StyleRange[styleRanges
				.size()]);
	}
}