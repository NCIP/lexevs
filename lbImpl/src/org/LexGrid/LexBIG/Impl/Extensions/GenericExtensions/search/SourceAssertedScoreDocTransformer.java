package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.helpers.ResolvedConceptReferenceTransformer;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.lexevs.locator.LexEvsServiceLocator;

public class SourceAssertedScoreDocTransformer implements ResolvedConceptReferenceTransformer {
    
    /**
     * 
     */
    private static final long serialVersionUID = -3758150388888800474L;

    public ResolvedConceptReference doTransform(Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude,
            ProxyProtectedScoreDocWrapper item) {
        Document doc = 
                LexEvsServiceLocator.getInstance().
                    getIndexServiceManager().getAssertedValueSetIndexService().
                    getById(item.getScoreDoc().doc);
            
            String code = doc.get("entityCode");
            String namespace = doc.get("entityCodeNamespace");
            String[] types = doc.getValues("type");
            String description = doc.get("entityDescription");
            String codingSchemeUri = doc.get("codingSchemeUri");
            String codingSchemeName = doc.get("codingSchemeName");
            String codingSchemeVersion = doc.get("codingSchemeVersion");
            
            ResolvedConceptReference ref = new ResolvedConceptReference();
            ref.setCode(code);
            ref.setCodeNamespace(namespace);
            ref.setEntityType(types);
            ref.setCodingSchemeName(codingSchemeName);
            ref.setCodingSchemeURI(codingSchemeUri);
            ref.setCodingSchemeVersion(codingSchemeVersion);
            if(StringUtils.isNotBlank(description)){
                ref.setEntityDescription(Constructors.createEntityDescription(description));
            }
            
            return ref;
        
    }
}
