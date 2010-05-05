package org.lexevs.dao.database.service.codednodegraph.model;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

public class GraphQuery implements Cloneable {

    private List<ConceptReference> restrictToSourceCodes = new ArrayList<ConceptReference>();
    private List<ConceptReference> restrictToTargetCodes = new ArrayList<ConceptReference>();
    private List<String> restrictToAssociations = new ArrayList<String>();
    private List<QualifierNameValuePair> restrictToAssociationsQualifiers = new ArrayList<QualifierNameValuePair>();
    private List<String> restrictToCodeSystem = new ArrayList<String>();
    private List<String> restrictToSourceCodeSystem = new ArrayList<String>();
    private List<String> restrictToTargetCodeSystem = new ArrayList<String>();
 
    public List<ConceptReference> getRestrictToSourceCodes() {
        return restrictToSourceCodes;
    }

    public void setRestrictToSourceCodes(List<ConceptReference> restrictToSourceCodes) {
        this.restrictToSourceCodes = restrictToSourceCodes;
    }

    public List<ConceptReference> getRestrictToTargetCodes() {
        return restrictToTargetCodes;
    }

    public void setRestrictToTargetCodes(List<ConceptReference> restrictToTargetCodes) {
        this.restrictToTargetCodes = restrictToTargetCodes;
    }

    public List<String> getRestrictToAssociations() {
        return restrictToAssociations;
    }

    public void setRestrictToAssociations(List<String> restrictToAssociations) {
        this.restrictToAssociations = restrictToAssociations;
    }

    public List<String> getRestrictToCodeSystem() {
        return restrictToCodeSystem;
    }

    public void setRestrictToCodeSystem(List<String> restrictToCodeSystem) {
        this.restrictToCodeSystem = restrictToCodeSystem;
    }

    public List<String> getRestrictToSourceCodeSystem() {
        return restrictToSourceCodeSystem;
    }

    public void setRestrictToSourceCodeSystem(List<String> restrictToSourceCodeSystem) {
        this.restrictToSourceCodeSystem = restrictToSourceCodeSystem;
    }

    public List<String> getRestrictToTargetCodeSystem() {
        return restrictToTargetCodeSystem;
    }

    public void setRestrictToTargetCodeSystem(List<String> restrictToTargetCodeSystem) {
        this.restrictToTargetCodeSystem = restrictToTargetCodeSystem;
    }
    
    public void setRestrictToAssociationsQualifiers(
			List<QualifierNameValuePair> restrictToAssociationsQualifiers) {
		this.restrictToAssociationsQualifiers = restrictToAssociationsQualifiers;
	}

	public List<QualifierNameValuePair> getRestrictToAssociationsQualifiers() {
		return restrictToAssociationsQualifiers;
	}

	public static class QualifierNameValuePair {
    	private String qualifierName;
    	private String qualifierValue;
    	
		public QualifierNameValuePair(String qualifierName,
				String qualifierValue) {
			super();
			this.qualifierName = qualifierName;
			this.qualifierValue = qualifierValue;
		}
		
		public String getQualifierName() {
			return qualifierName;
		}
		public void setQualifierName(String qualifierName) {
			this.qualifierName = qualifierName;
		}
		public String getQualifierValue() {
			return qualifierValue;
		}
		public void setQualifierValue(String qualifierValue) {
			this.qualifierValue = qualifierValue;
		}
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

    public static class CodeNamespacePair {
        private String code;
        private String namespace;
        
        public CodeNamespacePair(String code, String namespace) {
            super();
            this.code = code;
            this.namespace = namespace;
        }
        
        public String getCode() {
            return code;
        }
        
        public void setCode(String code) {
            this.code = code;
        }
        
        public String getNamespace() {
            return namespace;
        }
        
        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((code == null) ? 0 : code.hashCode());
            result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
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

	@Override
	public GraphQuery clone() throws CloneNotSupportedException {
		GraphQuery query = new GraphQuery();
		query.setRestrictToAssociations(new ArrayList<String>(this.restrictToAssociations));
		query.setRestrictToAssociationsQualifiers(new ArrayList<QualifierNameValuePair>(this.restrictToAssociationsQualifiers));
		query.setRestrictToCodeSystem(new ArrayList<String>(this.restrictToCodeSystem));
		query.setRestrictToSourceCodes(new ArrayList<ConceptReference>(this.restrictToSourceCodes));
		query.setRestrictToSourceCodeSystem(new ArrayList<String>(this.restrictToSourceCodeSystem));
		query.setRestrictToTargetCodes(new ArrayList<ConceptReference>(this.restrictToTargetCodes));
		query.setRestrictToTargetCodeSystem(new ArrayList<String>(this.restrictToTargetCodeSystem));
		
		return query;
	} 
}

