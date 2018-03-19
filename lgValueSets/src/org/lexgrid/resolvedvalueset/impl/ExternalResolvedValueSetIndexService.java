package org.lexgrid.resolvedvalueset.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.index.service.search.SourceAssertedValueSetSearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry.Entry;

public class ExternalResolvedValueSetIndexService extends SourceAssertedValueSetSearchIndexService {

	@Override
	public void updateIndexForEntity(String codingSchemeUri, String codingSchemeVersion, Entity entity) {
		super.updateIndexForEntity(codingSchemeUri, codingSchemeVersion, entity);

	}

	@Override
	public void addEntityToIndex(String codingSchemeUri, String codingSchemeVersion, Entity entity) {
		super.addEntityToIndex(codingSchemeUri, codingSchemeVersion, entity);
	}

	@Override
	public void deleteEntityFromIndex(String codingSchemeUri, String codingSchemeVersion, Entity entity) {
		super.deleteEntityFromIndex(codingSchemeUri, codingSchemeVersion, entity);
	}
	
	public List<Entity> getEntitiesForExternalResolvedValueSet(String codingSchemeUri, String codingSchemeVersion){
		LexEVSResolvedValueSetServiceImpl rvsSvc = new LexEVSResolvedValueSetServiceImpl();
		ResolvedConceptReferenceList list =  rvsSvc.getValueSetEntitiesForURI(codingSchemeUri);
		return Arrays.asList(list.getResolvedConceptReference()).stream().map(rcr -> transFormRCRToEntity(rcr)).collect(Collectors.toList());
	}
	
	private Entity transFormRCRToEntity(ResolvedConceptReference rcr) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AbsoluteCodingSchemeVersionReference> getExternalResolvedValueSetCodingSchemes(){
		List<RegistryEntry> entries = LexEvsServiceLocator.getInstance().getRegistry().getAllRegistryEntries();
		List<AbsoluteCodingSchemeVersionReference> vsEntries = entries.stream().filter(
				x -> x.getDbUri().equals(OntologyFormat.RESOLVEDVALUESET)).
				map(entry -> Constructors.createAbsoluteCodingSchemeVersionReference(
						entry.getResourceUri(), entry.getResourceVersion())).
				collect(Collectors.toList());
		return vsEntries;
	}

}
