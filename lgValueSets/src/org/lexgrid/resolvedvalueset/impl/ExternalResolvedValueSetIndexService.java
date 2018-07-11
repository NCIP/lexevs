package org.lexgrid.resolvedvalueset.impl;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.lexevs.dao.index.service.search.SourceAssertedValueSetSearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;

public class ExternalResolvedValueSetIndexService  {
	 SourceAssertedValueSetSearchIndexService service;
	public ExternalResolvedValueSetIndexService() {
		service = LexEvsServiceLocator.getInstance().
				getIndexServiceManager().getAssertedValueSetIndexService();
	}

	public void updateIndexForEntity(String codingSchemeUri, String codingSchemeVersion, Entity entity) {
		service.updateIndexForEntity(codingSchemeUri, codingSchemeVersion, entity);
	}
	
	public void addEntityToIndex(String codingSchemeUri, String codingSchemeVersion, String vsURI, String vsName, Entity entity) {
		service.addEntityToIndex(codingSchemeUri, codingSchemeVersion, vsURI, vsName, entity);
	}

	public void deleteEntityFromIndex(String codingSchemeUri, String codingSchemeVersion, Entity entity) {
		service.deleteEntityFromIndex(codingSchemeUri, codingSchemeVersion, entity);
	}
	
	public List<ResolvedConceptReference> getResolvedConceptReferencesForExternalResolvedValueSet(
			String codingSchemeUri, String codingSchemeVersion) throws URISyntaxException{
		LexEVSResolvedValueSetServiceImpl rvsSvc = new LexEVSResolvedValueSetServiceImpl();
		ResolvedConceptReferenceList list =  rvsSvc.getValueSetEntitiesForURI(codingSchemeUri);
		return Arrays.asList(
				list.getResolvedConceptReference()).stream().
				collect(Collectors.toList());
	}

	public List<AbsoluteCodingSchemeVersionReference> getExternalResolvedValueSetCodingSchemes(){
		List<RegistryEntry> entries = LexEvsServiceLocator.getInstance().getRegistry().getAllRegistryEntries();
		List<AbsoluteCodingSchemeVersionReference> vsEntries = entries.stream()
				.filter(z -> z.getDbName() != null)
				.filter(x -> x.getDbName().equals(OntologyFormat.RESOLVEDVALUESET.name())).
				map(entry -> Constructors.createAbsoluteCodingSchemeVersionReference(
						entry.getResourceUri(), entry.getResourceVersion())).
				collect(Collectors.toList());
		return vsEntries;
	}
	
	public void indexExternalResolvedValueSetsToAssertedValueSetIndex() {
		List<AbsoluteCodingSchemeVersionReference> refs =  getExternalResolvedValueSetCodingSchemes();
		refs.stream().forEach(ref -> {
			try {
				getResolvedConceptReferencesForExternalResolvedValueSet(
								ref.getCodingSchemeURN(), ref.getCodingSchemeVersion())
				.stream().forEach(rcr -> 
				this.addEntityToIndex(
						ref.getCodingSchemeURN(), 
						ref.getCodingSchemeVersion(), rcr.getCodingSchemeURI(), rcr.getCodingSchemeName(), rcr.getEntity()));
			} catch (URISyntaxException e) {
				throw new RuntimeException("Uri is not well formed: " + e);
			}
		});
		
	}

}
