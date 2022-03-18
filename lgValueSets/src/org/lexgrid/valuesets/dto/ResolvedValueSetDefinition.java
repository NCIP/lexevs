
package org.lexgrid.valuesets.dto;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.annotations.LgClientSideSafe;

/**
 * A resolved Value Domain definition containing the coding scheme version reference list
 * that was used to resolve the value domain and an iterator for resolved concepts.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
@LgClientSideSafe
public class ResolvedValueSetDefinition extends VSDSummary implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/
	
	private AbsoluteCodingSchemeVersionReferenceList codingSchemeVersionRefList;
	
	private ResolvedConceptReferencesIterator resolvedConceptReferenceIterator;

	public AbsoluteCodingSchemeVersionReferenceList getCodingSchemeVersionRefList() {
		return codingSchemeVersionRefList;
	}

	public void setCodingSchemeVersionRefList(
			AbsoluteCodingSchemeVersionReferenceList codingSchemeVersionRefList) {
		this.codingSchemeVersionRefList = codingSchemeVersionRefList;
	}

	public ResolvedConceptReferencesIterator getResolvedConceptReferenceIterator() {
		return resolvedConceptReferenceIterator;
	}

	public void setResolvedConceptReferenceIterator(
			ResolvedConceptReferencesIterator resolvedConceptReferenceIterator) {
		this.resolvedConceptReferenceIterator = resolvedConceptReferenceIterator;
	}    

}