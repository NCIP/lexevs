
package edu.mayo.informatics.lexgrid.convert.utility;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.LexOnt.CsmfAssociationDefinition;
import org.LexGrid.LexOnt.CsmfCodingSchemeName;
import org.LexGrid.LexOnt.CsmfCodingSchemeURI;
import org.LexGrid.LexOnt.CsmfDefaultLanguage;
import org.LexGrid.LexOnt.CsmfEntityDescription;
import org.LexGrid.LexOnt.CsmfFormalName;
import org.LexGrid.LexOnt.CsmfLocalName;
import org.LexGrid.LexOnt.CsmfMappings;
import org.LexGrid.LexOnt.CsmfSource;
import org.LexGrid.LexOnt.CsmfText;
import org.LexGrid.LexOnt.CsmfVersion;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedLanguage;
import org.LexGrid.naming.URIMap;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.Utility;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.exolab.castor.xml.Marshaller;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;

public class ManifestUtil {

/** Holds the reference for LgMessageDirectorIF */
private LgMessageDirectorIF messages_ = LoggerFactory.getLogger();

    /**
     * This method validates and returns the manifest object for the
     * manifestURI_. Returns null if the manifestURI is invalid or the manifest
     * is invalid.
     * 
     * @param ontologyNameSpace
     * @return
     * @throws LgConvertException
     */
    public CodingSchemeManifest getManifest(URI uri) {

        CodingSchemeManifest manifest = null;
        boolean valid = isValidFile(uri);
        String schema = "http://LexGrid.org/schema/2010/01/LexOnt/CodingSchemeManifest.xsd";
        if (valid) {
            try {
                org.exolab.castor.xml.Unmarshaller um = new org.exolab.castor.xml.Unmarshaller(
                        CodingSchemeManifest.class);
                boolean validating = um.isValidating();
                manifest = (CodingSchemeManifest) um.unmarshal(new InputStreamReader(uri.toURL()
                        .openConnection().getInputStream()));

                StringWriter myWriter = new StringWriter();
                Marshaller m1 = new Marshaller(myWriter);
                m1.setNamespaceMapping("", schema);
                m1.setSchemaLocation("http://LexGrid.org/schema/2010/01/LexOnt/CodingSchemeManifest" + schema);
                m1.marshal(manifest);

            } catch (Exception e) {
               throw new RuntimeException(e);
            }
        }

        return manifest;
    }

    /**
     * Performs additional validation to see if the manifest xml file is valid
     * for its schema.
     * http://LexGrid.org/schema/LexBIG/2009/01/CodingSchemeManifest.xsd
     * 
     */
    public boolean isValidManifest(URI uri) {
        if (getManifest(uri) != null) {
            return true;
        }
        return false;
    }

    /**
     * This method validates the manifest file
     * 
     * @param uri
     * @return boolean if the URI indicates valid file.
     */
    private boolean isValidFile(URI uri) {
        boolean isValid = false;

        try {
            if (uri != null) {
                if (uri.getScheme().equals("file")) {
                    new FileReader(new File(uri));
                } else {
                    new InputStreamReader(uri.toURL().openConnection().getInputStream());
                }

                isValid = true;
            }
        }

        catch (Exception e) {
            messages_.fatal("Validation Error" + ":" + e.getMessage());
        }

        return isValid;
    }

    /**
     * Method applies the given manifest data to an EMF-based CodingScheme based
     * on the suitable flag values and conditions.
     * 
     * @param manifest
     * @param emfCodingScheme
     * @throws LgConvertException
     */
    protected void doApplyCommonManifestElements(CodingSchemeManifest manifest, CodingScheme codingScheme, boolean postLoad) {

        if (manifest == null || codingScheme == null) {
            return;
        }

        Mappings emfMappings = codingScheme.getMappings();
        if (emfMappings == null) {
            emfMappings = new Mappings(); //TODO Check if this works?
            codingScheme.setMappings(emfMappings);
        }

    
        // set FormalName
        CsmfFormalName frmlName = manifest.getFormalName();
        if (frmlName != null)
            setFormalName(codingScheme, frmlName.getContent(), frmlName.getToOverride().booleanValue());

        if(!postLoad) {
            // set Registered Name
            CsmfCodingSchemeURI regName = manifest.getCodingSchemeURI();
            if (regName != null)
                setRegisteredName(codingScheme, regName.getContent(), regName.getToOverride().booleanValue());

            // Set CodingScheme
            CsmfCodingSchemeName csName = manifest.getCodingScheme();
            if (csName != null) {
                setCodingScheme(codingScheme, csName.getContent(), csName.getToOverride().booleanValue());
            }
            
            // set Represents Version
            CsmfVersion version = manifest.getRepresentsVersion();
            if (version != null) {
                setRepresentsVersion(codingScheme, version.getContent(), version.getToOverride().booleanValue());
            }
        }

        // set Entity Description
        CsmfEntityDescription entDesc = manifest.getEntityDescription();
        if (entDesc != null)
            setEntityDescription(codingScheme, entDesc.getContent(), entDesc.getToOverride().booleanValue());

        // set Default Language
        CsmfDefaultLanguage defLang = manifest.getDefaultLanguage();
        if (defLang != null)
            setDefaultLanguage(codingScheme, defLang.getContent(), defLang.getToOverride().booleanValue());

        // set Copyright Text
        CsmfText txt = manifest.getCopyright();
        if (txt != null)
            setCopyrightText(codingScheme, txt.getContent(), txt.getToOverride().booleanValue());

        // Add Sources
        preLoadAddSources(codingScheme, manifest.getSource());

        // Add Local Names
        preLoadAddLocalNames(codingScheme, manifest.getLocalName());

        // Transfer Mappings
        preLoadAddSupportedMappings(codingScheme, manifest.getMappings());
        
        if(!postLoad) {
            preLoadAssociationDefinitions(codingScheme, manifest.getAssociationDefinitions());
        }
    } 
    
    public void applyManifest(
            CodingSchemeManifest manifest, 
           CodingScheme codingScheme){
        if (manifest == null || codingScheme == null) {
            return;
        }
        
        this.doApplyCommonManifestElements(manifest, codingScheme, false);
        this.preLoadAssociationDefinitions(codingScheme, manifest.getAssociationDefinitions());
    }

    /**
     * Applies the given manifest to an existing coding scheme definition in a
     * SQL-based repository.
     * 
     * @param manifest
     * @param sqlConfig
     * @param tablePrefix
     * @param failOnAllErrors
     * @param messages
     * @param codingSchemes
     * @throws LgConvertException
     * @throws SQLException
     */
    public void applyManifest(
            CodingSchemeManifest manifest, 
            URNVersionPair versionPair) throws LgConvertException {
        if (manifest == null || versionPair == null) {
            return;
        }
        
        if(manifest.getCodingSchemeURI() != null) {
            LoggerFactory.getLogger().warn("Coding Scheme URI cannot be changed Post-Load.");
        }
        
        if(manifest.getCodingScheme() != null) {
            LoggerFactory.getLogger().warn("Coding Scheme Name cannot be changed Post-Load.");
        }
        
        if(manifest.getRepresentsVersion() != null) {
            LoggerFactory.getLogger().warn("Coding Scheme Version cannot be changed Post-Load.");
        }
        
        CodingSchemeService codingSchemeService = 
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodingSchemeService();

        final String uri = versionPair.getUrn();
        final String version = versionPair.getVersion();

        final CodingScheme codingScheme = DaoUtility.deepClone(codingSchemeService.getCodingSchemeByUriAndVersion(uri, version));
        
        codingScheme.setRelations(new Relations[0]);
        codingScheme.setProperties(new Properties());
        codingScheme.setEntities(new Entities());

        String revisionId = UUID.randomUUID().toString();
        
        EntryState es = new EntryState();
        es.setChangeType(ChangeType.MODIFY);
        es.setContainingRevision(revisionId);
        
        codingScheme.setEntryState(es);

        try {
            
            this.doApplyCommonManifestElements(manifest, codingScheme, true);
            this.postLoadAssociationDefinitions(codingScheme, manifest.getAssociationDefinitions());
            
            Revision revision = new Revision();
            revision.setRevisionId(revisionId);
            ChangedEntry ce = new ChangedEntry();
            ce.setChangedCodingSchemeEntry(codingScheme);
            revision.addChangedEntry(ce);
       
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService().loadRevision(revision, null, false);
        } catch (LBException e) {
           throw new RuntimeException(e);
        }
    }
    
    protected AssociationEntity findAssociationEntityInDatabase(String uri, String version, String code, String namespace) {
        Entity entity = LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().getEntityService().
                getEntity(uri, version, code, namespace);
        
        if(entity != null && entity instanceof AssociationEntity){
            return (AssociationEntity)entity;
        } else {
            return null;
        }
    }


    /**
     * This method sets the FormalName into CodingScheme.
     * 
     * @param fmlName
     * @param override
     */
    private void setFormalName(CodingScheme codingScheme, String fmlName, boolean override) {
        if (isNoop(fmlName))
            return;

        if ((override) || (isNoop(codingScheme.getFormalName())))
            codingScheme.setFormalName(fmlName.trim());
    }

    /**
     * This method sets the CodingScheme into CodingScheme.
     * 
     * @param csName
     * @param override
     */
    private void setCodingScheme(CodingScheme codingScheme, String csName, boolean override) {
        if (isNoop(csName))
            return;

        if ((override) || (isNoop(codingScheme.getCodingSchemeName())))
            codingScheme.setCodingSchemeName(csName.trim());
    }

    /**
     * This method sets the RegisteredName into CodingScheme.
     * 
     * @param regName
     * @param override
     */
    private void setRegisteredName(CodingScheme codingScheme, String regName, boolean override) {
        if (isNoop(regName))
            return;

        if ((override) || (isNoop(codingScheme.getCodingSchemeURI()))) {
            codingScheme.setCodingSchemeURI(regName.trim());
            List<SupportedCodingScheme> suppCodingSchemeList = null;
            
            if (codingScheme.getMappings() != null)
                suppCodingSchemeList = new ArrayList<SupportedCodingScheme>(
                        Arrays.asList(codingScheme.getMappings().getSupportedCodingScheme()));
            
            boolean urnPresent = false;
            for (int i = 0; i < suppCodingSchemeList.size(); i++) {
                SupportedCodingScheme suppCodingScheme = (SupportedCodingScheme) suppCodingSchemeList
                        .get(i);
                if (suppCodingScheme.getLocalId().equals(codingScheme.getCodingSchemeName())) {
                    urnPresent = true;
                    suppCodingScheme.setUri(regName);
                }
            }
            if (!urnPresent) {
                SupportedCodingScheme suppCodingScheme = new SupportedCodingScheme();
                suppCodingScheme.setLocalId(codingScheme.getCodingSchemeName());
                suppCodingScheme.setUri(regName);
                suppCodingSchemeList.add(suppCodingScheme);
            }
        }
    }

    /**
     * This method sets the DefaultLanguage into CodingScheme.
     * 
     * @param lang
     * @param override
     */
    private void setDefaultLanguage(CodingScheme codingScheme, String lang, boolean override) {

        if (isNoop(lang))
            return;

        if ((override) || isNoop(codingScheme.getDefaultLanguage())) {
            codingScheme.setDefaultLanguage(lang.trim());
            List<SupportedLanguage> suppLanguageList = null;
            
            if (codingScheme.getMappings() != null)
                suppLanguageList = new ArrayList<SupportedLanguage>(
                        Arrays.asList(codingScheme.getMappings().getSupportedLanguage()));
            
            boolean urnPresent = false;

            for (int i = 0; i < suppLanguageList.size(); i++) {
                SupportedLanguage suppLanguage = (SupportedLanguage) suppLanguageList
                        .get(i);
                if (suppLanguage.getLocalId().equals(codingScheme.getDefaultLanguage())) {
                    urnPresent = true;
                    suppLanguage.setUri(codingScheme.getCodingSchemeURI());
                }
            }

            if (!urnPresent) {
                SupportedLanguage suppLanguage =  new SupportedLanguage();
                suppLanguage.setLocalId(codingScheme.getDefaultLanguage());
                suppLanguage.setUri(codingScheme.getCodingSchemeURI());
                suppLanguageList.add(suppLanguage);
            }
        }
    }

    /**
     * This method sets the RepresentsVersion into CodingScheme.
     * 
     * @param version
     * @param override
     */
    private void setRepresentsVersion(CodingScheme codingScheme, String version, boolean override) {

        if (isNoop(version))
            return;

        if ((override) || isNoop(codingScheme.getRepresentsVersion())) {
            codingScheme.setRepresentsVersion(version.trim());
            return;
        }
    }

    /**
     * This method sets the CopyrightText into CodingScheme.
     * 
     * @param text
     * @param override
     */
    private void setCopyrightText(CodingScheme codingScheme, String text, boolean override) {
        if (isNoop(text))
            return;

        String copyright = null;

        if (codingScheme.getCopyright() != null)
            copyright = codingScheme.getCopyright().getContent();

        if ((override) || isNoop(copyright)) {
            Text txt = new Text();
            txt.setContent((String) text.trim());
            codingScheme.setCopyright(txt);
            return;
        }

    }

    /**
     * This method sets the Entity Description into CodingScheme.
     * 
     * @param desc
     * @param override
     */
    private void setEntityDescription(CodingScheme codingScheme, String desc, boolean override) {
        if (isNoop(desc))
            return;

        if ((override) || isNoop(codingScheme.getEntityDescription().toString())) {
            EntityDescription ed = new EntityDescription();
            ed.setContent(desc);
            codingScheme.setEntityDescription(ed);
        }
    }

    /**
     * Indicates whether the given string represents a null or empty resource.
     * 
     * @param s
     * @return boolean
     */
    private boolean isNoop(String s) {
        return s == null || s.equalsIgnoreCase("null") || s.trim().length() == 0;
    }

    /**
     * This method adds the LocalNames details from manifest into CodingScheme.
     * 
     * @param castorLocalNames
     */
    protected void preLoadAddLocalNames(CodingScheme codingScheme, CsmfLocalName[] castorLocalNames) {

        if (castorLocalNames != null) {

            List<String> emfLocalNames = Arrays.asList(codingScheme.getLocalName());
            boolean present = false;

            for (int i = 0; i < castorLocalNames.length; i++) {
                // if toUpdate = true then add otherwise only add when CS does
                // not have any local names
                if (castorLocalNames[i] != null && StringUtils.isNotBlank(castorLocalNames[i].getContent())) {

                    present = false;

                    if ((castorLocalNames[i].getToAdd().booleanValue()) || codingScheme.getLocalNameCount() == 0) {

                        for (int j = 0; j < emfLocalNames.size(); j++) {

                            if (emfLocalNames.get(j).equalsIgnoreCase(Utility.trim(castorLocalNames[i].getContent()))) {
                                present = true;
                                break;
                            }
                        }

                        if (!present) {
                            codingScheme.addLocalName(Utility.trim(castorLocalNames[i].getContent()));
                        }
                    }
                }
            }
        }
    }
    
    protected void postLoadAssociationDefinitions(CodingScheme codingScheme, CsmfAssociationDefinition assocDefinitions) throws LBException {
        if(assocDefinitions == null) {return;}
        
        EntityService entityService = 
            LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().
            getEntityService();

        String uri = codingScheme.getCodingSchemeURI();
        String version = codingScheme.getRepresentsVersion();

        for(AssociationEntity manifestEntity : assocDefinitions.getAssoc()) {
            String code = manifestEntity.getEntityCode();
            String namespace = manifestEntity.getEntityCodeNamespace();

            AssociationEntity originalAssocEntity = 
                findAssociationEntityInDatabase(
                        uri, version, code, namespace);
            
            if(originalAssocEntity == null) {
                LoggerFactory.getLogger().warn(
                        "No AssociationEntity was found for the Code: " + code + " Namespace: " + namespace + ". No changes will be made.");
                continue;
            }
            
            originalAssocEntity.setComment(new Comment[0]);
            originalAssocEntity.setPresentation(new Presentation[0]);
            originalAssocEntity.setProperty(new Property[0]);
            originalAssocEntity.setDefinition(new Definition[0]);

            if(originalAssocEntity == null) {
                manifestEntity.addEntityType(EntityTypes.ASSOCIATION.toString());
                entityService.insertEntity(uri, version, manifestEntity);
            } else {
                if(assocDefinitions.getToUpdate()) {
                    DaoUtility.updateBean(manifestEntity, originalAssocEntity);
                    if(originalAssocEntity.getEntryState() == null) {
                        EntryState es = new EntryState();
                        es.setChangeType(ChangeType.MODIFY);
                        es.setContainingRevision(UUID.randomUUID().toString());
                        originalAssocEntity.setEntryState(es);
                    }
                    entityService.updateEntity(uri, version, (Entity)originalAssocEntity);
                }
            }
        } 
    }
    
    protected void preLoadAssociationDefinitions(CodingScheme codingScheme, CsmfAssociationDefinition assocDefinitions) {
        if(assocDefinitions == null) {return;}

        for(AssociationEntity manifestEntity : assocDefinitions.getAssoc()) {
            String code = manifestEntity.getEntityCode();
            String namespace = manifestEntity.getEntityCodeNamespace();

            AssociationEntity originalAssocEntity = findAssociationEntityInCodingScheme(code, namespace, codingScheme.getEntities());

            if(originalAssocEntity == null) {
                if(codingScheme.getEntities() == null) {
                    codingScheme.setEntities(new Entities());
                }
                codingScheme.getEntities().addAssociationEntity(manifestEntity);
            } else {
                if(assocDefinitions.getToUpdate()) {
                    DaoUtility.updateBean(manifestEntity, originalAssocEntity);
                }
            }
        }
    }
    
    protected AssociationEntity findAssociationEntityInCodingScheme(String code, String namespace, Entities entities) {
        if(entities == null) {return null;}
        
        for(Entity entity : entities.getEntity()) {
            if(entitiesAreEquals(entity, code, namespace)){
                if(entity instanceof AssociationEntity) {
                    return (AssociationEntity)entity;
                }
            }
        }
        
        return null;
    }
        
        
    
    private boolean entitiesAreEquals(Entity entity, String code, String namespace) {
        if(! entity.getEntityCode().equals(code)){
            return false;
        }
        if(namespace !=null && entity.getEntityCodeNamespace() != null){
            if(! entity.getEntityCodeNamespace().equals(namespace)){
                return false;
            }
        }
        return true;
    }

    protected void preLoadAddSources(CodingScheme codingScheme, CsmfSource[] sources) {

        if (sources != null) {
            for(CsmfSource manifestSource : sources) {
                Source originalSource = findSourceByLocalId(
                        codingScheme.getSource(), manifestSource.getContent());
                
                if(originalSource == null) {
                    codingScheme.addSource(manifestSource);
                } else {
                    if(manifestSource.getToUpdate()) {
                       DaoUtility.updateBean(manifestSource, originalSource);
                    }
                }
            }
        }
    }
    
    protected void preLoadAddSupportedMappings(CodingScheme codingScheme, CsmfMappings manifestMappings) {
        if(manifestMappings == null) {return;}
        
        List<URIMap> originalUriMaps = DaoUtility.getAllURIMappings(codingScheme.getMappings());
        List<URIMap> manifestUriMaps = DaoUtility.getAllURIMappings(manifestMappings);

        if (manifestUriMaps != null) {
            for(URIMap manifestUriMap : manifestUriMaps) {
                
                URIMap originalUriMap = findUriMapByTypeAndLocalId(originalUriMaps, manifestUriMap);
                
                if(originalUriMap == null) {
                    DaoUtility.insertIntoMappings(codingScheme.getMappings(), manifestUriMap);
                } else {
                    if(manifestMappings.getToUpdate()) {
                       DaoUtility.updateBean(manifestUriMap, originalUriMap);
                    }
                }
            }
        }
    }
    
    private URIMap findUriMapByTypeAndLocalId(List<URIMap> uriMaps, URIMap neededMap) {
        if(uriMaps == null || uriMaps.size() == 0) {
            return null;
        }
        
        for(URIMap uriMap : uriMaps) {
            if(uriMap.getLocalId().equals(neededMap.getLocalId()) && uriMap.getClass().getName().equals(neededMap.getClass().getName())) {
                return uriMap;
            }
        }
        
        return null;
    }
    
    private URIMap findUriMapByLocalId(List<URIMap> uriMaps, String localId) {
        if(uriMaps == null || uriMaps.size() == 0) {
            return null;
        }
        
        for(URIMap uriMap : uriMaps) {
            if(uriMap.getLocalId().equals(localId)) {
                return uriMap;
            }
        }
        
        return null;
    }
    
    private Source findSourceByLocalId(Source[] sources, String localId) {
        if(sources == null || sources.length == 0) {
            return null;
        }
        
        for(Source source : sources) {
            if(source.getContent().equals(localId)) {
                return source;
            }
        }
        
        return null;
    }
}