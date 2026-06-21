
package org.lexevs.system.utility;

/*import static org.bouncycastle.util.encoders.Base64.decode;
import static org.bouncycastle.util.encoders.Base64.encode;*/

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.castor.core.util.Base64Encoder;
import org.lexevs.logging.Logger;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

/**
 * The Class CryptoUtility.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public final class CryptoUtility {
    
    /**
     * Instantiates a new crypto utility.
     */
    CryptoUtility() {}
 
    /** Internal Key. */
    protected static char[] key = "MayoClinic".toCharArray();
    
    /** The salt. */
    private static byte[] salt = "LEX-GRID".getBytes();
    
    /** The log. */
    private static Logger log = new Logger(); 
    
    /**
     * Encrypts data.
     * 
     * @param cleartext the cleartext
     * 
     * @return the string
     * 
     * @throws GeneralSecurityException on an encryption error
     */
    public static String encrypt(final String cleartext) {
        int count = 30;
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, count);
 
        byte[] cipherBytes = null;
        try {
            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
            cipher.init(Cipher.ENCRYPT_MODE, getKey(), parameterSpec);
            cipherBytes = cipher.doFinal(cleartext.getBytes());
        } catch (GeneralSecurityException e) {
            log.fatal("Error on passowrd encryption.", e);
        }
        
//        return new String(encode(cipherBytes));
//        return new String(new BASE64Encoder().encode(cipherBytes));
        Base64.Encoder encoder = Base64.getEncoder();
        return new String(encoder.encode(cipherBytes));
    }
 
    /**
     * Decrypts cipher text using the shared passphrase.
     * 
     * @param ciphertext the ciphertext
     * 
     * @return The decrypted text.
     */
    public static String decrypt(final String ciphertext) {
        int count = 30;
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, count);
 
        String result = null;
//        byte[] decodedBytes = decode(ciphertext);
        
        try {
//            byte[] decodedBytes = new BASE64Decoder().decodeBuffer(ciphertext);
            Base64.Decoder decoder= java.util.Base64.getDecoder();
            byte[] decodedBytes = decoder.decode(ciphertext);
            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
            cipher.init(Cipher.DECRYPT_MODE, getKey(), parameterSpec);
            result = new String(cipher.doFinal(decodedBytes));
            return result;
        } catch (GeneralSecurityException e) {
            log.fatal("Error on passowrd decryption.", e);
//        } catch (IOException e) {
//            e.printStackTrace();
        }
        return result;
    }
 
    /**
     * Gets the key.
     * 
     * @return the key
     * 
     * @throws GeneralSecurityException the general security exception
     */
    protected static SecretKey getKey() throws GeneralSecurityException {
        KeySpec keySpec = new PBEKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        return keyFactory.generateSecret(keySpec);
    }
}