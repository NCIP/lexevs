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
 * The exception to throw when invalid input is provided to a LexBIG service.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 * @created 27-Jan-2006 9:19:38 PM
 */
public class LBParameterException extends LBException
{
    String parameterName, parameterValue;

    public LBParameterException(String message, String parameterName, String parameterValue)
    {
        super(message +  " Parameter: " + parameterName + " Value: " + parameterValue);
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public LBParameterException(String message, String parameterName)
    {
        super(message +  " Parameter: " + parameterName);
        this.parameterName = parameterName;
    }
    
    public LBParameterException(String message)
    {
        super(message);
    }

    public String getParameterName()
    {
        return this.parameterName;
    }

    public String getParameterValue()
    {
        return this.parameterValue;
    }
}