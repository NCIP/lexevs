package org.LexGrid.LexBIG.Extensions.Generic;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.AlgorithmMatch;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.ModelMatch;

public interface NodeGraphResolutionExtension extends GenericExtension {
	
	public enum AlgorithmMatch {
		EXACT_MATCH("exactMatch"), 
		CONTAINS("contains"),
		STARTS_WITH("startsWith"),		
		LUCENE("LuceneQuery");
		
		private String match;
		
		private AlgorithmMatch(String match){
			this.match = match;
		}
		

		
		public String getMatch(){
			return match;
		}
	}
	
	public enum ModelMatch {
		
		NAME,
		CODE,
		PROPERTY
	}
	
	public enum Direction {
	    TARGET_OF,
	    SOURCE_OF
	}
	
	public Iterator<ConceptReference> getConceptReferencesForTextSearchAndAssociationSourceOf(
			AbsoluteCodingSchemeVersionReference reference, 
			String associationName, String textMatch, 
			AlgorithmMatch alg, ModelMatch model, String url);

	public Iterator<ConceptReference> getConceptReferencesForTextSearchAndAssociationTargetOf(
			AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
			AlgorithmMatch alg, ModelMatch model, String url);
	
	public List<ConceptReference> getConceptReferenceListResolvedFromGraphForEntityCode(AbsoluteCodingSchemeVersionReference reference, String associationName, Direction direction, String entiyCode,
			String url);
	
	public List<ResolvedConceptReference> getCandidateConceptReferencesForTextAndAssociation(AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
			AlgorithmMatch alg, ModelMatch model, String url);

}
