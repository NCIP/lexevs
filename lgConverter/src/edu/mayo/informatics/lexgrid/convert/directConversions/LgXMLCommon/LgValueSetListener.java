package edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon;


import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.Revision;
import org.mayo.edu.lgModel.LexGridBase;
import org.castor.xml.UnmarshalListener;

public class LgValueSetListener implements UnmarshalListener {
    
    private int nentities = 0;
    private int nassociations = 0;
    int modCount = 0;
    private static final int mod = 10;
    
    private boolean isCodingSchemeLoaded = false;
    private boolean isRevisionLoaded = false;
    private boolean isPropertiesPresent = false;
    private AssociationPredicate currentPredicate = new AssociationPredicate();
    private Revision revision = new Revision();
    private CodingScheme[] codingSchemes = null;

    private XMLDaoServiceAdaptor serviceAdaptor = null;
    private LgMessageDirectorIF messages_;

    


    public LgValueSetListener() {
        super();
        serviceAdaptor = new XMLDaoServiceAdaptor();
    }
    
    public LgValueSetListener(LgMessageDirectorIF messages) {
        super();
        serviceAdaptor = new XMLDaoServiceAdaptor();
        messages_ = messages;
    }
    /**
     * @return
     */
    int getNentities() {
        return nentities;
    }
    
    public CodingScheme[] getCodingSchemes() {
        return codingSchemes;
    }

    public void setCodingSchemes(CodingScheme[] codingSchemes) {
        this.codingSchemes = codingSchemes;
    }

    /**
     * @return
     */
    int getNassociations() {
        return nassociations;
    }

    /**
     * @return
     */
   boolean isPropertiesPresent() {
        return isPropertiesPresent;
    }

    /**
     * @param isPropertiesPresent
     */
    void setPropertiesPresent(boolean isPropertiesPresent) {
        this.isPropertiesPresent = isPropertiesPresent;
    }

    /**
     * @param e
     * @return
     */
    boolean isPredicateLoaded(AssociationPredicate e) {
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

    /* (non-Javadoc)
     * @see org.castor.xml.UnmarshalListener#attributesProcessed(java.lang.Object, java.lang.Object)
     */
    public void attributesProcessed(Object target, Object parent) {

    }

    /* (non-Javadoc)
     * @see org.castor.xml.UnmarshalListener#unmarshalled(java.lang.Object, java.lang.Object)
     */
    public void unmarshalled(Object target, Object parent) {
        
                messages_.debug("Unmarshalled target: "
                + (target != null ? target.getClass().getSimpleName() : "target is null"));
                messages_.debug("parent of Unmarshalled target: "
                + (parent != null ? parent.getClass().getSimpleName() : "parent is null"));
        
        if(target instanceof ValueSetDefinition && parent == null){
            setCodingSchemes(LexGridElementProcessor.setAndRetrieveCodingSchemes());
           messages_.info("Entity Count: " + nentities);
           messages_.info("Association Count: " + nassociations);
           try {
            LexGridElementProcessor.processValueSetDefinition(serviceAdaptor, target, parent);
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        }
    }

    /* (non-Javadoc)
     * @see org.castor.xml.UnmarshalListener#fieldAdded(java.lang.String, java.lang.Object, java.lang.Object)
     */
    public void fieldAdded(String fieldName, Object parent, Object child) {

        messages_.debug("fieldName:" + fieldName);
        messages_.debug("parent: " + parent.getClass().getSimpleName());
        messages_.debug("child: " + child.getClass().getSimpleName());
        
//        if (UnMarshallingLogic.isValueSetDefinition(parent, child)) {
//            String vsdURI = ((ValueSetDefinition) child).getValueSetDefinitionURI();
//            messages_.info("Loading value set definition uri : " + vsdURI);
//            try {
//                LexGridElementProcessor.processValueSetDefinition(serviceAdaptor, parent, child);
//            } catch (LBException e) {
//                messages_.error("Error loading VSD : " + vsdURI, e);
//                e.printStackTrace();                
//            }
//        }
        
    }
}
