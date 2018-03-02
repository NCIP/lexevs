package org.lexgrid.valuesets.sourceasserted.impl;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.lexevs.dao.database.service.valuesets.AssertedValueSetService;
import org.lexevs.dao.database.service.valuesets.AssertedValueSetServiceImpl;
import org.lexevs.locator.LexEvsServiceLocator;

public class AssertedValueSetEntityResolver implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2108382187250742000L;
	AssertedValueSetService vsSvc;
//	int maxValueSets = 0;
	String code;
	
	public AssertedValueSetEntityResolver(AssertedValueSetParameters params, String code) {
		vsSvc = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAssertedValueSetService();
		vsSvc.init(params);
		this.code = code;
//		maxValueSets = vsSvc.getVSEntityCountForTopNodeCode(code);
	}

	public ResolvedConceptReferenceList getResolvedConceptReferenceByCursorAndCode(String topNode, int position,
			int maxToReturn) {
		List<Entity> list = vsSvc.getPagedSourceAssertedValueSetEntities(topNode, position, maxToReturn);
		ResolvedConceptReferenceList refList = new ResolvedConceptReferenceList();
		list.stream().map((entity -> resolvedConceptReferenceFromEntityTransform(entity))).forEachOrdered(refList::addResolvedConceptReference);
		return refList;
	}
	
	public List<ResolvedConceptReference> getPagedConceptReferenceByCursorAndCode(String topNode, int position,
			int maxToReturn) {
		List<Entity> list = vsSvc.getPagedSourceAssertedValueSetEntities(topNode, position, maxToReturn);
		return list.stream().map((entity -> resolvedConceptReferenceFromEntityTransform(entity))).collect(Collectors.toList());
	}
	
	public int getTotalEntityCount() {
		return vsSvc.getVSEntityCountForTopNodeCode(this.code);
	}

	private ResolvedConceptReference resolvedConceptReferenceFromEntityTransform(Entity entity) {
        AssertedValueSetParameters params = ((AssertedValueSetServiceImpl) vsSvc).getParams();
        return AssertedValueSetServices.transformEntityToRCR(entity, params);
	}
	
	
	
	public static void main(String[] args) {
		AssertedValueSetService svc = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAssertedValueSetService();
		svc.init(new AssertedValueSetParameters.Builder("0.1.5").
				assertedDefaultHierarchyVSRelation("Concept_In_Subset").
				codingSchemeName("owl2lexevs").
				codingSchemeURI("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl")
				.build());
		ResolvedConceptReferenceList list = new AssertedValueSetEntityResolver(new AssertedValueSetParameters.Builder("0.1.5").
				assertedDefaultHierarchyVSRelation("Concept_In_Subset").
				codingSchemeName("owl2lexevs").
				codingSchemeURI("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl")
				.build(), "C99999").getResolvedConceptReferenceByCursorAndCode("C99999",0, 10);
		System.out.println("" + list.getResolvedConceptReferenceCount());
	}

}
