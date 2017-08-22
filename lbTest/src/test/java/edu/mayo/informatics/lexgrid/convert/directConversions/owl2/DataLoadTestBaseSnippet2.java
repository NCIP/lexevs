package edu.mayo.informatics.lexgrid.convert.directConversions.owl2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Definition;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import junit.framework.TestCase;

public class DataLoadTestBaseSnippet2 extends TestCase {

	/** The lbs. */
	protected LexBIGService lbs;
	protected CodingScheme cs;
	protected CodingScheme csp;
	protected CodedNodeSet cns;
	protected CodedNodeGraph cng;
	protected CodedNodeSet cnsp;
	protected CodedNodeGraph cngp;
	
	/**
	 * Sets the up lbs.
	 * @throws LBException 
	 */
	@Before
	public void setUp() throws Exception{
		lbs = ServiceHolder.instance().getLexBIGService();
		cs = lbs.resolveCodingScheme(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_VERSION));
		csp = lbs.resolveCodingScheme(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_PRIMITIVE_VERSION));
		cns = lbs.getNodeSet(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_VERSION), null);
		cng = lbs.getNodeGraph(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_VERSION),
				null);
		cnsp = lbs.getNodeSet(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_PRIMITIVE_VERSION), null);
		cngp = lbs.getNodeGraph(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN,
				Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_PRIMITIVE_VERSION), null);
	}
	
	
	@Test
	public void testSetUp() throws Exception {
		Assert.noNullElements(new Object[] {lbs,cns,cng});
	}
	
	protected boolean validateQualifier(String code, String qual, Iterator<? extends ResolvedConceptReference> itr) throws LBResourceUnavailableException, LBInvocationException{
		boolean validate = false;
		while (itr.hasNext()) {
			ResolvedConceptReference ref = itr.next();
			AssociationList assoc1 = ref.getSourceOf();
			Association[] assocs = assoc1.getAssociation();
			for (Association as : assocs) {
				AssociatedConceptList acl = as.getAssociatedConcepts();
				AssociatedConcept[] acs = acl.getAssociatedConcept();
				for (AssociatedConcept ac : acs) {
					if (ac.getCode().equals(code)) {
						if(ac.getAssociationQualifiers() != null){
						for(NameAndValue nv: ac.getAssociationQualifiers().getNameAndValue()){
						if(nv.getContent().equals(qual))
						validate = true;
						break;
						}
						}
					}
				}
			}
		}
		return validate;
	}
	
	protected boolean validatePropertyQualifierFromProperty(Property prop, String qual){
		boolean validate = false;

					for(PropertyQualifier pq: prop.getPropertyQualifier()){
					if(pq.getValue().getContent().equals(qual))
					validate = true;
					}
				
			
		
		return validate;
	}
	
	
	protected boolean validateTarget(String target,
			Iterator<? extends ResolvedConceptReference> itr) {
		boolean validate = false;
		while (itr.hasNext()) {
			ResolvedConceptReference ref = itr.next();
			AssociationList assoc1 = ref.getSourceOf();
			if(assoc1 == null){
				continue;
			}
			Association[] assocs = assoc1.getAssociation();
			for (Association as : assocs) {
				AssociatedConceptList acl = as.getAssociatedConcepts();
				AssociatedConcept[] acs = acl.getAssociatedConcept();
				for (AssociatedConcept ac : acs) {
					if (ac.getCode().equals(target)) {
						validate = true;
						break;
					}
				}
			}
		}
		return validate;
	}
	
	protected boolean validateProperty(String name, String value, ResolvedConceptReference rcr){
		Property[] props1 = rcr.getEntity().getAllProperties();
		Property[] props2 = rcr.getEntity().getDefinition();
		
		ArrayList<Property> allProps = new ArrayList<Property>();
		Collections.addAll(allProps, props1);
		Collections.addAll(allProps, props2);
		Property[] props = allProps.toArray(new Property[allProps.size()]);
		
		if(props == null){
			return false;
		}
		if(props.length == 0){
			return false;
		}
		boolean hasProp = false;
		for(Property prop: props){
			if(prop.getPropertyName().equals(name)  && prop.getValue().getContent().equals(value)){
				hasProp = true;
				break;
			}
		}
		return hasProp;
	}
	
	protected boolean validatePropertyQualifier(String name, String value, ResolvedConceptReference rcr){
		Property[] props1 = rcr.getEntity().getAllProperties();
		Property[] props2 = rcr.getEntity().getDefinition();
		
		ArrayList<Property> allProps = new ArrayList<Property>();
		Collections.addAll(allProps, props1);
		Collections.addAll(allProps, props2);
		Property[] props = allProps.toArray(new Property[allProps.size()]);
		
		if(props == null){
			return false;
		}
		if(props.length == 0){
			return false;
		}
		boolean hasProp = false;
		for(Property prop: props){
			if(prop.getPropertyName().equals(name) ){
				for(PropertyQualifier qual : prop.getPropertyQualifier()){
					if(qual.getValue().getContent().equals(value)){
					hasProp = true;
					break;
					}
				}
			}
		}
		return hasProp;
	}
	
	protected boolean validateDefinitionSource(String value, ResolvedConceptReference rcr){
		Definition prop = rcr.getEntity().getDefinitionAsReference().get(0);
		return prop.getSourceAsReference().stream().anyMatch(x -> x.getContent().equals(value));
	}
	
}
