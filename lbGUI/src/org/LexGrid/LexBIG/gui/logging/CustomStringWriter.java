
package org.LexGrid.LexBIG.gui.logging;

import java.io.StringWriter;

import org.eclipse.swt.custom.StyledText;

/**
 * A StringWriter implementation that writes to a SWT StyledText Composite. Used
 * by the log viewer.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 1052 $ checked in on $Date: 2006-01-30
 *          11:42:24 +0000 (Mon, 30 Jan 2006) $
 */
public class CustomStringWriter extends StringWriter {
	private StyledText area_;

	public CustomStringWriter(StyledText area) {
		super();
		area_ = area;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Writer#write(char[], int, int)
	 */
	@Override
	public void write(char[] cbuf, int off, int len) {
		super.write(cbuf, off, len);
		update();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Writer#write(int)
	 */
	@Override
	public void write(int c) {
		super.write(c);
		update();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Writer#write(java.lang.String, int, int)
	 */
	@Override
	public void write(String str, int off, int len) {
		super.write(str, off, len);
		update();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Writer#write(java.lang.String)
	 */
	@Override
	public void write(String str) {
		super.write(str);
		update();
	}

	private void update() {
	    if(! area_.isDisposed()) {
	        area_.getDisplay().asyncExec(new Runnable() {
	            public void run() {
	                area_.append(CustomStringWriter.this.getBuffer().substring(
	                        area_.getText().length()));
	                area_.setSelection(area_.getText().length());
	            }
	        });
	    }
	}
}