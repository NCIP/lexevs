/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedContainerName;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class MRMAP2LexGrid {
    //services
    AuthoringService service;
    DatabaseServiceManager dbManager;
    
    private List<String> propertyNames;
    private String mapPath;
    private String satPath;
    private LgMessageDirectorIF messages_;
    boolean mapMrSat;
    boolean isNewMapping = false;
    private HashSet<String> sources;
    
    //Supported Attribute Values
    private String nameForMappingScheme;
    private String nameForMappingVersion;
    private String nameforMappingURI;
    private String sourceScheme;
    private String sourceVersion;
    private String sourceURI;
    private String targetScheme;
    private String targetVersion;
    private String targetURI;

    
    //constants
    public static final String ASSOC_NAME = "mapped_to";
    public static final String APROX_ASSOC_NAME = "approximately_mapped_to";
    public static final boolean ISMAP = true;
    public static final String URIPREFIX = "urn:oid";
    
    //relations constants
    public static final String TORSAB = "TORSAB";
    public static final String TOVSAB = "TOVSAB";
    public static final String FROMRSAB =  "FROMRSAB";
    public static final String FROMVSAB = "FROMVSAB";
    public static final String MAPSETVERSION =  "MAPSETVERSION";
    public static final String SOS = "SOS";
    public static final String MAPSETNAME = "MAPSETNAME";

    //coding scheme constants
    public static final String CODING_SCHEME_NAME = "MappingCodingScheme";
    public static final String CODING_SCHEME_URI = "http://does.not.resolve";
    public static final String REPRESENTS_VERSION = "1.0";
    
    public MRMAP2LexGrid(
            LgMessageDirectorIF messages,
            String mrSatPath, String mrMapPath){
        this(
                messages,
                 mrSatPath,
                 mrMapPath,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }
    
    public MRMAP2LexGrid(
            LgMessageDirectorIF messages,
            String mrSatPath, String mrMapPath,
            String sourceCodingScheme, String sourceVersion, String sourceURI, 
            String targetCodingScheme, String targetVersion, String targetURI){
        this(
                messages,
                 mrSatPath,
                 mrMapPath,
                null,
                null,
                null,
                sourceCodingScheme,
                sourceVersion,
                sourceURI,
                targetCodingScheme,
                targetVersion,
                targetURI);
    }
    public MRMAP2LexGrid(
        LgMessageDirectorIF messages,
        String mrSatPath, String mrMapPath,
        String nameForMappingScheme,
        String nameForMappingVersion,
        String nameforMappingURI,
        String sourceScheme,
        String sourceVersion,
        String sourceURI,
        String targetScheme,
        String targetVersion,
        String targetURI){
        messages_ = messages;
        sources = new HashSet<String>();
        propertyNames = Arrays.asList(new String[]{
        	"MAPSETGRAMMER",
            "MAPSETRSAB",
            "MAPSETTYPE",
            "MAPSETVSAB",
            "MTH_MAPFROMEXHAUSTIVE",
            "MTH_MAPSETCOMPLEXITY",
            "MTH_MAPTOEXHAUSTIVE",
            "MTH_MAPFROMCOMPLEXITY", 
            "MTH_MAPTOCOMPLEXITY",
            "MR","DA","ST"});
        mapPath = mrMapPath;
        satPath = mrSatPath;
        this.nameForMappingScheme = nameForMappingScheme;
        this.nameForMappingVersion = nameForMappingVersion;
        this.nameforMappingURI = nameforMappingURI;
        this.sourceScheme = sourceScheme;
        this.sourceVersion = sourceVersion;
        this.sourceURI = sourceURI;
        this.targetScheme = targetScheme;
        this.targetVersion = targetVersion ;
        this.targetURI = targetURI;
        LexEvsServiceLocator locator = LexEvsServiceLocator.getInstance();
        dbManager = locator.getDatabaseServiceManager();
        service = dbManager.getAuthoringService();
    }

    public CodingScheme[] loadToRevision() throws LBRevisionException{
       CodingScheme[] schemes = processMrMapToLexGrid();
       CodingScheme[] schemesMinimal = new CodingScheme[schemes.length];
       for(int i = 0; i < schemes.length; i++){
           CodingScheme schemeToReturn = new CodingScheme();
           schemeToReturn.setCodingSchemeName(schemes[i].getCodingSchemeName());
           schemeToReturn.setRepresentsVersion(schemes[i].getRepresentsVersion());
           schemeToReturn.setCodingSchemeURI(schemes[i].getCodingSchemeURI());
           schemesMinimal[i] = schemeToReturn;
           service.loadRevision(schemes[i], null, true);

       }
   
       return  schemesMinimal;
    }
    
    public CodingScheme[] processMrMapToLexGrid() {

       Relations rel = null;
        HashMap<String, Relations> relationsMap;
        ArrayList<CodingScheme> schemes = new ArrayList<CodingScheme>();
        try {
        relationsMap = processMrSatBean(satPath, mapPath);
        Object[] os = relationsMap.keySet().toArray();

        for(Object o: os){
           rel = relationsMap.get(o);
           if(rel.isIsMapping() != null && rel.isIsMapping()){
            rel.addAssociationPredicate(processMrMapBean(mapPath, rel.getSourceCodingScheme(), rel.getTargetCodingScheme(), (String)o));
            CodingScheme scheme = createMrMapScheme(rel,
                    nameForMappingScheme,
                    nameForMappingVersion,
                    nameforMappingURI,
                    sourceScheme,
                    sourceVersion,
                    sourceURI,
                    targetScheme,
                    targetVersion,
                    targetURI);
            schemes.add(scheme);
           }
        }

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        CodingScheme[] schemesToReturn = new CodingScheme[schemes.size()];
        for(int i = 0; i < schemes.size(); i++){
        schemesToReturn[i]= schemes.get(i);
        }
        return schemesToReturn;
    }
    
    
    protected HashMap<String, Relations> processMrSatBean(String sPath, String mPath) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, FileNotFoundException {
        RRFLineReader satReader = new RRFLineReader(sPath);
        String[] mrSatRow;
        HashMap<String, Relations> relationsMap = null;
        int lineCount = 0;
        int modCount = 0;
        try {
         relationsMap = processRelationsContainers(mPath);
         messages_.info("Searching MRSAT for mapping metadata");
            while((mrSatRow = satReader.readRRFLine()) != null){
                lineCount++;
                MrSat metaData = processMrSatRow(mrSatRow, lineCount);
               if (relationsMap.containsKey(metaData.getCui())){
               processMrSatToRelation(metaData, relationsMap.get(metaData.getCui()));
                }
               if (lineCount% 100000 == 99999){
                   modCount = modCount + 100000;
                   messages_.debug("MRSAT lines processed: " + modCount);
               }
            }
            satReader.close();
            messages_.info("Finished Searching MRSAT for mapping data");
        } catch (IOException e) {

            e.printStackTrace();
        }
        return relationsMap;
    }

    protected void processMrSatToRelation(MrSat metaData, Relations relation) {
        
        if(relation.getProperties() == null){
            Properties properties = new Properties();
            relation.setProperties(properties);
        }
        if(relation.getContainerName() == null){
            relation.setContainerName(metaData.getCui());
        }
        if(relation.getOwner() == null){
            relation.setOwner(metaData.getSab());
        }
        if(relation.getIsMapping() == null){
        relation.setIsMapping(ISMAP);}
        String atnValue = metaData.getAtn();
        if(propertyNames.contains(atnValue)){
            Property prop = new Property();
            prop.setPropertyName(metaData.getAtn());
            Text value = new Text();
            value.setContent(metaData.getAtv());
            prop.setValue(value);
            relation.getProperties().addProperty(prop);
        }
        if(atnValue.equals(TORSAB)){
            relation.setTargetCodingScheme(metaData.getAtv());
        }
        if(atnValue.equals(TOVSAB)){
            relation.setTargetCodingSchemeVersion(metaData.getAtv());
        }
        if(atnValue.equals(FROMRSAB)){
            relation.setSourceCodingScheme(metaData.getAtv());
        }
        if(atnValue.equals(FROMVSAB)){
            relation.setSourceCodingSchemeVersion(metaData.getAtv());
        }
        if(atnValue.equals(MAPSETVERSION)){
            relation.setRepresentsVersion(metaData.getAtv());
        }
        if(atnValue.equals(SOS) || atnValue.equals(MAPSETNAME)){
            EntityDescription entityDescription = new EntityDescription();
            entityDescription.setContent(metaData.getAtv());
            relation.setEntityDescription(entityDescription);
        }
    }
    
    //TODO create JUnit
    protected AssociationPredicate processMrMapBean(String path, String sourceSchemeNamespace, String targetSchemeNamespace, String currentRelation) throws Exception {
        String[] mrMapRow;
        RRFLineReader mapReader = new RRFLineReader(path);
        AssociationPredicate predicate  = createAssociationPredicate();

            try {
                messages_.info("Processing MRMAP mappings");
                while((mrMapRow = mapReader.readRRFLine()) != null){
                    MrMap map = processMrMapRow(mrMapRow);
                    if(currentRelation.equals(map.getMapsetcui())){
                        if(map.getFromid()!= null && map.getToexpr() != null&& !map.getToexpr().equals("")){
                        processAndMergeIntoSource(map, predicate, sourceSchemeNamespace, targetSchemeNamespace);
                        }
                        else{
                            messages_.warn("Mapping source: " + map.getFromid() +  " or target: " 
                                    + map.getToexpr() + " is empty -- skipping relation");
                           
                        }
                    }
                }
                messages_.info("Finished Processing MRMAP mappings");
                mapReader.close();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }   
     
            return predicate;
    }
    protected HashMap<String, Relations> processRelationsContainers(String mapPath) throws IOException{
        HashMap<String, Relations> relations = new HashMap<String, Relations>();
        RRFLineReader mapReader = new RRFLineReader(mapPath);
        String[] mrMapRow;
            while((mrMapRow = mapReader.readRRFLine()) != null){
                if(!relations.containsKey(mrMapRow[0])){
                    Relations rel = new Relations();
                    rel.setContainerName(mrMapRow[0]);
                    relations.put(mrMapRow[0], rel);
                }
            }
        
        return relations;
    }
    private AssociationPredicate processAndMergeIntoSource(MrMap map, AssociationPredicate predicate, String sourceEntityCodeNamespace, String targetEntityCodeNamespace) throws Exception  {

        if(sources.add(map.getFromid())){
           AssociationSource source = createNewAssociationSourceWithTarget(map,targetEntityCodeNamespace);
           source.setSourceEntityCodeNamespace(sourceEntityCodeNamespace);
           predicate.addSource(source);
           return predicate;
        }
        else{
            
    
            return addTargetToExistingSource(map, predicate, sourceEntityCodeNamespace,targetEntityCodeNamespace);
        }
       
    }

    private AssociationPredicate createAssociationPredicate() {
        AssociationPredicate predicate  = new AssociationPredicate();
        predicate.setAssociationName(ASSOC_NAME);
        return predicate;
    }
    
    protected AssociationSource createNewAssociationSourceWithTarget(MrMap map, String targetEntityCodeNamespace)
            throws LBParameterException, IndexOutOfBoundsException, IllegalArgumentException, IllegalAccessException,
            ClassNotFoundException {
        AssociationSource source = new AssociationSource();
        source.setSourceEntityCode(map.getFromid());
        source.addTargetData(createTargetData(map));
        source.addTarget(createAssociationTarget(map, targetEntityCodeNamespace));

        return source;
    }

    protected AssociationTarget createAssociationTarget(MrMap map, String targetEntityCodeNamespace)
            throws LBParameterException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
        AssociationTarget target = new AssociationTarget();
        target.setTargetEntityCodeNamespace(targetEntityCodeNamespace);
        target.setAssociationInstanceId(map.getMapid());
        target.setTargetEntityCode(getFirstParsableEntityCode(map.getToexpr()));
        target.setAssociationQualification(getAssociationQualifiers(map));
        return target;
    }
    
    private String getFirstParsableEntityCode(String parseTarget) {
        
        String s = "<";
        String and = " AND";
        String emptyString = "";
        if(parseTarget.contains(s)){
        int index = parseTarget.indexOf(">");
        parseTarget = parseTarget.substring(0, index);
        parseTarget = parseTarget.replace("<", "");}
        if (parseTarget.contains(and)){
          int index =  parseTarget.indexOf(and);
          parseTarget = parseTarget.substring(0, index);}
        return parseTarget;
    }
    protected AssociationData createTargetData(MrMap map) {
        AssociationData data = new AssociationData();
        if(map.getMapid() != null)
        data.setAssociationInstanceId(map.getMapid());
        Text text = new Text();
        text.setContent(map.getToexpr());
        data.setAssociationDataText(text);
        return data;
    }
    
    public List<AssociationQualification> getAssociationQualifiers(MrMap map) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException  {
        ArrayList<AssociationQualification> qualifiers = new ArrayList<AssociationQualification>();
        
            Class<?> cl = Class.forName("edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.MrMap");
            Field[] fields = cl.getFields();
           for(Field f: fields){
               AssociationQualification qualifier = new AssociationQualification();
              
               if(f.get(map) != null && f.getName() != "mapsetcui" && f.getName() != "toexpr"){
                   qualifier.setAssociationQualifier(f.getName());
                   Text text = new Text();
                   text.setContent((String)f.get(map));
                   qualifier.setQualifierText(text);
                   qualifiers.add(qualifier);
               }
           }
            
        return qualifiers;
    }

    protected AssociationPredicate addTargetToExistingSource(MrMap map, AssociationPredicate predicate, String sourceEntityCodeNamespace, String targetEntityCodeNamespace) throws IndexOutOfBoundsException, LBParameterException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
     AssociationSource[] sources = predicate.getSource();
     for(AssociationSource s: sources){
         //DEBUG code
         //System.out.println(s.getSourceEntityCode());
        if(s.getSourceEntityCode().equals(map.getFromid())){
            AssociationTarget[] targets = s.getTarget();
            for(AssociationTarget t : targets){
                //only testing for unique code.  name spaces should be equal
                if( t.getTargetEntityCode().equals(map.getToexpr())){
                messages_.warn("source: " + s.getSourceEntityCode() + " and Target: " + t.getTargetEntityCode() 
                        + "appear to be duplicates, skipping load of this mapping");
                return predicate;
                }
            }
          
            s.addTarget(createAssociationTarget(map, targetEntityCodeNamespace));
            AssociationData[] data = s.getTargetData();
            for(AssociationData d: data){
               if(d.getAssociationInstanceId().equals(map.getMapid())){
                       messages_.warn("source: " + s.getSourceEntityCode() + " and Target Data: " + d.getAssociationInstanceId() 
                               + "appear to be duplicates, skipping load of this mapping");
                       return predicate;
               }
            }
            s.addTargetData(createTargetData(map));
            }
     }
        return predicate;
    }
    
    
    protected CodingScheme createMrMapScheme(Relations rel, String codingSchemeName, String codingSchemeVersion,
            String codingSchemeURI, String sourceSchemeName, String sourceSchemeVersion, String sourceSchemeURI,
            String targetSchemeName, String targetSchemeVersion, String targetSchemeURI) {
        CodingScheme scheme = new CodingScheme();
        //Create the basics of a mapping Coding Scheme.
        if (codingSchemeName == null) {
            scheme.setCodingSchemeName(createDescriptiveSchemeName(rel));
        }
        if (codingSchemeVersion == null) {
            scheme.setCodingSchemeURI(createUniqueSchemeURI(rel));
        }
        if (codingSchemeURI == null) {
            scheme.setRepresentsVersion(setMappingSchemeVersion(rel));
        }
        scheme.setFormalName(createDescriptiveSchemeName(rel));
        scheme.addLocalName(createDescriptiveSchemeName(rel));
        //Create a supported version of it for the coding scheme
        SupportedCodingScheme supportedScheme = new SupportedCodingScheme();
        supportedScheme.setContent(scheme.getCodingSchemeName());
        supportedScheme.setLocalId(scheme.getCodingSchemeName());
        supportedScheme.setUri(scheme.getCodingSchemeURI());
        
        SupportedNamespace nameSpace = new SupportedNamespace();
        nameSpace.setContent(scheme.getCodingSchemeName());
        nameSpace.setLocalId(scheme.getCodingSchemeName());
        nameSpace.setUri(scheme.getCodingSchemeURI());
        nameSpace.setEquivalentCodingScheme(scheme.getCodingSchemeName());
        
        //create a supported relations container.
        SupportedContainerName container = new SupportedContainerName();
        container.setContent(rel.getContainerName());
        container.setLocalId(rel.getContainerName());
        
        // Supported source scheme namespace to coding scheme mapping
        SupportedCodingScheme supportedSourceScheme = new SupportedCodingScheme();
        supportedSourceScheme = makeSupportedCodingScheme(rel,sourceSchemeName,sourceSchemeURI, true );

        SupportedNamespace supportedSourceNamespace = new SupportedNamespace();
        supportedSourceNamespace.setLocalId(rel.getSourceCodingScheme());
        if(sourceSchemeURI != null){
            supportedSourceNamespace.setUri(sourceSchemeURI);
        }
        else{
            supportedSourceNamespace.setUri(supportedSourceScheme.getUri());
        }
        supportedSourceNamespace.setContent(rel.getSourceCodingScheme());
        supportedSourceNamespace.setEquivalentCodingScheme(rel.getSourceCodingScheme());
        
        // supported target scheme namespace to coding scheme mapping
        SupportedCodingScheme supportedTargetScheme = makeSupportedCodingScheme(rel,targetSchemeName,targetSchemeURI, false);

        SupportedNamespace supportedTargetNamespace = new SupportedNamespace();
        supportedTargetNamespace.setLocalId(rel.getTargetCodingScheme());
        if (targetSchemeURI != null) {
            supportedTargetNamespace.setUri(targetSchemeURI);
        }
        else{
            supportedTargetNamespace.setUri(supportedTargetScheme.getUri());
        }
        supportedTargetNamespace.setContent(rel.getTargetCodingScheme());
        supportedTargetNamespace.setEquivalentCodingScheme(rel.getTargetCodingScheme());
        
        Mappings mappings = new Mappings();
        mappings.addSupportedCodingScheme(supportedSourceScheme);
        mappings.addSupportedCodingScheme(supportedTargetScheme);
        mappings.addSupportedCodingScheme(supportedScheme);
        
        mappings.addSupportedNamespace(supportedSourceNamespace);
        mappings.addSupportedNamespace(supportedTargetNamespace);
        mappings.addSupportedNamespace(nameSpace);

        SupportedAssociation supportedMapping = new SupportedAssociation();
        supportedMapping.setContent(rel.getAssociationPredicate(0).getAssociationName());
        supportedMapping.setLocalId(rel.getAssociationPredicate(0).getAssociationName());
        mappings.addSupportedAssociation(supportedMapping);
        mappings.addSupportedContainerName(container);
        scheme.setMappings(mappings);
        rel.setTargetCodingSchemeVersion(null);
        rel.setSourceCodingSchemeVersion(null);
        scheme.addRelations(rel);
        return scheme;
    }


    private SupportedCodingScheme makeSupportedCodingScheme(Relations rel, String sourceSchemeName,
            String sourceSchemeURI, boolean isSource) {

        if (sourceSchemeName == null) {
            if (rel.getSourceCodingScheme() != null) {
                return makeSupportedCodingSchemeFromRelAttributes(rel, isSource);
            } else {
                return makeSupportedCodingSchemeFromAssocNamespaces(rel, isSource);
            }
        } else {
            return makeSupportedCodingSchemeFromParams(sourceSchemeName, sourceSchemeURI, sourceSchemeName);
        }

    }

    private SupportedCodingScheme makeSupportedCodingSchemeFromParams(String targetSchemeName, String targetSchemeURI,
            String schemeContent) {
        SupportedCodingScheme supportedCodingScheme = new SupportedCodingScheme();
        supportedCodingScheme.setLocalId(targetSchemeName);
        supportedCodingScheme.setUri(targetSchemeURI);
        supportedCodingScheme.setContent(schemeContent);
        return supportedCodingScheme;
    }

    private SupportedCodingScheme makeSupportedCodingSchemeFromAssocNamespaces(Relations rel, boolean isSource) {
        if(isSource)
        { SupportedCodingScheme supportedCodingScheme = new SupportedCodingScheme();
        supportedCodingScheme.setLocalId(rel.getAssociationPredicate(0).getSource(0).getSourceEntityCodeNamespace());
        supportedCodingScheme.setContent(rel.getAssociationPredicate(0).getSource(0).getSourceEntityCodeNamespace());
        supportedCodingScheme.setUri(URIPREFIX + ":" + rel.getAssociationPredicate(0).getSource(0).getSourceEntityCodeNamespace() + ":" + "version");
        return supportedCodingScheme;}
        else{
            SupportedCodingScheme supportedCodingScheme = new SupportedCodingScheme();
            supportedCodingScheme.setLocalId(rel.getAssociationPredicate(0).getSource(0).getTarget(0).getTargetEntityCodeNamespace());
            supportedCodingScheme.setContent(rel.getAssociationPredicate(0).getSource(0).getTarget(0).getTargetEntityCodeNamespace());
            supportedCodingScheme.setUri(URIPREFIX + ":" + rel.getAssociationPredicate(0).getSource(0).getTarget(0).getTargetEntityCodeNamespace() + ":" + "version");
            return supportedCodingScheme;
        }
    }

    private SupportedCodingScheme makeSupportedCodingSchemeFromRelAttributes(Relations rel, boolean isSource) {
        if(isSource){
            SupportedCodingScheme supportedCodingScheme = new SupportedCodingScheme();
            supportedCodingScheme.setLocalId(rel.getSourceCodingScheme());
            supportedCodingScheme.setContent(rel.getSourceCodingScheme());
            supportedCodingScheme.setUri(URIPREFIX + ":" + rel.getSourceCodingScheme() + ":" + rel.getSourceCodingSchemeVersion());
            return supportedCodingScheme;
        }
        else{
        SupportedCodingScheme supportedCodingScheme = new SupportedCodingScheme();
        supportedCodingScheme.setLocalId(rel.getTargetCodingScheme());
        supportedCodingScheme.setContent(rel.getTargetCodingScheme());
        supportedCodingScheme.setUri(URIPREFIX + ":" + rel.getTargetCodingScheme() + ":" + rel.getTargetCodingSchemeVersion());
        return supportedCodingScheme;}
    }

    private String setMappingSchemeVersion(Relations rel) {
        
        return (rel.getRepresentsVersion()== null? "1.0": rel.getRepresentsVersion());
    }

    private String createUniqueSchemeURI(Relations rel) {

        return "urn:oid:" + rel.getContainerName() + "." + rel.getSourceCodingScheme() + "." + rel.getTargetCodingScheme();
    }

    private String createDescriptiveSchemeName(Relations rel) {
      return rel.getSourceCodingSchemeVersion() + "_TO_" + rel.getTargetCodingSchemeVersion();
    }

    protected MrMap processMrMapRow(String [] mapRow) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        MrMap mrMap = new MrMap();
        Class<?> mapClass = mrMap.getClass();
        Field[] columns = mapClass.getDeclaredFields();
        for(int i = 0; i < mapRow.length; i++){
            columns[i].set(mrMap, mapRow[i]);
        }
        return mrMap;
    }
    protected MrSat processMrSatRow(String [] mapRow, int lineCount) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        MrSat mrSat = new MrSat();
        Class<?> mapClass = mrSat.getClass();
        Field[] columns = mapClass.getDeclaredFields();
        try{
        for(int i = 0; i < mapRow.length; i++){
            columns[i].set(mrSat, mapRow[i]);
        }
        }
        catch(IndexOutOfBoundsException e){
            messages_.warn("Error in row " + lineCount + " of MRSAT -- number of columns is larger than specified for UMLS MRSAT: " + e.getMessage()); 
            messages_.warn("This associated mapping data will not be processed: " );
            for(String s : mapRow){
                messages_.warn(s);
            }
            
        }
        return mrSat;
    }
    

}