
package org.LexGrid.LexBIG.Impl.helpers;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

/**
 * This class is used when I want to create a conceptReference that I know
 * doesn't require any translating of the codingScheme.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class KnownConceptReference extends ConceptReference {
    private static final long serialVersionUID = 752926435292695777L;

    public KnownConceptReference(String codingScheme, String conceptCode, String namespace) {
        this.setCodingSchemeName(codingScheme);
        this.setCode(conceptCode);
        this.setCodeNamespace(namespace);
    }

}