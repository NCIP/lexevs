
package org.LexGrid.LexBIG.Impl.helpers.lazyloading;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DocumentStoredFieldVisitor;
import org.apache.lucene.index.StoredFieldVisitor;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;

/**
 * The Class NonProxyLazyCodeToReturn.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NonProxyLazyCodeToReturn extends AbstractNonProxyLazyCodeToReturn {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8339301513416366127L;
    
    private String internalCodeSystemName;
    private String internalVersionString;

    /**
     * Instantiates a new non proxy lazy code to return.
     * 
     * @param scoreDoc the score doc
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     */
    public NonProxyLazyCodeToReturn(ScoreDoc scoreDoc, String internalCodeSystemName, String internalVersionString) {
        this(internalCodeSystemName, internalVersionString, scoreDoc.score, scoreDoc.doc);
    }

    /**
     * Instantiates a new non proxy lazy code to return.
     * 
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     * @param score the score
     * @param documentId the document id
     */
    public NonProxyLazyCodeToReturn(String internalCodeSystemName, String internalVersionString, float score,
            int documentId) {
        super(score, documentId);
        this.setVersion(internalVersionString);
        this.internalCodeSystemName = internalCodeSystemName;
        this.internalVersionString = internalVersionString;
    }

    @Override
    protected Document buildDocument() throws Exception {
        String uri = this.getSystemResourceService().getUriForUserCodingSchemeName(internalCodeSystemName, internalVersionString);
        //TODO start here for updates to the Set<String>
        return 
            this.getEntityIndexService().getDocumentById(uri, internalVersionString, this.getDocumentId(), this.doGetStringFields());
    }
    
    protected StoredFieldVisitor doGetFieldSelector() {

        return new DocumentStoredFieldVisitor(SQLTableConstants.TBLCOL_ENTITYCODE,
              LuceneLoaderCode.ENTITY_UID_FIELD,
              SQLTableConstants.TBLCOL_ENTITYDESCRIPTION,
              LuceneLoaderCode.CODING_SCHEME_ID_FIELD,
              LuceneLoaderCode.CODING_SCHEME_VERSION_FIELD,
              SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE,
              "entityType");
    }
    
    protected Set<String> doGetStringFields() {
        return new HashSet<String>(Arrays.asList(
                        SQLTableConstants.TBLCOL_ENTITYCODE,
                        LuceneLoaderCode.ENTITY_UID_FIELD,
                        SQLTableConstants.TBLCOL_ENTITYDESCRIPTION,
                        LuceneLoaderCode.CODING_SCHEME_ID_FIELD,
                        LuceneLoaderCode.CODING_SCHEME_VERSION_FIELD,
                        SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE,
                        "entityType"));
    }
}