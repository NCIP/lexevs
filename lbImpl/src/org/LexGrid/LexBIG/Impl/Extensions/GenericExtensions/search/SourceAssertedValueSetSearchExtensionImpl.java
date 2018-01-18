package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
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
        // TODO Auto-generated method stub
        return null;
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
