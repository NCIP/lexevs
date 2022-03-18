
package org.LexGrid.LexBIG.Impl.helpers;

import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search.ProxyProtectedScoreDocWrapper; 

public interface ResolvedConceptReferenceTransformer extends org.LexGrid.LexBIG.Impl.helpers.Transformer
<ProxyProtectedScoreDocWrapper,Set<AbsoluteCodingSchemeVersionReference>,ResolvedConceptReference> {

}