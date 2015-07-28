package org.lexevs.dao.indexer.utility;

public class CodingSchemeMetaData {

	private CodingSchemeMetaData() {
		// No one else should be calling this.
	}
	

	String indexDirectoryName;
	String codingSchemeUri;
	String codingSchemeVersion;
	boolean hasNormFields;
	boolean hasDoubleMetaphoneFields;
	boolean indexStarted;
	boolean indexComplete;
	SchemeURIVersionPair uriVersionPair;
	
	public CodingSchemeMetaData(String indexDirectoryName,
			String codingSchemeUri, String codingSchemeVersion,
			boolean hasNormFields, boolean hasDoubleMetaphoneFields,
			boolean indexStarted, boolean indexComplete,
			SchemeURIVersionPair uriVersionPair) {
		super();
		this.indexDirectoryName = indexDirectoryName;
		this.codingSchemeUri = codingSchemeUri;
		this.codingSchemeVersion = codingSchemeVersion;
		this.hasNormFields = hasNormFields;
		this.hasDoubleMetaphoneFields = hasDoubleMetaphoneFields;
		this.indexStarted = indexStarted;
		this.indexComplete = indexComplete;
		this.uriVersionPair = uriVersionPair;
	}

	public class SchemeURIVersionPair{
		public SchemeURIVersionPair(String directoryName){
			URIVersion = codingSchemeUri + ":" + codingSchemeVersion;
			directoryName = indexDirectoryName;
		}
		private String URIVersion;
		private String directoryName;
		
		public String getURIVersion(){
			return URIVersion;
		}
		
		public String getDirectoryName(){
			return directoryName;
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
	        boolean sameuriVersionPair = (this.uriVersionPair == other.uriVersionPair) ||
	        		(this.uriVersionPair != null && other.uriVersionPair != null &&
	        		(equals(this.uriVersionPair.getURIVersion(), other.uriVersionPair.getURIVersion()) &&
	        		 equals(this.uriVersionPair.getDirectoryName(), other.uriVersionPair.getDirectoryName())));
	        if (!sameuriVersionPair) return false;
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
