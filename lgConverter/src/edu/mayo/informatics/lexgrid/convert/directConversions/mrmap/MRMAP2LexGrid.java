package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.ObjectToString;
import org.LexGrid.versions.Revision;

public class MRMAP2LexGrid {
    List<String> PropertyNames;
   //RRFLineReader mapReader;
   //RRFLineReader satReader;
    private LgMessageDirectorIF messages_;
    boolean mapMrSat;
    boolean isNewMapping = false;
    String currentMapping = null;
    HashSet<String> sources;
    AssociationSource[] sourcesAndTargets;
    
    //constants
    public static final String ASSOC_NAME = "mapped_to";
    public static final String APROX_ASSOC_NAME = "approximately_mapped_to";
    public static final boolean ISMAP = true;
    public static final String TORSAB = "TORSAB";
    public static final String TOVSAB = "TOVSAB";
    public static final String FROMRSAB =  "FROMRSAB";
    public static final String FROMVSAB = "TOVSAB";
    public static final String MAPSETVERSION =  "MAPSETVERSION";
    public static final String SOS = "SOS";
    public static final String MAPSETNAME = "MAPSETNAME";

    
    public MRMAP2LexGrid(boolean mapMrSat, String sourceIdentifier, 
        String targetIdentifier, 
        LgMessageDirectorIF messages){
        messages_ = messages;
        sources = new HashSet<String>();
        PropertyNames = Arrays.asList(new String[]{ "MAPSETGRAMMER",
            "MAPSETRSAB",
            "MAPSETTYPE",
            "MAPSETVSAB",
            "MTH_MAPFROMEXHAUSTIVE",
            "MTH_MAPSETCOMPLEXITY",
            "MTH_MAPTOEXHAUSTIVE",
            "MTH_MAPFROMCOMPLEXITY", 
            "MTH_MAPTOCOMPLEXITY",
            "MR","DA","ST"});
       // satReader = new RRFLineReader(satPath);
      //  mapReader = new RRFLineReader(mapPath);
        
    }

    
    public Revision processMrMapToLexGrid() {
        
        return null;
    }
    
    
    protected Relations processMrSatBean(String path) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, FileNotFoundException {
        RRFLineReader satReader = new RRFLineReader(path);
        String[] mrSatRow;
        Relations relation = new Relations();
        try {
            while((mrSatRow = satReader.readRRFLine()) != null){
                MrSat metaData = processMrSatRow(mrSatRow);
               processMrSatToRelation(metaData, relation);
            }
            satReader.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
        ObjectToString.toString(relation);
        return relation;
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
        if(PropertyNames.contains(atnValue)){
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
    protected AssociationPredicate processMrMapBean(String path) throws Exception {
        String[] mrMapRow;
        RRFLineReader mapReader = new RRFLineReader(path);
        AssociationPredicate predicate  = createAssociationPredicate();

            try {
                while((mrMapRow = mapReader.readRRFLine()) != null){
                    
                    MrMap map = processMrMapRow(mrMapRow);
                    currentMapping = map.getMapsetcui();
                    if (currentMapping != null && !map.getMapsetcui().equals(currentMapping)){
                        break;
                    }
               processAndMergeIntoSource(map, predicate);

                }
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
    
    private AssociationPredicate processAndMergeIntoSource(MrMap map, AssociationPredicate predicate) throws Exception {

        if(sources.add(map.getFromid())){
           AssociationSource source = createNewAssociationSourceWithTarget(map);
           predicate.addSource(source);
           return predicate;
        }
        else{
    
            return addTargetToExistingSource(map, predicate);
        }
       
    }

    private AssociationPredicate createAssociationPredicate() {
        AssociationPredicate predicate  = new AssociationPredicate();
        predicate.setAssociationName(ASSOC_NAME);
        return predicate;
    }
    
    protected AssociationSource createNewAssociationSourceWithTarget(MrMap map) throws Exception {
    AssociationSource source = new AssociationSource();
    source.setSourceEntityCode(map.getFromid());
    source.addTargetData(createTargetData(map));
    source.addTarget(createAssociationTarget(map));
    return source;
    }
    
   protected AssociationTarget createAssociationTarget(MrMap map) throws Exception {
        AssociationTarget target = new AssociationTarget();
        target.setAssociationInstanceId(map.getMapid());
        target.setTargetEntityCode(map.getToid());
        target.setAssociationQualification(getAssociationQualifiers(map));
        return target;
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
    
    public List<AssociationQualification> getAssociationQualifiers(MrMap map) throws Exception {
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

    protected AssociationPredicate addTargetToExistingSource(MrMap map, AssociationPredicate predicate) throws IndexOutOfBoundsException, Exception {
     AssociationSource[] sources = predicate.getSource();
     for(AssociationSource s: sources){
         //DEBUG code
         //System.out.println(s.getSourceEntityCode());
        if(s.getSourceEntityCode().equals(map.getFromid())){
            AssociationTarget[] targets = s.getTarget();
            for(AssociationTarget t : targets){
                //only testing for unique code.  name spaces should be equal
                if( t.getTargetEntityCode().equals(map.getToid())){
                messages_.warn("source: " + s.getSourceEntityCode() + " and Target: " + t.getTargetEntityCode() 
                        + "appear to be duplicates, skipping load of this mapping");
                return predicate;
                }
            }
            s.addTarget(createAssociationTarget(map));
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
    
    
    private CodingScheme createMrMapScheme(MrMap map) {
        // TODO Auto-generated method stub
        return null;
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
    protected MrSat processMrSatRow(String [] mapRow) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        MrSat mrSat = new MrSat();
        Class<?> mapClass = mrSat.getClass();
        Field[] columns = mapClass.getDeclaredFields();
        for(int i = 0; i < mapRow.length; i++){
            columns[i].set(mrSat, mapRow[i]);
        }
        return mrSat;
    }
//    public static void main(String[] args){
//        try {
//           Relations relation = new MRMAP2LexGrid(true, null, null, null).processMrSatBean("../lbTest/resources/testData/mrmap_mapping/MRSAT.RRF");
//           System.out.println(relation.getContainerName());
//           System.out.println(relation.getSourceCodingScheme());
//           System.out.println(relation.getSourceCodingSchemeVersion());
//           System.out.println(relation.getTargetCodingScheme());
//           System.out.println(relation.getTargetCodingSchemeVersion());
//           System.out.println(relation.getRepresentsVersion());
//           System.out.println(relation.getOwner());
//           Properties properties = relation.getProperties();
//           Property[] property = properties.getProperty();
//           for(Property p: property){
//               System.out.println("propertyName:  " + p.getPropertyName());
//               System.out.println("propertyValue:  " + p.getValue().getContent());
//           }
//          
//        } catch (SecurityException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        
//    }
}
