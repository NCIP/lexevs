
package org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback;

import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;

/**
 * The Class AbstractCycleDetectingCallback.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractCycleDetectingCallback implements CycleDetectingCallback {
 
    private static final long serialVersionUID = 969524253718668584L;

    /**
     * To associated concept key.
     * 
     * @param associationName the association name
     * @param associatedConcept the associated concept
     * 
     * @return the associated concept key
     */
    protected AssociatedConceptKey toAssociatedConceptKey(String associationName, AssociatedConcept associatedConcept) {
        return new AssociatedConceptKey(
                associatedConcept.getCode(), 
                associatedConcept.getCodeNamespace(), 
                associationName);  
    }
    
    /**
     * The Class AssociatedConceptKey.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    protected static class AssociatedConceptKey implements Serializable {
        
        private static final long serialVersionUID = 2861958458743850735L;

        /** The code. */
        private String code;
        
        /** The namespace. */
        private String namespace;
        
        /** The association name. */
        private String associationName;

        /**
         * Instantiates a new associated concept key.
         * 
         * @param code the code
         * @param namespace the namespace
         * @param associationName the association name
         */
        public AssociatedConceptKey(String code, String namespace, String associationName) {
            super();
            this.code = code;
            this.namespace = namespace;
            this.associationName = associationName;
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
        
        /**
         * Gets the association name.
         * 
         * @return the association name
         */
        public String getAssociationName() {
            return associationName;
        }
        
        /**
         * Sets the association name.
         * 
         * @param associationName the new association name
         */
        public void setAssociationName(String associationName) {
            this.associationName = associationName;
        }
        
        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((associationName == null) ? 0 : associationName.hashCode());
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
            AssociatedConceptKey other = (AssociatedConceptKey) obj;
            if (associationName == null) {
                if (other.associationName != null)
                    return false;
            } else if (!associationName.equals(other.associationName))
                return false;
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
}