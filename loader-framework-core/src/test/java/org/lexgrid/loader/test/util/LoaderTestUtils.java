
package org.lexgrid.loader.test.util;

import java.io.File;
import java.util.logging.Logger;

/**
 * The Class TestUtils.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LoaderTestUtils {
	
	/** The log. */
	private static Logger log = Logger.getLogger(LoaderTestUtils.class.getName());
	
	/** The Constant TMP_DIR. */
	private static final String TMP_DIR = "src/test/tmp";	
	
	/** The Constant INDEX_DIR. */
	private static final String INDEX_DIR = "src/test/resources/lbIndex";	
	
	/** The Constant REGISTRY_FILE. */
	private static final String REGISTRY_FILE = "src/test/resources/config/registry.xml";	
	
	/** The Constant LOCK_FILE. */
	private static final String LOCK_FILE = "src/test/resources/config/lock.xml";
	
	/**
	 * Delete directory.
	 * 
	 * @param path the path
	 * 
	 * @return true, if successful
	 */
	public static boolean deleteDirectory(File path) {
		if( path.exists() ) {
			File[] files = path.listFiles();
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				}
				else {
					if(!files[i].delete()){
						return false;
					}
				}
			}			
			return path.delete();
		}
		//if it doesn't exists, consider the delete a success.
		return true;	
	}
	
	public static boolean emptyDirectory(File path) {
		if( path.exists() ) {
			File[] files = path.listFiles();
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				}
				else {
					if(!files[i].delete()){
						return false;
					}
				}
			}			
		}
		//if it doesn't exists, consider the delete a success.
		return true;	
	}

	public static void cleanUpDatabase(){
		shutdownnHsqldb();
		cleanTmpDirContents();
	}
	
	
	/**
	 * Clean up all.
	 */
	public static void cleanUpAll(){
		cleanIndexes();
		deleteRegistry();
		deleteLock();
	}
	
	/**
	 * Clean tmp dir.
	 */
	public static void cleanTmpDirContents(){
		File tmpDir = new File(TMP_DIR);
		if(!emptyDirectory(tmpDir)){
			tmpDir.deleteOnExit();
		}
	}
	
	/**
	 * Clean indexes.
	 */
	public static void cleanIndexes(){
		File indexDir = new File(INDEX_DIR);
		if(!deleteDirectory(indexDir)){
			indexDir.deleteOnExit();
		}
	}
	
	/**
	 * Delete registry.
	 */
	public static void deleteRegistry(){
		File registry = new File(REGISTRY_FILE);
		registry.delete();
	}
	
	/**
	 * Delete lock.
	 */
	public static void deleteLock(){
		File lock = new File(LOCK_FILE);
		lock.delete();
	}
	
	/**
	 * Shutdownn hsqldb.
	 */
	public static void shutdownnHsqldb() {	
		try {
			//String url = SystemResourceService.instance().getSystemVariables().getAutoLoadDBURL();
			//Class.forName("org.hsqldb.jdbcDriver");
			//Connection con = DriverManager.getConnection(url, "sa", "");
			//String sql = "SHUTDOWN";
			//Statement stmt = con.createStatement();
			//stmt.executeUpdate(sql);
			//stmt.close();
		} catch (Exception e) {
			log.warning("Could not close HSQL Database -- already closed. " + e.getMessage());
		}
	}	
	
}