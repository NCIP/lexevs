
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