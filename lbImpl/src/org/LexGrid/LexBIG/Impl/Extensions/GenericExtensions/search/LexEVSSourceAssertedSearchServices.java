
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.indexer.utility.CodingSchemeMetaData;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.util.CollectionUtils;

public class LexEVSSourceAssertedSearchServices {
    BuildMatchAlgorithmQuery queryBuilder;
    LexEvsServiceLocator locator;

    public LexEVSSourceAssertedSearchServices(BuildMatchAlgorithmQuery queryBuilder) {
        this.queryBuilder = queryBuilder;
        locator = LexEvsServiceLocator.getInstance();
    }

    public ResolvedConceptReferencesIterator getQueryResults(
            Set<AbsoluteCodingSchemeVersionReference> codeSystemToInclude) {
        List<ScoreDoc> scoreDocs = locator.getIndexServiceManager().
                getAssertedValueSetIndexService()
                .query(codeSystemToInclude, queryBuilder.getQuery());
        return new SourceAssertedValueSetScoreDocIteratorWrapper(
                codeSystemToInclude, scoreDocs);
    }

    public static Set<AbsoluteCodingSchemeVersionReference> resolveCodeSystemReferences(
            Set<CodingSchemeReference> references) throws LBParameterException {
        if (CollectionUtils.isEmpty(references)) {
            return null;
        }

        Set<AbsoluteCodingSchemeVersionReference> returnSet = 
                new HashSet<AbsoluteCodingSchemeVersionReference>();
        ConcurrentMetaData metadata = ConcurrentMetaData.getInstance();
        for (CodingSchemeReference ref : references) {
            CodingSchemeMetaData csm = metadata.getCodingSchemeMetaDataForNameAndVersion(
                    ref.getCodingScheme(),
                    ref.getVersionOrTag().getVersion());
            if (csm == null) {
                csm = metadata.getCodingSchemeMetaDataForUriAndVersion(ref.getCodingScheme(),
                        ref.getVersionOrTag().getVersion());
            }
            if (csm == null) {
                continue;
            }
            if ((ref.getCodingScheme().equals(csm.getCodingSchemeName())
                    || ref.getCodingScheme().equals(csm.getCodingSchemeUri())
                            && ref.getVersionOrTag().getVersion().
                            equals(csm.getCodingSchemeVersion()))) {
                returnSet.add(csm.getRef());
            }
        }
        return returnSet;
    }

}