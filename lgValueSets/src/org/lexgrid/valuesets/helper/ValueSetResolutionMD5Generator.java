
package org.lexgrid.valuesets.helper;

import java.io.StringWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

public class ValueSetResolutionMD5Generator {
	ValueSetDefinition vdd;
	HashMap<String, String> refVersions;
	String versionTag;
	HashMap<String, ValueSetDefinition> referencedVSDs;
	private LexEVSValueSetDefinitionServicesImpl vds_;

	public ValueSetResolutionMD5Generator(URI valueSetDefinitionURI,
			String valueSetDefinitionRevisionId,
			AbsoluteCodingSchemeVersionReferenceList csVersionList,
			String versionTag) throws LBException {
		vdd = this.getValueSetDefinitionService().getValueSetDefinition(
				valueSetDefinitionURI, valueSetDefinitionRevisionId);
		this.refVersions = this.getValueSetDefinitionService()
				.getServiceHelper().pruneVersionList(csVersionList);

		this.versionTag = versionTag;

	}

	public ValueSetResolutionMD5Generator(ValueSetDefinition vdd,
			HashMap<String, String> refVersions, String versionTag,
			HashMap<String, ValueSetDefinition> referencedVSDs) {
		this.vdd = vdd;
		this.refVersions = refVersions;
		this.versionTag = versionTag;
		this.referencedVSDs = referencedVSDs;

	}

	public String generateMD5() throws Exception {
		String plaintext = "";

		plaintext = convertValueSetDefinitionToString(vdd);
		if (refVersions != null && !refVersions.isEmpty()) {
			Map<String, String> sortedRefVersions = new TreeMap<String, String>(
					refVersions);
			sortedRefVersions.toString();
			plaintext += sortedRefVersions.toString();

		}
		if (StringUtils.isNotBlank(versionTag)) {
			plaintext += versionTag;
		}
		if (referencedVSDs != null && !referencedVSDs.isEmpty()) {
			Map<String, ValueSetDefinition> sortedReferencedVSDs = new TreeMap<String, ValueSetDefinition>(
					referencedVSDs);
			for (ValueSetDefinition def : sortedReferencedVSDs.values()) {
				plaintext += convertValueSetDefinitionToString(def);
			}
		}
		return DigestUtils.md5Hex(plaintext);
	}

	String convertValueSetDefinitionToString(ValueSetDefinition vsd)
			throws Exception {
		StringWriter writer = new StringWriter();
		if (vdd != null) {
			vdd.marshal(writer);
		}
		return writer.toString();
	}


	private LexEVSValueSetDefinitionServicesImpl getValueSetDefinitionService() {
		if (vds_ == null) {
			vds_ = new LexEVSValueSetDefinitionServicesImpl();
		}
		return vds_;
	}

	public static void main(String[] args) {
		try {
			ValueSetResolutionMD5Generator vsrg = new ValueSetResolutionMD5Generator(
					new URI(""), null, null, null);

			System.out.println(vsrg.generateMD5());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}