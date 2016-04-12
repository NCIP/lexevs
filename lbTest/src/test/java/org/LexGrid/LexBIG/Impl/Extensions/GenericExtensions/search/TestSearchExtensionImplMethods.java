package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Test;

public class TestSearchExtensionImplMethods {

	@Test
	public void testIndexMismatchToRegistry() throws LBException{
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		SearchExtensionImpl searchExtension = (SearchExtensionImpl) lbs.getGenericExtension("SearchExtension");
		Set<CodingSchemeReference> refs = new HashSet<CodingSchemeReference>();
		CodingSchemeReference ref = new CodingSchemeReference();
		ref.setCodingScheme("SchemeNotThere");
		ref.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromVersion("v1.0"));
		refs.add(ref);
		assertNotNull(searchExtension.resolveCodeSystemReferences(refs));
	}

}
