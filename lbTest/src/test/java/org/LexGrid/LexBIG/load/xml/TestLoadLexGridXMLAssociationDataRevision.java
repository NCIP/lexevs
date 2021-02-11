
package org.LexGrid.LexBIG.load.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.management.relation.Relation;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedData;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.CodedNodeGraphImpl;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.association.AssociationDataService;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

public class TestLoadLexGridXMLAssociationDataRevision extends TestCase {

	protected LexBIGService service;
	protected DatabaseServiceManager dbManager;
	protected CodingSchemeService csService;
	protected LexEvsServiceLocator locator;
	protected ArrayList<String> revisionGuid;

	public TestLoadLexGridXMLAssociationDataRevision(String serverName) {
		super(serverName);
	}

	public void setUp() {
		ServiceHolder.configureForSingleConfig();
		service = ServiceHolder.instance().getLexBIGService();
		locator = LexEvsServiceLocator.getInstance();
		dbManager = locator.getDatabaseServiceManager();
		csService = dbManager.getCodingSchemeService();
		revisionGuid = new ArrayList<String>();
	}

	public void testLoadAutombiles4AssociationData()
			throws LBParameterException, LBInvocationException,
			InterruptedException, LBException {
		// ServiceHolder.configureForSingleConfig();
		// service = ServiceHolder.instance().getLexBIGService();
		LexBIGServiceManager lbsm = service.getServiceManager(null);

		LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
				.getLoader("LexGrid_Loader");

		loader
				.load(new File(
						"resources/testData/csRevision/OriginalAutombiles.xml")
						.toURI(), true, true);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(500);
		}

		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

		lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

		lbsm.setVersionTag(loader.getCodingSchemeReferences()[0],
				LBConstants.KnownTags.PRODUCTION.toString());
		AuthoringService vDao = dbManager.getAuthoringService();
	}

	public void testvalidateAssociationDataTestLoad() throws LBException {
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion("2.0");
		CodedNodeGraph cng = service.getNodeGraph("urn:oid:22.22.0.2", csvt,
				"rel");
		cng = cng.restrictToAssociations(ConvenienceMethods
				.createNameAndValueList("hasSubtype"), null);
		ResolvedConceptReferenceList rcrl = cng.resolveAsList(
				ConvenienceMethods.createConceptReference("A0001",
						"Automobiles"), true, true, -1, -1, null, null, null,
				-1);
		ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(0);
		AssociatedData assocData = rcr.getSourceOf().getAssociation(0)
				.getAssociatedData().getAssociatedData(0);
		assertTrue(assocData.getId().equals("inst00a"));
	}

	public void testLoadAssociationDataRevision1() throws LBParameterException,
			LBInvocationException, InterruptedException, LBException {

		LexBIGServiceManager lbsm = service.getServiceManager(null);

		LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
				.getLoader("LexGrid_Loader");

		loader.load(new File("resources/testData/csRevision/Revision1.xml")
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

	public void testvalidateAssociationDataRevisionTestLoad()
			throws LBException {
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion("2.0");
		CodedNodeGraph cng = service.getNodeGraph("urn:oid:22.22.0.2", csvt,
				"rel");
		cng = cng.restrictToAssociations(ConvenienceMethods
				.createNameAndValueList("sameAs"), null);
		ResolvedConceptReferenceList rcrl = cng
				.resolveAsList(ConvenienceMethods.createConceptReference("005",
						"Automobiles"), true, true, -1, -1, null, null, null,
						-1);
		ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(0);
		AssociatedData assocData = rcr.getSourceOf().getAssociation(0)
				.getAssociatedData().getAssociatedData(0);
		assertTrue(assocData.getId().equals("inst00b"));

	}

	@SuppressWarnings("static-access")
	public void testvalidateRelationRevision() throws LBException {
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion("2.0");
		CodingScheme cs = service
				.resolveCodingScheme("urn:oid:22.22.0.2", csvt);
		Enumeration<? extends Relations> enumerate = cs.enumerateRelations();
		Relations relations = null;
		while (enumerate.hasMoreElements()) {
			relations = enumerate.nextElement();
			if (relations.getContainerName().equals("rel")) {
				break;
			}
		}
		assertTrue(relations.getEntryState().getContainingRevision().equals(
				"testRelease2010Jan_testData"));
		assertTrue(relations.getEntryState().getChangeType().DEPENDENT
				.equals("DEPENDENT"));
	}

	public void testLoadAssociationDataRevision2() throws LBParameterException,
			LBInvocationException, InterruptedException, LBException {

		LexBIGServiceManager lbsm = service.getServiceManager(null);

		LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
				.getLoader("LexGrid_Loader");

		loader.load(new File("resources/testData/csRevision/Revision2.xml")
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

	public void testvalidateAssociationDataRevision2TestLoad()
			throws LBException {
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion("2.0");
		CodedNodeGraph cng = service.getNodeGraph("urn:oid:22.22.0.2", csvt,
				"rel");
		cng = cng.restrictToAssociations(ConvenienceMethods
				.createNameAndValueList("hasSubtype"), null);
		ResolvedConceptReferenceList rcrl = cng
				.resolveAsList(ConvenienceMethods.createConceptReference("A0001",
						"Automobiles"), true, true, -1, -1, null, null, null,
						-1);
		ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(0);
		AssociatedData assocData = rcr.getSourceOf().getAssociation(0)
				.getAssociatedData().getAssociatedData(0);
		assertTrue(assocData.getId().equals("inst00a"));

	}

	@SuppressWarnings("static-access")
	public void testvalidateCodingSchemeRevision2() throws LBException {
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion("2.0");
		CodingScheme cs = service
				.resolveCodingScheme("urn:oid:22.22.0.2", csvt);
//		Enumeration<? extends Relations> enumerate = cs.enumerateRelations();
//		Relations relations = null;
//		while (enumerate.hasMoreElements()) {
//			relations = enumerate.nextElement();
//			if (relations.getContainerName().equals("rel")) {
//				break;
//			}
//		}
		assertTrue(cs.getEntryState().getContainingRevision().equals(
				"testRelease2010Feb_testData"));
		assertTrue(cs.getEntryState().getPrevRevision().equals("testRelease2010Jan_testData"));
		assertTrue(cs.getEntryState().getChangeType().DEPENDENT
				.equals("DEPENDENT"));
	}
	
	public void testLoadAssociationDataRevision3() throws LBParameterException,
			LBInvocationException, InterruptedException, LBException {

		LexBIGServiceManager lbsm = service.getServiceManager(null);

		LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
				.getLoader("LexGrid_Loader");

		loader.load(new File("resources/testData/csRevision/Revision3.xml")
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

	public void testvalidateAssociationDataRevision3TestLoad()
			throws LBException {
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion("2.0");
		CodedNodeGraph cng = service.getNodeGraph("urn:oid:22.22.0.2", csvt,
				"rel");
		cng = cng.restrictToAssociations(ConvenienceMethods
				.createNameAndValueList("sameAs"), null);
		ResolvedConceptReferenceList rcrl = cng.resolveAsList(
				ConvenienceMethods.createConceptReference("0054",
						"Automobiles"), true, true, -1, -1, null, null, null,
				-1);
		ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(0);
		AssociatedData assocData = rcr.getSourceOf().getAssociation(0)
				.getAssociatedData().getAssociatedData(0);
		assertTrue(assocData.getId().equals("inst00b"));

	}

	@SuppressWarnings("static-access")
	public void testvalidateCodingSchemeRevision3() throws LBException {
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion("2.0");
		CodingScheme cs = service
				.resolveCodingScheme("urn:oid:22.22.0.2", csvt);
		// Enumeration<? extends Relations> enumerate = cs.enumerateRelations();
		// Relations relations = null;
		// while (enumerate.hasMoreElements()) {
		// relations = enumerate.nextElement();
		// if (relations.getContainerName().equals("rel")) {
		// break;
		// }
		// }
		assertTrue(cs.getEntryState().getContainingRevision().equals(
				"testRelease2010Mar_testData"));
		assertTrue(cs.getEntryState().getPrevRevision().equals(
				"testRelease2010Feb_testData"));
		assertTrue(cs.getEntryState().getChangeType().DEPENDENT
				.equals("DEPENDENT"));
	}
	
	public void testLoadAssociationDataRevision4() throws LBParameterException,
			LBInvocationException, InterruptedException, LBException {

		LexBIGServiceManager lbsm = service.getServiceManager(null);

		LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm
				.getLoader("LexGrid_Loader");

		loader.load(new File("resources/testData/csRevision/Revision4.xml")
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

	public void testvalidateAssociationDataRevision4TestLoad()
			throws LBException {
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion("2.0");
		CodedNodeGraph cng = service.getNodeGraph("urn:oid:22.22.0.2", csvt,
				"rel");
		cng = cng.restrictToAssociations(ConvenienceMethods
				.createNameAndValueList("partOf"), null);
		ResolvedConceptReferenceList rcrl = cng.resolveAsList(
				ConvenienceMethods
						.createConceptReference("GearBox", "Automobiles"), true,
				true, -1, -1, null, null, null, -1);
		ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(0);
		AssociatedData assocData = rcr.getSourceOf().getAssociation(0)
				.getAssociatedData().getAssociatedData(0);
		assertNull(assocData);

	}

	@SuppressWarnings("static-access")
	public void testvalidateCodingSchemeRevision4() throws LBException {
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		csvt.setVersion("2.0");
		CodingScheme cs = service
				.resolveCodingScheme("urn:oid:22.22.0.2", csvt);
		// Enumeration<? extends Relations> enumerate = cs.enumerateRelations();
		// Relations relations = null;
		// while (enumerate.hasMoreElements()) {
		// relations = enumerate.nextElement();
		// if (relations.getContainerName().equals("rel")) {
		// break;
		// }
		// }
		assertTrue(cs.getEntryState().getContainingRevision().equals(
				"testRelease2010Apr_testData"));
		assertTrue(cs.getEntryState().getPrevRevision().equals(
				"testRelease2010Mar_testData"));
		assertTrue(cs.getEntryState().getChangeType().DEPENDENT
				.equals("DEPENDENT"));
	}

	public void testRemoveAutomobiles() throws LBException {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);

		AbsoluteCodingSchemeVersionReference a = ConvenienceMethods
				.createAbsoluteCodingSchemeVersionReference(
						"urn:oid:22.22.0.2", "2.0");

		locator.getSystemResourceService().removeCodingSchemeResourceFromSystem("urn:oid:22.22.0.2", "2.0");

	}

}