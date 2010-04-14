package edu.mayo.informatics.lexgrid.convert.directConversions.claml;

import java.io.File;
import java.io.IOException;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Concept;

import org.LexGrid.LexBIG.claml.ClassKinds;
import org.LexGrid.LexBIG.claml.RubricKinds;


//GForge 22352 - setting the association depth to 1 does not return all the root nodes 
/**
 * This class converts the EMF rendering of ICD10 to ClaML format and writes it to file.
 * 
 * Note - the references for exclusion1 && inclusion relationships were appended to the 
 * definitions.  Major assumptions were made in getReference in order to parse out those values.
 */
public class EMF2ClaML {

    private LexBIGService lbSvc_ = null;
    private CodedNodeGraph cns_;
    
    private static ClaMLXMLWriter writer_;
    private static String codeSystemName_ = "";
    private static SortOptionList sortByCode_ = Constructors.createSortOptionList(new String[] {"code"});



    /**
     * Writes the contents of the LexGrid code system to the output file in ClaML format.
     * @param outputPath
     * @param codeSystemName
     * @throws Exception
     */
    public void convertEMFToClaML(String outputPath, String codeSystemName) throws Exception {
        codeSystemName_ = codeSystemName;
        lbSvc_ = new LexBIGServiceImpl();
        cns_ = lbSvc_.getNodeGraph(codeSystemName_, null, null);

        writer_ = new ClaMLXMLWriter(new File(outputPath));
        writer_.writeClaMLHeader(ClaMLConstants.LANG);
        exportEMF();
        writer_.writeClaMLFooter();
        writer_.close();
    }

    /**
     * Export the Lexgrid EMF content to a ClaML representation.
     */
    public void exportEMF() throws IOException, LBResourceUnavailableException, LBInvocationException, LBParameterException, LBException {
        // GForge 22352 - setting the association depth to 1 does not return all the root nodes 
        // Need to get all the root nodes without their associations first && then go through each one to get their RCR with associations included.
        ResolvedConceptReferenceList rcrlNoAssoc = cns_.resolveAsList(null, true, false, Integer.MAX_VALUE, 0, null, null, sortByCode_, null, -1);
        ResolvedConceptReference[] rcrNoAssoc = rcrlNoAssoc.getResolvedConceptReference();
        for (ResolvedConceptReference refNoAssoc : rcrNoAssoc)  
        {
            ResolvedConceptReferenceList rcrlWithAssoc = cns_.resolveAsList(refNoAssoc, true, false, Integer.MAX_VALUE, 1, null, null, sortByCode_, null, -1);
            ResolvedConceptReference[] rcrWithAssoc = rcrlWithAssoc.getResolvedConceptReference();
            for (ResolvedConceptReference refWithAssoc : rcrWithAssoc)  
            {
                // using the **** to detect missing code values
                generateClaMLContent(refWithAssoc, "****");
                getSubconcepts(refWithAssoc);
            }
        }
    }

    /**
     * Recursively gets all the subconcepts and writes them to the export file
     * @param ref
     * @throws IOException
     * @throws LBResourceUnavailableException
     * @throws LBInvocationException
     * @throws LBParameterException
     * @throws LBException
     */
    void getSubconcepts(ResolvedConceptReference ref) throws IOException, LBResourceUnavailableException, LBInvocationException, LBParameterException, LBException {
        LexBIGServiceImpl lbSvc_ = new LexBIGServiceImpl();
        CodedNodeGraph cns = lbSvc_.getNodeGraph(codeSystemName_, null, null);

        Association[] sourceAssocs = null;
        AssociatedConcept[] childConcepts = null;
        AssociationList sourceList = ref.getSourceOf();
        if(sourceList!=null) 
        {
            sourceAssocs = sourceList.getAssociation();
            for (Association sourceAssoc : sourceAssocs) 
            {
                // get all the children of the ref
                if(sourceAssoc.getAssociationName().contains(ClaMLConstants.CHILD_REL)) 
                {   
                    AssociatedConceptList childConceptList = sourceAssoc.getAssociatedConcepts();
                    childConcepts = childConceptList.getAssociatedConcept();
                    // resolve the all the children
                    for (AssociatedConcept childConcept : childConcepts) 
                    {
                        ResolvedConceptReferenceList rccrl = cns.resolveAsList((ResolvedConceptReference)childConcept, true, false, Integer.MAX_VALUE, 1, null, null, sortByCode_, null, -1);
                        ResolvedConceptReference[] resolvedChildConceptReferences = rccrl.getResolvedConceptReference();
                        // write out children && recursively get their children to write out
                        for (ResolvedConceptReference resolvedChildConceptReference : resolvedChildConceptReferences) 
                        {
                            generateClaMLContent(resolvedChildConceptReference, ref.getCode());
                            getSubconcepts(resolvedChildConceptReference);
                        }
                    }
                }
            }
        }
    }


    /**
     * Writes the entire Class hierarchy for a concept
     * @param ref
     * @param superCode
     * @return
     * @throws IOException
     * @throws LBResourceUnavailableException
     * @throws LBInvocationException
     * @throws LBParameterException
     */
    void generateClaMLContent(ResolvedConceptReference ref, String superCode ) throws IOException, LBResourceUnavailableException, LBInvocationException, LBParameterException {
        int counter = 1;            // counter is used to help create unique ids for concepts
        
        //printVariableValues(ref);  

        String code = ref.getCode();
        String desc = ref.getEntityDescription().getContent();
        // Class header && Superclasses
        // chapter classes do not write super classes
        if(desc.contains(ClaMLConstants.SUPER_NAME)) {
            writer_.writeClassHeader(ClassKinds.CHAPTER.description(), code);
            desc = desc.substring(0, desc.indexOf(ClaMLConstants.SUPER_NAME));
        }
        else if(code.contains("-")) {
            writer_.writeClassHeader(ClassKinds.BLOCK.description(), code);
            
            // hack workaround to replace super codes which do not validate against the dtd
            if(code.equalsIgnoreCase("K90-K94"))
            {
                superCode = "K00-K93";         
            }
            if(code.equalsIgnoreCase("O94-O9A"))
            {
                superCode = "O00-O99";
            }
            if(code.equalsIgnoreCase("V00-X58"))
            {
                superCode = "V01-Y98";
            }
            writer_.writeSuperClassLine(superCode);
        }
        else {
            // hack workaround to replace concept codes which do not validate against the dtd
            if(code.equalsIgnoreCase("O80, O82"))
            {
                code = "O80-O82";
            }
            if(code.equalsIgnoreCase("X52, X58"))
            {
                code = "X52-X58";
            }
            writer_.writeClassHeader(ClassKinds.CATEGORY.description(), code);

            // hack workaround to replace super codes which do not validate against the dtd
            if(code.equalsIgnoreCase("D49"))
            {
                superCode = "D37-D48";         
            }
            writer_.writeSuperClassLine(superCode);
        }

        // Subconcepts
        org.LexGrid.LexBIG.DataModel.Core.Association[] sources = null;
        AssociationList sourceList = ref.getSourceOf();
        if(sourceList!=null) {
            sources = sourceList.getAssociation();
            for (org.LexGrid.LexBIG.DataModel.Core.Association source : sources) {
                if(source.getAssociationName().contains(ClaMLConstants.CHILD_REL)) {   
                    AssociatedConceptList acl = source.getAssociatedConcepts();
                    AssociatedConcept[] assocConcepts = acl.getAssociatedConcept();
                    for (AssociatedConcept assocConcept : assocConcepts) {
                        writer_.writeSubClassLine(assocConcept.getConceptCode());
                    }
                }
            }
        }

        // Concept's definition
        writer_.writeRubricHeader(RubricKinds.PREFERRED.description(), code +ClaMLConstants.SEP +counter);
        writer_.writeLabelHeader(ClaMLConstants.DEFAULT, ClaMLConstants.LANG, desc);
        writer_.writeLabelFooter();
        writer_.writeRubricFooter();
        counter++;

        // Related concepts
        // start with the exclusion2 relations - these are listed as sources
        if(sourceList!=null) {
            String assocCode = "";
            String assocDesc = "";
            sources = sourceList.getAssociation();
            for (org.LexGrid.LexBIG.DataModel.Core.Association source : sources) {
                if(source.getAssociationName().contains(ClaMLConstants.EXCLUDES2)){  
                    AssociatedConceptList acl = source.getAssociatedConcepts();
                    AssociatedConcept[] assocConcepts = acl.getAssociatedConcept();
                    for (AssociatedConcept assocConcept : assocConcepts) {
                        assocCode = assocConcept.getConceptCode();
                        writer_.writeRubricHeader(RubricKinds.EXCLUSION2.description(), code +ClaMLConstants.SEP +assocCode);
                        assocDesc = assocConcept.getEntityDescription().getContent();
                        if(assocDesc.contains(ClaMLConstants.SUPER_NAME)) {
                            assocDesc = assocDesc.substring(0, assocDesc.indexOf(ClaMLConstants.SUPER_NAME));
                        }
                        writer_.writeLabelHeader(ClaMLConstants.DEFAULT, ClaMLConstants.LANG, assocDesc );
                        writer_.writeReferenceLine(assocCode, ClaMLConstants.IN_BRACKETS, assocCode);
                        writer_.writeLabelFooter();
                        writer_.writeRubricFooter();
                    }
                }
            }
        }

        // write out exclusion1 && inclusion relations - these are listed as properties
        Concept concept = ref.getReferencedEntry();
        if(concept!=null) {
            Property[] properties = concept.getAllProperties();
            if(properties!=null) {
                String propertyName = null; 
                String reference = null;
                String definition = "";
                boolean isRubric = false;
                for (Property property : properties) {
                    propertyName = property.getPropertyName();
                    definition = property.getValue().getContent();
                    reference = null;
                    isRubric = false;
	
                    if(propertyName.equalsIgnoreCase(ClaMLConstants.EXCLUDES1))
                    {
                        writer_.writeRubricHeader(RubricKinds.EXCLUSION1.description(), code +ClaMLConstants.SEP +counter);
                        isRubric = true;
                    }
                    else if(propertyName.equalsIgnoreCase(ClaMLConstants.INCLUDES) 
                            || propertyName.equalsIgnoreCase(ClaMLConstants.SYNONYMS))
                    {
                        writer_.writeRubricHeader(RubricKinds.INCLUSION.description(), code +ClaMLConstants.SEP +counter);
                        isRubric = true;
                    }

                    // only print out association properties.  
                    // some have reference embedded in definition so parse that out
                    if(isRubric)
                    {
                        definition = definition.trim();
                        // if there is a reference in the definition, get valid value
                        reference = getReference(definition);
                        if(reference!=null)
                        {
                            reference = cleanUpReference(reference);
                        }

                        writer_.writeLabelHeader(ClaMLConstants.DEFAULT, ClaMLConstants.LANG, definition );
                        if(reference!=null)
                        {
                            writer_.writeReferenceLine(reference, ClaMLConstants.IN_BRACKETS, reference);
                        }
                        writer_.writeLabelFooter();
                        writer_.writeRubricFooter();
                        counter++;
                    }
                }
            }
        }

        writer_.writeClassFooter();
    }
    
    /**
     * The definition may end with a reference value appended.  If the suffix contains a number, it
     * is assumed to be a reference && is returned.  Otherwise, null is returned.
     * @param definition
     * @return
     */
    String getReference(String definition)
    {
        String reference = null;

        if(definition.endsWith(")") && definition.contains("(") && !definition.contains("/"))
        {
            reference = definition.substring(definition.lastIndexOf("("));
        }

        // making sure references start with ( then capital letter then number seems to filter out the false reports
        if(reference!=null && (reference.matches("[(][A-Z][0-9](.*)" )))
        {
            return reference;
        }
        return null;
    }
    
    /**
     * Make sure the reference is valid according to dtd
     * @param reference
     * @return
     */
    String cleanUpReference(String reference)
    {
        reference = reference.replace("(", "");
        reference = reference.replace(")", "");
        reference = reference.replace(" ", "");
        if(reference.endsWith("-"))
        {
            reference = reference.replace("-", "");
        }
        if(reference.contains(","))
        {
            reference = reference.replace(",", "-");
        }
        if(reference.endsWith("."))
        {
            reference = reference.replace(".", "");
        }

        // need to remove an html special character set for dtd validation
        int indexOf160 = reference.indexOf(160);
        if(indexOf160 > -1)
        {
            String string160 = reference.substring(indexOf160, indexOf160+1);
            reference = reference.replace(string160, "");
        }
        
        return reference;
    }

    /**
     * helper class to print various variable values
     * @param ref
     */
    void printVariableValues(ResolvedConceptReference ref) {

        String code = ref.getCode();
        System.out.println("code = " +code);

        String codeNamespace = ref.getCodeNamespace();
        System.out.println("codeNamespace = " +codeNamespace);

        String codingSchemeName = ref.getCodingSchemeName();
        System.out.println("codingSchemeName = " +codingSchemeName);

        String codingSchemeURI = ref.getCodingSchemeURI();
        System.out.println("codingSchemeURI = " +codingSchemeURI);

        String codingSchemeVersion = ref.getCodingSchemeVersion();
        System.out.println("codingSchemeVersion = " +codingSchemeVersion);

        String conceptCode = ref.getConceptCode();
        System.out.println("conceptCode = " +conceptCode);

        EntityDescription ed = ref.getEntityDescription();
        String entityContent = ed.getContent();
        System.out.println("entityContent = " +entityContent);

        if(entityContent.contains("CHAPTER")) {
            System.out.println("found chapter = " +conceptCode);
        }

        String[] et = ref.getEntityType();
        for (String entityType : et) {
            System.out.println("entityType = " +entityType);
        }

        AssociationList sourceList = ref.getSourceOf();
        if(sourceList!=null) {
            org.LexGrid.LexBIG.DataModel.Core.Association[] sources = sourceList.getAssociation();
            for (org.LexGrid.LexBIG.DataModel.Core.Association source : sources) {
                System.out.println("source = " +source.getAssociationName());
                AssociatedConceptList acl = source.getAssociatedConcepts();
                AssociatedConcept[] assocConcepts = acl.getAssociatedConcept();
                for (AssociatedConcept assocConcept : assocConcepts) {
                    System.out.println("\tsource assoc getConceptCode = " +assocConcept.getConceptCode());
                    System.out.println("\t\tsource assoc desc = " +assocConcept.getEntityDescription().getContent());
                }
            }
        }

        AssociationList targetList = ref.getTargetOf(); 
        if(targetList!=null) {
            org.LexGrid.LexBIG.DataModel.Core.Association[] targets = targetList.getAssociation();
            for (org.LexGrid.LexBIG.DataModel.Core.Association target : targets) {
                System.out.println("target = " +target.getAssociationName());
            }
        }

        Concept concept = ref.getReferencedEntry();
        if(concept!=null) {
            Property[] properties = concept.getAllProperties();
            if(properties!=null) {
                String propertyName = null; 
                for (Property property : properties) {
                    propertyName = property.getPropertyName();
                    if(propertyName.equalsIgnoreCase("Excludes1"))
                    {
                        System.out.println("\tExcludes1 = " +property.getValue().getContent());
                    }
                    if(propertyName.equalsIgnoreCase("Includes"))
                    {
                        System.out.println("\tIncludes = " +property.getValue().getContent());
                    }

                }
            }
            org.LexGrid.concepts.Comment[] comments = concept.getComment();
            if(comments!=null) {
                for (org.LexGrid.concepts.Comment comment : comments) {
                    System.out.println("comment = " +comment.getValue());
                }
            }
            org.LexGrid.concepts.Definition[] definitions = concept.getDefinition();
            if(definitions!=null) {
                for (org.LexGrid.concepts.Definition definition : definitions) {
                    System.out.println("definition = " +definition.getValue());
                }
            }
        }
    }	
		
}
