
package org.LexGrid.LexBIG.Impl.export.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;

public class StringToFileUriConverter {

/**
     * Returns a file URI corresponding to the given string.
     * 
     * taken from org.LexGrid.LexBIG.admin.Util.java
     * 
     * @param s
     * @return java.net.URI
     * @throws org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException
     */
public static URI convert(String s) throws LBResourceUnavailableException {
        String trimmed = s.trim();
        try {
            // Resolve to file, treating the string as either a
            // standard file path or URI.
            File f;
            if (!(f = new File(trimmed)).exists()) {
                f = new File(new URI(trimmed.replace(" ", "%20")));
                if (!f.exists())
                    throw new FileNotFoundException();
            }

            // Accomodate embedded spaces ...
            return new URI(f.toURI().toString().replace(" ", "%20"));
        } catch (Exception e) {
            throw new LBResourceUnavailableException("UNABLE TO RESOLVE RESOURCE: " + trimmed);
        }
    }

}