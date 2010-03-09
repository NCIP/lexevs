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
package org.LexGrid.LexBIG.Utility.logging;

import java.util.Collection;

/**
 * This interface extends the LgMessageDirectorIF to add caching of messages.
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 */
public interface CachingMessageDirectorIF extends LgMessageDirectorIF {
    public static final int DEBUG = 0;
    public static final int INFO = 1;
    public static final int WARN = 2;
    public static final int ERROR = 3;
    public static final int FATAL = 4;

    /**
     * The function returns all the messages logged
     */
    public Collection getAllMsgs();

    /**
     * The function returns a count of the messages logged at a perticular level
     */
    public long getMsgsCount(int level);

    /**
     * The function returns all the messages logged
     */
    public Collection getMsgs(int level);

    /**
     * The function returns a count of all the messages logged
     */
    public long getAllMsgsCount();

    /**
     * The function returns all the messages logged at and above a perticular
     * level.
     * 
     * For example, if level= ResourceLogger.ERROR, then the fuction returns all
     * the ERROR and FATAL messages.
     * 
     * @param level
     *            : The level at and above which messages would be returned
     */
    public Collection getAllMsgsAtAndAbove(int level);

    /**
     * The function returns the count of all messages logged at and above a
     * perticular level.
     * 
     * For example, if level= ResourceLogger.ERROR, then the fuction returns all
     * the ERROR and FATAL messages.
     * 
     * @param level
     *            : The level at and above which messages would be returned
     */
    public long getAllMsgsAtAndAboveCount(int level);
}