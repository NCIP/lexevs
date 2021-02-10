
package org.lexevs.dao.indexer.utility;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.system.model.LocalCodingScheme;

/**
 * @author <A HREF="mailto:bauer.scott@mayo.edu">Scott Bauer </A>
 *
 */
public class CodingSchemeMetaData {

	NamedDirectory directory;

	String indexDirectoryName;
	String codingSchemeUri;
	String codingSchemeVersion;
	String codingSchemeName;
	String nameVersionKey;
	AbsoluteCodingSchemeVersionReference ref;

	/**
	 * hasIndex:  does the Coding Scheme have an index
	 */
	private boolean hasIndex = false;

	public CodingSchemeMetaData(String codingSchemeUri,
			String codingSchemeVersion, String codingSchemeName,
			NamedDirectory directory) {
		super();
		this.codingSchemeUri = codingSchemeUri;
		this.codingSchemeVersion = codingSchemeVersion;
		this.codingSchemeName = codingSchemeName;
		this.nameVersionKey = LocalCodingScheme.getLocalCodingScheme(
				codingSchemeName, codingSchemeVersion).getKey();
		this.directory = directory;
		if (directory != null) {
			hasIndex = true;
		}
		this.ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(codingSchemeUri);
		ref.setCodingSchemeVersion(codingSchemeVersion);
	}

	public NamedDirectory getDirectory() {
		return directory;
	}

	public void setDirectory(NamedDirectory directory) {
		this.directory = directory;
	}

	public String getIndexDirectoryName() {
		return indexDirectoryName;
	}

	public void setIndexDirectoryName(String indexDirectoryName) {
		this.indexDirectoryName = indexDirectoryName;
	}

	public String getCodingSchemeUri() {
		return codingSchemeUri;
	}

	public void setCodingSchemeUri(String codingSchemeUri) {
		this.codingSchemeUri = codingSchemeUri;
	}

	public String getCodingSchemeVersion() {
		return codingSchemeVersion;
	}

	public void setCodingSchemeVersion(String codingSchemeVersion) {
		this.codingSchemeVersion = codingSchemeVersion;
	}

	public String getCodingSchemeName() {
		return codingSchemeName;
	}

	public void setCodingSchemeName(String codingSchemeName) {
		this.codingSchemeName = codingSchemeName;
	}

	public String getNameVersionKey() {
		return nameVersionKey;
	}

	public void setNameVersionKey(String nameVersion) {
		this.nameVersionKey = nameVersion;
	}

	public boolean isHasIndex() {
		return hasIndex;
	}

	/**
	 * @return the ref
	 */
	public AbsoluteCodingSchemeVersionReference getRef() {
		return ref;
	}

	/**
	 * @param ref
	 *            the ref to set
	 */
	public void setRef(AbsoluteCodingSchemeVersionReference ref) {
		this.ref = ref;
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
				+ ((nameVersionKey == null) ? 0 : nameVersionKey.hashCode());
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
		if (nameVersionKey == null) {
			if (other.nameVersionKey != null)
				return false;
		} else if (!nameVersionKey.equals(other.nameVersionKey))
			return false;
		return true;
	}

}