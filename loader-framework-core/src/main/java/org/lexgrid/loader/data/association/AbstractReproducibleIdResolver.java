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
package org.lexgrid.loader.data.association;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

/**
 * The Class AbstractReproducibleKeyResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractReproducibleIdResolver<T> implements AssociationInstanceIdResolver<T>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.association.AssociationInstanceIdResolver#resolveMultiAttributesKey(java.lang.Object)
	 */
	public abstract String resolveAssociationInstanceId(T key);
	
    /**
     * Generate key.
     * 
     * @param basis the basis
     * 
     * @return the string
     */
    protected  String generateKey(String... basis) {
        MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error Creating Key Generator");
		}
        md.reset();
        for (int i = 0; i < basis.length; i++)
            if (basis[i] != null)
                md.update(basis[i].getBytes());
        byte[] bytes = md.digest();
        return String.valueOf(Hex.encodeHex(bytes));
    }
}