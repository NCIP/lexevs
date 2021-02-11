
package org.LexGrid.LexBIG.Impl.export.xml.lgxml;

import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;

import junit.framework.Test;
import junit.framework.TestSuite;




public class AllTests {

	public static Test suite() {
		ServiceHolder.configureForSingleConfig();
		TestSuite suite = new TestSuite("LexGrid XML Exporter Tests");
		suite.addTestSuite(org.LexGrid.LexBIG.Impl.export.xml.lgxml.ExportAutomobiles2.class);
		suite.addTestSuite(org.LexGrid.LexBIG.Impl.export.xml.lgxml.AssociationLoop.class);
		suite.addTestSuite(org.LexGrid.LexBIG.Impl.export.xml.lgxml.MultipuleRelations.class);
		suite.addTestSuite(org.LexGrid.LexBIG.Impl.export.xml.lgxml.EntitiesOnly.class);
		suite.addTestSuite(org.LexGrid.LexBIG.Impl.export.xml.lgxml.AssociationsOnly.class);
		suite.addTestSuite(org.LexGrid.LexBIG.Impl.export.xml.lgxml.RestrictToAssociation.class);
		return suite;
	}	
}