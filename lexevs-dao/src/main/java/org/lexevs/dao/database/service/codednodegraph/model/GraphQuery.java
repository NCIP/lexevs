
package org.lexevs.dao.database.service.codednodegraph.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

/**
 * The Class GraphQuery.
 */
public class GraphQuery implements Cloneable, Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7682901213668779899L;
	
	/** The restrict to source codes. */
	private List<ConceptReference> restrictToSourceCodes = new ArrayList<ConceptReference>();
    
    /** The restrict to target codes. */
    private List<ConceptReference> restrictToTargetCodes = new ArrayList<ConceptReference>();
    
    /** The restrict to associations. */
    private List<String> restrictToAssociations = new ArrayList<String>();
    
    /** The restrict to associations qualifiers. */
    private List<QualifierNameValuePair> restrictToAssociationsQualifiers = new ArrayList<QualifierNameValuePair>();
    
    /** The restrict to source code system. */
    private List<String> restrictToSourceCodeSystem = new ArrayList<String>();
    
    /** The restrict to target code system. */
    private List<String> restrictToTargetCodeSystem = new ArrayList<String>();
    
    /** The restrict to entity types. */
    private List<String> restrictToEntityTypes = new ArrayList<String>();
    
    /** The restrict to anonymous. */
    private Boolean restrictToAnonymous;

    /**
     * Gets the restrict to entity types.
     * 
     * @return the restrict to entity types
     */
    public List<String> getRestrictToEntityTypes() {
		return restrictToEntityTypes;
	}

	/**
	 * Sets the restrict to entity types.
	 * 
	 * @param restrictToEntityTypes the new restrict to entity types
	 */
	public void setRestrictToEntityTypes(List<String> restrictToEntityTypes) {
		this.restrictToEntityTypes = restrictToEntityTypes;
	}

	/**
	 * Checks if is restrict to anonymous.
	 * 
	 * @return the boolean
	 */
	public Boolean isRestrictToAnonymous() {
		return restrictToAnonymous;
	}

	/**
	 * Sets the restrict to anonymous.
	 * 
	 * @param restrictToAnonymous the new restrict to anonymous
	 */
	public void setRestrictToAnonymous(Boolean restrictToAnonymous) {
		this.restrictToAnonymous = restrictToAnonymous;
	}

	/**
	 * Gets the restrict to source codes.
	 * 
	 * @return the restrict to source codes
	 */
	public List<ConceptReference> getRestrictToSourceCodes() {
        return restrictToSourceCodes;
    }

    /**
     * Sets the restrict to source codes.
     * 
     * @param restrictToSourceCodes the new restrict to source codes
     */
    public void setRestrictToSourceCodes(List<ConceptReference> restrictToSourceCodes) {
        this.restrictToSourceCodes = restrictToSourceCodes;
    }

    /**
     * Gets the restrict to target codes.
     * 
     * @return the restrict to target codes
     */
    public List<ConceptReference> getRestrictToTargetCodes() {
        return restrictToTargetCodes;
    }

    /**
     * Sets the restrict to target codes.
     * 
     * @param restrictToTargetCodes the new restrict to target codes
     */
    public void setRestrictToTargetCodes(List<ConceptReference> restrictToTargetCodes) {
        this.restrictToTargetCodes = restrictToTargetCodes;
    }

    /**
     * Gets the restrict to associations.
     * 
     * @return the restrict to associations
     */
    public List<String> getRestrictToAssociations() {
        return restrictToAssociations;
    }

    /**
     * Sets the restrict to associations.
     * 
     * @param restrictToAssociations the new restrict to associations
     */
    public void setRestrictToAssociations(List<String> restrictToAssociations) {
        this.restrictToAssociations = restrictToAssociations;
    }

    /**
     * Gets the restrict to source code system.
     * 
     * @return the restrict to source code system
     */
    public List<String> getRestrictToSourceCodeSystem() {
        return restrictToSourceCodeSystem;
    }

    /**
     * Sets the restrict to source code system.
     * 
     * @param restrictToSourceCodeSystem the new restrict to source code system
     */
    public void setRestrictToSourceCodeSystem(List<String> restrictToSourceCodeSystem) {
        this.restrictToSourceCodeSystem = restrictToSourceCodeSystem;
    }

    /**
     * Gets the restrict to target code system.
     * 
     * @return the restrict to target code system
     */
    public List<String> getRestrictToTargetCodeSystem() {
        return restrictToTargetCodeSystem;
    }

    /**
     * Sets the restrict to target code system.
     * 
     * @param restrictToTargetCodeSystem the new restrict to target code system
     */
    public void setRestrictToTargetCodeSystem(List<String> restrictToTargetCodeSystem) {
        this.restrictToTargetCodeSystem = restrictToTargetCodeSystem;
    }
    
    /**
     * Sets the restrict to associations qualifiers.
     * 
     * @param restrictToAssociationsQualifiers the new restrict to associations qualifiers
     */
    public void setRestrictToAssociationsQualifiers(
			List<QualifierNameValuePair> restrictToAssociationsQualifiers) {
		this.restrictToAssociationsQualifiers = restrictToAssociationsQualifiers;
	}

	/**
	 * Gets the restrict to associations qualifiers.
	 * 
	 * @return the restrict to associations qualifiers
	 */
	public List<QualifierNameValuePair> getRestrictToAssociationsQualifiers() {
		return restrictToAssociationsQualifiers;
	}

	/**
	 * The Class QualifierNameValuePair.
	 */
	public static class QualifierNameValuePair implements Serializable{
  
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -3222676374299687322L;
		
		/** The qualifier name. */
		private String qualifierName;
    	
	    /** The qualifier value. */
	    private String qualifierValue;
    	
		/**
		 * Instantiates a new qualifier name value pair.
		 * 
		 * @param qualifierName the qualifier name
		 * @param qualifierValue the qualifier value
		 */
		public QualifierNameValuePair(String qualifierName,
				String qualifierValue) {
			super();
			this.qualifierName = qualifierName;
			this.qualifierValue = qualifierValue;
		}
		
		/**
		 * Gets the qualifier name.
		 * 
		 * @return the qualifier name
		 */
		public String getQualifierName() {
			return qualifierName;
		}
		
		/**
		 * Sets the qualifier name.
		 * 
		 * @param qualifierName the new qualifier name
		 */
		public void setQualifierName(String qualifierName) {
			this.qualifierName = qualifierName;
		}
		
		/**
		 * Gets the qualifier value.
		 * 
		 * @return the qualifier value
		 */
		public String getQualifierValue() {
			return qualifierValue;
		}
		
		/**
		 * Sets the qualifier value.
		 * 
		 * @param qualifierValue the new qualifier value
		 */
		public void setQualifierValue(String qualifierValue) {
			this.qualifierValue = qualifierValue;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((qualifierName == null) ? 0 : qualifierName.hashCode());
			result = prime
					* result
					+ ((qualifierValue == null) ? 0 : qualifierValue.hashCode());
			return result;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			QualifierNameValuePair other = (QualifierNameValuePair) obj;
			if (qualifierName == null) {
				if (other.qualifierName != null)
					return false;
			} else if (!qualifierName.equals(other.qualifierName))
				return false;
			if (qualifierValue == null) {
				if (other.qualifierValue != null)
					return false;
			} else if (!qualifierValue.equals(other.qualifierValue))
				return false;
			return true;
		}
    }

    /**
     * The Class CodeNamespacePair.
     */
    public static class CodeNamespacePair implements Serializable{
    
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -239301238480326835L;
		
		/** The code. */
		private String code;
        
        /** The namespace. */
        private String namespace;
        
        /**
         * Instantiates a new code namespace pair.
         * 
         * @param code the code
         * @param namespace the namespace
         */
        public CodeNamespacePair(String code, String namespace) {
            super();
            this.code = code;
            this.namespace = namespace;
        }
        
        /**
         * Gets the code.
         * 
         * @return the code
         */
        public String getCode() {
            return code;
        }
        
        /**
         * Sets the code.
         * 
         * @param code the new code
         */
        public void setCode(String code) {
            this.code = code;
        }
        
        /**
         * Gets the namespace.
         * 
         * @return the namespace
         */
        public String getNamespace() {
            return namespace;
        }
        
        /**
         * Sets the namespace.
         * 
         * @param namespace the new namespace
         */
        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((code == null) ? 0 : code.hashCode());
            result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
            return result;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CodeNamespacePair other = (CodeNamespacePair) obj;
            if (code == null) {
                if (other.code != null)
                    return false;
            } else if (!code.equals(other.code))
                return false;
            if (namespace == null) {
                if (other.namespace != null)
                    return false;
            } else if (!namespace.equals(other.namespace))
                return false;
            return true;
        }   
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public GraphQuery clone() throws CloneNotSupportedException {
		super.clone();
		
		GraphQuery query = new GraphQuery();
		query.setRestrictToAssociations(new ArrayList<String>(this.restrictToAssociations));
		query.setRestrictToAssociationsQualifiers(new ArrayList<QualifierNameValuePair>(this.restrictToAssociationsQualifiers));
		query.setRestrictToSourceCodes(new ArrayList<ConceptReference>(this.restrictToSourceCodes));
		query.setRestrictToSourceCodeSystem(new ArrayList<String>(this.restrictToSourceCodeSystem));
		query.setRestrictToTargetCodes(new ArrayList<ConceptReference>(this.restrictToTargetCodes));
		query.setRestrictToTargetCodeSystem(new ArrayList<String>(this.restrictToTargetCodeSystem));
		query.setRestrictToEntityTypes(new ArrayList<String>(this.restrictToEntityTypes));
		query.setRestrictToAnonymous(this.restrictToAnonymous);
		
		return query;
	} 
}