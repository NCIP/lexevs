/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package edu.mayo.informatics.lexgrid.convert.exceptions;

/**
 * Exception for handling general conversion errors.
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu ">Kevin Peterson </A>
 * @version 1.0 - cvs $Revision: 1.2 $ checked in on $Date: 2005/09/27 19:52:23
 *          $
 */
public class LgConvertException extends Exception {

    private static final long serialVersionUID = 937874412463504273L;

    public LgConvertException() {
        super();
    }

    public LgConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public LgConvertException(Throwable cause) {
        super(cause);
    }

    public LgConvertException(String message) {
        super(message);
    }

}