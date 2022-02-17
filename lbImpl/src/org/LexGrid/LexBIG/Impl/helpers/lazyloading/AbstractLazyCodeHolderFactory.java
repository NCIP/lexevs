
package org.LexGrid.LexBIG.Impl.helpers.lazyloading;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.helpers.AdditiveCodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.CodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.CodeToReturn;
import org.LexGrid.LexBIG.Impl.helpers.DefaultCodeHolder;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.locator.LexEvsServiceLocator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A factory for creating LazyCodeHolder objects.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractLazyCodeHolderFactory implements CodeHolderFactory {
    
    private static final long serialVersionUID = 4365560788599358013L;

    @Override
    public CodeHolder buildCodeHolder(
            Set<? extends AbsoluteCodingSchemeVersionReference> references,
            Query query) throws LBInvocationException, LBParameterException {

        List<ScoreDoc> scoreDocs = LexEvsServiceLocator.getInstance().
            getIndexServiceManager().
                getEntityIndexService().queryCommonIndex(this.toRefs(references), query);
        
        AdditiveCodeHolder codeHolder = new DefaultCodeHolder();
        
        for(ScoreDoc doc : scoreDocs){
            codeHolder.add(this.buildCodeToReturn(doc, this.toRefs(references)));
        }

        return codeHolder;
    }

    private List<AbsoluteCodingSchemeVersionReference> toRefs(Set<? extends AbsoluteCodingSchemeVersionReference> refs) {
        List<AbsoluteCodingSchemeVersionReference> returnList = new ArrayList<AbsoluteCodingSchemeVersionReference>();

        for(AbsoluteCodingSchemeVersionReference ref : refs) {
            returnList.add(ref);
        }

        return returnList;
    }

    @Override
    public CodeHolder buildCodeHolder(CodeHolder additiveCodeHolder,
            Set<? extends AbsoluteCodingSchemeVersionReference> references,
            Query query) throws LBInvocationException, LBParameterException {

        List<ScoreDoc> scoreDocs = LexEvsServiceLocator.getInstance().
            getIndexServiceManager().
                getEntityIndexService().queryCommonIndex(this.toRefs(references), query);
        
        AdditiveCodeHolder codeHolder = new DefaultCodeHolder();
        
        for(ScoreDoc doc : scoreDocs){
            codeHolder.add(this.buildCodeToReturn(doc, this.toRefs(references)));
        }
        
        if(additiveCodeHolder != null){
         for(CodeToReturn ctr: additiveCodeHolder.getAllCodes()){
        	 codeHolder.add(ctr);
         }
        }

        return codeHolder;
    }
    /**
     * Builds the code to return.
     * 
     * @param doc the doc
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     * 
     * @return the code to return
     */
    protected abstract CodeToReturn buildCodeToReturn(ScoreDoc doc, String internalCodeSystemName, String internalVersionString);

    protected abstract CodeToReturn buildCodeToReturn(ScoreDoc doc, List<AbsoluteCodingSchemeVersionReference> references);
}