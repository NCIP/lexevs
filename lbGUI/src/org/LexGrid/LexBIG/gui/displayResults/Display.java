
package org.LexGrid.LexBIG.gui.displayResults;

import prefuse.Visualization;

/**
 * Hack class to fix bad behavior.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class Display extends prefuse.Display {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	public Display(Visualization visualization) {
		super(visualization);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see prefuse.Display#registerDefaultCommands()
	 */
	@Override
	protected void registerDefaultCommands() {
		// make this method do nothing. Its trying to register key stroke
		// methods
		// for swing, which don't work, and its trying to create a JFile chooser
		// which is really freaking slow.
	}

}