package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ProcessStatus;
//import org.LexGrid.LexBIG.Impl.loaders.MessageDirector;
import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.relations.Relations;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;

public class MappingRelationsUtil {

    public MappingRelationsUtil() {
        // TODO Auto-generated constructor stub
    }
    
    //constants
    public static final boolean ISMAP = true;
    
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
    
    public HashMap<String, Relations> processMrSatBean(String sPath, String mPath) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, FileNotFoundException {
        RRFLineReader satReader = new RRFLineReader(sPath);
        String[] mrSatRow;
        HashMap<String, Relations> relationsMap = null;
        int lineCount = 0;
        int modCount = 0;
        try {
         relationsMap = processRelationsContainers(mPath);
//         messages.info("Searching MRSAT for mapping metadata");
         System.out.println("Searching MRSAT for mapping metadata");
            while((mrSatRow = satReader.readRRFLine()) != null){
                lineCount++;
                MrSat metaData = processMrSatRow(mrSatRow, lineCount);
               if (relationsMap.containsKey(metaData.getCui())){
               processMrSatToRelation(metaData, relationsMap.get(metaData.getCui()));
                }
               if (lineCount% 100000 == 99999){
                   modCount = modCount + 100000;
//                  messages_.debug("MRSAT lines processed: " + modCount);
               }
            }
            satReader.close();
//            messages.info("Finished Searching MRSAT for mapping data");
            System.out.println("Finished Searching MRSAT for mapping data");
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
        return relationsMap;
    }
    
    public HashMap<String, Relations> processRelationsContainers(String mapPath) throws IOException{
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
            System.out.println("Error in row " + lineCount + " of MRSAT -- number of columns is larger than specified for UMLS MRSAT: " + e.getMessage()); 
            System.out.println("This associated mapping data will not be processed: " );
            for(String s : mapRow){
                System.out.println(s);
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
 

}
