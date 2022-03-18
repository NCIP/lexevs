
package org.lexgrid.valuesets.sourceasserted.impl;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.valuesets.AssertedValueSetService;
import org.lexevs.dao.database.service.valuesets.AssertedValueSetServiceImpl;
import org.lexevs.locator.LexEvsServiceLocator;

public class AssertedValueSetEntityResolver implements Serializable {

/**
	 * 
	 */
private static final long serialVersionUID = -2108382187250742000L;
	AssertedValueSetService vsSvc;
    AssertedValueSetParameters params;
	String code;
	
	public AssertedValueSetEntityResolver(AssertedValueSetParameters params, String code) {
		this.params = params;
		vsSvc = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAssertedValueSetService();
		vsSvc.init(params);
		this.code = code;
	}

	public ResolvedConceptReferenceList getResolvedConceptReferenceByCursorAndCode(String topNode, int position,
			int maxToReturn) {
		AssertedValueSetService avss = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAssertedValueSetService();
		avss.init(this.params);
		List<Entity> list = avss.getPagedSourceAssertedValueSetEntities(topNode, position, maxToReturn);
		ResolvedConceptReferenceList refList = new ResolvedConceptReferenceList();
		list.stream().map((entity -> resolvedConceptReferenceFromEntityTransform(topNode, entity))).forEachOrdered(refList::addResolvedConceptReference);
		return refList;
	}
	
	public List<ResolvedConceptReference> getPagedConceptReferenceByCursorAndCode(String topNode, int position,
			int maxToReturn) {
		AssertedValueSetService avss = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAssertedValueSetService();
		avss.init(this.params);
		List<Entity> list = avss.getPagedSourceAssertedValueSetEntities(topNode, position, maxToReturn);
		return list.stream().map((entity -> resolvedConceptReferenceFromEntityTransform(topNode, entity))).collect(Collectors.toList());
	}
	
	public int getTotalEntityCount() {
		return vsSvc.getVSEntityCountForTopNodeCode(this.code);
	}

	private ResolvedConceptReference resolvedConceptReferenceFromEntityTransform(String topNode, Entity entity) {
        AssertedValueSetParameters params = ((AssertedValueSetServiceImpl) vsSvc).getParams();
        Entity topNodeEntity = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getEntityService().getEntity(params.getCodingSchemeURI(), 
        		params.getCodingSchemeVersion(), topNode, null);
        return AssertedValueSetServices.transformEntityToRCR(topNodeEntity, entity, params);
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