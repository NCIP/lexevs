package org.lexevs.dao.indexer.utility;

import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;

public class CodingSchemeMetaData {
	
//	private LuceneMultiDirecoryFactory luceneDirectoryFactory;
	
	NamedDirectory directory;

	String indexDirectoryName;
	String codingSchemeUri;
	String codingSchemeVersion;
	String codingSchemeName;
	String uriVersion;
	
	private CodingSchemeMetaData() {
		// No one else should be calling this.
	}


	public CodingSchemeMetaData(
			String codingSchemeUri, String codingSchemeVersion, String codingSchemeName,
		    NamedDirectory directory) {
		super();
		this.codingSchemeUri = codingSchemeUri;
		this.codingSchemeVersion = codingSchemeVersion;
		this.codingSchemeName = codingSchemeName;
		this.uriVersion = codingSchemeUri + ":" + codingSchemeVersion;
		this.directory = directory;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((codingSchemeName == null) ? 0 : codingSchemeName.hashCode());
		result = prime * result
				+ ((codingSchemeUri == null) ? 0 : codingSchemeUri.hashCode());
		result = prime
				* result
				+ ((codingSchemeVersion == null) ? 0 : codingSchemeVersion
						.hashCode());
		result = prime
				* result
				+ ((indexDirectoryName == null) ? 0 : indexDirectoryName
						.hashCode());
		result = prime * result
				+ ((uriVersion == null) ? 0 : uriVersion.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CodingSchemeMetaData other = (CodingSchemeMetaData) obj;
		if (codingSchemeName == null) {
			if (other.codingSchemeName != null)
				return false;
		} else if (!codingSchemeName.equals(other.codingSchemeName))
			return false;
		if (codingSchemeUri == null) {
			if (other.codingSchemeUri != null)
				return false;
		} else if (!codingSchemeUri.equals(other.codingSchemeUri))
			return false;
		if (codingSchemeVersion == null) {
			if (other.codingSchemeVersion != null)
				return false;
		} else if (!codingSchemeVersion.equals(other.codingSchemeVersion))
			return false;
		if (indexDirectoryName == null) {
			if (other.indexDirectoryName != null)
				return false;
		} else if (!indexDirectoryName.equals(other.indexDirectoryName))
			return false;
		if (uriVersion == null) {
			if (other.uriVersion != null)
				return false;
		} else if (!uriVersion.equals(other.uriVersion))
			return false;
		return true;
	}


	

}
