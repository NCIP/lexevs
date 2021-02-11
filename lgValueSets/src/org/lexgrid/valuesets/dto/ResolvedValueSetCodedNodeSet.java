
package org.lexgrid.valuesets.dto;

import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;

/**
 * Contains coding scheme version reference list that was used to resolve the value domain
 * and the coded node set.
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class ResolvedValueSetCodedNodeSet implements Serializable {

	private static final long serialVersionUID = -3146969083391859655L;

	private AbsoluteCodingSchemeVersionReferenceList codingSchemeVersionRefList_;

	private CodedNodeSet codedNodeSet_;

	public CodedNodeSet getCodedNodeSet() {
		return codedNodeSet_;
	}

	public void setCodedNodeSet(CodedNodeSet codedNodeSet) {
		this.codedNodeSet_ = codedNodeSet;
	}

	public AbsoluteCodingSchemeVersionReferenceList getCodingSchemeVersionRefList() {
		return codingSchemeVersionRefList_;
	}

	public void setCodingSchemeVersionRefList(
			AbsoluteCodingSchemeVersionReferenceList codingSchemeVersionRefList) {
		this.codingSchemeVersionRefList_ = codingSchemeVersionRefList;
	}
}