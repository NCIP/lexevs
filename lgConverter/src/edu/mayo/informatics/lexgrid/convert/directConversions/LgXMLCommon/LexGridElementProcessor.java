
package edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon;

import java.util.ArrayList;
import java.util.HashMap;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;

import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;

/**
 * @author  <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 *
 */
public class LexGridElementProcessor {
    
    /**
     * This list of coding schemes is eventually passed to the baseloader for post processing
     */
    private static ArrayList<CodingScheme> codingSchemes = new ArrayList<CodingScheme>();
    
    /**
     * This list of Value Set Definitions is eventually passed to the baseloader for post processing
     */
    private static ArrayList<ValueSetDefinition> valueSetDefinitions = new ArrayList<ValueSetDefinition>();
    
    /**
     * This list of Pick List Definitions is eventually passed to the baseloader for post processing
     */
    private static ArrayList<PickListDefinition> pickListDefinitions = new ArrayList<PickListDefinition>();
    
    /**
     * We'll convert the array list to an array eventually
     */
    private static  CodingScheme[] cs = null;

    /**
     * We'll convert the array list to an array eventually
     */
    private static  ValueSetDefinition[] vsd = null;
    
    /**
     * We'll convert the array list to an array eventually
     */
    private static  PickListDefinition[] pld = null;
    
    /**
     * Map of relations and their predicates to track what has been loaded and when.
     */
    private static HashMap<String, ArrayList<String>> relMap = new HashMap<String, ArrayList<String>>();

    private static ManifestUtil manifestUtil = new ManifestUtil();

    /**
     * @return
     */
    public static CodingScheme[] setAndRetrieveCodingSchemes() {
        cs = new CodingScheme[codingSchemes.size()];
        for (int i = 0; i < codingSchemes.size(); i++) {
            cs[i] = codingSchemes.get(i);
        }
        return cs;
    }
    
    public static ValueSetDefinition[] setAndRetrieveValueSetDefinitions() {
        vsd = new ValueSetDefinition[valueSetDefinitions.size()];
        for (int i = 0; i < valueSetDefinitions.size(); i++) {
            vsd[i] = valueSetDefinitions.get(i);
        }
        return vsd;
    }
    
    public static PickListDefinition[] setAndRetrievePickListDefinitions() {
        pld = new PickListDefinition[pickListDefinitions.size()];
        for (int i = 0; i < pickListDefinitions.size(); i++) {
            pld[i] = pickListDefinitions.get(i);
        }
        return pld;
    }

    /**
     * @param service
     * @param parent
     * @param child
     */
    public static void processCodingSchemeMetadata(
            XMLDaoServiceAdaptor service, 
            Object parent, 
            Object child, 
            CodingSchemeManifest codingSchemeManifest) {
        CodingScheme scheme = (CodingScheme) parent;
        
        if(codingSchemeManifest != null) {
            manifestUtil.applyManifest(codingSchemeManifest, scheme);
        }
        try {
            codingSchemes.add(scheme);
            service.storeCodingScheme(scheme);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void processCodingSchemeMetadata(
            XMLDaoServiceAdaptor service, 
            Object parent, 
            Object child) {
        processCodingSchemeMetadata(service, parent, child, null);
    }
    /**
     * @param service
     * @param parent
     * @param child
     * @param systemReleaseUI
     */
    public static void processCodingSchemeSystemReleaseRevision(XMLDaoServiceAdaptor service, Object parent, Object child, String systemReleaseUI) {
        CodingScheme scheme = (CodingScheme) child;
        ChangedEntry change = (ChangedEntry)parent;
        Revision revision = (Revision)change.getParent();
        change.setChangedCodingSchemeEntry(scheme);
        revision.addChangedEntry(change);
        try {
            codingSchemes.add(scheme);
            service.storeCodingSchemeSystemReleaseRevision(revision, systemReleaseUI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * @param service
     * @param parent
     * @param child
     */
    public static void processCodingSchemeEntity(XMLDaoServiceAdaptor service, Object parent, Object child) {
        Entity e = (Entity) child;
        Entities entities = (Entities) parent;
        CodingScheme c = (CodingScheme) entities.getParent();
        service.storeEntity(e, c);
        entities.removeEntity(e);
    }

    /**
     * @param isPredicateLoaded
     * @param service
     * @param parent
     * @param child
     */
    public static void processCodingSchemeAssociation(boolean isPredicateLoaded, XMLDaoServiceAdaptor service,
            Object parent, Object child) {
        AssociationPredicate a = (AssociationPredicate) parent;
        Relations relations = (Relations) a.getParent();
        CodingScheme cs = (CodingScheme) relations.getParent();
        service.storeRelation(cs.getCodingSchemeURI(), cs.getRepresentsVersion(), relations);
        if (!isPredicateLoaded) {
            service.storeAssociationPredicate(cs.getCodingSchemeURI(), cs.getRepresentsVersion(), relations
                    .getContainerName(), (AssociationPredicate) parent);
        } else {
            service.storeAssociation(cs.getCodingSchemeURI(), cs.getRepresentsVersion(), relations.getContainerName(),
                    a.getAssociationName(), (AssociationSource) child);
        }
        a.removeSource((AssociationSource) child);
    }

    /**
     * @param parent
     */
    public static void removeEntitiesContainer(Object parent) {
        ((CodingScheme) parent).setEntities(null);
    }

    /**
     * @param service
     * @param parent
     * @param child
     * @throws LBRevisionException 
     */
    public static void processRevisionMetadata(XMLDaoServiceAdaptor service, Revision revision) throws LBRevisionException {
       service.storeRevisionMetaData(revision);
    }

    /**
     * @param parent
     * @return
     * @throws LBRevisionException 
     */
    public static void processSystemReleaseMetadata(XMLDaoServiceAdaptor service, Object parent) throws LBRevisionException {
        service.storeSystemRelease((SystemRelease)parent);
    }

    /**
     * @param service
     * @param parent
     * @param child
     * @param mappings
     * @param systemReleaseURI
     * @throws LBException 
     */
    public static void processValueSet(XMLDaoServiceAdaptor service, Object parent, Object child, Mappings mappings,
            String systemReleaseURI) throws LBException {
        ValueSetDefinition valueSet = (ValueSetDefinition) child;
        service.storeValueSet(valueSet, systemReleaseURI, mappings);
    }

    /**
     * @param service
     * @param parent
     * @param child
     * @throws LBException
     */
    public static void processValueSetDefinition(XMLDaoServiceAdaptor service, Object parent, Object child) throws LBException {
        ValueSetDefinition valueSet = (ValueSetDefinition) parent;
        service.storeValueSetDefinition(valueSet);
    }

    /**
     * @param service
     * @param parent
     * @param child
     * @throws LBException
     */
    public static void processPickListDefinition(XMLDaoServiceAdaptor service, Object parent, Object child) throws LBException {
        PickListDefinition pickList = (PickListDefinition) parent;
        service.storePickListDefinition(pickList);
    }
    /**
     * @param service
     * @param parent
     * @param child
     * @return
     */
    public static Mappings processValueSetMappings(XMLDaoServiceAdaptor service, Object parent, Object child) {
        Mappings mappings = (Mappings) child;
        return mappings;
    }

    /**
     * @param service
     * @param parent
     * @param child
     * @return
     */
    public static Mappings processPickListMappings(XMLDaoServiceAdaptor service, Object parent, Object child) {
        Mappings mappings = (Mappings) child;
        return mappings;
    }

    /**
     * @param service
     * @param child
     * @param mappings
     * @param systemReleaseURI
     * @throws LBException 
     * @throws LBParameterException 
     */
    public static void processPickListDefinition(XMLDaoServiceAdaptor service, Object child, Mappings mappings,
            String systemReleaseURI) throws LBParameterException, LBException {
        PickListDefinition picklist = (PickListDefinition) child;
        service.storePickList(picklist, systemReleaseURI, mappings);
    }

    /**
     * @param service
     * @param parent
     * @param child
     */
    public static void processCodingSchemeMetadataRevision(XMLDaoServiceAdaptor service, Object parent,
            Object child) {
        CodingScheme scheme = (CodingScheme)parent;
        codingSchemes.add(scheme);
       service.storeCodingSchemeRevision(scheme);
        
    }
    
    /**
     * @param service
     * @param parent
     * @param child
     */
    public static void processCodingSchemeEntityRevision(XMLDaoServiceAdaptor service, Object parent, Object child) {
        Entity e = (Entity) child;
        Entities entities = (Entities) parent;
        CodingScheme c = (CodingScheme) entities.getParent();
        service.storeEntityRevision(e, c);
        entities.removeEntity(e);
    }
    
    /**
     * @param isPredicateLoaded
     * @param service
     * @param parent
     * @param child
     */
    public static void processCodingSchemeAssociationRevision(boolean isPredicateLoaded, XMLDaoServiceAdaptor service,
            AssociationSource source, Object child) {
        AssociationPredicate a = (AssociationPredicate)source.getParent();
        Relations relations = (Relations) a.getParent();
        CodingScheme cs = (CodingScheme) relations.getParent();
        service.storeRelationsRevision(cs.getCodingSchemeURI(), cs.getRepresentsVersion(), relations);
            service.storeAssociationPredicate(cs.getCodingSchemeURI(), cs.getRepresentsVersion(), relations
                    .getContainerName(), a);
            service.storeAssociationRevision(cs.getCodingSchemeURI(), cs.getRepresentsVersion(), relations.getContainerName(),
                    a.getAssociationName(), source, (AssociationTarget)child);
        a.removeSource(source);
    }

    /**
     * @param service
     * @param parent
     * @param child
     */
    public static void processCodingSchemePropertyRevision(XMLDaoServiceAdaptor service, CodingScheme c, Object parent, Object child) {
        Property p = (Property)child;
        service.storeCodingSchemePropertyRevision(p, c);
        
    }
    /**
     * @param service
     * @param parent
     * @param child
     */
    public static void processRelationsPropertyRevision(XMLDaoServiceAdaptor service, Object parent, Object child) {
        Property p = (Property)child;
        Relations r = (Relations)parent;
        CodingScheme c = (CodingScheme)r.getParent();
        service.storeRelationsPropertyRevision(c.getCodingSchemeURI(), c.getRepresentsVersion(), r.getContainerName(), p);
        
    }
   
    /**
     * @param service
     * @param cs
     * @param parent
     * @param p
     */
    public static void processEntityPropertyRevision(XMLDaoServiceAdaptor service, CodingScheme cs, Object parent, Property p){
        Entity e = (Entity)parent;
        service.storeEntityPropertyRevision(cs.getCodingSchemeURI(), cs.getRepresentsVersion(), e.getEntityCode(), e.getEntityCodeNamespace(), p);
    }
    /**
     * @param service
     * @param child
     */
    public static void processValueSetDefinitionRevision(XMLDaoServiceAdaptor service,
            Object child) {
        ValueSetDefinition vsDefinition = (ValueSetDefinition)child;
      service.storeValueSetDefinitionRevision(vsDefinition);
        
    }
    /**
     * @param service
     * @param parent
     * @param child
     * @param mappings
     */
    public static void processValueSetDefinitionSystemReleaseRevision(XMLDaoServiceAdaptor service,
            Object parent, Object child, Mappings mappings) {
        ChangedEntry entry = (ChangedEntry)parent;
        Revision revision = (Revision)entry.getParent();
        ValueSetDefinition vsDefinition = (ValueSetDefinition)child;
      service.storeValueSetDefinitionSystemReleaseRevision(vsDefinition, mappings, revision.getRevisionId());
        
    }
    /**
     * @param service
     * @param child
     */
    public static void processPickListtDefinitionRevision(XMLDaoServiceAdaptor service,
            Object child) {
        PickListDefinition plDefinition = (PickListDefinition)child;
       service.storePickListDefinitionRevision(plDefinition);
        
    }
    /**
     * @param service
     * @param parent
     * @param child
     * @param mappings
     */
    public static void processPickListDefinitionSystemReleaseRevision(XMLDaoServiceAdaptor service,
           Object parent,  Object child, Mappings mappings) {
        ChangedEntry entry = (ChangedEntry)parent;
        Revision revision = (Revision)entry.getParent();
        PickListDefinition plDefinition = (PickListDefinition)child;
       service.storePickListDefinitionSystemReleaseRevision(plDefinition, mappings, revision.getRevisionId());
        
    }
    /**
     * @param service
     * @param source
     * @param data
     */
    public static void processAssociationData(XMLDaoServiceAdaptor service, AssociationSource source, AssociationData data) {
      AssociationPredicate predicate = (AssociationPredicate)source.getParent();
      Relations relation = (Relations)predicate.getParent();
      CodingScheme scheme  = (CodingScheme)relation.getParent();
      service.storeAssociationPredicate(scheme.getCodingSchemeURI(), scheme.getRepresentsVersion(), relation
              .getContainerName(), predicate);
      service.storeRelationsRevision(scheme.getCodingSchemeURI(), scheme.getRepresentsVersion(), relation);
      service.storeAssociatonData(scheme.getCodingSchemeURI(), predicate.getAssociationName(), relation.getContainerName(), scheme.getRepresentsVersion(), source, data);
        
    }

    /**
     * @param service
     * @param parent
     * @param child
     */
    public static void processRelationsRevision(XMLDaoServiceAdaptor service, Object parent, Object child) {
        AssociationPredicate ap = (AssociationPredicate)parent;
        Relations r = (Relations)ap.getParent();
        CodingScheme cs = (CodingScheme)r.getParent();
       //This is a new instance of a relation container with an empty predicate
     if(!relMap.containsKey(r.getContainerName())){
         ArrayList<String> newPredicateList = new ArrayList<String>();
         newPredicateList.add(ap.getAssociationName());
         relMap.put(r.getContainerName(), newPredicateList);
         service.storeRelationsRevision(cs.getCodingSchemeURI(), cs.getRepresentsVersion(), r);
         service.storeAssociationPredicate(cs.getCodingSchemeURI(), cs.getRepresentsVersion(), r.getContainerName(), ap);
     }
        
     if(!relMap.get(r.getContainerName()).contains(ap.getAssociationName())){
         service.storeAssociationPredicate(cs.getCodingSchemeURI(), cs.getRepresentsVersion(), r.getContainerName(), ap);
     }
    }


}