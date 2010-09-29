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
package org.LexGrid.LexBIG.Extensions.Load;

import org.LexGrid.LexBIG.Exceptions.LBException;

/**
 * 
 * Known issues are listed in tracker item #10516 at
 * http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=10516&group_id=14&atid=137
 * 
 * Loading the HL7 RIM to a LexBIG environment will result in 250 coding
 * schemes many of which are minimal place holders for full size terminologies
 * 
 * Functionality:
 * Loads coding schemes from an HL7 RIM database stored in an MSAccess
 * database according to a mapping of HL7 RIM database elements to LexBIG
 *
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * @author <A HREF="mailto:stancl.craig@mayo.edu">Craig Stancl</A>
 *
 */
public interface HL7_Loader extends Loader {
	   public static final String name = "HL7Loader";
	   public static final String description = "This loader loads coding schemes from the HL7 RIM database"
	            + " into a LexGrid database.";

		/**
		 * Not implemented
		 * 
		 * Load content from a database repository. This will also result in
		 * implicit generation of standard indices required by the LexBIG runtime.
		 * <p>
		 * An exception is raised if resources cannot be accessed or another load
		 * operation is already in progress.
		 * 
		 * @param path
		 * 
		 *            Location of the source Access database.<tt>
		 *            Example: C:/rim0216d-rim-vocab.mdb</tt>
		 * @param driver
		 * 
		 *			  String representation of the driver class.<tt> 
		 *			  Example: sun.jdbc.odbc.JdbcOdbcDriver</tt>
		 * 
		 * @param stopOnErrors
		 * 
		 *            True means stop if any load error is detected. False means
		 *            attempt to load what can be loaded if recoverable errors are
		 *            encountered.
		 * 
		 * @param async
		 * 	          Flag controlling whether load occurs in the calling thread. If
		 *            true, the load will occur in a separate asynchronous process.
		 *            If false, this method blocks until the load operation
		 *            completes or fails. Regardless of setting, the getStatus and
		 *            getLog calls are used to fetch results.
		 * 
		 * @throws LBException
		 */
//		public void load(String path, String driver,
//			boolean stopOnErrors, boolean async) throws LBException;
		//TODO Comments
		
		/**
		 * @param path
		 *            Location of the source Access database.<tt>
		 *            Example: C:/rim0216d-rim-vocab.mdb</tt>
		 * @param URI
		 * 	          Location of the manifest file.
		 * @param stopOnErrors
		 *            True means stop if any load error is detected. False means
		 *            attempt to load what can be loaded if recoverable errors are
		 *            encountered.
		 * @param async
		 * 	          Flag controlling whether load occurs in the calling thread. If
		 *            true, the load will occur in a separate asynchronous process.
		 *            If false, this method blocks until the load operation
		 *            completes or fails. Regardless of setting, the getStatus and
		 *            getLog calls are used to fetch results.
		 * 
		 * @throws LBException
		 */
		public void load(String path, 
				boolean stopOnErrors, boolean async) throws LBException;

		//TODO Comments
		/**
		 * @param dbName 
		 *            Location of the source Access database.<tt>
		 *            Example: C:/rim0216d-rim-vocab.mdb</tt>
		 * @param validationLevel
		 *            Loader-specific level of validation; 0 = verify that the
		 *            database and target terminologies are present and accessible.
		 * @throws LBException
		 */
		public void validate(String dbName,
				int validationLevel)
				throws LBException;




}