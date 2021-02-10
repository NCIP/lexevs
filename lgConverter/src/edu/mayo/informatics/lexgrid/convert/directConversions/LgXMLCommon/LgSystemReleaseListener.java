
package edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.codingSchemes.CodingSchemes;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListDefinitions;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitions;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;
import org.castor.xml.UnmarshalListener;
import org.mayo.edu.lgModel.LexGridBase;

/**
 * @author  <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 * Listener for loading a system release element and its contained 
 * coding schemes, picklists, value sets and revision.
 */
public class LgSystemReleaseListener implements UnmarshalListener {

    private boolean isPropertiesPresent = false;
    private boolean isSystemReleaseSet = false;
    private boolean inEditHistory = false;
    private Mappings currentPickListMappings = new Mappings();
    private Mappings currentValueSetMappings;
    private SystemRelease systemRelease = new SystemRelease();
    private AssociationPredicate currentPredicate = new AssociationPredicate();
    private XMLDaoServiceAdaptor serviceAdaptor = null;
    private CodingScheme[] codingSchemes = null;
    private ValueSetDefinition[] valueSetDefinitions = null;
    private PickListDefinition[] pickListDefinitions = null;
    private LgMessageDirectorIF messages_;
    private ArrayList<SystemReleaseSurvey> survey = null;
    /**
     * 
     */
    public LgSystemReleaseListener() {
        super();
        serviceAdaptor = new XMLDaoServiceAdaptor();    
    }
    
    /**
     * @param messages
     */
    public LgSystemReleaseListener(LgMessageDirectorIF messages, ArrayList<SystemReleaseSurvey> survey) {
        super();
        serviceAdaptor = new XMLDaoServiceAdaptor();
        messages_ = messages;
        this.survey = survey;
    }
    public void initializeSystemReleaseLoad(SystemRelease release){
        try {
            LexGridElementProcessor.processSystemReleaseMetadata(serviceAdaptor, getSystemRelease());
        } catch (LBRevisionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    public boolean isPropertiesPresent() {
        return isPropertiesPresent;
    }

    /**
     * @param isPropertiesPresent
     */
    public void setPropertiesPresent(boolean isPropertiesPresent) {
        this.isPropertiesPresent = isPropertiesPresent;
    }

    /**
     * @param e
     * @return
     */
    private boolean isPredicateLoaded(AssociationPredicate e) {
        if (currentPredicate.equals(e))
            return true;
        else {
            currentPredicate = e;
            return false;
        }
    }

    /* (non-Javadoc)
     * @see org.castor.xml.UnmarshalListener#initialized(java.lang.Object, java.lang.Object)
     */
    public void initialized(Object target, Object parent) {
        if (target != null && target instanceof LexGridBase)
            ((LexGridBase) target).setParent(parent);
        else
            messages_.error(target.getClass().getName() + " is not an instance of LexGridBase");

    }

    public void attributesProcessed(Object target, Object parent) {

    }

    /* (non-Javadoc)
     * @see org.castor.xml.UnmarshalListener#unmarshalled(java.lang.Object, java.lang.Object)
     */
    public void unmarshalled(Object target, Object parent) {
        if(target instanceof CodingSchemes && parent instanceof SystemRelease){
            setCodingSchemes(LexGridElementProcessor.setAndRetrieveCodingSchemes());
        }
        else if (target instanceof ValueSetDefinition && (parent == null || parent instanceof ValueSetDefinitions)) {
            if (getValueSetDefinitions() == null || getValueSetDefinitions().length == 0)
                setValueSetDefinitions(new ValueSetDefinition[] {(ValueSetDefinition) target});
            else
            {
                List<ValueSetDefinition> vsdList = new ArrayList<ValueSetDefinition>();
                vsdList.add((ValueSetDefinition) target);
                vsdList.addAll(Arrays.asList(getValueSetDefinitions()));
                ValueSetDefinition[] vsdArray = new ValueSetDefinition[vsdList.size()];
                int i = 0;
                for (ValueSetDefinition vsd : vsdList)
                {
                    vsdArray[i++] = vsd;
                }
                setValueSetDefinitions(vsdArray);
            }
        }
        else if (target instanceof PickListDefinition && (parent instanceof PickListDefinitions)) {
            if (getPickListDefinitions() == null || getPickListDefinitions().length == 0)
                setPickListDefinitions(new PickListDefinition[] {(PickListDefinition) target});
            else
            {
                List<PickListDefinition> pldList = new ArrayList<PickListDefinition>();
                pldList.add((PickListDefinition) target);
                pldList.addAll(Arrays.asList(getPickListDefinitions()));
                PickListDefinition[] pldArray = new PickListDefinition[pldList.size()];
                int i = 0;
                for (PickListDefinition pld : pldList)
                {
                    pldArray[i++] = pld;
                }
                setPickListDefinitions(pldArray);
            }
        }
    }


    public CodingScheme[] getCodingSchemes() {
        return codingSchemes;
    }

    public void setCodingSchemes(CodingScheme[] codingSchemes) {
        this.codingSchemes = codingSchemes;
    }
    
    public ValueSetDefinition[] getValueSetDefinitions() {
        return valueSetDefinitions;
    }

    public void setValueSetDefinitions(ValueSetDefinition[] valueSetDefinitions) {
        this.valueSetDefinitions = valueSetDefinitions;
    }
    
    public PickListDefinition[] getPickListDefinitions() {
        return pickListDefinitions;
    }

    public void setPickListDefinitions(PickListDefinition[] pickListDefinitions) {
        this.pickListDefinitions = pickListDefinitions;
    }

    /*
     * (non-Javadoc)
     * @see org.castor.xml.UnmarshalListener#fieldAdded(java.lang.String, java.lang.Object, java.lang.Object)
     */
    public void fieldAdded(String fieldName, Object parent, Object child) {
        if (!inEditHistory && parent instanceof Revision && child instanceof EntityDescription) {
            inEditHistory = true;
        }
        if (!inEditHistory) {
            if (!isSystemReleaseSet) {
                systemRelease = getSystemRelease();
                initializeSystemReleaseLoad(getSystemRelease());
                isSystemReleaseSet = true;
            }
            if (UnMarshallingLogic.loadSystemReleaseCodingSchemeWithNoProperties(parent, child, survey)) {
                LexGridElementProcessor.processCodingSchemeMetadata(serviceAdaptor, parent, child);
            }
            if (UnMarshallingLogic.loadSystemReleaseCodingSchemeWithProperties(parent, child, survey)) {
                LexGridElementProcessor.processCodingSchemeMetadata(serviceAdaptor, parent, child);
            }

            if (!inEditHistory && UnMarshallingLogic.isCodingSchemeEntity(parent, child)) {
                LexGridElementProcessor.processCodingSchemeEntity(serviceAdaptor, parent, child);

            } else if (UnMarshallingLogic.isCodingSchemeEntities(parent, child)) {
                LexGridElementProcessor.removeEntitiesContainer(parent);

            } else if (UnMarshallingLogic.isCodingSchemeAssociation(parent, child)) {
                LexGridElementProcessor.processCodingSchemeAssociation(this
                        .isPredicateLoaded((AssociationPredicate) parent), serviceAdaptor, parent, child);

            }

            if (UnMarshallingLogic.isValueSetMappings(parent, child)) {
                currentValueSetMappings = LexGridElementProcessor
                        .processValueSetMappings(serviceAdaptor, parent, child);
            }
            if (UnMarshallingLogic.isValueSet(parent, child)) {
                String vsdURI = ((ValueSetDefinition) child).getValueSetDefinitionURI();
                messages_.info("Loading value set definition uri : " + vsdURI);
                try {
                    LexGridElementProcessor.processValueSet(serviceAdaptor, parent, child, currentValueSetMappings,
                            systemRelease.getReleaseURI());
                } catch (LBException e) {
                    messages_.error("Error loading VSD : " + vsdURI, e);
                    e.printStackTrace();
                }
            }
            if (UnMarshallingLogic.isPickListMappings(parent, child)) {
                currentPickListMappings = LexGridElementProcessor
                        .processPickListMappings(serviceAdaptor, parent, child);
            }
            if (UnMarshallingLogic.isPickListDefinition(parent, child)) {
                String pickListId = ((PickListDefinition) child).getPickListId();
                try {
                    messages_.info("Loading pick list definition id : " + pickListId);
                    LexGridElementProcessor.processPickListDefinition(serviceAdaptor, child, currentPickListMappings,
                            systemRelease.getReleaseURI());
                } catch (LBParameterException e) {
                    messages_.error("Error loading PickList : " + pickListId, e);
                    e.printStackTrace();
                } catch (LBException e) {
                    messages_.error("Error loading PickList : " + pickListId, e);
                    e.printStackTrace();
                }
            }
        }

        if (UnMarshallingLogic.isSystemReleaseCodingSchemeRevision(parent, child)) {
            LexGridElementProcessor.processCodingSchemeSystemReleaseRevision(serviceAdaptor, parent, child,
                    systemRelease.getReleaseURI());
        }
        if (UnMarshallingLogic.isSystemReleaseValueSetRevision(parent, child)) {
            LexGridElementProcessor.processValueSetDefinitionSystemReleaseRevision(serviceAdaptor, parent, child, null);
        }
        if (UnMarshallingLogic.isSystemReleasePickListRevision(parent, child)) {

            LexGridElementProcessor.processPickListDefinitionSystemReleaseRevision(serviceAdaptor, parent, child, null);
        }

    }


    /**
     * @return the messages_
     */
    public LgMessageDirectorIF getMessages_() {
        return messages_;
    }

    /**
     * @param messages the messages_ to set
     */
    public void setMessages_(LgMessageDirectorIF messages) {
        messages_ = messages;
    }

    public void setSystemReleaseMetaData(SystemRelease systemReleaseMetadata) {
     systemRelease = systemReleaseMetadata;
        
    }
    public SystemRelease getSystemRelease(){
        return systemRelease;
    }

}