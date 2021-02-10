
package org.lexevs.registry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.lexevs.exceptions.UnexpectedInternalError;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.ResourceManager;

/**
 * This class is used for managing write locks between multiple JVMs running in
 * the same enviroment.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class WriteLockManager {
    
    /** The wlm_. */
    private static WriteLockManager wlm_;

    /** The file_. */
    private File file_;
    
    /** The lock file lock_. */
    private File lockFileLock_;
    
    /** The registry revision_. */
    private int registryRevision_ = 0;
    
    /** The previous registry revision_. */
    private int previousRegistryRevision_ = 0;

    /** The active locks_. */
    private ArrayList<CodingSchemeLock> activeLocks_;
    
    /** The my id_. */
    private String myId_;

    /** The lock file lock count. */
    private int lockFileLockCount = 0;
    
    /** The update time. */
    private int updateTime = 5; // how often to reread the file (in
    // minutes)
    /** The write lock_. */
    private boolean writeLock_ = false;
    
    /** The read lock_. */
    private boolean readLock_ = false;

    /** The continue pinging_. */
    private boolean continuePinging_ = true;

    /**
     * Gets the logger.
     * 
     * @return the logger
     */
    protected static LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    /**
     * Instance.
     * 
     * @return the write lock manager
     * 
     * @throws LBInvocationException the LB invocation exception
     */
    public static WriteLockManager instance() throws LBInvocationException {
        if (wlm_ == null) {
            String id = getLogger().error("No WriteLockManager is configured.");
            throw new LBInvocationException("No WriteLockManager is configured!", id);
        } else {
            return wlm_;
        }
    }

    /*
     * This instance method is used to create a new write lock manager - mostly
     * for bootstrapping.
     */
    /**
     * Instance.
     * 
     * @param registryFile the registry file
     * 
     * @return the write lock manager
     * 
     * @throws LBInvocationException the LB invocation exception
     */
    public synchronized static WriteLockManager instance(File registryFile) throws LBInvocationException {
        if (wlm_ == null) {
            try {
                wlm_ = new WriteLockManager(registryFile);
            } catch (UnexpectedInternalError e) {
                String id = getLogger().error("Problem starting the Write Lock Manager.");
                throw new LBInvocationException("Problem starting the Write Lock Manager.", id);
            }
        } else {
            // the WriteLockManager already exists - but it it in the correct
            // place?
            // In the GUI - you can redirect to a different config file - which
            // will require a
            // different lock location
            if (wlm_.file_.getParentFile().equals(registryFile.getParentFile())) {
                return wlm_;
            } else {
                try {
                    // stop the ping thread in the old write lock manager.
                    wlm_.continuePinging_ = false;
                    wlm_ = null;
                    // create the new write lock manager.
                    wlm_ = new WriteLockManager(registryFile);
                } catch (UnexpectedInternalError e) {
                    String id = getLogger().error("Problem starting the Write Lock Manager.");
                    throw new LBInvocationException("Problem starting the Write Lock Manager.", id);
                }
            }

        }
        return wlm_;
    }

    /**
     * Instantiates a new write lock manager.
     * 
     * @param registryFile the registry file
     * 
     * @throws UnexpectedInternalError the unexpected internal error
     * @throws LBInvocationException the LB invocation exception
     */
    private WriteLockManager(File registryFile) throws UnexpectedInternalError, LBInvocationException {
        myId_ = UUID.randomUUID().toString();

        file_ = new File(registryFile.getParentFile(), "lock.xml");
        lockFileLock_ = new File(registryFile.getParentFile(), "lock.xml.lock");

        readFile();
        previousRegistryRevision_ = registryRevision_;

        Thread pingThread = new Thread(new PingThread());
        // this allows the JVM to exit while this thread is still active.
        pingThread.setDaemon(true);
        pingThread.start();
    }

    /**
     * Lock an individual terminology for loading, indexing, etc.
     * 
     * @param urn the urn
     * @param version the version
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void acquireLock(String urn, String version) throws LBInvocationException, LBParameterException {
        lockLockFile();

        try {
            readFile();
            CodingSchemeLock csl = new CodingSchemeLock(urn, version);

            // Since I have the lock, the ping thread will not be able to read
            // the file and change this
            // array, so this is safe

            boolean IStole = false;
            for (int i = 0; i < activeLocks_.size(); i++) {
                if (activeLocks_.get(i).isLocked(csl)) {
                    // If double the update time plus 1 minute has occured,
                    // assume a JVM died without
                    // releasing a lock. Steal.
                    if ((System.currentTimeMillis() - activeLocks_.get(i).lockDate) > ((updateTime * 2 + 1) * 60 * 1000)) {
                        getLogger().warn("Stealing an apparant dead lock for " + urn + " , " + version);
                        activeLocks_.set(i, csl);
                        IStole = true;
                    } else if (activeLocks_.get(i).ownerId.equals(myId_)) {
                        // I already own the lock. do nothing.
                    } else {
                        throw new LBInvocationException(
                                "Lock Unavailable - Another process is currently modifying the code system that you are trying to modify.",
                                "");
                    }
                }
            }

            if (!IStole) {
                activeLocks_.add(csl);
            }
            writeFile();
        } catch (UnexpectedInternalError e) {
            String id = getLogger().error("Problem in acquring a lock", e);
            throw new LBInvocationException("There was a problem acquiring the write lock", id);
        } catch (LBInvocationException e) {
            throw e;
        } finally {
            releaseLockFile();
        }
    }

    /**
     * Unlock a particular coding scheme (finished loading, indexing, etc).
     * 
     * @param urn the urn
     * @param version the version
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public void releaseLock(String urn, String version) throws LBInvocationException, LBParameterException {
        lockLockFile();

        try {
            readFile();
            CodingSchemeLock csl = new CodingSchemeLock(urn, version);

            // Since I have the lock, the ping thread will not be able to read
            // the file and change this
            // array, so this is safe
            for (int i = 0; i < activeLocks_.size(); i++) {
                if (activeLocks_.get(i).equals(csl)) {
                    activeLocks_.remove(i);
                    break;
                }
            }
            writeFile();
        } catch (UnexpectedInternalError e) {
            String id = getLogger().error("Problem in acquring a lock", e);
            throw new LBInvocationException("There was a problem acquiring the write lock", id);
        } finally {
            releaseLockFile();
        }
    }

    /**
     * The Class PingThread.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    private class PingThread implements Runnable {
        /*
         * This threads job is to maintain locks that were acquired by this JVM.
         * A lock is only good for 10 minutes from the time it was entered into
         * the lock file. So this thread needs to keep updating the timestamp of
         * the lock so that locks acquired by this JVM don't expire. If this JVM
         * stops without releasing its locks, they will automaticially expire
         * after 10 minutes.
         * 
         * A second job of this thread is to notice if this JVM needs to re-read
         * the configuration because another JVM changed it (added a coding
         * scheme, etc)
         * 
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() {
            while (continuePinging_) {
                try {
                    // 5 minutes (arbitrary selection)
                    Thread.sleep(updateTime * 60 * 1000);

                    lockLockFile();

                    try {
                        checkForRegistryUpdates(); // this re-reads the lock
                                                   // file - and notices
                                                   // updates.

                        boolean updatedALock = false;
                        CodingSchemeLock[] locks = getLocks();
                        for (int i = 0; i < locks.length; i++) {
                            if (locks[i].ownerId.equals(myId_)) {
                                locks[i].lockDate = System.currentTimeMillis();
                                updatedALock = true;
                            }
                        }
                        if (updatedALock) {
                            writeFile();
                        }
                    } finally {
                        releaseLockFile();
                    }

                } catch (Exception e) {
                    getLogger().error("Something failed while running the lock monitor thread", e);
                }
            }
        }
    }

    /**
     * Registry was revised.
     * 
     * @throws LBInvocationException the LB invocation exception
     */
    public void registryWasRevised() throws LBInvocationException {
        lockLockFile();

        try {
            readFile();
            registryRevision_++;
            writeFile();
            previousRegistryRevision_ = registryRevision_;
        } catch (UnexpectedInternalError e) {
            String msg = getLogger().error("Problem marking registry revision", e);
            throw new LBInvocationException("Problem marking registry revision.", msg);
        } finally {
            releaseLockFile();
        }
    }

    /**
     * See if another vm or thread has written a newer version of the registry
     * file. If yes, reload the resource manager.
     * 
     * @throws UnexpectedInternalError      * @throws LBInvocationException the LB invocation exception
     */
    public void checkForRegistryUpdates() throws LBInvocationException {
        lockLockFile();

        try {
            readFile();

            if (previousRegistryRevision_ != registryRevision_) {
                getLogger().debug("Discovered a configuration change - rereading config");

                ResourceManager.reInit(null);
                previousRegistryRevision_ = registryRevision_;
            }
        } catch (UnexpectedInternalError e) {
            String msg = getLogger().error("Problem checking for updates", e);
            throw new LBInvocationException("Problem checking for updates.", msg);
        } finally {
            releaseLockFile();
        }
    }

    /**
     * The Class CodingSchemeLock.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    private class CodingSchemeLock {
        
        /** The urn. */
        String urn;
        
        /** The version. */
        String version;
        
        /** The lock date. */
        long lockDate;
        
        /** The owner id. */
        String ownerId;

        /**
         * Instantiates a new coding scheme lock.
         * 
         * @param urn the urn
         * @param version the version
         * 
         * @throws LBParameterException the LB parameter exception
         */
        public CodingSchemeLock(String urn, String version) throws LBParameterException {
            if (urn == null || urn.length() == 0) {
                throw new LBParameterException("URN is required to create lock", "urn");
            }
            if (version == null || version.length() == 0) {
                throw new LBParameterException("Version is required to create lock", "version");
            }
            this.urn = urn;
            this.version = version;
            this.lockDate = System.currentTimeMillis();
            this.ownerId = myId_;
        }

        /**
         * Instantiates a new coding scheme lock.
         */
        public CodingSchemeLock() {

        }

        /**
         * Equals.
         * 
         * @param csl the csl
         * 
         * @return true, if successful
         */
        public boolean equals(CodingSchemeLock csl) {
            if (this.urn.equals(csl.urn) && this.version.equals(csl.version) && this.ownerId.equals(csl.ownerId)) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * Checks if is locked.
         * 
         * @param csl the csl
         * 
         * @return true, if is locked
         */
        public boolean isLocked(CodingSchemeLock csl) {
            if (this.urn.equals(csl.urn) && this.version.equals(csl.version)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Lock lock file.
     * 
     * @throws LBInvocationException the LB invocation exception
     */
    public void lockLockFile() throws LBInvocationException {
        try {
            int i = 0;
            while (true) {
                long temp = System.currentTimeMillis();
                while (lockFileLockCount == 0 && lockFileLock_.exists()) {
                    // Can't read the lock file without making sure that I am
                    // the only thread
                    // looking at this file right now.

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // do nothing.
                    }

                    // try to lock for 5 seconds.
                    if ((temp + (5 * 1000)) < System.currentTimeMillis()) {
                        throw new UnexpectedInternalError("The Lock file '" + lockFileLock_.getAbsolutePath()
                                + "' is owned by another VM.");
                    }
                }

                if (lockFileLockCount == 0) {
                    try {
                        boolean gotLock = lockFileLock_.createNewFile();
                        if (gotLock) {
                            lockFileLockCount++;
                            return;
                        }
                    } catch (IOException e) {
                        // do nothing - another VM must have create the file
                        // first. Let the outer while
                        // loop send me around again.
                    }
                } else {
                    // already had one lock in this vm, add another.
                    lockFileLockCount++;
                    return;
                }

                i++;
                if (i > 50) {
                    throw new UnexpectedInternalError("Could not aquire lock on the lock file '"
                            + lockFileLock_.getAbsolutePath() + "' another VM owns it and has not released it.");
                }

            }
        } catch (UnexpectedInternalError e) {
            String id = getLogger().error("Problem aquiring lock", e);
            throw new LBInvocationException("Could not acquire lock on lock file", id);
        }
    }

    /**
     * Release lock file.
     */
    public void releaseLockFile() {
        if (lockFileLockCount > 0) {
            lockFileLockCount--;
        }
        if (lockFileLockCount == 0 && lockFileLock_.exists()) {
            boolean deleted = lockFileLock_.delete();
            if (!deleted) {
                getLogger().error(
                        "There was a problem trying to release the lock file - " + lockFileLock_.getAbsolutePath());
            }
        }
    }

    /**
     * Gets the locks.
     * 
     * @return the locks
     */
    private synchronized CodingSchemeLock[] getLocks() {
        return activeLocks_.toArray(new CodingSchemeLock[activeLocks_.size()]);
    }

    /**
     * Gets the lock count.
     * 
     * @return the lock count
     */
    public int getLockCount() {
        return activeLocks_.size();
    }

    /**
     * Read file.
     * 
     * @throws UnexpectedInternalError the unexpected internal error
     * @throws LBInvocationException the LB invocation exception
     */
    @SuppressWarnings("unchecked")
    private synchronized void readFile() throws UnexpectedInternalError, LBInvocationException {
        // read in the contents of the xml file, populating the local variables.
        lockLockFile();
        try {
            SAXBuilder saxBuilder = new SAXBuilder();

            while (writeLock_) {
                // writeFile is running, wait for it to finish.
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
            readLock_ = true;

            if (file_.exists()) {
                Document document = saxBuilder.build(file_);
                readLock_ = false;
                Element root = document.getRootElement();
                Element vars = root.getChild("locks");

                // I changed the format of this - don't want to take a null
                // pointer if we encounter the old version
                if (vars.getChild("registryRevision") == null) {
                    registryRevision_ = 0;
                } else {
                    registryRevision_ = Integer.parseInt(vars.getChild("registryRevision").getAttributeValue("value"));
                }

                Element codingSchemes = root.getChild("codingSchemeLocks");
                List<Element> list = codingSchemes.getChildren("codingSchemeLock");

                activeLocks_ = new ArrayList<CodingSchemeLock>();

                for (int i = 0; i < list.size(); i++) {
                    Element cur = (Element) list.get(i);
                    CodingSchemeLock csl = new CodingSchemeLock();
                    csl.urn = cur.getAttributeValue("urn");
                    csl.version = cur.getAttributeValue("version");
                    csl.lockDate = Long.parseLong(cur.getAttributeValue("lockDate"));
                    csl.ownerId = cur.getAttributeValue("ownerId");

                    activeLocks_.add(csl);
                }
            } else {
                readLock_ = false;
                previousRegistryRevision_ = registryRevision_;
                activeLocks_ = new ArrayList<CodingSchemeLock>();
                writeFile();
            }
        } catch (NumberFormatException e) {
            throw new UnexpectedInternalError(
                    "The existing LexBIG lock file contains an invalid value for a field the requires a number - lastUpdateTime, lockDate");
        } catch (JDOMException e) {
            throw new UnexpectedInternalError("The existing LexBIG lock file is invalid", e);
        } catch (IOException e) {
            throw new UnexpectedInternalError("Could not access the specified lock file.");
        } finally {
            readLock_ = false;
            releaseLockFile();
        }
    }

    /**
     * Write file.
     * 
     * @throws UnexpectedInternalError the unexpected internal error
     * @throws LBInvocationException the LB invocation exception
     */
    private synchronized void writeFile() throws UnexpectedInternalError, LBInvocationException {
        // Take the local variables, and write them out to the file.
        try {
            Document document = new Document(new Element("LexBIG_Lock_File"));
            Element root = document.getRootElement();
            Element newElement = new Element("locks");

            root.addContent(newElement);

            Element parentElement = newElement;

            newElement = new Element("registryRevision");
            newElement.setAttribute("value", registryRevision_ + "");

            parentElement.addContent(newElement);

            newElement = new Element("codingSchemeLocks");
            root.addContent(newElement);

            parentElement = newElement;

            for (int i = 0; i < activeLocks_.size(); i++) {
                newElement = new Element("codingSchemeLock");
                CodingSchemeLock curEntry = activeLocks_.get(i);
                newElement.setAttribute("urn", curEntry.urn);
                newElement.setAttribute("version", curEntry.version);
                newElement.setAttribute("lockDate", curEntry.lockDate + "");
                newElement.setAttribute("ownerId", curEntry.ownerId);
                parentElement.addContent(newElement);
            }

            XMLOutputter xmlFormatter = new XMLOutputter(Format.getPrettyFormat());

            // Aquire lock to for permission to write
            lockLockFile();

            try {
                writeLock_ = true;
                while (readLock_) {
                    // readFile is running, wait for it to finish.
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // do nothing
                    }
                }
                FileWriter writer = new FileWriter(file_);

                writer.write(xmlFormatter.outputString(document));

                writer.close();
            } finally {
                writeLock_ = false;
                releaseLockFile();
            }
        } catch (IOException e) {
            throw new UnexpectedInternalError("There was a problem writing out the registry information", e);
        }
    }

}