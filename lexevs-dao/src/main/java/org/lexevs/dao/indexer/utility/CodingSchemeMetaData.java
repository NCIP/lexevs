package org.lexevs.dao.indexer.utility;

import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;

public class CodingSchemeMetaData {
	
//	private LuceneMultiDirecoryFactory luceneDirectoryFactory;
	
	NamedDirectory directory;
	
	
	String indexDirectoryName;
	String codingSchemeUri;
	String codingSchemeVersion;
	boolean hasNormFields;
	boolean hasDoubleMetaphoneFields;
	boolean indexStarted;
	boolean indexComplete;
	
	private CodingSchemeMetaData() {
		// No one else should be calling this.
	}
	
	public CodingSchemeMetaData(
			String codingSchemeUri, String codingSchemeVersion,
			boolean hasNormFields, boolean hasDoubleMetaphoneFields,
			boolean indexStarted, boolean indexComplete, NamedDirectory directory) {
		super();
		this.codingSchemeUri = codingSchemeUri;
		this.codingSchemeVersion = codingSchemeVersion;
		this.hasNormFields = hasNormFields;
		this.hasDoubleMetaphoneFields = hasDoubleMetaphoneFields;
		this.indexStarted = indexStarted;
		this.indexComplete = indexComplete;
		this.directory = directory;
	}

	public class SchemeURIVersionPair{
		public SchemeURIVersionPair(){
			URIVersion = codingSchemeUri + ":" + codingSchemeVersion;
		}
		private String URIVersion;
		
		public String getURIVersion(){
			return URIVersion;
		}		
	}
	
	  @Override
	    public boolean equals(Object obj) {
	        if (obj == null) return false;
	        if (getClass() != obj.getClass()) return false;
	        final CodingSchemeMetaData other = (CodingSchemeMetaData) obj;
	        boolean sameindexDirectoryName = equals(this.indexDirectoryName, other.indexDirectoryName);
	        if (!sameindexDirectoryName) return false;
	        boolean samecodingSchemeUri = equals(this.codingSchemeUri, other.codingSchemeUri);
	        if (!samecodingSchemeUri) return false;
	        boolean samecodingSchemeVersion = equals(this.codingSchemeVersion, other.codingSchemeVersion);
	        if (!samecodingSchemeVersion) return false;
	        boolean samehasNormFields = (this.hasNormFields == other.hasNormFields);
	        if (!samehasNormFields) return false;
	        boolean samehasDoubleMetaphoneFields = (this.hasDoubleMetaphoneFields == other.hasDoubleMetaphoneFields); 
	        if (!samehasDoubleMetaphoneFields) return false;
	        boolean sameindexStarted = (this.indexStarted == other.indexStarted);
	        if (!sameindexStarted) return false;
	        boolean sameindexComplete = (this.indexComplete == other.indexComplete); 
	        if (!sameindexComplete) return false;
	        return true;
	    }

	    @Override
	    public int hashCode() {
	        int hash = 3;
	        hash = 89 * hash + (this.codingSchemeVersion == null ? 0 :this.codingSchemeVersion.toUpperCase().hashCode());
	        hash = 89 * hash + (this.codingSchemeUri == null ? 0 : this.codingSchemeUri.toUpperCase().hashCode());
	        return hash;
	    }
	    
	    
	    /**
	     * String equality check.
	     * @param a
	     * @param b
	     * @return
	     */
	    private boolean equals(String a, String b) {
			return (a == b) || (a != null && b != null && a.equalsIgnoreCase(b));
	    }

}
