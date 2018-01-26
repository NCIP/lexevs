package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.util.HashSet;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Generic.SourceAssertedValueSetSearchExtension;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

public class SourceAssertedValueSetSearchExtensionImpl extends AbstractExtendable implements SourceAssertedValueSetSearchExtension {

    /**
     * 
     */
    private static final long serialVersionUID = 6623199682766189795L;

    @Override
    public ResolvedConceptReferencesIterator search(String text, Set<CodingSchemeReference> assertedSources,
            Set<CodingSchemeReference> addedValueSets, MatchAlgorithm matchAlgorithm)
            throws LBParameterException {
        return search(text, assertedSources, addedValueSets, matchAlgorithm, false, false);
    }

    @Override
    public ResolvedConceptReferencesIterator search(String text, CodingSchemeReference codingScheme,
            MatchAlgorithm matchAlgorithm) throws LBParameterException {
        Set<CodingSchemeReference> ref =  new HashSet<CodingSchemeReference>();
        ref.add(codingScheme);
        return search(text, ref, null, matchAlgorithm, false, false);
    }

    @Override
    public ResolvedConceptReferencesIterator search(String text, MatchAlgorithm matchAlgorithm)
            throws LBParameterException {
        return search(text,null, null, matchAlgorithm, false, false);
    }

    @Override
    public ResolvedConceptReferencesIterator search(String text, Set<CodingSchemeReference> resolvedValueSet,
            MatchAlgorithm matchAlgorithm) throws LBParameterException {
        return search(text,null, resolvedValueSet,  matchAlgorithm, false, false);
    }

    @Override
    public ResolvedConceptReferencesIterator search(String text,
            Set<CodingSchemeReference> sourceAssertedValueSetSchemeReferences,
            Set<CodingSchemeReference> resolvedValueSets, MatchAlgorithm matchAlgorithm, boolean includeAnonymous)
            throws LBParameterException {
        return search(text,sourceAssertedValueSetSchemeReferences, 
                resolvedValueSets, matchAlgorithm, includeAnonymous, false);
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
}
