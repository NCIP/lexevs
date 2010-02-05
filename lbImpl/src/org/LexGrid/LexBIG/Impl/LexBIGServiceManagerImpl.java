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

import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Export.Exporter;
import org.LexGrid.LexBIG.Extensions.Index.Index;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.dataAccess.ResourceManager;
import org.LexGrid.LexBIG.Impl.helpers.MyClassLoader;
import org.LexGrid.LexBIG.Impl.loaders.metadata.BaseMetaDataLoader;
import org.LexGrid.LexBIG.Impl.logging.LgLoggerIF;
import org.LexGrid.LexBIG.Impl.logging.LoggerFactory;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.annotations.LgAdminFunction;

/**
 * This class implements the LexBigServiceManager.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LexBIGServiceManagerImpl implements LexBIGServiceManager {
    private static final long serialVersionUID = -546654153157636317L;

    private LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.LexBigServiceManager#setVersionTag(org
     * .LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference,
     * java.lang.String)
     */
    @LgAdminFunction
    public void setVersionTag(AbsoluteCodingSchemeVersionReference codingSchemeVersion, String tag)
            throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { codingSchemeVersion, tag });
        ResourceManager.instance().updateTag(codingSchemeVersion, tag);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.LexGrid.LexBIG.LexBIGService.LexBigServiceManager#
     * activateCodingSchemeVersion
     * (org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
     */
    @LgAdminFunction
    public void activateCodingSchemeVersion(AbsoluteCodingSchemeVersionReference codingSchemeVersion)
            throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { codingSchemeVersion });
        ResourceManager.instance().getRegistry().activate(codingSchemeVersion);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.LexGrid.LexBIG.LexBIGService.LexBigServiceManager#
     * deactivateCodingSchemeVersion
     * (org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference,
     * java.lang.String)
     */
    @LgAdminFunction
    public void deactivateCodingSchemeVersion(AbsoluteCodingSchemeVersionReference codingSchemeVersion, Date date)
            throws LBInvocationException, LBParameterException {
        getLogger().logMethod(new Object[] { codingSchemeVersion });
        ResourceManager.instance().deactivate(codingSchemeVersion, date);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.LexGrid.LexBIG.LexBIGService.LexBigServiceManager#
     * removeCodingSchemeVersion
     * (org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
     */
    @LgAdminFunction
    public void removeCodingSchemeVersion(AbsoluteCodingSchemeVersionReference codingSchemeVersionReference)
            throws LBParameterException, LBInvocationException {
        getLogger().logMethod(new Object[] { codingSchemeVersionReference });
        ResourceManager rm = ResourceManager.instance();
        CodingSchemeVersionStatus status = rm.getRegistry().getStatus(
                codingSchemeVersionReference.getCodingSchemeURN(),
                codingSchemeVersionReference.getCodingSchemeVersion());

        if (status == null) {
            throw new LBParameterException("The specified coding scheme is not a registered coding scheme",
                    "codingSchemeVersionReference");
        } else if (status == CodingSchemeVersionStatus.ACTIVE) {
            throw new LBParameterException("You cannot remove a 'ACTIVE' coding scheme.");
        }

        // must be marked as inactive or pending. Do the delete.
        rm.removeCodeSystem(codingSchemeVersionReference);
    }

    @LgAdminFunction
    public void removeHistoryService(String codingScheme) throws LBParameterException, LBInvocationException {
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

        ResourceManager.instance().removeHistoryService(urn);
    }

    public ExtensionDescriptionList getLoadExtensions() {
        getLogger().logMethod(new Object[] {});
        return ExtensionRegistryImpl.instance().getLoadExtensions();
    }

    @LgAdminFunction
    public Index getIndex(String name) throws LBParameterException, LBInvocationException {
        getLogger().logMethod(new Object[] { name });
        try {
            MyClassLoader temp = MyClassLoader.instance();
            ExtensionDescription ed = ExtensionRegistryImpl.instance().getIndexExtension(name);

            if (ed == null) {
                throw new LBParameterException("No indexer is available for ", "loaderName", name);
            }
            return (Index) temp.loadClass(ed.getExtensionClass()).newInstance();
        } catch (Exception e) {
            String id = getLogger().error("Error getting index " + name, e);
            throw new LBInvocationException("Unexpected error getting index for " + name, id);
        }
    }

    public ExtensionDescriptionList getIndexExtensions() {
        getLogger().logMethod(new Object[] {});
        return ExtensionRegistryImpl.instance().getIndexExtensions();
    }

    @LgAdminFunction
    public Loader getLoader(String name) throws LBParameterException, LBInvocationException {
        getLogger().logMethod(new Object[] { name });
        try {
            MyClassLoader temp = MyClassLoader.instance();
            ExtensionDescription ed = ExtensionRegistryImpl.instance().getLoadExtension(name);

            if (ed == null) {
                throw new LBParameterException("No loader is available for ", "loaderName", name);
            }
            return (Loader) temp.loadClass(ed.getExtensionClass()).newInstance();
        }

        catch (Exception e) {
            String id = getLogger().error("Error getting loader " + name, e);
            throw new LBInvocationException("Unexpected error getting loader for " + name, id);
        }
    }

    public ExtensionRegistry getExtensionRegistry() {
        getLogger().logMethod(new Object[] {});
        return ExtensionRegistryImpl.instance();
    }

    public ExtensionDescriptionList getExportExtensions() {
        getLogger().logMethod(new Object[] {});
        return ExtensionRegistryImpl.instance().getExportExtensions();
    }

    public Exporter getExporter(String name) throws LBException {
        getLogger().logMethod(new Object[] { name });
        try {
            MyClassLoader temp = MyClassLoader.instance();
            ExtensionDescription ed = ExtensionRegistryImpl.instance().getExportExtension(name);

            if (ed == null) {
                throw new LBParameterException("No loader is available for ", "loaderName", name);
            }
            return (Exporter) temp.loadClass(ed.getExtensionClass()).newInstance();
        }

        catch (Exception e) {
            String id = getLogger().error("Error getting loader " + name, e);
            throw new LBInvocationException("Unexpected error getting loader for " + name, id);
        }
    }

    @LgAdminFunction
    public void removeCodingSchemeVersionMetaData(AbsoluteCodingSchemeVersionReference codingSchemeVersion)
            throws LBException {
        getLogger().logMethod(new Object[] { codingSchemeVersion });
        if (codingSchemeVersion == null || codingSchemeVersion.getCodingSchemeURN() == null
                || codingSchemeVersion.getCodingSchemeURN().length() == 0
                || codingSchemeVersion.getCodingSchemeVersion() == null
                || codingSchemeVersion.getCodingSchemeVersion().length() == 0) {
            throw new LBParameterException("The coding scheme URN and version must be supplied.");
        }
        try {
            BaseMetaDataLoader.removeMeta(codingSchemeVersion.getCodingSchemeURN(), codingSchemeVersion
                    .getCodingSchemeVersion());
        } catch (Exception e) {
            String id = getLogger().error("Problem removing metadata", e);
            throw new LBInvocationException("Unexpected error removing metadata", id);
        }
    }
}