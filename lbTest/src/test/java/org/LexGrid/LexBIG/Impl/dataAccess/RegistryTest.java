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
package org.LexGrid.LexBIG.Impl.dataAccess;

import java.util.Date;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.util.sql.DBUtility;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.Registry.DBEntry;
import org.lexevs.registry.service.Registry.HistoryEntry;
import org.lexevs.system.ResourceManager;

/**
 * JUnit test cases for the registry system.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RegistryTest extends TestCase {

    @SuppressWarnings("null")
    public void testRegistry() throws Exception {
        Registry registry = ResourceManager.instance().getRegistry();

        int preDbCount = registry.getDBEntries().length;
        int preHistCount = registry.getHistoryEntries().length;

        String id = registry.getNextDBIdentifier();

        registry.addNewItem("2.3.4", "1.0", CodingSchemeVersionStatus.ACTIVE.toString(), "<JUNIT_IGNORE>jdbc:test",
                "production", id + "", "prefix");

        String historyId = registry.getNextHistoryIdentifier();
        registry.addNewHistory("1.5.7", "<JUNIT_IGNORE>testUrl", historyId + "theDBName", "thePrefix");

        Date date = registry.getLastUpdateTime();

        registry = null;
        ResourceManager.reInit(null);
        registry = ResourceManager.instance().getRegistry();

        assertTrue(registry.getNextDBIdentifier().equals(DBUtility.computeNextIdentifier(id)));
        assertTrue(registry.getNextHistoryIdentifier().equals(DBUtility.computeNextIdentifier(historyId)));

        assertTrue(registry.getLastUpdateTime().getTime() >= date.getTime());

        DBEntry[] dbe = registry.getDBEntries();

        assertTrue(dbe != null);
        assertTrue(dbe.length == preDbCount + 1);

        assertTrue(dbe[preDbCount].dbURL.equals("<JUNIT_IGNORE>jdbc:test"));
        assertTrue(dbe[preDbCount].status.equals(CodingSchemeVersionStatus.ACTIVE.toString()));
        assertTrue(dbe[preDbCount].tag.equals("production"));
        assertTrue(dbe[preDbCount].urn.equals("2.3.4"));
        assertTrue(dbe[preDbCount].version.equals("1.0"));
        assertTrue(dbe[preDbCount].dbName.equals(id + ""));
        assertTrue(dbe[preDbCount].prefix.equals("prefix"));
        assertTrue(dbe[preDbCount].deactiveDate == 0);
        assertTrue(dbe[preDbCount].lastUpdateDate > 0);

        HistoryEntry[] history = registry.getHistoryEntries();
        assertTrue(history != null);
        assertTrue(history.length == preHistCount + 1);
        assertTrue(history[preHistCount].urn.equals("1.5.7"));
        assertTrue(history[preHistCount].dbURL.equals("<JUNIT_IGNORE>testUrl"));
        assertTrue(history[preHistCount].dbName.equals(historyId + "theDBName"));
        assertTrue(history[preHistCount].prefix.equals("thePrefix"));
        assertTrue(history[preHistCount].lastUpdateDate <= registry.getLastUpdateTime().getTime());

        registry.remove(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference("2.3.4", "1.0"));
        registry.removeHistoryEntry("1.5.7");
    }
}