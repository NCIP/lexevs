package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.annotations.LgClientSideSafe;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.locator.LexEvsServiceLocator;

@LgClientSideSafe
public class ScoreDocTransformer implements Serializable {

    private static final long serialVersionUID = 7176335324999288237L;

    public ResolvedConceptReferenceList transform(Iterable<ScoreDoc> items) {
        ResolvedConceptReferenceList list = new ResolvedConceptReferenceList();
        for(ScoreDoc item : items){
            list.addResolvedConceptReference(this.doTransform(item));
        }
        
        return list;
    }
    
    
    protected ResolvedConceptReference doTransform(ScoreDoc item) {
        Document doc = 
            LexEvsServiceLocator.getInstance().
                getIndexServiceManager().
                getSearchIndexService().
                getById(item.doc);
        
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