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

import org.cts2.core.types.EntryState;
import org.dozer.DozerConverter;

/**
 * The Class IsActiveToEntryStateConverter.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IsActiveToEntryStateConverter extends DozerConverter<Boolean,EntryState>{

	/**
	 * Instantiates a new checks if is active to entry state converter.
	 */
	public IsActiveToEntryStateConverter() {
		super(Boolean.class, EntryState.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Boolean convertFrom(EntryState es, Boolean bl) {
		if(es == null){
			return null;
		}
		
		switch (es){
			case ACTIVE :{ return true ; }
			case INACTIVE :{ return true ; }
			default : { throw new IllegalArgumentException("Could not map EntryState"); }
		}
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public EntryState convertTo(Boolean bl, EntryState es) {
		if(bl == null){
			return null;
		}
		
		if(bl){
			return EntryState.ACTIVE;
		} else {
			return EntryState.INACTIVE;
		}
	}

}
