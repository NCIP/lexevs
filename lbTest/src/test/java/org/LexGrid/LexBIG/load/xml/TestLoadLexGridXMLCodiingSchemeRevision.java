
package org.LexGrid.LexBIG.load.xml;

import java.io.File;
import java.util.ArrayList;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

import junit.framework.TestCase;

public class TestLoadLexGridXMLCodiingSchemeRevision extends TestCase {

	protected LexBIGService service;
	protected DatabaseServiceManager dbManager;
	protected CodingSchemeService csService;
	protected LexEvsServiceLocator locator;
	protected ArrayList<String> revisionGuid;


	public void setUp() {
		service = LexBIGServiceImpl.defaultInstance();
		locator = LexEvsServiceLocator.getInstance();
		dbManager = locator.getDatabaseServiceManager();
		csService = dbManager.getCodingSchemeService();
		revisionGuid = new ArrayList<String>();
	}

	public void testLoadScheme4AssocTargetRevision()
			throws LBParameterException, LBInvocationException,
			InterruptedException, LBException {

		LexBIGServiceManager lbsm = service.getServiceManager(null);

		LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
				.getLoader("LexGrid_Loader");

		loader.load(
				new File(
						"resources/testData/csRevision/OriginalAutombilesCS.xml")
						.toURI(), true, true);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(500);
		}

		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

		lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

		lbsm.setVersionTag(loader.getCodingSchemeReferences()[0],
				LBConstants.KnownTags.PRODUCTION.toString());

	}

	public void testValidateSchemeLoad() throws LBException {
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion("1.1");
		CodedNodeGraph cng = service.getNodeGraph("urn:oid:22.2urn:oid:22.22.0.4.3", csvt,
				"rel");
		cng = cng.restrictToAssociations(
				ConvenienceMethods.createNameAndValueList("hasSubtype"), null);
		ResolvedConceptReferenceList rcrl = cng.resolveAsList(
				ConvenienceMethods.createConceptReference("GM", "Automobiles"),
				true, true, -1, -1, null, null, null, -1);
		ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(0);
		assertNotNull(rcr);
	}

	public void testLoadTargetRevision1() throws LBParameterException,
			LBInvocationException, InterruptedException, LBException {

		LexBIGServiceManager lbsm = service.getServiceManager(null);

		LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
				.getLoader("LexGrid_Loader");

		loader.load(new File("resources/testData/csRevision/RevisionCS.1.xml")
				.toURI(), true, true);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(500);
		}

		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

		lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

		lbsm.setVersionTag(loader.getCodingSchemeReferences()[0],
				LBConstants.KnownTags.PRODUCTION.toString());
	}

	public void testLoadTargetRevision2() throws LBParameterException,
			LBInvocationException, InterruptedException, LBException {

		LexBIGServiceManager lbsm = service.getServiceManager(null);

		LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
				.getLoader("LexGrid_Loader");

		loader.load(new File("resources/testData/csRevision/RevisionCS.2.xml")
				.toURI(), true, true);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(500);
		}

		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

		lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

		lbsm.setVersionTag(loader.getCodingSchemeReferences()[0],
				LBConstants.KnownTags.PRODUCTION.toString());
	}

	public void testLoadTargetRevision3() throws LBParameterException,
			LBInvocationException, InterruptedException, LBException {

		LexBIGServiceManager lbsm = service.getServiceManager(null);

		LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
				.getLoader("LexGrid_Loader");

		loader.load(new File("resources/testData/csRevision/RevisionCS.3.xml")
				.toURI(), true, true);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(500);
		}

		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

		lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

		lbsm.setVersionTag(loader.getCodingSchemeReferences()[0],
				LBConstants.KnownTags.PRODUCTION.toString());
	}

	public void testLoadTargetRevision4() throws LBParameterException,
			LBInvocationException, InterruptedException, LBException {

		LexBIGServiceManager lbsm = service.getServiceManager(null);

		LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
				.getLoader("LexGrid_Loader");

		loader.load(new File("resources/testData/csRevision/RevisionCS.4.xml")
				.toURI(), true, true);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(500);
		}

		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

		lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

		lbsm.setVersionTag(loader.getCodingSchemeReferences()[0],
				LBConstants.KnownTags.PRODUCTION.toString());
	}

	public void testLoadTargetRevision5() throws LBParameterException,
			LBInvocationException, InterruptedException, LBException {

		LexBIGServiceManager lbsm = service.getServiceManager(null);

		LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
				.getLoader("LexGrid_Loader");

		loader.load(new File("resources/testData/csRevision/RevisionCS.5.xml")
				.toURI(), true, true);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(500);
		}

		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

		lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

		lbsm.setVersionTag(loader.getCodingSchemeReferences()[0],
				LBConstants.KnownTags.PRODUCTION.toString());
	}
	public void testRemoveAutombiles() throws LBException {
		LexBIGServiceManager lbsm = service.getServiceManager(null);

		AbsoluteCodingSchemeVersionReference a = ConvenienceMethods
				.createAbsoluteCodingSchemeVersionReference(
						"urn:oid:22.22.0.4", "1.1");

		lbsm.deactivateCodingSchemeVersion(a, null);

		lbsm.removeCodingSchemeVersion(a);
		AuthoringService authServ = LexEvsServiceLocator.getInstance()
				.getDatabaseServiceManager().getAuthoringService();
		assertTrue(authServ
				.removeRevisionRecordbyId("ScottCSRelease2010"));
		assertTrue(authServ
				.removeRevisionRecordbyId("ScottCSRelease2010Feb"));
		assertTrue(authServ
				.removeRevisionRecordbyId("ScottCSRelease2010Mar"));
		assertTrue(authServ
				.removeRevisionRecordbyId("ScottCSRelease2010April"));
		assertTrue(authServ
				.removeRevisionRecordbyId("ScottCSRelease2010May"));
	}
}