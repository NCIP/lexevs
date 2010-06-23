package org.LexGrid.LexBIG.load.xml;

import java.util.ArrayList;

import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.locator.LexEvsServiceLocator;

import junit.framework.TestCase;

public class TestLoadLexGridXMLCodiingSchemeRevision extends TestCase {


		protected LexBIGService service;
		protected DatabaseServiceManager dbManager;
		protected CodingSchemeService csService;
		protected LexEvsServiceLocator locator;
		protected ArrayList<String> revisionGuid;

		public TestLoadLexGridXMLCodiingSchemeRevision(String serverName) {
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
				
}
