package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.locator.LexEvsServiceLocator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class EntityToVSDTransformer {
    CodedNodeGraphService codedNodeGraphDao;
    EntityService entityService;
    private String SOURCE_NAME = "Contributing_Source";
    String baseURI = null;
       
    private LgMessageDirectorIF messages_;
    
   public EntityToVSDTransformer(String baseURI){
       codedNodeGraphDao = LexEvsServiceLocator.getInstance().
               getDatabaseServiceManager().getCodedNodeGraphService();
       entityService = LexEvsServiceLocator.getInstance().
               getDatabaseServiceManager().getEntityService();
       this.baseURI = baseURI;
   }
    
    protected String getPredicateGuidForValueSetRelation(String associationName, String codingSchemeUri,
            String codingSchemeVersion){
        List<String> list = new ArrayList<String>();
        list.add(associationName);
        List<String> uids = codedNodeGraphDao.getAssociationPredicateUidsForNames(
                codingSchemeUri, codingSchemeVersion, null, list);
        if( uids == null || uids.size() < 1){
            throw new RuntimeException("No association is asserted for :" 
        + associationName + " in " + codingSchemeUri + ":" + codingSchemeVersion);
        }
        return uids.get(0);
    }
    
 
    
    protected Entity getEntityByCodeAndNamespace(String codingSchemeUri, String version, String code, String namespace){
        Entity entity = entityService.getEntity(codingSchemeUri, version, code, namespace);
        return entity;
    }
    
    //Entity with more than one source will be processed into more than one definition
    public List<ValueSetDefinition> transformEntityToValueSetDefinition(Entity entity, String sourceName){
       final String source = getDefaultSourceIfNull(sourceName);
       List<Property> props = entity.getPropertyAsReference();
       List<Property> list = props.stream().filter(x -> x.getPropertyName().equals(source)).collect(Collectors.toList());
       List<ValueSetDefinition> defs = new ArrayList<ValueSetDefinition>();
      for(Property p: list){
          ValueSetDefinition def = new ValueSetDefinition();
          DefinitionEntry entry = new DefinitionEntry();
         
      }
       return defs;
    }

    private String getDefaultSourceIfNull(String sourceName) {
        return sourceName == null?SOURCE_NAME: sourceName;
    }

}
