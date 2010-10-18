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
package edu.mayo.informatics.indexer.utility;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import edu.mayo.informatics.indexer.api.exceptions.InternalErrorException;

/**
 * This class is used for managing write locks between multiple JVMs running in
 * the same enviroment. This makes sure that only one JVM is reading or writing
 * from the MetaData at a time.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class WriteLockManager {
    private static WriteLockManager wlm_;

    private File file_;
    private boolean IHaveLock = false;
    private final Logger logger = Logger.getLogger("Indexer.WriteLockManager");

    public static WriteLockManager instance() {
        return wlm_;
    }

    /*
     * This instance method is used to create a new write lock manager - mostly
     * for bootstrapping.
     */
    protected static WriteLockManager instance(File parentFolder) {
        if (wlm_ == null) {
            wlm_ = new WriteLockManager(parentFolder);
        }
        return wlm_;
    }

    private WriteLockManager(File parentFolder) {
        file_ = new File(parentFolder, "lock");
    }

    protected synchronized void lock() throws InternalErrorException {
        int i = 0;
        long startTime = System.currentTimeMillis();
        while (true) {
            while (!IHaveLock && file_.exists()) {
                // Can't read the lock file without making sure that I am the
                // only thread
                // looking at this file right now.
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    // do nothing.
                }

                // if the file has been locked for more than 5 seconds, steal
                // the lock.
                if ((startTime + 5000) < System.currentTimeMillis()) {
                    // steal lock - relock as mine
                    logger
                            .error("Stealing lock for metadata file - this should never happen.  If it does, data may disappear from the metadata file.");
                    file_.delete();
                }
            }

            if (!IHaveLock) {
                try {
                    IHaveLock = file_.createNewFile();
                } catch (IOException e) {
                    // this can happen if another thread created the file after
                    // we left the above loop,
                    // but before we got here.
                }
            }

            if (IHaveLock) {
                return;
            }

            i++;
            if (i > 50) {
                throw new InternalErrorException("Could not aquire lock on the lock file");
            }
        }
    }

    protected synchronized void unlock() throws InternalErrorException {
        if (IHaveLock) {
            boolean deleted = file_.delete();
            if (deleted || !file_.exists()) {
                IHaveLock = false;
            } else {
                throw new InternalErrorException("Problem deleting the lock file");
            }
        }
    }
}