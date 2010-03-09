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
package edu.mayo.informatics.lexgrid.convert.emfConversions;

import java.util.Iterator;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.codingSchemes.CodingschemesFactory;
import org.LexGrid.emf.codingSchemes.persistence.CodingSchemeHome;
import org.LexGrid.emf.concepts.Entities;
import org.LexGrid.emf.concepts.Entity;
import org.LexGrid.emf.concepts.impl.EntityImpl;
import org.LexGrid.emf.relations.Association;
import org.LexGrid.emf.relations.AssociationSource;
import org.LexGrid.emf.relations.Relations;
import org.LexGrid.emf.relations.impl.AssociationImpl;
import org.LexGrid.emf.relations.impl.AssociationSourceImpl;
import org.LexGrid.emf.relations.impl.RelationsImpl;
import org.LexGrid.managedobj.BaseService;
import org.LexGrid.managedobj.HomeServiceBroker;
import org.LexGrid.managedobj.ServiceInitException;
import org.LexGrid.managedobj.ServiceUnavailableException;
import org.LexGrid.managedobj.jdbc.JDBCConnectionDescriptor;
import org.LexGrid.persistence.sql.CodingSchemeService;
import org.apache.log4j.Logger;

import edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces.EMFRead;
import edu.mayo.informatics.lexgrid.convert.emfConversions.emfInterfaces.EMFWrite;
import edu.mayo.informatics.lexgrid.convert.utility.Constants;
import edu.mayo.informatics.lexgrid.convert.utility.MessageRedirector;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Reads and Writes SQL <-> EMF
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @version subversion $Revision: 8763 $ checked in on $Date: 2008-06-12
 *          16:35:45 +0000 (Thu, 12 Jun 2008) $
 */
public class SQLReadWrite extends EMFReadImpl implements EMFRead, EMFWrite {
    private static Logger log = Logger.getLogger("convert.SQLReadWrite");
    LgMessageDirectorIF messages_;

    private String sqlServer, sqlDriver, sqlUsername, sqlPassword, tablePrefix;
    boolean failOnAllErrors;
    private URNVersionPair[] codingSchemes;
    private HomeServiceBroker broker_ = null;
    private CodingSchemeHome csContext_ = null;

    public SQLReadWrite(String sqlServer, String sqlDriver, String sqlUsername, String sqlPassword, String tablePrefix,
            LoaderPreferences loaderPrefs, boolean failOnAllErrors, LgMessageDirectorIF messages,
            URNVersionPair[] codingSchemes) {
        this.sqlServer = sqlServer;
        this.sqlDriver = sqlDriver;
        this.sqlUsername = sqlUsername;
        this.sqlPassword = sqlPassword;
        this.tablePrefix = tablePrefix;
        this.messages_ = messages;
        this.failOnAllErrors = failOnAllErrors;
        this.codingSchemes = codingSchemes;
        this.loaderPreferences = loaderPrefs;
    }

    public SQLReadWrite(String sqlServer, String sqlDriver, String sqlUsername, String sqlPassword, String tablePrefix,
            boolean failOnAllErrors, LgMessageDirectorIF messages, URNVersionPair[] codingSchemes) {
        this.sqlServer = sqlServer;
        this.sqlDriver = sqlDriver;
        this.sqlUsername = sqlUsername;
        this.sqlPassword = sqlPassword;
        this.tablePrefix = tablePrefix;
        this.messages_ = messages;
        this.failOnAllErrors = failOnAllErrors;
        this.codingSchemes = codingSchemes;
    }

    // /////////////////////////////////////////////////////
    // Core read methods (from EMFRead interface)
    // /////////////////////////////////////////////////////
    public CodingScheme readCodingScheme(String registeredName) throws Exception {
        URNVersionPair[] tmpCodingSchemes = new URNVersionPair[codingSchemes.length + 1];
        for (int i = 0; i < codingSchemes.length; i++) {
            tmpCodingSchemes[i] = codingSchemes[i];
        }
        tmpCodingSchemes[codingSchemes.length] = new URNVersionPair(registeredName, null);

        try {
            CodingScheme cs = (CodingScheme) getCodingSchemeService().findByPrimaryKey(registeredName);
            codingSchemes = tmpCodingSchemes;
            return cs;
        } catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
            return null;
        }
    }

    // /////////////////////////////////////////////////////
    // Core write methods (from EMFWrite interface)
    // /////////////////////////////////////////////////////

    // /////////////////////////////////////////////////////
    // Writes the entire scheme (as populated) in one shot...
    // /////////////////////////////////////////////////////
    public void writeCodingScheme(CodingScheme codingScheme) throws Exception {
        try {
            getCodingSchemeService().insert(codingScheme);
        } catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
        } finally {
            closeServices();
        }
    }

    // /////////////////////////////////////////////////////
    // Writes the scheme in segments.
    // Note that if these methods are used, the caller is
    // responsible for closing services.
    // /////////////////////////////////////////////////////
   public void writeCodingSchemeRecord(CodingScheme codingScheme) throws Exception {
        try {
            getCodingSchemeService().insertCodingSchemeRecord(codingScheme);
        } catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
        }
    }

   public void writeCodingSchemeMultiAttributes(CodingScheme codingScheme) throws Exception {
       try {
           getCodingSchemeService().insertMultiAttributes(codingScheme);
       } catch (Exception e) {
           log.error("Failed...", e);
           messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
       }
   }

    public void writeCodingSchemeSupportedAttributes(CodingScheme codingScheme) throws Exception {
        try {
            getCodingSchemeService().insertSupportedAttributes(codingScheme);
        } catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
        }
    }

    public void writeCodingSchemeConcepts(CodingScheme codingScheme) throws Exception {
        try {
            getCodingSchemeService().insertEntities(codingScheme);
        } catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
        }
    }

    public void writeCodingSchemeRelations(CodingScheme codingScheme) throws Exception {
        try {
            getCodingSchemeService().insertRelations(codingScheme);
        } catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
        }
    }

    // /////////////////////////////////////////////////////
    // Writes individual entities or associations ...
    // Note that if these methods are used, the caller is
    // responsible for closing services.
    // /////////////////////////////////////////////////////
    public void writeEntity(Entity entity) throws Exception {
        try {
            BaseService svc = getCodingSchemeService().getNestedService(EntityImpl.class);
            svc.insert(entity);
        } catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
        }
    }

    public void writeAssociationSourceTarget(AssociationSource source) throws Exception {
        try {
            BaseService svc = getCodingSchemeService().getNestedService(RelationsImpl.class).getNestedService(
                    AssociationImpl.class).getNestedService(AssociationSourceImpl.class);
            svc.insert(source);
        } catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
        }
    }

    // /////////////////////////////////////////////////////
    // Deletion of an entire scheme ...
    // /////////////////////////////////////////////////////
    public void clearCodingScheme(String codingScheme) throws Exception {
        try {
            CodingScheme cs = CodingschemesFactory.eINSTANCE.createCodingScheme();
            cs.setCodingSchemeName(codingScheme);
            getCodingSchemeService().remove(cs);
        } catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
        } finally {
            closeServices();
        }
    }

    // /////////////////////////////////////////////////////
    // Streaming methods (from EMFWrite interface) to write
    // individual concepts and associations. Control is
    // inverted in this case; items are requested on demand.
    // /////////////////////////////////////////////////////
    public void streamedWriteOnAssociation(CodingScheme codingScheme, Relations relationsContainer,
            Association association) throws Exception {
        BaseService svc = getCodingSchemeService().getNestedService(RelationsImpl.class).getNestedService(
                AssociationImpl.class);
        // Note: temporarily add containers so that the service can
        // resolve lineage, but then remove so that we do not hang on
        // to items unnecessarily in memory.
        relationsContainer.getAssociation().add(association);
        try {
            messages_.info("Storing " + association.getEntityCode());
            svc.insert(association);
        } finally {
            relationsContainer.getAssociation().remove(association);
        }
    }

    public void streamedWriteOnAssociations(CodingScheme codingScheme, Relations relationsContainer,
            Iterator associations) throws Exception {
        while (associations.hasNext())
            streamedWriteOnAssociation(codingScheme, relationsContainer, (Association) associations.next());
    }

    public void streamedWriteOnAssociationInstances(CodingScheme codingScheme, Relations relationsContainer,
            Association associationContainer, Iterator associationInstances) throws Exception {
        BaseService svc = getCodingSchemeService().getNestedService(RelationsImpl.class).getNestedService(
                AssociationImpl.class).getNestedService(AssociationSourceImpl.class);
        while (associationInstances.hasNext()) {
            // Note: temporarily add containers so that the service can
            // resolve lineage, but then remove so that we do not hang on
            // to items unnecessarily in memory.
            AssociationSourceImpl source = (AssociationSourceImpl) associationInstances.next();
            relationsContainer.getAssociation().add(associationContainer);
            associationContainer.getSource().add(source);
            try {
                svc.insert(source);
            } finally {
                associationContainer.getSource().remove(source);
                relationsContainer.getAssociation().remove(associationContainer);
            }
        }
    }

    public void streamedWriteOnConcepts(CodingScheme codingScheme, Entities conceptsContainer, Iterator concepts)
            throws Exception {
        BaseService svc = getCodingSchemeService().getNestedService(EntityImpl.class);
        int count = 0;
        while (concepts.hasNext()) {
            // Note: temporarily add to container so that the service can
            // resolve lineage, but then remove so that we do not hang on
            // to the items in memory.
            EntityImpl ce = (EntityImpl) concepts.next();
            conceptsContainer.getEntity().add(ce);
            try {
                svc.insert(ce);
            } finally {
                conceptsContainer.getEntity().remove(ce);
            }
            if (++count % 100 == 0)
                messages_.info("Concepts processed: " + count);
        }
    }

    public void closeStreamedWrite() {
        closeServices();
    }

    // /////////////////////////////////////////////////////
    // Currently unsupported operations ...
    // /////////////////////////////////////////////////////

    // Primary read ...
    public CodingScheme[] readAllCodingSchemes() throws Exception {
        throw new UnsupportedOperationException();
    }

    public CodingScheme readCodingScheme() throws Exception {
        return null;
    }

    // Incremental read (from EMFRead interface) ...
    public Iterator streamedReadOnAssociations(CodingScheme codingScheme, Relations relationsContainer)
            throws Exception {
        throw new UnsupportedOperationException();
    }

    public Iterator streamedReadOnAssociationInstances(CodingScheme codingScheme, Relations relationsContainer,
            Association associationContainer) throws Exception {
        throw new UnsupportedOperationException();
    }

    public Iterator streamedReadOnConcepts(CodingScheme codingScheme, Entities conceptsContainer) throws Exception {
        throw new UnsupportedOperationException();
    }

    public boolean supportsStreamedRead(CodingScheme codingScheme) {
        return false;
    }

    public void closeStreamedRead() {
    }

    // /////////////////////////////////////////////////////
    // Supporting methods for SQL-based services
    // /////////////////////////////////////////////////////

    /**
     * Close and free managed SQL services.
     */
    public void closeServices() {
        if (broker_ != null)
            try {
                broker_.close();
            } catch (RuntimeException e) {
            } finally {
                csContext_ = null;
                broker_ = null;
            }
    }

    /**
     * Returns the broker used to manage SQL services.
     * 
     * @return HomeServiceBroker
     */
    protected HomeServiceBroker getBroker() {
        if (broker_ == null)
            broker_ = new HomeServiceBroker();
        return broker_;
    }

    /**
     * Returns a SQL-based services to load, manage, and persist CodingScheme
     * objects.
     * 
     * @return CodingSchemeService
     * @throws ServiceInitException
     */
    protected CodingSchemeService getCodingSchemeService() throws ServiceInitException {
        HomeServiceBroker broker = getBroker();
        CodingSchemeService css = null;
        if (csContext_ != null)
            try {
                css = (CodingSchemeService) broker.getService(csContext_);
            } catch (ServiceUnavailableException sue) {
            }
        if (css == null) {
            JDBCConnectionDescriptor desc = new JDBCConnectionDescriptor();
            desc.setDbUid(sqlUsername);
            desc.setDbPwd(sqlPassword);
            desc.setDbUrl(sqlServer);
            desc.setUseUTF8(true);
            desc.setAutoRetryFailedConnections(true);
            try {
                desc.setDbDriver(sqlDriver);
            } catch (ClassNotFoundException e) {
                throw new ServiceInitException("The requested driver was not found on the classpath");
            }
            css = new CodingSchemeService(broker, desc, false, tablePrefix, failOnAllErrors, new MessageRedirector(
                    messages_));
            css.setPageSize(Constants.mySqlBatchSize);
            csContext_ = new CodingSchemeHome(broker);
            broker.registerService(csContext_, css);
        }
        return css;
    }

    /**
     * Returns the CodingScheme version pairs associated with this read/write
     * instance.
     */
    public URNVersionPair[] getUrnVersionPairs() throws Exception {
        return codingSchemes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#finalize()
     */
    protected void finalize() throws Throwable {
        closeServices();
        super.finalize();
    }

    public String getDBDriver() {
        return sqlDriver;
    }

    public String getDBTablePrefix() {
        return tablePrefix;
    }

    public String getDBServer() {
        return sqlServer;
    }

    public String getDBUsername() {
        return sqlUsername;
    }

    public String getDBPassword() {
        return sqlPassword;
    }

    public void setStreamingOn(boolean streamOn) {
        // TODO Auto-generated method stub

    }

    public boolean getStreamingOn() {
        // TODO Auto-generated method stub
        return false;
    }

}