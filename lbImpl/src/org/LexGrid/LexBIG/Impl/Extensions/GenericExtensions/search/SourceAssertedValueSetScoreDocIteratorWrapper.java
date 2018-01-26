package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.apache.lucene.search.ScoreDoc;

public class SourceAssertedValueSetScoreDocIteratorWrapper extends SearchScoreDocIterator {

    /**
     * 
     */
    private static final long serialVersionUID = -8590261353875073492L;
    
    private SourceAssertedScoreDocTransformerExecutor saVSTransformerExecutor = new SourceAssertedScoreDocTransformerExecutor();
    protected SourceAssertedScoreDocTransformer sAVSTransformer;
    
    

    protected SourceAssertedValueSetScoreDocIteratorWrapper(Set<AbsoluteCodingSchemeVersionReference> codeSystemRefs,
            List<ScoreDoc> list) {
        super(codeSystemRefs, list);
      this.sAVSTransformer = new SourceAssertedScoreDocTransformer();
    }

    
    @Override
    public ResolvedConceptReferenceList next(int maxToReturn) throws LBResourceUnavailableException,
            LBInvocationException {
        ResolvedConceptReferenceList results = 
                this.saVSTransformerExecutor.transform(codeSystemsToInclude,
                       this.sAVSTransformer, 
                       new ArrayList<ScoreDoc>(this.list.subList(pos, this.adjustEndPos(pos + maxToReturn))));
         
         pos += results.getResolvedConceptReferenceCount();
         
         return results;
    }


    @Override
    public ResolvedConceptReferenceList get(int start, int end) throws LBResourceUnavailableException,
            LBInvocationException, LBParameterException {
        List<ScoreDoc> subList = this.list.subList(start, this.adjustEndPos(end));
        //TODO Adapt to multiple code systems.
        return this.saVSTransformerExecutor.transform(this.codeSystemsToInclude, this.sAVSTransformer, subList);
    }

}
