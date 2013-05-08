/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.gui.displayResults;

import java.awt.event.MouseEvent;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;

import prefuse.controls.FocusControl;
import prefuse.visual.VisualItem;

/**
 * Focus Listener for the graph to allow me to back select to a swt list..
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class MyFocusControl extends FocusControl {
	DisplayCodedNodeSet dcns_;

	public MyFocusControl(int clicks, String act, DisplayCodedNodeSet dcns) {
		super(clicks, act);
		dcns_ = dcns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see prefuse.controls.FocusControl#itemClicked(prefuse.visual.VisualItem,
	 * java.awt.event.MouseEvent)
	 */
	@Override
	public void itemClicked(VisualItem item, MouseEvent e) {
		super.itemClicked(item, e);

		ResolvedConceptReference rcr = (ResolvedConceptReference) item
				.get("RCR");
		if (rcr != null) {
			dcns_.graphItemSelected(rcr);
		}
	}
}