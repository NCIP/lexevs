
package org.lexgrid.valuesets.helper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.PropertyMatchValue;
import org.LexGrid.valueSets.PropertyReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.valuesets.helper.compiler.ValueSetDefinitionCompiler;

/**
* @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
*/
public class DefaultCompiler implements ValueSetDefinitionCompiler {
	
	private ValueSetDefinitionService vsds_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getValueSetDefinitionService();

	private VSDServiceHelper helper;
	
	public DefaultCompiler(VSDServiceHelper helper) {
		this.helper = helper;
	}
	
	@Override
	public CodedNodeSet compileValueSetDefinition(ValueSetDefinition vdd,
			HashMap<String, String> refVersions, String versionTag, HashMap<String, ValueSetDefinition> referencedVSDs)
			throws LBException {
		return this.getCodedNodeSetForValueSet(vdd, refVersions, versionTag, referencedVSDs);
	}
	
	 /**
     * Resolves the supplied valueSetDefinition object against the list of coding scheme versions 
     * @param vdd - the value domain to be resolved
     * @param refVersions - a map from coding scheme URIs to the corresponding version
     * @param versionTag - a tag (e.g. "production", "test", etc. used to resolve missing coding schemes)
     *   If a coding scheme does not appear in this list the resolution will be as follows:
     *                1) If the service supports a single version of the coding scheme it will be used.
     *                2) If there is more than one version the one that uses the supplied versionTag will be used
     *                3) If the versionTag isn't supplied, or if none of the versions matches it, then the one
     *                    marked "production" will be used
     *                4) If there isn't one marked production, then the "latest" will be used    
	 * @param referencedVSDs - List of ValueSetDefinitions referenced by vsDef. If provided, these ValueSetDefinitions will be used to resolve vsDef.
	 */
    protected CodedNodeSet getCodedNodeSetForValueSet( 
	        ValueSetDefinition vdd, HashMap<String, String> refVersions, String versionTag, 
	        HashMap<String, ValueSetDefinition> referencedVSDs) 
                                                        throws LBException {
	    CodedNodeSet finalNodeSet = null;
	    
		// Iterate over the value domain resolving contents
		if(vdd != null && vdd.getDefinitionEntry() != null) {
		    Iterator<DefinitionEntry> defIter = vdd.getDefinitionEntryAsReference().iterator();
		    while(defIter.hasNext()) {
		        DefinitionEntry vdDef = defIter.next();
		        CodedNodeSet product = null;
		        
		        // All of the contents of a coding scheme
		        if(vdDef.getCodingSchemeReference() != null) {
		            product = helper.getNodeSetForCodingScheme(vdd, vdDef.getCodingSchemeReference().getCodingScheme(), refVersions, versionTag);
		        } else if(vdDef.getValueSetDefinitionReference() != null) {
		        	String refVSDURI = vdDef.getValueSetDefinitionReference().getValueSetDefinitionURI();
		        	
		        	// A value set definition can not reference to itself, this will cause a cycle  
		        	if (vdd.getValueSetDefinitionURI().equalsIgnoreCase(refVSDURI))
		        		throw new LBException("ValueSetDefinition can not reference itself");
		        	
		            ValueSetDefinition innerVdd = null;
                    try {
                    	
                    	// check if referenced VSD is supplied, if so, we will use it to resolve and won't bother to look if that VSD is 
                    	// available in the service
                    	if (referencedVSDs != null)
                    		innerVdd = referencedVSDs.get(refVSDURI);
                    	
                    	// look for referenced VSD in the terminology service if not supplied
                    	if (innerVdd == null)
                    		innerVdd = vsds_.getValueSetDefinitionByUri(new URI(vdDef.getValueSetDefinitionReference().getValueSetDefinitionURI()));
                    } catch (URISyntaxException e) {
                        // TODO This is a data error.  We whine in enough places that it isn't worth doing here
                    }
                    if(innerVdd != null)
                        product = getCodedNodeSetForValueSet(innerVdd, refVersions, versionTag, referencedVSDs);   
		        } else if(vdDef.getEntityReference() != null) {
		            product = getNodeSetForEntityReference(vdd, vdDef.getEntityReference(), refVersions, versionTag);
		        } else if (vdDef.getPropertyReference() != null) {
		        	product = getNodeSetForPropertyReference(vdd, vdDef.getPropertyReference(), refVersions, versionTag);
		        }
		        	
		        if(product != null) {
    		        if (vdDef.getOperator() != null)
    		        {
    		        	if (vdDef.getOperator().value().equals(DefinitionOperator.OR.value())) 
    		        		finalNodeSet = finalNodeSet == null? product : finalNodeSet.union(product);
    		        	else if (vdDef.getOperator().value().equals(DefinitionOperator.AND.value()))
    		        		finalNodeSet = finalNodeSet == null? null : finalNodeSet.intersect(product);
    		        	else if (vdDef.getOperator().value().equals(DefinitionOperator.SUBTRACT.value()))
    		        		finalNodeSet = finalNodeSet == null? null : finalNodeSet.difference(product);
    		        }
		        } else {
		            // TODO we probably want to say something when we get no resolution at all.
		        }
		    }
		}
		return finalNodeSet;
	}
	

	
	/**
	 * Return a coded node set that represents the supplied entity reference
	 * @param vdd - containing value set definition
	 * @param entityRef - entity reference to resolve
	 * @param refVersions - fixed versions to resolve against
	 * @param versionTag - version tag to resolve elsewise
	 * @return corresponding coded node set
	 * @throws LBException
	 */
	protected CodedNodeSet getNodeSetForEntityReference(
	        ValueSetDefinition vdd, EntityReference entityRef, HashMap<String, String> refVersions, String versionTag) 
    throws LBException {
	    
	    // Locate the coding scheme namespace
	    String entityCodeCodingScheme = helper.getCodingSchemeNameForNamespaceName(vdd.getMappings(), entityRef.getEntityCodeNamespace());
	    if(StringUtils.isEmpty(entityCodeCodingScheme))
	        entityCodeCodingScheme = vdd.getDefaultCodingScheme();
	    if(StringUtils.isEmpty(entityCodeCodingScheme)) {
	        // TODO report an error here.  Can't have a code without some coding scheme reference
	        return null;
	    }
	    AbsoluteCodingSchemeVersionReference resVersion = helper.resolveCSVersion(entityCodeCodingScheme, vdd.getMappings(), versionTag, refVersions);
	    CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
	    try{
	    	versionOrTag.setVersion(resVersion.getCodingSchemeVersion());
	    }catch(NullPointerException e){
	    	throw new LBException("Coding Scheme not found in the system");
	    }
	    ConceptReference cr = ConvenienceMethods.createConceptReference(entityRef.getEntityCode(), resVersion.getCodingSchemeURN());
	    // Option 1: A single entity code
	    if (StringUtils.isEmpty(entityRef.getReferenceAssociation()) ) {
	        ConceptReferenceList crl = new ConceptReferenceList();
	        
	        crl.addConceptReference(cr);
	        return helper.getLexBIGService().getCodingSchemeConcepts(resVersion.getCodingSchemeURN(), versionOrTag).restrictToCodes(crl);
	    }
	    
	    // Option 2: Some type of graph
	    // TODO file model bug report because we don't know the relation container name here...
	    CodedNodeGraph cng = helper.getLexBIGService().getNodeGraph(resVersion.getCodingSchemeURN(), versionOrTag, null);
	    cng = cng.restrictToAssociations(Constructors.createNameAndValueList(entityRef.getReferenceAssociation()), null);
	    return entityRef.isLeafOnly()?
	            leavesOfGraph(cng, entityRef.isTargetToSource(), cr, vdd, refVersions, versionTag) :
	            cng.toNodeList(cr, !entityRef.isTargetToSource(), entityRef.isTargetToSource(), entityRef.isTransitiveClosure()? -1 : 1, -1);
	}
	
	/**
	 * Return a coded node set that represents the supplied property reference
	 * @param vdd - containing value set definition
	 * @param propertyRef - property reference to resolve
	 * @param refVersions - fixed versions to resolve against
	 * @param versionTag - version tag to resolve elsewise
	 * @return corresponding coded node set
	 * @throws LBException
	 */
	protected CodedNodeSet getNodeSetForPropertyReference(
	        ValueSetDefinition vdd, PropertyReference propertyRef, HashMap<String, String> refVersions, String versionTag) 
    throws LBException {
	    
		AbsoluteCodingSchemeVersionReference resVersion = helper.resolveCSVersion(propertyRef.getCodingScheme(), vdd.getMappings(), versionTag, refVersions);
	    CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
	    try{
	    	if (refVersions != null && refVersions.containsKey(resVersion.getCodingSchemeURN()))
	    		versionOrTag.setVersion(refVersions.get(resVersion.getCodingSchemeURN()));
	    	else
	    		versionOrTag.setVersion(resVersion.getCodingSchemeVersion());	    	
	    }catch(NullPointerException e){
	    	throw new LBException("Coding Scheme not found in the system");
	    }
	    String propertyMatchValue = null;
	    String matchAlgorithm = null;
	    
	    CodedNodeSet cns = helper.getLexBIGService().getNodeSet(propertyRef.getCodingScheme(), versionOrTag, null);
	    
	    PropertyMatchValue pmv = propertyRef.getPropertyMatchValue();
	    if (pmv != null)
	    {
	    	propertyMatchValue = pmv.getContent();
	    	matchAlgorithm = pmv.getMatchAlgorithm();
	    }
	    if (StringUtils.isEmpty(matchAlgorithm))
	    	matchAlgorithm = MatchAlgorithms.LuceneQuery.name();
	    
	    if (StringUtils.isNotEmpty(propertyMatchValue))
	    {
	    	cns.restrictToMatchingProperties(
	    		StringUtils.isNotEmpty(propertyRef.getPropertyName()) ? Constructors.createLocalNameList(propertyRef.getPropertyName()): null, 
	    				new PropertyType[] { PropertyType.PRESENTATION, PropertyType.GENERIC }, propertyMatchValue, matchAlgorithm, null);
	    }
	    else if (StringUtils.isNotEmpty(propertyRef.getPropertyName()))
	    {
	    	cns.restrictToProperties(Constructors.createLocalNameList(propertyRef.getPropertyName()), null);
	    }
	    	
	    return cns;
	}
	
	/**
     * Return the leaf nodes for the supplied graph.  As the graph to be traversed could be quite large, this is
     * done breadth first and non-recursively.  With apologies to Walt Whitman
     * 
     * Note: were we to implement both the forward and reverse closure on
     * transitive graphs, this routine could be replaced with the intersection of the supplied graph and the immediate
     * children or ancestors of the top or bottom nodes respectively.
     * 
     * @param cng - graph to be traversed
     * @param isTargetToSource - direction to traverse the graph
     * @param root - the root node to start the traverse at
     * @param vdd  - value domain definition to resolve leaf nodes against if isLeaf is set
     * @param refVersions - map of coding Scheme URI to version (may be updated by this routine)
     * @param versionTag  - version tag (e.g. devel, production, etc.) to resolve new nodes
     * @return - a list of all leaf nodes
	 * @throws LBException 
     */
    protected CodedNodeSet leavesOfGraph(
            CodedNodeGraph cng, boolean isTargetToSource, ConceptReference root, ValueSetDefinition vdd, HashMap<String,String> refVersions, String versionTag) 
                    throws LBException {
 
        ConceptReferenceList leaves = new ConceptReferenceList();
        ConceptReferenceList probes = new ConceptReferenceList();
        probes.addConceptReference(root);
        HashSet<String> seenNode = new HashSet<String>();          // Trade performance for space
       
        while(probes.getConceptReferenceCount() > 0) {
            ConceptReferenceList newProbes = new ConceptReferenceList();
            Iterator<? extends ConceptReference> cri = probes.iterateConceptReference();
            while(cri.hasNext()) {
                ConceptReference probe = cri.next();
                // Never look at a node more than once. 
                if(seenNode.contains(helper.constructKey(probe)))
                    continue;
                if(seenNode.size() < helper.getMaxLeafCacheSize())
                    seenNode.add(helper.constructKey(probe));
                
                boolean probeHasChildren = false;
                CodedNodeSet directChildren = cng.toNodeList(probe, !isTargetToSource, isTargetToSource, 1, -1);
                if(directChildren != null) {
                    ResolvedConceptReferencesIterator dcIter = directChildren.resolve(null, null, null, null, false);
                    while(dcIter.hasNext()) {
                        ConceptReference childNode = dcIter.next();
                        if(!helper.equalReferences(probe, childNode)) {
                            probeHasChildren = true;
                            newProbes.addConceptReference(childNode);
                        }
                    }
                }
                
                if(!probeHasChildren)
                    leaves.addConceptReference(probe);
            }
            probes = newProbes;
        }
        return helper.conceptReferenceListToCodedNodeSet(leaves, vdd, refVersions, versionTag );
    }  
}