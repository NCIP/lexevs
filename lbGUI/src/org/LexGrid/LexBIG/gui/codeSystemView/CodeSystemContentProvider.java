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
package org.LexGrid.LexBIG.gui.codeSystemView;

import java.util.Arrays;
import java.util.Comparator;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content provider for the Code Systems SWT Table.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CodeSystemContentProvider implements IStructuredContentProvider {
	private LB_GUI lbGui_;
	private static Logger log = Logger.getLogger("LB_GUI_LOGGER");

	public CodeSystemContentProvider(LB_GUI lbGui) {
		lbGui_ = lbGui;
	}

	public Object[] getElements(Object arg0) {
		try {
			return getCodeSystems();
		} catch (LBInvocationException e) {
			log.error("Unexpected Error", e);
			return new String[] {};
		}
	}

	public void dispose() {
		// do nothing
	}

	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		currentCodingSchemeRenderings_ = null;
	}

	private CodingSchemeRendering[] currentCodingSchemeRenderings_ = null;

	private CodingSchemeRendering[] getCodeSystems()
			throws LBInvocationException {
		if (currentCodingSchemeRenderings_ == null) {
			if (lbGui_.getLbs() != null) {
				currentCodingSchemeRenderings_ = lbGui_.getLbs()
						.getSupportedCodingSchemes().getCodingSchemeRendering();
				Arrays.sort(currentCodingSchemeRenderings_,
						new CodingSchemeRenderingComparator());
			} else {
				currentCodingSchemeRenderings_ = new CodingSchemeRendering[] {};
			}

		}
		return currentCodingSchemeRenderings_;
	}

	private class CodingSchemeRenderingComparator implements
			Comparator<CodingSchemeRendering> {

		public int compare(CodingSchemeRendering o1, CodingSchemeRendering o2) {
			return o1.getCodingSchemeSummary().getFormalName()
					.compareToIgnoreCase(
							o2.getCodingSchemeSummary().getFormalName());
		}

	}

}