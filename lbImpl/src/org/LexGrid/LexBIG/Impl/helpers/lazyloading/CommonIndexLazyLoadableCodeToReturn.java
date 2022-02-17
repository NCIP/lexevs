
package org.LexGrid.LexBIG.Impl.helpers.lazyloading;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;

/**
 * The Class LazyLoadableCodeToReturn.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CommonIndexLazyLoadableCodeToReturn extends AbstractNonProxyLazyCodeToReturn {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2847101693632620073L;
    
    private List<AbsoluteCodingSchemeVersionReference> references;
 
    /**
     * Instantiates a new lazy loadable code to return.
     */
    public CommonIndexLazyLoadableCodeToReturn(){
        super();
    }
    
    public CommonIndexLazyLoadableCodeToReturn(
            List<AbsoluteCodingSchemeVersionReference> references,
            ScoreDoc doc){
        this(references, doc.score, doc.doc);
    }
 
    /**
     * Instantiates a new lazy loadable code to return.
     * 
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     * @param score the score
     * @param documentId the document id
     */
    public CommonIndexLazyLoadableCodeToReturn(
            List<AbsoluteCodingSchemeVersionReference> references,
            float score,
            int documentId){
        super(score, documentId);
        this.references = references;
    }
    
    protected Document buildDocument() throws Exception {
        return getEntityIndexService().getDocumentFromCommonIndexById(this.references, this.getDocumentId());
    }
}