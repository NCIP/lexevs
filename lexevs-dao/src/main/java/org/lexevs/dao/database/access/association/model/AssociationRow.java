package org.lexevs.dao.database.access.association.model;
public class AssociationRow {
		private String associationPredicateGuid;
		private String sourceEntityCode;
		private String sourceEntityNamespace;
		private String targetEntityCode;
		private String targetEntityNamespace;
		
		public AssociationRow(String predicateGuid, 
				String sourceCode,
				String sourceNamespace,
				String targetCode,
				String targetNamespace){
			this.associationPredicateGuid = predicateGuid;
			this.sourceEntityCode = sourceCode;
			this.sourceEntityNamespace = sourceNamespace;
			this.targetEntityCode = targetCode;
			this.targetEntityNamespace = targetNamespace;
			
		}
		/**
		 * @return the associationPredicateGuid
		 */
		public String getAssociationPredicateGuid() {
			return associationPredicateGuid;
		}
		/**
		 * @param associationPredicateGuid the associationPredicateGuid to set
		 */
		public void setAssociationPredicateGuid(String associationPredicateGuid) {
			this.associationPredicateGuid = associationPredicateGuid;
		}
		/**
		 * @return the sourceEntityCode
		 */
		public String getSourceEntityCode() {
			return sourceEntityCode;
		}
		/**
		 * @param sourceEntityCode the sourceEntityCode to set
		 */
		public void setSourceEntityCode(String sourceEntityCode) {
			this.sourceEntityCode = sourceEntityCode;
		}
		/**
		 * @return the sourceEntityNamespace
		 */
		public String getSourceEntityNamespace() {
			return sourceEntityNamespace;
		}
		/**
		 * @param sourceEntityNamespace the sourceEntityNamespace to set
		 */
		public void setSourceEntityNamespace(String sourceEntityNamespace) {
			this.sourceEntityNamespace = sourceEntityNamespace;
		}
		/**
		 * @return the targetEntityCode
		 */
		public String getTargetEntityCode() {
			return targetEntityCode;
		}
		/**
		 * @param targetEntityCode the targetEntityCode to set
		 */
		public void setTargetEntityCode(String targetEntityCode) {
			this.targetEntityCode = targetEntityCode;
		}
		/**
		 * @return the targetEntityNamespace
		 */
		public String getTargetEntityNamespace() {
			return targetEntityNamespace;
		}
		/**
		 * @param targetEntityNamespace the targetEntityNamespace to set
		 */
		public void setTargetEntityNamespace(String targetEntityNamespace) {
			this.targetEntityNamespace = targetEntityNamespace;
		}
		
		public String toString(){
			return "PredicateGuid: " + associationPredicateGuid 
					+ " sourceCode: " + sourceEntityCode 
					+ " sourceNamespace: " + sourceEntityNamespace 
					+ " targetCode: " + targetEntityCode 
					+ " targetNamespace: " + targetEntityNamespace;
			
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((associationPredicateGuid == null) ? 0 : associationPredicateGuid.hashCode());
			result = prime * result + ((sourceEntityCode == null) ? 0 : sourceEntityCode.hashCode());
			result = prime * result + ((sourceEntityNamespace == null) ? 0 : sourceEntityNamespace.hashCode());
			result = prime * result + ((targetEntityCode == null) ? 0 : targetEntityCode.hashCode());
			result = prime * result + ((targetEntityNamespace == null) ? 0 : targetEntityNamespace.hashCode());
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
			AssociationRow other = (AssociationRow) obj;
			if (associationPredicateGuid == null) {
				if (other.associationPredicateGuid != null)
					return false;
			} else if (!associationPredicateGuid.equals(other.associationPredicateGuid))
				return false;
			if (sourceEntityCode == null) {
				if (other.sourceEntityCode != null)
					return false;
			} else if (!sourceEntityCode.equals(other.sourceEntityCode))
				return false;
			if (sourceEntityNamespace == null) {
				if (other.sourceEntityNamespace != null)
					return false;
			} else if (!sourceEntityNamespace.equals(other.sourceEntityNamespace))
				return false;
			if (targetEntityCode == null) {
				if (other.targetEntityCode != null)
					return false;
			} else if (!targetEntityCode.equals(other.targetEntityCode))
				return false;
			if (targetEntityNamespace == null) {
				if (other.targetEntityNamespace != null)
					return false;
			} else if (!targetEntityNamespace.equals(other.targetEntityNamespace))
				return false;
			return true;
		}

	}