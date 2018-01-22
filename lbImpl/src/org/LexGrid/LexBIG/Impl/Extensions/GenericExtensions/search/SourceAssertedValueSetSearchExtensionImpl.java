package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Generic.SourceAssertedValueSetSearchExtension;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;

public class SourceAssertedValueSetSearchExtensionImpl extends AbstractExtendable implements SourceAssertedValueSetSearchExtension {

    /**
     * 
     */
    private static final long serialVersionUID = 6623199682766189795L;

    @Override
    public ResolvedConceptReferencesIterator search(String text, Set<CodingSchemeReference> codingSchemesToInclude,
            Set<CodingSchemeReference> codingSchemesToExclude, MatchAlgorithm matchAlgorithm)
            throws LBParameterException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResolvedConceptReferencesIterator search(String text, AbsoluteCodingSchemeVersionReference codingScheme,
            MatchAlgorithm matchAlgorithm) throws LBParameterException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResolvedConceptReferencesIterator search(String text, MatchAlgorithm matchAlgorithm)
            throws LBParameterException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResolvedConceptReferencesIterator search(String text, Set<CodingSchemeReference> resolvedValueSet,
            MatchAlgorithm matchAlgorithm) throws LBParameterException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResolvedConceptReferencesIterator search(String text,
            Set<CodingSchemeReference> sourceAssertedValueSetSchemeReferences,
            Set<CodingSchemeReference> resolvedValueSets, MatchAlgorithm matchAlgorithm, boolean includeAnonymous)
            throws LBParameterException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResolvedConceptReferencesIterator search(String text,
            Set<CodingSchemeReference> sourceAssertedValueSetSchemeReferences,
            Set<CodingSchemeReference> resolvedValueSets, MatchAlgorithm matchAlgorithm, boolean includeAnonymous,
            boolean includeInactive) throws LBParameterException {
        
        BuildMatchAlgorithmQuery.Builder builder = new BuildMatchAlgorithmQuery.Builder(text, includeAnonymous, includeInactive);
        switch(matchAlgorithm){
        
        case CODE_EXACT:
        builder.codeExact();
        return new LexEVSSourceAssertedSearchServices(builder.buildMatchQuery()).
                getQueryResults(LexEVSSourceAssertedSearchServices.
                        resolveCodeSystemReferences(resolvedValueSets));
        
        case PRESENTATION_EXACT:
            builder.presentationExact();
            return new LexEVSSourceAssertedSearchServices(builder.buildMatchQuery()).
                    getQueryResults(LexEVSSourceAssertedSearchServices.
                            resolveCodeSystemReferences(resolvedValueSets));   

        case PRESENTATION_CONTAINS:
            builder.presentationContains();
            return new LexEVSSourceAssertedSearchServices(builder.buildMatchQuery()).
                    getQueryResults(LexEVSSourceAssertedSearchServices.
                            resolveCodeSystemReferences(resolvedValueSets));
            
        case PROPERTY_EXACT:
            builder.propertyExact();
            return new LexEVSSourceAssertedSearchServices(builder.buildMatchQuery()).
                    getQueryResults(LexEVSSourceAssertedSearchServices.
                            resolveCodeSystemReferences(resolvedValueSets));
            
        case PROPERTY_CONTAINS:
            builder.propertyContains();
            return new LexEVSSourceAssertedSearchServices(builder.buildMatchQuery()).
                    getQueryResults(LexEVSSourceAssertedSearchServices.
                            resolveCodeSystemReferences(resolvedValueSets));
          
        case LUCENE:
            builder.lucene();
            return new LexEVSSourceAssertedSearchServices(builder.buildMatchQuery()).
                    getQueryResults(LexEVSSourceAssertedSearchServices.
                            resolveCodeSystemReferences(resolvedValueSets));
        default:
            builder.matchAllDocs();
            return new LexEVSSourceAssertedSearchServices(builder.buildMatchQuery()).
                    getQueryResults(LexEVSSourceAssertedSearchServices.
                            resolveCodeSystemReferences(resolvedValueSets));
        }
    }

    @Override
    protected void doRegister(ExtensionRegistry registry, ExtensionDescription description)
            throws LBParameterException {
        registry.registerGenericExtension(description);
    }

    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setDescription("Asserted Value Set Search Extension for LexEVS.");
        ed.setExtensionBaseClass(GenericExtension.class.getName());
        ed.setExtensionClass(this.getClass().getName());
        ed.setName("AssertedValueSetSearchExtension");
        ed.setVersion("1.0");

        return ed;
    }
    
    public static void main(String ...args) {
        LexBIGService svc = LexBIGServiceImpl.defaultInstance();
        try {
            SourceAssertedValueSetSearchExtensionImpl ext = (SourceAssertedValueSetSearchExtensionImpl)svc.getGenericExtension("AssertedValueSetSearchExtension");

            ResolvedConceptReferencesIterator itr = ext.search("C48323", null, null, MatchAlgorithm.CODE_EXACT, false, false);
            itr.hasNext();
            ResolvedConceptReference ref = itr.next();
           System.out.println(ref.getEntityDescription().getContent());
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
