/*
 * Copyright: (c) 2004-2007 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.Exceptions;

/**
 * The exception to throw when invocation of a LexBIG service fails due to an
 * unexpected problem captured and logged for administrative action.  The logID
 * will contain information that the LexBIG admins can use to track down the
 * details of the internal error.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 * @created 27-Jan-2006 9:19:38 PM
 */
public class LBInvocationException extends LBException
{
    private String logId;

    public LBInvocationException(String message, String logId)
    {
        super(message + " - LogID Reference " + logId);
        this.logId = logId;
    }

    public String getLogId()
    {
        return this.logId;
    }

}