
package edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon;

import java.util.ArrayList;
import java.util.List;

/**
 * Holder for association loaded from Text files.
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala </A>
 * @version subversion $Revision: 5296 $ checked in on $Date: 2007-05-16
 *          21:55:43 +0000 (Wed, 16 May 2007) $
 */
public class Association {
    private String relationName;
//    private String sourceCodingScheme;
    private Concept source;
//    private String targetCodingScheme;
    private List<Concept> targetSet;

    public Association() {
        targetSet = new ArrayList<Concept>();
    }

    public String toString() {
        String str = "[" + source.toString() + "] " + relationName + " ";
        for (Concept c : targetSet){
            str = str + "[" + c.toString() + "], ";
        }
        return str + "\n\n";
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public Concept getSourceConcept() {
        return source;
    }

    public void setSourceConcept(Concept source) {
        this.source = source;
    }

    public List<Concept> getTargetConceptSet() {
        return targetSet;
    }

    public void setTargetConceptSet(List<Concept> targetSet) {
        this.targetSet = targetSet;
    }
    
    public boolean addTargetConcept(Concept targetConcept) {
        if (targetSet.contains(targetConcept) == true)
            return false;
        targetSet.add(targetConcept); 
        return true;
    }

    public boolean equals(Object o) {
        if (o instanceof Association) {
            if ((equals(((Association)o).getRelationName(),this.relationName) && 
                    equals(((Association)o).getSourceConcept(),this.source) &&
                    ((Association)o).getTargetConceptSet().size() == targetSet.size())== false
                  )
                return false;
            else {
                return targetSet.containsAll(((Association)o).getTargetConceptSet());
            }
        }
        return false;
    }
    
    private boolean equals(Object src, Object dest){
        if (src == null && dest == null)
            return true;
        else if (src == null && dest != null)
            return false;
        else if (src != null && dest == null)
            return false;
        else
            return src.equals(dest);
    }
    
}