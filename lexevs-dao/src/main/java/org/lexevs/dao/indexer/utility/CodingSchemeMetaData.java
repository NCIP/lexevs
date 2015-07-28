package org.lexevs.dao.indexer.utility;

public class CodingSchemeMetaData {

	public CodingSchemeMetaData() {
		// TODO Auto-generated constructor stub
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
	        boolean sameindexDirectoryName = (this.indexDirectoryName == other.indexDirectoryName) || 
	        		(this.indexDirectoryName != null && this.indexDirectoryName.equalsIgnoreCase(other.indexDirectoryName));
	        if (!sameindexDirectoryName) return false;
	        boolean samecodingSchemeUri = (this.codingSchemeUri == other.codingSchemeUri) || 
	        		(this.codingSchemeUri != null && this.codingSchemeUri.equalsIgnoreCase(other.codingSchemeUri));
	        if (!samecodingSchemeUri) return false;
	        boolean samecodingSchemeVersion = (this.codingSchemeVersion == other.codingSchemeVersion) ||
	        		(this.codingSchemeVersion != null && this.codingSchemeVersion.equalsIgnoreCase(other.codingSchemeVersion));
	        if (!samecodingSchemeVersion) return false;
	        boolean samehasNormFields = (this.hasNormFields == other.hasNormFields);
	        if (!samehasNormFields) return false;
	        boolean sameA = (this.a == other.a) || (this.a != null && this.a.equalsIgnoreCase(other.a));
	        if (!sameA) return false;
	        boolean sameA = (this.a == other.a) || (this.a != null && this.a.equalsIgnoreCase(other.a));
	        if (!sameA) return false;
	        boolean sameA = (this.a == other.a) || (this.a != null && this.a.equalsIgnoreCase(other.a));
	        if (!sameA) return false;
	    	String ;
	    	String ;
	    	String ;
	    	boolean hasNormFields;
	    	boolean hasDoubleMetaphoneFields;
	    	boolean indexStarted;
	    	boolean indexComplete;
	    	SchemeURIVersionPair uriVersionPair;
	        return true;
	    }

	    @Override
	    public int hashCode() {
	        int hash = 3;
	        hash = 89 * hash + (this.a == null ? 0 :this.a.toUpperCase().hashCode());
	        hash = 89 * hash + (this.b == null ? 0 : this.b.toUpperCase().hashCode());
	        return hash;
	    }

}
