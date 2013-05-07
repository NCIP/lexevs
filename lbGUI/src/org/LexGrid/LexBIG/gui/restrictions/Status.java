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
package org.LexGrid.LexBIG.gui.restrictions;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;

/**
 * Holder to store the RestrictToStatus restrictions.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class Status extends Restriction {
	public String conceptStatus;
	public ActiveOption activeOption;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (activeOption != null) {
			sb.append(activeOption.toString() + ", ");
		}
		if (conceptStatus != null && conceptStatus.length() > 0) {
			sb.append(conceptStatus + ", ");

		}
		return "Status '" + sb.toString() + "'";
	}

}