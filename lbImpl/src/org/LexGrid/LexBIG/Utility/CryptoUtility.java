package org.LexGrid.LexBIG.Utility;

/*import static org.bouncycastle.util.encoders.Base64.decode;
import static org.bouncycastle.util.encoders.Base64.encode;*/

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.LexGrid.LexBIG.Impl.logging.Logger;

public final class CryptoUtility {
    CryptoUtility() {}
 
    /**
     * Internal Key
     */
    protected static char[] key = "MayoClinic".toCharArray();
    private static byte[] salt = "LEX-GRID".getBytes();
    private static Logger log = new Logger(); 
    
    /**
    * Encrypts data
    * @param cleartext
    * @return
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
        return new String(new BASE64Encoder().encode(cipherBytes));
    }
 
    /**
     * Decrypts cipher text using the shared passphrase.
     * @param cipherText The text to decrypt.
     * @return The decrypted text.
     */
    public static String decrypt(final String ciphertext) {
        int count = 30;
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, count);
 
        String result = null;
//        byte[] decodedBytes = decode(ciphertext);
        
        try {
            byte[] decodedBytes = new BASE64Decoder().decodeBuffer(ciphertext);
            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
            cipher.init(Cipher.DECRYPT_MODE, getKey(), parameterSpec);
            result = new String(cipher.doFinal(decodedBytes));
            return result;
        } catch (GeneralSecurityException e) {
            log.fatal("Error on passowrd decryption.", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
 
    protected static SecretKey getKey() throws GeneralSecurityException {
        KeySpec keySpec = new PBEKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        return keyFactory.generateSecret(keySpec);
    }
}

 
