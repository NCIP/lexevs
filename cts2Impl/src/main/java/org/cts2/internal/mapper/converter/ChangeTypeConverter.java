/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.internal.mapper.converter;

import org.LexGrid.versions.types.ChangeType;
import org.dozer.DozerConverter;

/**
 * The Class ChangeTypeConverter. LexGrid ChangeType <--> CTS2 ChangeType
 *
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class ChangeTypeConverter extends DozerConverter<ChangeType,org.cts2.core.types.ChangeType> {

	public ChangeTypeConverter() {
		super(ChangeType.class, org.cts2.core.types.ChangeType.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public ChangeType convertFrom(
			org.cts2.core.types.ChangeType cts2ChangeType,
			ChangeType lgChangeType) {
		switch (cts2ChangeType) {
			case CREATE : {
				return ChangeType.NEW;
			}
			case UPDATE :  {
				return ChangeType.MODIFY;
			}
			case DELETE : {
				return ChangeType.REMOVE;
			}
			case METADATA : {
				return ChangeType.VERSIONABLE;
			}
			default : {
				throw new RuntimeException(cts2ChangeType + " cannot be mapped.");
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public org.cts2.core.types.ChangeType convertTo(ChangeType lgChangeType, org.cts2.core.types.ChangeType cts2ChangeType) {
		switch (lgChangeType) {
			case NEW : {
				return org.cts2.core.types.ChangeType.CREATE;
			}
			case MODIFY :  {
				return org.cts2.core.types.ChangeType.UPDATE;
			}
			case REMOVE : {
				return org.cts2.core.types.ChangeType.DELETE;
			}
			case VERSIONABLE : {
				return org.cts2.core.types.ChangeType.METADATA;
			}
			default : {
				throw new RuntimeException(lgChangeType + " cannot be mapped.");
			}
		}
	}
}
