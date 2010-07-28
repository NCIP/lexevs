package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.Revision;

public class MRMAP2LexGrid {
    
   RRFLineReader mapReader;
   RRFLineReader satReader;
    private LgMessageDirectorIF messages_;
    boolean mapMrSat;
    boolean isNewMapping = false;
    String currentMapping = null;
    HashSet<String> sources;
    AssociationSource[] sourcesAndTargets;
    
    //constants
    public static final String ASSOC_NAME = "mapped_to";
    public static final String APROX_ASSOC_NAME = "approximately_mapped_to";
    
    public MRMAP2LexGrid(boolean mapMrSat, String sourceIdentifier, 
        String targetIdentifier, LgMessageDirectorIF messages){
        messages_ = messages;
        sources = new HashSet();
    }
    public Revision processMrMapToLexGrid() {
        
        return null;
    }
    
    private void processMrSatBean(MrSat metaData) {
        // TODO Auto-generated method stub
        
    }

    
    protected AssociationPredicate processMrMapBean() throws Exception {
        String[] mrMapRow;
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
    
    private Relations createMrSatRelation(AssociationSource targetsAndSources) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
            String[] mrSatRow;
            try {
                while((mrSatRow = satReader.readRRFLine()) != null){
                    MrSat metaData = processMrSatRow(mrSatRow);
                   processMrSatBean(metaData);
                }
                satReader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
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

}
