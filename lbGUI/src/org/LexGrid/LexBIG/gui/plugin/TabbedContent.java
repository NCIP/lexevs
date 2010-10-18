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
package org.LexGrid.LexBIG.gui.plugin;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * This interface allows an implementor to add a custom tab to the lower right
 * hand palette of tabs.
 * 
 * Example usage: <code>
        // create a tab with a checkbox. The checkbox text is whatever concept is selected in the UI
        TabbedContent tc = new TabbedContent() {
  		    private Composite c;
      	    private Button b;
        	public void conceptSelected(ResolvedConceptReference rcr) {
        		b.setText(rcr.getConceptCode() + ": " + rcr.getEntityDescription().getContent());
        	}
        	public Control getControl() {
        		if (b == null) {
        			b = new Button(c, SWT.CHECK);
        		}
        		return b;
        	}
        	public String getTabName() {
        		return "Checkbox Tab";
        	}
        	public void setParentComposite(Composite c) {
        		this.c = c;
        	}
        };

 * </code>
 * 
 * @author <A HREF="mailto:leisch.jason@mayo.edu">Jason Leisch</A>
 */
public interface TabbedContent {
	/**
	 * Notifies the implentor that the concept identified by rcr has been
	 * selected in the left-side pane of the UI.
	 * 
	 * @param rcr
	 *            the ResolvedConceptReference that has been selected in the
	 *            left side pane of the UI.
	 */
	void conceptSelected(ResolvedConceptReference rcr);

	/**
	 * Returns the name of the tab.
	 */
	String getTabName();

	/**
	 * Returns the Control that is placed in the tab
	 */
	Control getControl();

	/**
	 * This method will be called before getControl so that the implementing
	 * class can create a control that has the correct parent. If the Control
	 * returned by the getControl method is not created with "c" as its parent,
	 * the control will not show up in the interface.
	 * 
	 * @param c
	 *            the Composite that is expected to be the parent of the control
	 *            retrieved from getControl.
	 */
	void setParentComposite(Composite c);
}