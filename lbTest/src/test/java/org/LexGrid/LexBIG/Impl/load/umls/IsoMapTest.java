
package org.LexGrid.LexBIG.Impl.load.umls;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.easymock.classextension.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.service.SystemResourceService;
import org.lexgrid.loader.logging.StatusTrackingLogger;
import org.lexgrid.loader.rrf.factory.IsoMapFactory;

/**
 * The Class DataLoadTestBase.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@SuppressWarnings("unchecked")
public class IsoMapTest extends TestCase {

	private IsoMapFactory factory;
	
	@Before
	public void setUp() throws Exception {
		final SystemResourceService mockSrs = EasyMock.createMock(SystemResourceService.class);
		
		SystemVariables sv = EasyMock.createMock(SystemVariables.class);
		
		EasyMock.expect(sv.getConfigFileLocation()).andReturn(
				System.getProperty("java.io.tmpdir") + File.separator + "lbconfig.props").anyTimes();
		
		EasyMock.expect(mockSrs.getSystemVariables()).andReturn(sv).anyTimes();
		
		LexEvsServiceLocator testLocator = new LexEvsServiceLocator(){

			@Override
			public SystemResourceService getSystemResourceService() {
				return mockSrs;
			}
			
		};
		
		EasyMock.replay(mockSrs,sv);

		factory = new IsoMapFactory(){

			@Override
			protected String getPath() {
				return super.getPath();
			}
			
		};
		
		factory.setLogger(new TestStatusTrackingLogger());
		factory.setLexEvsServiceLocator(testLocator);
	}

	@After
	public void tearDown() throws Exception {
		//can't delete the file without a gc... strange.
		System.gc();
		if(!this.getFile().delete()) {
			this.getFile().deleteOnExit();
		}
	}
	
	@Test
	public void testGetIsoMap() throws Exception {
		Map<String,String> isoMap = (Map<String, String>) factory.getObject();
		
		assertTrue(isoMap.size() > 0);
	}
	
	@Test
	public void testGetIsoMapWithAddition() throws Exception {

		File newFile = this.getFile();
		try {
			newFile.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Could not create a file at: " + newFile.getPath(), e);
		}
		
		this.writeToFile(newFile, "someNewSab=newOid");
		
		Map<String,String> isoMap = (Map<String, String>) factory.getObject();
		
		String newOid = isoMap.get("someNewSab");
		
		assertEquals("newOid", newOid);
	}
	
	@Test
	public void testGetIsoMapWithOverride() throws Exception {
		//overriding AOD=urn:oid:2.16.840.1.113883.6.112
		
		Map<String,String> isoMap = (Map<String, String>) factory.getObject();
		assertEquals("urn:oid:2.16.840.1.113883.6.112", isoMap.get("AOD"));
		
		File newFile = this.getFile();
		newFile.createNewFile();
		
		this.writeToFile(newFile, "AOD=newOid");
		
		isoMap = (Map<String, String>) factory.getObject();
		assertEquals("newOid", isoMap.get("AOD"));
	}
	
	private class TestStatusTrackingLogger implements StatusTrackingLogger {

		@Override
		public LoadStatus getProcessStatus() {
			return null;
		}

		@Override
		public void busy() {
			//
		}

		@Override
		public String debug(String message) {
			return "";
		}

		@Override
		public String error(String message) {
			return "";
		}

		@Override
		public String error(String message, Throwable sourceException) {
			return "";
		}

		@Override
		public String fatal(String message) {
			return "";
		}

		@Override
		public String fatal(String message, Throwable sourceException) {
			return "";
		}

		@Override
		public void fatalAndThrowException(String message) throws Exception {
			//
		}

		@Override
		public void fatalAndThrowException(String message,
				Throwable sourceException) throws Exception {
			//
		}

		@Override
		public String info(String message) {
			return "";
		}

		@Override
		public String warn(String message) {
			return "";
		}

		@Override
		public String warn(String message, Throwable sourceException) {
			return "";
		}
		
	};
	
	private void writeToFile(File file, String text) throws Exception {
		FileWriter writer = new FileWriter(file);
		writer.write(text);
		writer.flush();
		writer.close();
	}
	
	private File getFile() {
		File file = 
			new File(getPath());

		return file;
	}
	
	private String getPath() {
		return System.getProperty("java.io.tmpdir") + File.separator + IsoMapFactory.ISO_MAP_FILE_NAME;
	}
}