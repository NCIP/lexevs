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
package org.LexGrid.LexBIG.Impl.loaders;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Spliterator;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.MrMap_Loader;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.relations.Relations;

import edu.mayo.informatics.lexgrid.convert.directConversions.MrmapToSQL;
import edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.MappingRelationsUtil;
import edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.MrSat;
import edu.mayo.informatics.lexgrid.convert.options.URIOption;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Loader class for MrMap and MrSat RRF files resulting in a 
 * mapping coding scheme or schemes depending on the content of these files.
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class MrmapRRFLoader extends BaseLoader implements MrMap_Loader{
    
    private static final long serialVersionUID = -689653003698478622L;
    

    Map.Entry<String, Relations> rel;
    public final static String VALIDATE = "Validate";
      public final static String MRSAT_URI = "MRSAT file path";
      public final static String MANIFEST_URI = "add'l Manifest";
      
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
      
      CachingMessageDirectorIF messages = getMessageDirector();
      
      public static final List<String>   propertyNames = Arrays.asList(new String[]{
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
      
    @SuppressWarnings("unused")
    private static boolean validate = true;

    public MrmapRRFLoader(){
        
        super();
    }


    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
    URIOption mrSatURI  = new URIOption(MRSAT_URI);
    holder.getURIOptions().add(mrSatURI);
        return holder;
    }

    @Override
    protected URNVersionPair[] doLoad() throws Exception {

        MrmapToSQL map = new MrmapToSQL();
        if (getCodingSchemeManifest() != null) {
            messages.warn("Pre-load of manifests is not supported in the MrMap Loader.  "
                    + "Manifest files can be post loaded instead");
        }
        if (getLoaderPreferences() != null) {
            messages.warn("Loader Preferences are not supported in the MrMap Loader");
        }

        URI sourceSat = this.getOptions().getURIOption(MRSAT_URI).getOptionValue();
        if (rel == null || !rel.getValue().isIsMapping()) {
            Map<String, Relations> rels = new MappingRelationsUtil().processMrSatBean(sourceSat.getPath(),
                    getResourceUri().getPath());
            if (rels.entrySet() == null || rels.isEmpty()) {
                throw new RuntimeException("This mapping does not define any mapping relations");
            }
            CodingScheme[] schemes = null;
            Iterator<Entry<String, Relations>> itr = rels.entrySet().iterator();
            List<CodingScheme> listScheme  = new ArrayList<CodingScheme>();
            while (itr.hasNext()) {
                // schemes are reset to the next load.  This needs to be additive
                schemes = map.load(getMessageDirector(), this.getResourceUri(),
                        this.getOptions().getURIOption(MRSAT_URI).getOptionValue(), 
                        null, null, null, null, null, null,
                        null, null, this.getResourceUri().toString(), 
                        itr.next(), this.getCodingSchemeManifest());
                setDoApplyPostLoadManifest(false);
                listScheme.addAll(Arrays.asList(schemes));
            }
            
            return this.constructVersionPairsFromCodingSchemes(listScheme.toArray());
        }
        CodingScheme[] schemes = map.load(getMessageDirector(), this.getResourceUri(),
                this.getOptions().getURIOption(MRSAT_URI).getOptionValue(), null, null, null, null, null, null, null,
                null, this.getResourceUri().toString(), rel, this.getCodingSchemeManifest());
        setDoApplyPostLoadManifest(false);
        return this.constructVersionPairsFromCodingSchemes((Object[]) schemes);
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
            messages.warn("Error in row " + lineCount + " of MRSAT -- number of columns is larger than specified for UMLS MRSAT: " + e.getMessage()); 
            messages.warn("This associated mapping data will not be processed: " );
            for(String s : mapRow){
                messages.warn(s);
            }
            
        }
        return mrSat;
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
 
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(MrmapRRFLoader.class.getInterfaces()[0].getName());
        temp.setExtensionClass(MrmapRRFLoader.class.getName());
        temp.setDescription(MrmapRRFLoader.description);
        temp.setName(MrmapRRFLoader.name);
        
        return temp;
    }


    public void load(URI mrMapsource, URI mrSatSource, String nameForMappingScheme, String nameForMappingVersion,
            String nameforMappingURI, Map.Entry<String, Relations> relation, boolean stopOnErrors, boolean async) throws LBException{
        this.load(mrMapsource, mrSatSource, nameForMappingScheme, 
                nameForMappingVersion, nameforMappingURI, null, null, 
                null, null, null, null, relation, stopOnErrors, async);
    }
    @Override
    public void load(URI mrMapsource, URI mrSatSource, String nameForMappingScheme, String nameForMappingVersion,
            String nameforMappingURI, String sourceScheme, String sourceVersion, String sourceURI, String targetScheme,
            String targetVersion, String targetURI, Map.Entry<String, Relations> relation,  boolean stopOnErrors, boolean async) throws LBException{
        this.getOptions().getURIOption(MRSAT_URI).setOptionValue(mrSatSource);
        rel = relation;
        this.load(mrMapsource);
    }

    @Override
    public void validate(String source, int validationLevel) throws LBException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public OntologyFormat getOntologyFormat() {
        return OntologyFormat.MRMAP;
    }

}