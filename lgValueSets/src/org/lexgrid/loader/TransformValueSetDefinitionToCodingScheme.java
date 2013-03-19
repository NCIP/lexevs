package org.lexgrid.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
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
import org.lexgrid.valuesets.dto.ResolvedValueSetCodedNodeSet;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

public class TransformValueSetDefinitionToCodingScheme {
	URI valueSetDefinitionURI;
	String valueSetDefinitionRevisionId;
	AbsoluteCodingSchemeVersionReferenceList csVersionList;
	String csVersionTag;

	private LexEVSValueSetDefinitionServicesImpl vds_;

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
		CodingScheme codingScheme;
		// read it with BufferedReader
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));

		// CodingScheme.unmarshalCodingScheme(stream);
		codingScheme = CodingScheme.unmarshalCodingScheme(br);
		setCodingSchemeVersion(codingScheme);
		return codingScheme;

	}

	void setCodingSchemeVersion(CodingScheme codingScheme) throws LBException {
		int uuid = generateHashCode();
		codingScheme.setRepresentsVersion("" + uuid);
	}
	

	int generateHashCode() throws LBException {
		ResolvedValueSetCodedNodeSet rvscs = getValueSetDefinitionService()
				.getCodedNodeSetForValueSetDefinition(valueSetDefinitionURI,
						valueSetDefinitionRevisionId, csVersionList,
						csVersionTag);
		AbsoluteCodingSchemeVersionReferenceList acsrl = rvscs
				.getCodingSchemeVersionRefList();
		sort(acsrl);
		int uuid = valueSetDefinitionURI.toString().hashCode();

		for (AbsoluteCodingSchemeVersionReference ref : acsrl
				.getAbsoluteCodingSchemeVersionReference()) {
			if (ref.getCodingSchemeURN() != null) {
				uuid += ref.getCodingSchemeURN().hashCode();
			} else {
				uuid += "NULL".hashCode();
			}
			if (ref.getCodingSchemeVersion() != null) {
				uuid += ref.getCodingSchemeVersion().hashCode();
			} else {
				uuid += "NULL".hashCode();
			}
		}
		return uuid;
	}

	
	void sort(AbsoluteCodingSchemeVersionReferenceList acsrl) {
		Comparator<AbsoluteCodingSchemeVersionReference> cmp = new Comparator<AbsoluteCodingSchemeVersionReference>() {
			@Override
			public int compare(AbsoluteCodingSchemeVersionReference lhs,
					AbsoluteCodingSchemeVersionReference rhs) {
				if (lhs.getCodingSchemeURN() != null
						&& !lhs.getCodingSchemeURN().equals(
								rhs.getCodingSchemeURN())) {
					return lhs.getCodingSchemeURN().compareTo(
							rhs.getCodingSchemeURN());
				} else if (lhs.getCodingSchemeVersion() != null
						&& !lhs.getCodingSchemeVersion().equals(
								rhs.getCodingSchemeVersion())) {
					return lhs.getCodingSchemeVersion().compareTo(
							rhs.getCodingSchemeVersion());
				}

				return 0;
			}
		};
		Arrays.sort(acsrl.getAbsoluteCodingSchemeVersionReference(), cmp);
	}
	

	private LexEVSValueSetDefinitionServicesImpl getValueSetDefinitionService() {
		if (vds_ == null) {
			vds_ = new LexEVSValueSetDefinitionServicesImpl();
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

			TransformValueSetDefinitionToCodingScheme tvd2cs = new TransformValueSetDefinitionToCodingScheme(
					new URI("SRITEST:AUTO:EveryThing"), null, null, null);
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
