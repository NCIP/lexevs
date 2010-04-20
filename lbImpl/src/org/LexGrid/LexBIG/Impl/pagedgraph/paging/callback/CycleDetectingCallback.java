package org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback;

import java.util.HashMap;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;

public class CycleDetectingCallback {
    
    private Map<AssociatedConceptKey,AssociatedConcept> associatedConceptMap = 
        new HashMap<AssociatedConceptKey,AssociatedConcept>();

    public AssociatedConcept onNextAssociatedConcept(String associationName, AssociatedConcept associatedConcept) {
        AssociatedConceptKey key = toAssociatedConceptKey(associationName, associatedConcept);
        if(! associatedConceptMap.containsKey(key)) {
            associatedConceptMap.put(key, associatedConcept);
        }
        
        return associatedConceptMap.get(key);
    }
    
    public boolean isAssociatedConceptAlreadyInGraph(String associationName, AssociatedConcept associatedConcept) {
        AssociatedConceptKey key = toAssociatedConceptKey(associationName, associatedConcept);
        
        return associatedConceptMap.containsKey(key);
    }
    
    private AssociatedConceptKey toAssociatedConceptKey(String associationName, AssociatedConcept associatedConcept) {
        return new AssociatedConceptKey(
                associatedConcept.getCode(), 
                associatedConcept.getCodeNamespace(), 
                associationName);  
    }
    
    private static class AssociatedConceptKey {
        private String code;
        private String namespace;
        private String associationName;

        public AssociatedConceptKey(String code, String namespace, String associationName) {
            super();
            this.code = code;
            this.namespace = namespace;
            this.associationName = associationName;
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
        public String getAssociationName() {
            return associationName;
        }
        public void setAssociationName(String associationName) {
            this.associationName = associationName;
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((associationName == null) ? 0 : associationName.hashCode());
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
