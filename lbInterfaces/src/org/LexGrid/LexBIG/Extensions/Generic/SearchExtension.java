package org.LexGrid.LexBIG.Extensions.Generic;

import java.util.Set;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

public interface SearchExtension extends GenericExtension {
	
	public ResolvedConceptReferencesIterator search(String text) throws LBParameterException;
	
	public ResolvedConceptReferencesIterator search(
			String text, 
			Set<CodeSystemReference> codeSystems) throws LBParameterException;
	
	public ResolvedConceptReferencesIterator search(
			String text, 
			Set<CodeSystemReference> codeSystemsToInclude,
			Set<CodeSystemReference> codeSystemsToExclude) throws LBParameterException;
	
}
