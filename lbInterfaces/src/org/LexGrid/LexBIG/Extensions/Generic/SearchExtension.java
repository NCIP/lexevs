package org.LexGrid.LexBIG.Extensions.Generic;

import java.io.Serializable;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

public interface SearchExtension extends GenericExtension {
	
	public ResolvedConceptReferencesIterator search(String text);
	
	public ResolvedConceptReferencesIterator search(
			String text, 
			Set<CodeSystemReference> codeSystems);
	
	public ResolvedConceptReferencesIterator search(
			String text, 
			Set<CodeSystemReference> codeSystemsToInclude,
			Set<CodeSystemReference> codeSystemsToExclude);
	
}
