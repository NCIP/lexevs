
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
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.annotations.LgAdminFunction;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.service.SystemResourceService;

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
    
    private SystemResourceService systemResourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
    private Registry registry = LexEvsServiceLocator.getInstance().getRegistry();

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
        systemResourceService.updateCodingSchemeResourceTag(codingSchemeVersion, tag);
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
        systemResourceService.updateCodingSchemeResourceStatus(codingSchemeVersion, CodingSchemeVersionStatus.ACTIVE);
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
        systemResourceService.updateCodingSchemeResourceStatus(codingSchemeVersion, CodingSchemeVersionStatus.INACTIVE);
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
        
       RegistryEntry entry = registry.getCodingSchemeEntry(codingSchemeVersionReference);

        if (entry == null) {
            throw new LBParameterException("The specified coding scheme is not a registered coding scheme",
                    codingSchemeVersionReference.getCodingSchemeURN() + " - " + 
                    codingSchemeVersionReference.getCodingSchemeVersion());
        } else if (entry.getStatus().equals((CodingSchemeVersionStatus.ACTIVE.toString()))) {
            throw new LBParameterException("You cannot remove a 'ACTIVE' coding scheme.");
        }

        // must be marked as inactive or pending. Do the delete.
        systemResourceService.removeCodingSchemeResourceFromSystem(
                codingSchemeVersionReference.getCodingSchemeURN(), 
                codingSchemeVersionReference.getCodingSchemeVersion());
    }

    @LgAdminFunction
    public void removeHistoryService(String codingScheme) throws LBParameterException, LBInvocationException {
        getLogger().logMethod(new Object[] { codingScheme });
        String uri;
        try {
            uri = LexEvsServiceLocator.getInstance().getSystemResourceService().getUriForUserCodingSchemeName(codingScheme, null);
        } catch (LBParameterException e) {
            // this means that no coding scheme that was loaded could map to a
            // URN - but
            // we could still work right iff they provided a urn as the coding
            // scheme.
            uri = codingScheme;
        }

        LexEvsServiceLocator.getInstance().getSystemResourceService().removeNciHistoryResourceToSystemFromSystem(uri);
    }

    public ExtensionDescriptionList getLoadExtensions() {
        getLogger().logMethod(new Object[] {});
        return ExtensionRegistryImpl.instance().getLoadExtensions();
    }

    @LgAdminFunction
    public Index getIndex(String name) throws LBParameterException, LBInvocationException {
        getLogger().logMethod(new Object[] { name });
        try {
            ClassLoader temp = LexEvsServiceLocator.getInstance().getSystemResourceService().getClassLoader();
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
            ClassLoader temp = LexEvsServiceLocator.getInstance().getSystemResourceService().getClassLoader();
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
            ClassLoader temp = LexEvsServiceLocator.getInstance().getSystemResourceService().getClassLoader();
            ExtensionDescription ed = ExtensionRegistryImpl.instance().getExportExtension(name);

            if (ed == null) {
                throw new LBParameterException("No exporter is available for ", "exporterName", name);
            }
            return (Exporter) temp.loadClass(ed.getExtensionClass()).newInstance();
        }

        catch (Exception e) {
            String id = getLogger().error("Error getting exporter " + name, e);
            throw new LBInvocationException("Unexpected error getting exporter for " + name, id);
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
            LexEvsServiceLocator.getInstance().
                getIndexServiceManager().
                getMetadataIndexService().
                removeMetadata(codingSchemeVersion.getCodingSchemeURN(), codingSchemeVersion
                        .getCodingSchemeVersion());
          
        } catch (Exception e) {
            String id = getLogger().error("Problem removing metadata", e);
            throw new LBInvocationException("Unexpected error removing metadata", id);
        }
    }

    @Override
    @LgAdminFunction
    public void registerCodingSchemeAsSupplement(AbsoluteCodingSchemeVersionReference parentCodingScheme,
            AbsoluteCodingSchemeVersionReference supplementCodingScheme) throws LBException {
        LexEvsServiceLocator.getInstance().getSystemResourceService().
            registerCodingSchemeSupplement(
                    parentCodingScheme, 
                    supplementCodingScheme);
    }

    @Override
    @LgAdminFunction
    public void unRegisterCodingSchemeAsSupplement(AbsoluteCodingSchemeVersionReference parentCodingScheme,
            AbsoluteCodingSchemeVersionReference supplementCodingScheme) throws LBException {
        LexEvsServiceLocator.getInstance().getSystemResourceService().
        unRegisterCodingSchemeSupplement(
                parentCodingScheme, 
                supplementCodingScheme);
    }

    @Override
    public void shutdown() {
        LexEvsServiceLocator.getInstance().getSystemResourceService().shutdown();
    }
}