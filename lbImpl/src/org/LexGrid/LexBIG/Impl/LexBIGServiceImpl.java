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
package org.LexGrid.LexBIG.Impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeTagList;
import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.SortDescriptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ReferenceLink;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.RenderingDetail;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.SortContext;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Query.Filter;
import org.LexGrid.LexBIG.Extensions.Query.Sort;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.History.NCIThesaurusHistorySQLQueries;
import org.LexGrid.LexBIG.Impl.History.NCIThesaurusHistoryServiceImpl;
import org.LexGrid.LexBIG.Impl.History.UMLSHistoryServiceImpl;
import org.LexGrid.LexBIG.Impl.dataAccess.Registry;
import org.LexGrid.LexBIG.Impl.dataAccess.ResourceManager;
import org.LexGrid.LexBIG.Impl.dataAccess.SQLImplementedMethods;
import org.LexGrid.LexBIG.Impl.dataAccess.SQLInterface;
import org.LexGrid.LexBIG.Impl.helpers.MyClassLoader;
import org.LexGrid.LexBIG.Impl.internalExceptions.InternalException;
import org.LexGrid.LexBIG.Impl.logging.LgLoggerIF;
import org.LexGrid.LexBIG.Impl.logging.LoggerFactory;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;

/**
 * Implementation of the LexBIGService Interface.
 * 
 * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LexBIGServiceImpl implements LexBIGService {
    private static LexBIGServiceImpl lexbigService_ = null;
    private static final long serialVersionUID = 6785772690490820208L;

    private LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    /**
     * Returns a default singleton instance of the service.
     * <p>
     * Note: This is the recommended method of acquiring the service, since it
     * will allow the application to run without change in distributed LexBIG
     * environments (in which case the default instance is actually a
     * distributed service). However, use of the public constructor is supported
     * to preserve backward compatibility.
     * 
     * @return LexBIGServiceImpl
     */
    public static LexBIGServiceImpl defaultInstance() {
        if (lexbigService_ == null)
            try {
                lexbigService_ = new LexBIGServiceImpl();
            } catch (LBInvocationException e) {
                LoggerFactory.getLogger().error("Error initializing service", e);
            }
        return lexbigService_;
    }

    /**
     * Assigns the default singleton instance of the service.
     * <p>
     * Note: While this method is public, it is generally not intended to be
     * part of the externalized API. It is made public so that the runtime
     * system has the ability to assign the default instance when running in
     * distributed LexBIG environments, etc.
     * 
     * @param LexBIGServiceImpl
     *            the default instance.
     */
    public static void setDefaultInstance(LexBIGServiceImpl defaultInstance) {
        lexbigService_ = defaultInstance;
    }

    /**
     * @throws LBInvocationException
     * @throws Exception
     * 
     */
    public LexBIGServiceImpl() throws LBInvocationException {
        ResourceManager r = ResourceManager.instance();
        if (r == null) {
            System.err.println("Initialization Failure.  Beginning debug print:");
            System.out.println("Initialization Failure.  Beginning debug print:");
            ResourceManager.dumpLogQueue();
            throw new LBInvocationException(
                    "There was a problem starting up.  Please see the log files and / or system.out", "");

        }
        r.getLogger().logMethod();
    }

    /**
     * @throws LBInvocationException
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#getSupportedCodingSchemes()
     */
    public CodingSchemeRenderingList getSupportedCodingSchemes() throws LBInvocationException {
        getLogger().logMethod();
        try {
            CodingSchemeRenderingList temp = new CodingSchemeRenderingList();
            SQLInterface[] si = ResourceManager.instance().getAllSQLInterfaces();

            for (int i = 0; i < si.length; i++) {
                PreparedStatement getCodingSchemes = null;

                try {
                    getCodingSchemes = si[i].checkOutPreparedStatement("Select * from "
                            + si[i].getTableName(SQLTableConstants.CODING_SCHEME));

                    ResultSet results = getCodingSchemes.executeQuery();
                    while (results.next()) {

                        CodingSchemeRendering csr = new CodingSchemeRendering();

                        csr.setCodingSchemeSummary(new CodingSchemeSummary());
                        csr.getCodingSchemeSummary().setCodingSchemeDescription(new EntityDescription());
                        csr.getCodingSchemeSummary().getCodingSchemeDescription().setContent(
                                results.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION));
                        csr.getCodingSchemeSummary().setCodingSchemeURI(
                                (si[i].supports2009Model() ? results
                                        .getString(SQLTableConstants.TBLCOL_CODINGSCHEMEURI) : results
                                        .getString(SQLTableConstants.TBLCOL_REGISTEREDNAME)));
                        csr.getCodingSchemeSummary().setFormalName(
                                results.getString(SQLTableConstants.TBLCOL_FORMALNAME));
                        csr.getCodingSchemeSummary().setLocalName(
                                results.getString(SQLTableConstants.TBLCOL_CODINGSCHEMENAME));
                        csr.getCodingSchemeSummary().setRepresentsVersion(
                                results.getString(SQLTableConstants.TBLCOL_REPRESENTSVERSION));

                        csr.setRenderingDetail(new RenderingDetail());
                        Registry registry = ResourceManager.instance().getRegistry();

                        csr.getRenderingDetail().setVersionStatus(
                                registry.getStatus(csr.getCodingSchemeSummary().getCodingSchemeURI(), csr
                                        .getCodingSchemeSummary().getRepresentsVersion()));
                        String tag = registry.getTag(csr.getCodingSchemeSummary().getCodingSchemeURI(), csr
                                .getCodingSchemeSummary().getRepresentsVersion());
                        CodingSchemeTagList tags = new CodingSchemeTagList();
                        if (tag != null && tag.length() > 0) {
                            tags.addTag(tag);
                        }
                        csr.getRenderingDetail().setVersionTags(tags);

                        csr.getRenderingDetail().setDeactivateDate(
                                registry.getDeactivateDate(csr.getCodingSchemeSummary().getCodingSchemeURI(), csr
                                        .getCodingSchemeSummary().getRepresentsVersion()));
                        csr.getRenderingDetail().setLastUpdateTime(
                                registry.getLastUpdateDate(csr.getCodingSchemeSummary().getCodingSchemeURI(), csr
                                        .getCodingSchemeSummary().getRepresentsVersion()));

                        // reference links don't appear to be fully "baked" yet.
                        // No use right now anyway.
                        csr.setReferenceLink(new ReferenceLink());
                        csr.getReferenceLink().setHref(null);
                        csr.getReferenceLink().setContent(null);

                        // Service handle has to do with the caGrid stuff.
                        csr.setServiceHandle(new ReferenceLink());
                        csr.getServiceHandle().setHref(null);
                        csr.getServiceHandle().setContent(null);

                        temp.addCodingSchemeRendering(csr);
                    }
                } finally {
                    si[i].checkInPreparedStatement(getCodingSchemes);
                }
            }

            return temp;
        } catch (Exception e) {
            String id = getLogger().error("There was an unexpected error", e);
            throw new LBInvocationException("There was an unexpected error", id);
        }
    }

    /**
     * 
     * 
     * @throws LBInvocationException
     * @throws LBParameterException
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#resolveCodingScheme(String,
     *      org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
     */
    public CodingScheme resolveCodingScheme(String codingSchemeName, CodingSchemeVersionOrTag tagOrVersion)
            throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { codingSchemeName, tagOrVersion });
        String version = null;
        if (tagOrVersion == null || tagOrVersion.getVersion() == null || tagOrVersion.getVersion().length() == 0) {
            version = ResourceManager.instance().getInternalVersionStringFor(codingSchemeName,
                    (tagOrVersion == null ? null : tagOrVersion.getTag()));
        } else {
            version = tagOrVersion.getVersion();
        }

        try {
            return SQLImplementedMethods.buildCodingScheme(ResourceManager.instance()
                    .getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeName, version), version);
        }

        catch (InternalException e) {
            throw new LBInvocationException("There was an unexpected error", e.getLogId());
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String id = getLogger().error("There was an unexpected error", e);
            throw new LBInvocationException("There was an unexpected error", id);
        }
    }

    /**
     * 
     * 
     * @throws LBInvocationException
     * @throws LBParameterException
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#resolveCodingScheme(String,
     *      org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
     */
    public String resolveCodingSchemeCopyright(String codingSchemeName, CodingSchemeVersionOrTag tagOrVersion)
            throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { codingSchemeName, tagOrVersion });
        String version = null;
        if (tagOrVersion == null || tagOrVersion.getVersion() == null || tagOrVersion.getVersion().length() == 0) {
            version = ResourceManager.instance().getInternalVersionStringFor(codingSchemeName,
                    (tagOrVersion == null ? null : tagOrVersion.getTag()));
        } else {
            version = tagOrVersion.getVersion();
        }

        try {
            return SQLImplementedMethods.getCodingSchemeCopyright(ResourceManager.instance()
                    .getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeName, version), version);
        }

        catch (InternalException e) {
            throw new LBInvocationException("There was an unexpected error", e.getLogId());
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String id = getLogger().error("There was an unexpected error", e);
            throw new LBInvocationException("There was an unexpected error", id);
        }
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#getCodingSchemeConcepts(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeURNorName,
     *      org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, boolean)
     * @deprecated
     */
    @Deprecated
    public CodedNodeSet getCodingSchemeConcepts(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            boolean activeOnly) throws LBException {
        getLogger().logMethod(new Object[] { codingScheme, versionOrTag, new Boolean(activeOnly) });
        CodedNodeSetImpl cns = new CodedNodeSetImpl(codingScheme, versionOrTag, new Boolean(activeOnly));
        cns.restrictToEntityTypes(Constructors.createLocalNameList("concept"));
        return cns;
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#getCodingSchemeConcepts(java.lang.String,
     *      org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
     */
    public CodedNodeSet getCodingSchemeConcepts(String codingScheme, CodingSchemeVersionOrTag versionOrTag)
            throws LBException {
        getLogger().logMethod(new Object[] { codingScheme, versionOrTag });
        CodedNodeSetImpl cns = new CodedNodeSetImpl(codingScheme, versionOrTag, null);
        cns.restrictToEntityTypes(Constructors.createLocalNameList("concept"));
        return cns;
    }

    /**
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#getNodeSet(java.lang.String,
     *      org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag,
     *      org.LexGrid.LexBIG.DataModel.Collections.LocalNameList)
     */
    public CodedNodeSet getNodeSet(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            LocalNameList entityTypes) throws LBException {
        getLogger().logMethod(new Object[] { codingScheme, versionOrTag, entityTypes });
        CodedNodeSetImpl cns = new CodedNodeSetImpl(codingScheme, versionOrTag, null);
        if (entityTypes != null && entityTypes.getEntryCount() > 0)
            cns.restrictToEntityTypes(entityTypes);
        return cns;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#getNodeGraph(String,
     * org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, String)
     */
    public CodedNodeGraph getNodeGraph(String codingScheme, CodingSchemeVersionOrTag tagOrVersion, String relationContainerName)
            throws LBParameterException, LBInvocationException, LBResourceUnavailableException {
        getLogger().logMethod(new Object[] { codingScheme, tagOrVersion, relationContainerName });
        String version = null;
        if (tagOrVersion == null || tagOrVersion.getVersion() == null || tagOrVersion.getVersion().length() == 0) {
            version = ResourceManager.instance().getInternalVersionStringFor(codingScheme,
                    (tagOrVersion == null ? null : tagOrVersion.getTag()));
        } else {
            version = tagOrVersion.getVersion();
        }

        try {
            return new CodedNodeGraphImpl(codingScheme, version, relationContainerName);
        } catch (LBParameterException e) {
            throw e;
        } catch (LBResourceUnavailableException e) {
            throw e;
        } catch (Exception e) {
            String id = getLogger().error("There was an unexpected error", e);
            throw new LBInvocationException("There was an unexpected error", id);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#getLastUpdateTime()
     */
    public Date getLastUpdateTime() {
        getLogger().logMethod();
        return ResourceManager.instance().getRegistry().getLastUpdateTime();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.LexBIGService#getServiceManager(Object)
     */
    @LgClientSideSafe
    public LexBIGServiceManager getServiceManager(Object credentials) throws LBParameterException,
            LBInvocationException {
        getLogger().logMethod(new Object[] { credentials });
        try {
            return ResourceManager.instance().getLexBIGServiceManager(credentials);
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String id = getLogger().error("There was an unexpected error", e);
            throw new LBInvocationException("There was an unexpected error", id);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.LexBIGService#getHistoryService(String)
     */
    @LgClientSideSafe
    public HistoryService getHistoryService(String codingScheme) throws LBParameterException {
        getLogger().logMethod(new Object[] { codingScheme });
        String urn;
        try {
            urn = ResourceManager.instance().getURNForExternalCodingSchemeName(codingScheme);
        } catch (LBParameterException e) {
            // this means that no coding scheme that was loaded could map to a
            // URN - but
            // we could still work right iff they provided a urn as the coding
            // scheme.
            urn = codingScheme;
        }

        if (urn.equals(NCIThesaurusHistorySQLQueries.NCIThesaurusURN)) {
            return new NCIThesaurusHistoryServiceImpl(urn);
        } else if (urn.equals(HistoryService.metaURN)) {
            return new UMLSHistoryServiceImpl(urn);
        } else {
            throw new LBParameterException("No history service could be located for", "codingScheme", codingScheme);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGService#getMatchAlgorithms()
     */
    @LgClientSideSafe
    public ModuleDescriptionList getMatchAlgorithms() {
        ModuleDescriptionList mdl = new ModuleDescriptionList();
        
        ExtensionDescriptionList edl = 
            ExtensionRegistryImpl.instance().getSearchExtensions();
        
        for(ExtensionDescription ed : edl.getExtensionDescription()){
            mdl.addModuleDescription(ed);
        }
        
        return mdl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.LexBIGService#getSortAlgorithms(SortContext
     * )
     */
    @LgClientSideSafe
    public SortDescriptionList getSortAlgorithms(SortContext context) {
        getLogger().logMethod(new Object[] { context });
        return ResourceManager.instance().getSortAlgorithms(context);
    }

    @LgClientSideSafe
    public GenericExtension getGenericExtension(String name) throws LBParameterException, LBInvocationException {
        getLogger().logMethod(new Object[] { name });
        try {
            MyClassLoader temp = MyClassLoader.instance();
            ExtensionDescription ed = ExtensionRegistryImpl.instance().getGenericExtension(name);

            if (ed == null) {
                throw new LBParameterException("No generic extension is available for ", "name", name);
            }
            return (GenericExtension) temp.loadClass(ed.getExtensionClass()).newInstance();
        }

        catch (Exception e) {
            String id = getLogger().error("Error getting generic extension " + name, e);
            throw new LBInvocationException("Unexpected error getting generic extension for " + name, id);
        }
    }

    @LgClientSideSafe
    public ExtensionDescriptionList getGenericExtensions() {
        getLogger().logMethod(new Object[] {});
        return ExtensionRegistryImpl.instance().getGenericExtensions();
    }

    @LgClientSideSafe
    public Filter getFilter(String name) throws LBException {
        getLogger().logMethod(new Object[] { name });
        return ExtensionRegistryImpl.instance().getFilter(name);
    }

    @LgClientSideSafe
    public ExtensionDescriptionList getFilterExtensions() {
        getLogger().logMethod(new Object[] {});
        return ExtensionRegistryImpl.instance().getFilterExtensions();
    }

    @LgClientSideSafe
    public Sort getSortAlgorithm(String name) throws LBException {
        getLogger().logMethod(new Object[] { name });
        return ExtensionRegistryImpl.instance().getSortAlgorithm(name);
    }

    @LgClientSideSafe
    public LexBIGServiceMetadata getServiceMetadata() throws LBException {
        getLogger().logMethod(new Object[] {});
        return new LexBIGServiceMetadataImpl();
    }

}