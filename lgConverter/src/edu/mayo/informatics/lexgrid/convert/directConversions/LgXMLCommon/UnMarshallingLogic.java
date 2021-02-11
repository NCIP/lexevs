
package edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon;

import java.util.ArrayList;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListDefinitions;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitions;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EditHistory;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;

/**
 * This class contains a number of methods to determine places in a LexGrid xml
 * file where the parser should stop and some kind of load processing should take 
 * place. 
 * @author  <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 *
 */
public class UnMarshallingLogic {
private static final int CHANGE_AGENT_TYPE = 0;
private static final int CHANGE_INSTRUCTIONS_TYPE = 1;

    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isCodingSchemeMappings(Object parent, Object child) {
        return parent instanceof CodingScheme && child instanceof Mappings;
    }

    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isCodingSchemeProperties(Object parent, Object child) {
        return parent instanceof CodingScheme && child instanceof Properties;
    }

    /**
     * Checks for any kind of entity
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isCodingSchemeEntity(Object parent, Object child) {
        return child instanceof Entity && parent instanceof Entities || child instanceof AssociationEntity && parent instanceof Entities;
    }

    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isCodingSchemeEntities(Object parent, Object child) {
        return child instanceof Entities && parent instanceof CodingScheme;
    }

    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isCodingSchemeAssociation(Object parent, Object child) {
        return child instanceof AssociationSource && parent instanceof AssociationPredicate;
    }
    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isCodingSchemeAssociationSource(Object parent, Object child) {
        return child instanceof AssociationTarget && parent instanceof AssociationSource;
    }
    
    /**
     * @param serviceAdaptor
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isCodingSchemeProperty(XMLDaoServiceAdaptor serviceAdaptor, Object parent, Object child) {
        return child instanceof Property && parent instanceof Properties;
       }

    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isRevisionWithFirstChild(Object parent, Object child) {
        return parent instanceof Revision && child instanceof Text;
    }

    public static boolean isSystemReleaseRevisionInstance(Object parent, Object child) {
        return (parent instanceof Revision && child instanceof Text) ||
        (parent instanceof EditHistory && child instanceof Revision);
    }
    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isSytemRelease(Object parent, Object child) {
        return (parent instanceof SystemRelease && child instanceof EntityDescription );
    }

    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isValueSet(Object parent, Object child) {
        return child instanceof ValueSetDefinition && parent instanceof ValueSetDefinitions;
    }
    
    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isValueSetDefinition(Object parent, Object child) {
        return child instanceof ValueSetDefinition && parent == null;
    }
    
    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isValueSetDefinitionEntry(Object parent, Object child) {
        return child instanceof DefinitionEntry && parent instanceof ValueSetDefinition;
    }
    
    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isValueSetMappings(Object parent, Object child) {
        return child instanceof Mappings && parent instanceof ValueSetDefinitions;
    }
    
    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isValueSetProperties(Object parent, Object child) {
        return parent instanceof ValueSetDefinition && child instanceof Properties;
    }

    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isPickListMappings(Object parent, Object child) {
        return child instanceof Mappings && parent instanceof PickListDefinitions;
    }

    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isPickListDefinition(Object parent, Object child) {
        return child instanceof PickListDefinition && parent instanceof PickListDefinitions;
    }

    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isValueSetDefinitionRevision(Object parent, Object child) {
        return child instanceof ValueSetDefinition && parent instanceof ChangedEntry;
    }

    public static boolean isPickListDefinitionRevision(Object parent, Object child) {
    return child instanceof PickListDefinition && parent instanceof ChangedEntry;
    }

    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isRevisionInstance(Object parent, Object child) {
       return child instanceof Revision && parent instanceof EditHistory;
    }

    /**
     * Checking for optional elements in the Revision meta data
     * @param lastMetaDataType
     * @param parent
     * @param child
     * @return boolean indicator to load Revision meta data at a given point. 
     */
    public static boolean isRevisionWithLastChild(int lastMetaDataType, Object parent, Object child) {
     if (lastMetaDataType == CHANGE_AGENT_TYPE)
           return child instanceof String && parent instanceof Revision;
       else if (lastMetaDataType == CHANGE_INSTRUCTIONS_TYPE){
           return child instanceof Text && parent instanceof Revision;
       }
         return child instanceof ChangedEntry && parent instanceof Revision;
     
    }
    
    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isSystemReleaseRevision(Object parent, Object child){
        return parent instanceof EditHistory && child instanceof Revision;
    }
    public static boolean isCodingSchemePropertiesRevision(Object parent, Object child) {
        return parent instanceof Properties && child instanceof Property;
    }
    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isCodingSchemeAssociationData(Object parent, Object child) {
        return child instanceof AssociationData && parent instanceof AssociationSource;
    }

    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isCodingSchemeEntityProperty(Object parent, Object child) {
       return child instanceof Property && parent instanceof Entity;
    }
    
    /**
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isCodingSchemeRelation(Object parent, Object child) {
        return child instanceof EntityDescription && parent instanceof Relations;
    }
    
    /**
     *Looking for an empty predicate just because the model allows it.
     *runs the risk of allowing a large predicate to be populated.
     * @param parent
     * @param child
     * @return boolean
     */
    public static boolean isCodingSchemeRelationWithEmptyPredicate(Object parent, Object child) {
        return parent instanceof AssociationPredicate && child == null;
    }

    
    /**
     * @param parent
     * @param child
     * @param survey
     * @return boolean
     */
    public static boolean loadSystemReleaseCodingSchemeWithNoProperties(Object parent, Object child,
            ArrayList<SystemReleaseSurvey> survey) {
        if (parent instanceof CodingScheme && child instanceof Mappings) {
            AbsoluteCodingSchemeVersionReference csr = new AbsoluteCodingSchemeVersionReference();
            CodingScheme cs = (CodingScheme)parent;
            csr.setCodingSchemeURN(cs.getCodingSchemeURI());
            csr.setCodingSchemeVersion(cs.getRepresentsVersion());
            for (SystemReleaseSurvey srs : survey) {
                if (srs.getCodingScheme().getCodingSchemeURN().equals(csr.getCodingSchemeURN()) 
                        && srs.getCodingScheme().getCodingSchemeVersion().equals(csr.getCodingSchemeVersion())  
                        && srs.getRevisionId() == "NEW"
                        && srs.isPropertiesPresent() == false 
                        && srs.isLoaded() == false) {
                    srs.setLoaded(true);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param parent
     * @param child
     * @param survey
     * @return boolean
     */
    public static boolean loadSystemReleaseCodingSchemeWithProperties(Object parent, Object child,
            ArrayList<SystemReleaseSurvey> survey) {
        if (parent instanceof CodingScheme && child instanceof Properties) {
            AbsoluteCodingSchemeVersionReference csr = new AbsoluteCodingSchemeVersionReference();
            CodingScheme cs = (CodingScheme) parent;
            csr.setCodingSchemeURN(cs.getCodingSchemeURI());
            csr.setCodingSchemeVersion(cs.getRepresentsVersion());
            for (SystemReleaseSurvey srs : survey) {
                if (srs.getCodingScheme().getCodingSchemeURN().equals(csr.getCodingSchemeURN()) 
                        && srs.getCodingScheme().getCodingSchemeVersion().equals(csr.getCodingSchemeVersion())
                        && srs.getRevisionId() == "NEW"
                        && srs.isPropertiesPresent() == true 
                        && srs.isLoaded() == false) {
                    srs.setLoaded(true);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isSystemReleaseCodingSchemeRevision(Object parent, Object child) {
       return parent instanceof ChangedEntry && child instanceof CodingScheme; 
    }

    public static boolean isSystemReleaseValueSetRevision(Object parent, Object child) {
      return parent instanceof ChangedEntry && child instanceof ValueSetDefinition;
    }

    public static boolean isSystemReleasePickListRevision(Object parent, Object child) {
        return parent instanceof ChangedEntry && child instanceof PickListDefinition;
    }





}