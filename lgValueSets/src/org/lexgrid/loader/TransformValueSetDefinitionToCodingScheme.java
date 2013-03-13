package org.lexgrid.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;
import org.lexevs.logging.messaging.impl.CommandLineMessageDirector;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

public class TransformValueSetDefinitionToCodingScheme {
	URI valueSetDefinitionURI;
	String valueSetDefinitionRevisionId;
	AbsoluteCodingSchemeVersionReferenceList csVersionList;
	String csVersionTag;

	private LexEVSValueSetDefinitionServices vds_;

	TransformValueSetDefinitionToCodingScheme(URI valueSetDefinitionURI,
			String valueSetDefinitionRevisionId,
			AbsoluteCodingSchemeVersionReferenceList csVersionList,
			String csVersionTag) {
		this.valueSetDefinitionURI = valueSetDefinitionURI;
		this.valueSetDefinitionRevisionId = valueSetDefinitionRevisionId;
		this.csVersionList = csVersionList;
		this.csVersionTag = csVersionTag;
	}

	CodingScheme transform() throws Exception {

		InputStream stream = getValueSetDefinitionService()
				.exportValueSetResolution(valueSetDefinitionURI,
						valueSetDefinitionRevisionId, csVersionList,
						csVersionTag, false);
		CodingScheme codingScheme ;
		//read it with BufferedReader
    	BufferedReader br = new BufferedReader(new InputStreamReader(stream));
 
		//CodingScheme.unmarshalCodingScheme(stream);	
		codingScheme= CodingScheme.unmarshalCodingScheme(br);		
		return codingScheme;

	}

	CodingScheme transformOld() throws Exception {

		ValueSetDefinitionService vsdServ = LexEvsServiceLocator.getInstance()
				.getDatabaseServiceManager().getValueSetDefinitionService();

		ResolvedValueSetDefinition resDef = getValueSetDefinitionService()
				.resolveValueSetDefinition(valueSetDefinitionURI,
						valueSetDefinitionRevisionId, csVersionList,
						csVersionTag, null);

		ValueSetDefinition vsd = vsdServ.getValueSetDefinitionByRevision(this
				.getValueSetDefinitionURI().toString(), this
				.getValueSetDefinitionRevisionId());

		String codingSchemeUri = vsd.getValueSetDefinitionURI();
		String codingSchemeVersion = vsd.getEntryState() == null ? "UNASSIGNED"
				: vsd.getEntryState().getContainingRevision();

		String codingSchemeName = StringUtils.isEmpty(vsd
				.getValueSetDefinitionName()) ? codingSchemeUri : vsd
				.getValueSetDefinitionName();

		CodingScheme cs = new CodingScheme();

		cs.setCodingSchemeName(codingSchemeName);
		cs.setCodingSchemeURI(codingSchemeUri);
		cs.setRepresentsVersion(codingSchemeVersion);
		if (vsd.getEffectiveDate() != null)
			cs.setEffectiveDate(vsd.getEffectiveDate());
		if (vsd.getExpirationDate() != null)
			cs.setExpirationDate(vsd.getExpirationDate());
		cs.setEntryState(vsd.getEntryState());
		cs.setFormalName(codingSchemeName);
		cs.setIsActive(vsd.getIsActive());
		cs.setMappings(vsd.getMappings());
		cs.setOwner(vsd.getOwner());
		cs.setProperties(vsd.getProperties());
		cs.setSource(vsd.getSource());
		cs.setStatus(vsd.getStatus());

		Entities entities = new Entities();
		cs.setEntities(entities);

		Iterator<? extends AbsoluteCodingSchemeVersionReference> csList = resDef
				.getCodingSchemeVersionRefList()
				.iterateAbsoluteCodingSchemeVersionReference();
		System.out.println("Value domain: " + valueSetDefinitionURI.toString()
				+ " used: ");
		while (csList.hasNext()) {
			AbsoluteCodingSchemeVersionReference csr = csList.next();
			System.out.println(csr.getCodingSchemeURN() + " : "
					+ csr.getCodingSchemeVersion());
		}

		ResolvedConceptReferencesIterator crIter = resDef
				.getResolvedConceptReferenceIterator();

		while (crIter.hasNext()) {
			ResolvedConceptReference rcr = crIter.next();
			Entity entity = rcr.getEntity();
			entities.addEntity(entity);

		}

		return cs;

	}

	private LexEVSValueSetDefinitionServices getValueSetDefinitionService() {
		if (vds_ == null) {
			vds_ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
		}
		return vds_;
	}

	public URI getValueSetDefinitionURI() {
		return valueSetDefinitionURI;
	}

	public void setValueSetDefinitionURI(URI valueSetDefinitionURI) {
		this.valueSetDefinitionURI = valueSetDefinitionURI;
	}

	public String getValueSetDefinitionRevisionId() {
		return valueSetDefinitionRevisionId;
	}

	public void setValueSetDefinitionRevisionId(
			String valueSetDefinitionRevisionId) {
		this.valueSetDefinitionRevisionId = valueSetDefinitionRevisionId;
	}

	public AbsoluteCodingSchemeVersionReferenceList getCsVersionList() {
		return csVersionList;
	}

	public void setCsVersionList(
			AbsoluteCodingSchemeVersionReferenceList csVersionList) {
		this.csVersionList = csVersionList;
	}

	public String getCsVersionTag() {
		return csVersionTag;
	}

	public void setCsVersionTag(String csVersionTag) {
		this.csVersionTag = csVersionTag;
	}

	public static void main(String args[]) {
		try {

			TransformValueSetDefinitionToCodingScheme tvd2cs = new TransformValueSetDefinitionToCodingScheme(new URI("SRITEST:AUTO:EveryThing"), null, null, null);
			CachingMessageDirectorIF message = new CachingMessageDirectorImpl(
					new CommandLineMessageDirector());
			CodingScheme codingScheme = tvd2cs.transform();

			URI output_filename = URI
					.create("file:///tmp/transformValueSetDefinitionToCodeSystem.xml");
			File file = new File(output_filename);
			// file.createNewFile();
			FileWriter out = new FileWriter(file);
			codingScheme.marshal(out);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
