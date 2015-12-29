package org.lexevs.dao.index.operation;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.dataAccess.CleanUpUtility;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;

public class DefaultLexEVSIndexOperationsWriteOverIndexTest {
	static AbsoluteCodingSchemeVersionReference reference = new AbsoluteCodingSchemeVersionReference();
	static String prefix;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);

		LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
				.getLoader("LexGrid_Loader");

		loader.load(new File("resources/testData/Automobiles.xml").toURI(),
				true, true);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(1000);
		}
		
		reference.setCodingSchemeURN("urn:oid:11.11.0.1");
		reference.setCodingSchemeVersion("1.0");
		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());
		Registry registry = LexEvsServiceLocator.getInstance().getRegistry();
		RegistryEntry entry = registry.getEntriesForUri("urn:oid:11.11.0.1").get(0);
		prefix = entry.getPrefix();
		LexEvsServiceLocator.getInstance().getLexEvsDatabaseOperations().dropCodingSchemeTablesByPrefix("lb" + prefix);
		LexEvsServiceLocator.getInstance().getRegistry().removeEntry(entry);
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);
		lbsm.removeCodingSchemeVersion(reference);
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIndexDeletionAndOverwrite() throws LBParameterException, LBInvocationException, InterruptedException {
		
		LexBIGServiceManager lbsm;
		try {
			lbsm = ServiceHolder.instance().getLexBIGService()
					.getServiceManager(null);
			LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
					.getLoader("LexGrid_Loader");

			loader.load(new File("resources/testData/Automobiles.xml").toURI(),
					true, true);

			while (loader.getStatus().getEndTime() == null) {
				Thread.sleep(1000);
			}

		} catch (LBException e) {
			//success.  make it fail first then ask user for input at index time
			System.out.println("success");
		} 

	}

}
