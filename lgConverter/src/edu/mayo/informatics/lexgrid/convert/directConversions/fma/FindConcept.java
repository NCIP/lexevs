
package edu.mayo.informatics.lexgrid.convert.directConversions.fma;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;

/*
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * 
 * @version subversion $Revision: 2917 $ checked in on $Date: 2006-06-19
 *          15:52:21 +0000 (Mon, 19 Jun 2006) $
 */

public class FindConcept {

    KnowledgeBase kb;

    FindConcept(KnowledgeBase kb) {
        this.kb = kb;
    }

    boolean find(Cls parentObj, String concept) {
        if (parentObj instanceof Instance) {
            Collection classes = kb.getDirectSubclasses((Cls) parentObj);

            if ((classes != null) && (classes.size() > 0)) {

                Iterator itr = classes.iterator();
                while (itr.hasNext()) {
                    Object obj = itr.next();
                    if (obj instanceof Instance) {
                        Cls c = (Cls) obj;
                        if (!c.getName().contains(concept)) {
                            // System.out.println("Processing
                            // concept=" + c.getName());
                            if (find(c, concept)) {
                                System.out.println(parentObj.getName());
                                return true;
                            }
                        } else {
                            System.out.println("Found " + concept);
                            System.out.println(parentObj.getName());
                            return true;
                        }
                    }
                }
            }

        }

        return false;
    }

    public static void main(String[] args) {
        Collection error = new ArrayList();
        Project proj = Project.loadProjectFromFile(args[0], error);
        System.out.println("Project name= " + proj.getName());

        KnowledgeBase kb = proj.getKnowledgeBase();
        // Cls testCls = kb.getCls("Protein");
        FindConcept fc = new FindConcept(kb);
        // fc.find(testCls, "Type XX collagen");
        for (int i = 0; i < FMA2LGConstants.topClasses.length; i++) {
            System.out.println("Processing Branch -->" + FMA2LGConstants.topClasses[i]);
            Cls topCls = kb.getCls(FMA2LGConstants.topClasses[i]);
            fc.find(topCls, "fm_live");
        }

    }
}