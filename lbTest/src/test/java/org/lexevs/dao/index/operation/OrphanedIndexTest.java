
package org.lexevs.dao.index.operation;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.dataAccess.CleanUpUtility;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.utility.MyClassLoader;

public class OrphanedIndexTest {
	
	String uri = "urn:oid:11.11.0.1";
	String ver = "1.0";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		
		ExtensionRegistry exReg = ServiceHolder.instance().getLexBIGService().getServiceManager(null).getExtensionRegistry();
		ExtensionDescription[] descrips =  exReg.getExportExtensions().getExtensionDescription();
		for(ExtensionDescription ed : descrips){
			exReg.unregisterExportExtension(ed.getName());
		}
		descrips = exReg.getFilterExtensions().getExtensionDescription();
		for(ExtensionDescription ed : descrips){
			exReg.unregisterFilterExtension(ed.getName());
		}
		descrips = exReg.getGenericExtensions().getExtensionDescription();
		for(ExtensionDescription ed : descrips){
			exReg.unregisterGenericExtension(ed.getName());
		}
		descrips = exReg.getIndexExtensions().getExtensionDescription();
		for(ExtensionDescription ed : descrips){
			exReg.unregisterIndexExtension(ed.getName());
		}
		descrips = exReg.getLoadExtensions().getExtensionDescription();
		for(ExtensionDescription ed : descrips){
			exReg.unregisterLoadExtension(ed.getName());
		}
		descrips = exReg.getSearchExtensions().getExtensionDescription();
		for(ExtensionDescription ed : descrips){
			exReg.unregisterSearchExtension(ed.getName());
		}
		descrips = exReg.getSortExtensions().getSortDescription();
		for(ExtensionDescription ed : descrips){
			exReg.unregisterSortExtension(ed.getName());
		}
		LexBIGServiceImpl.setDefaultInstance(null);
		LexEvsServiceLocator.getInstance().destroy();
		
		String location = SystemVariables.getAbsoluteIndexLocation();
		File testIndex = new File(location + System.getProperty("file.separator") + "myIndexTest" );
		testIndex.mkdir();
	}
	
	@Rule public ExpectedException thrown = ExpectedException.none();
	@Test
	public void test() throws RuntimeException{
		thrown.expect(RuntimeException.class);
		thrown.expectMessage("Indexes seem to be created in another context as they do not match "
				+ "database registrations. If these indexes were copied from another service then "
				+ "please edit the config.props file to match the source service. Otherwise delete "
				+ "them and rebuild them from scratch");
		LexBIGServiceImpl.defaultInstance();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		CleanUpUtility.removeAllUnusedDatabases();
		assertTrue(CleanUpUtility.listUnusedDatabases().length == 0);
		CleanUpUtility.removeAllUnusedIndexes();
		assertTrue(CleanUpUtility.listUnusedIndexes().length == 0);
	}

}