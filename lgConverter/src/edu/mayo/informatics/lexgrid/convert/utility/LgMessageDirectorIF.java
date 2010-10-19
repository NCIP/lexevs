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
package edu.mayo.informatics.lexgrid.convert.utility;

/**
 * Interface to help pass debug, error, and warning types of messages around
 * from component to component.
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu ">Kevin Peterson </A>
 * @author <A HREF="mailto:daniel.armbrust@mayo.edu ">Daniel Armbrust </A>
 */
public interface LgMessageDirectorIF {

    /**
     * Output to indicate system is busy - spin a cursor, print a dot, etc.
     */
    public void busy();

    /**
     * Displays a status type of message (not an error or a warning)
     * 
     * @return Implementations may return a string, such as a log id.
     */
    public String info(String message);

    /**
     * Take a debug message.
     * 
     * @return Implementations may return a string, such as a log id.
     */
    public String debug(String message);

    /**
     * Display a warning to the user.
     * 
     * @return Implementations may return a string, such as a log id.
     */
    public String warn(String message);

    /**
     * Display a warning to the user
     * 
     * @param sourceException
     *            The source exception that caused the warning
     * @return Implementations may return a string, such as a log id.
     */
    public String warn(String message, Throwable sourceException);

    /**
     * Display a non-fatal error to the user.
     * 
     * @return Implementations may return a string, such as a log id.
     */
    public String error(String message);

    /**
     * Display a non-fatal error to the user
     * 
     * @param sourceException
     *            The source exception that caused the error.
     */
    public String error(String message, Throwable sourceException);

    /**
     * Display a fatal error to the user.
     * 
     * @return Implementations may return a string, such as a log id.
     */
    public String fatal(String message);

    /**
     * Display a fatal error to the user.
     * 
     * @return Implementations may return a string, such as a log id.
     * @param sourceException
     *            The source exception that caused the error.
     */
    public String fatal(String message, Throwable sourceException);

    /**
     * Display a fatal error to the user - and then throw an exception.
     */
    public void fatalAndThrowException(String message) throws Exception;

    /**
     * Display a fatal error to the user - and then throw an exception.
     * 
     * @param sourceException
     *            The source exception that caused the error.
     */
    public void fatalAndThrowException(String message, Throwable sourceException) throws Exception;
}