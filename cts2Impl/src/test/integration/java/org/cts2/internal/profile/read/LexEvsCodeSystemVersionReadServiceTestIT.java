package org.cts2.internal.profile.read;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.test.LexEvsTestRunner.LoadContent;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;
import org.cts2.codesystemversion.CodeSystemVersion;
import org.cts2.service.core.ReadContext;
import org.cts2.test.BaseCts2IntegrationTest;
import org.cts2.utility.ConstructorUtils;
import org.junit.Test;
import org.lexevs.locator.LexEvsServiceLocator;

public class LexEvsCodeSystemVersionReadServiceTestIT extends BaseCts2IntegrationTest {

	@Resource
	private LexEvsCodeSystemVersionReadService lexEvsCodeSystemVersionReadService;

	@Test
	public void testInit(){
		assertNotNull(lexEvsCodeSystemVersionReadService);
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void testResolveCodeSystemVersion(){
		CodeSystemVersion codeSystemVersion = 
			lexEvsCodeSystemVersionReadService.read(ConstructorUtils.nameToNameOrURI("Automobiles:1.0"), null, null);
		
		assertEquals("urn:oid:11.11.0.1", codeSystemVersion.getAbout());
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void testResolveCodeSystemVersionDifferentRevisionFirstRevision() throws Exception {
		Revision rev1 = new Revision();
		rev1.setRevisionId("r1");

		CodingScheme cs1 = new CodingScheme();
		cs1.setCodingSchemeURI("urn:oid:11.11.0.1");
		cs1.setRepresentsVersion("1.0");
		cs1.setEntryState(new EntryState());
		cs1.getEntryState().setChangeType(ChangeType.MODIFY);
		cs1.getEntryState().setContainingRevision("r1");
		cs1.setFormalName("r1 formal name");
		
		rev1.addChangedEntry(new ChangedEntry());
		rev1.getChangedEntry()[0].setChangedCodingSchemeEntry(cs1);
		
		Revision rev2 = new Revision();
		rev2.setRevisionId("r2");
		
		CodingScheme cs2 = new CodingScheme();
		cs2.setCodingSchemeURI("urn:oid:11.11.0.1");
		cs2.setRepresentsVersion("1.0");
		cs2.setEntryState(new EntryState());
		cs2.getEntryState().setChangeType(ChangeType.MODIFY);
		cs2.getEntryState().setContainingRevision("r2");
		cs2.setFormalName("r2 formal name");
		
		rev2.addChangedEntry(new ChangedEntry());
		rev2.getChangedEntry()[0].setChangedCodingSchemeEntry(cs2);
		
		LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService().loadRevision(rev1, null, false);
		LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService().loadRevision(rev2, null, false);
		
		ReadContext readContext = new ReadContext();
		readContext.setChangeSetContext("r1");
		
		CodeSystemVersion codeSystemVersion = 
			lexEvsCodeSystemVersionReadService.read(ConstructorUtils.nameToNameOrURI("Automobiles:1.0"), null, readContext);
		
		assertEquals("r1 formal name", codeSystemVersion.getFormalName());
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void testResolveCodeSystemVersionDifferentRevisionAnotherRevision() throws Exception {
		Revision rev1 = new Revision();
		rev1.setRevisionId("r1");

		CodingScheme cs1 = new CodingScheme();
		cs1.setCodingSchemeURI("urn:oid:11.11.0.1");
		cs1.setRepresentsVersion("1.0");
		cs1.setEntryState(new EntryState());
		cs1.getEntryState().setChangeType(ChangeType.MODIFY);
		cs1.getEntryState().setContainingRevision("r1");
		cs1.setFormalName("r1 formal name");
		
		rev1.addChangedEntry(new ChangedEntry());
		rev1.getChangedEntry()[0].setChangedCodingSchemeEntry(cs1);
		
		Revision rev2 = new Revision();
		rev2.setRevisionId("r2");
		
		CodingScheme cs2 = new CodingScheme();
		cs2.setCodingSchemeURI("urn:oid:11.11.0.1");
		cs2.setRepresentsVersion("1.0");
		cs2.setEntryState(new EntryState());
		cs2.getEntryState().setChangeType(ChangeType.MODIFY);
		cs2.getEntryState().setContainingRevision("r2");
		cs2.setFormalName("r2 formal name");
		
		rev2.addChangedEntry(new ChangedEntry());
		rev2.getChangedEntry()[0].setChangedCodingSchemeEntry(cs2);
		
		LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService().loadRevision(rev1, null, false);
		LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService().loadRevision(rev2, null, false);
		
		ReadContext readContext = new ReadContext();
		readContext.setChangeSetContext("r2");
		
		CodeSystemVersion codeSystemVersion = 
			lexEvsCodeSystemVersionReadService.read(ConstructorUtils.nameToNameOrURI("Automobiles:1.0"), null, readContext);
		
		assertEquals("r2 formal name", codeSystemVersion.getFormalName());
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void testResolveCodeSystemVersionByDate() throws Exception {
		Revision rev1 = new Revision();
		rev1.setRevisionId("r1");

		CodingScheme cs1 = new CodingScheme();
		cs1.setCodingSchemeURI("urn:oid:11.11.0.1");
		cs1.setRepresentsVersion("1.0");
		cs1.setEntryState(new EntryState());
		cs1.getEntryState().setChangeType(ChangeType.MODIFY);
		cs1.getEntryState().setContainingRevision("r1");
		cs1.setFormalName("r1 formal name");
		
		rev1.addChangedEntry(new ChangedEntry());
		rev1.getChangedEntry()[0].setChangedCodingSchemeEntry(cs1);
		
		Revision rev2 = new Revision();
		rev2.setRevisionId("r2");
		
		CodingScheme cs2 = new CodingScheme();
		cs2.setCodingSchemeURI("urn:oid:11.11.0.1");
		cs2.setRepresentsVersion("1.0");
		cs2.setEntryState(new EntryState());
		cs2.getEntryState().setChangeType(ChangeType.MODIFY);
		cs2.getEntryState().setContainingRevision("r2");
		cs2.setFormalName("r2 formal name");
		
		rev2.addChangedEntry(new ChangedEntry());
		rev2.getChangedEntry()[0].setChangedCodingSchemeEntry(cs2);
		
		LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService().loadRevision(rev1, null, false);
		Date date1 = new Date();
		Thread.sleep(100);
		
		LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService().loadRevision(rev2, null, false);
		Date date2 = new Date();
		Thread.sleep(100);
		
		ReadContext readContext = new ReadContext();
		readContext.setReferenceTime(date1);
		
		CodeSystemVersion codeSystemVersion = 
			lexEvsCodeSystemVersionReadService.read(ConstructorUtils.nameToNameOrURI("Automobiles:1.0"), null, readContext);
		
		assertEquals("r1 formal name", codeSystemVersion.getFormalName());
	}
	
	@Test
	@LoadContent(contentPath="classpath:content/Automobiles.xml")
	public void testResolveCodeSystemVersionByDifferentDate() throws Exception {
		Revision rev1 = new Revision();
		rev1.setRevisionId("r1");

		CodingScheme cs1 = new CodingScheme();
		cs1.setCodingSchemeURI("urn:oid:11.11.0.1");
		cs1.setRepresentsVersion("1.0");
		cs1.setEntryState(new EntryState());
		cs1.getEntryState().setChangeType(ChangeType.MODIFY);
		cs1.getEntryState().setContainingRevision("r1");
		cs1.setFormalName("r1 formal name");
		
		rev1.addChangedEntry(new ChangedEntry());
		rev1.getChangedEntry()[0].setChangedCodingSchemeEntry(cs1);
		
		Revision rev2 = new Revision();
		rev2.setRevisionId("r2");
		
		CodingScheme cs2 = new CodingScheme();
		cs2.setCodingSchemeURI("urn:oid:11.11.0.1");
		cs2.setRepresentsVersion("1.0");
		cs2.setEntryState(new EntryState());
		cs2.getEntryState().setChangeType(ChangeType.MODIFY);
		cs2.getEntryState().setContainingRevision("r2");
		cs2.setFormalName("r2 formal name");
		
		rev2.addChangedEntry(new ChangedEntry());
		rev2.getChangedEntry()[0].setChangedCodingSchemeEntry(cs2);
		
		LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService().loadRevision(rev1, null, false);
		Date date1 = new Date();
		Thread.sleep(100);
		
		LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService().loadRevision(rev2, null, false);
		Date date2 = new Date();
		Thread.sleep(100);
		
		ReadContext readContext = new ReadContext();
		readContext.setReferenceTime(date2);
		
		CodeSystemVersion codeSystemVersion = 
			lexEvsCodeSystemVersionReadService.read(ConstructorUtils.nameToNameOrURI("Automobiles:1.0"), null, readContext);
		
		assertEquals("r2 formal name", codeSystemVersion.getFormalName());
	}
}
