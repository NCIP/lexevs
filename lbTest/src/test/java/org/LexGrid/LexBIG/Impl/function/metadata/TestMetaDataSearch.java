
package org.LexGrid.LexBIG.Impl.function.metadata;

import java.util.HashSet;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.MetadataProperty;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * Class to test the MetaData search capabilities.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class TestMetaDataSearch extends LexBIGServiceTestCase {
	
	@Override
	protected String getTestID() {
		return TestMetaDataSearch.class.getName();
	}
	
    public void testListCodingSchemes() throws Exception {
        LexBIGServiceMetadata md = ServiceHolder.instance().getLexBIGService().getServiceMetadata();
        AbsoluteCodingSchemeVersionReference[] acsvrl = md.listCodingSchemes()
                .getAbsoluteCodingSchemeVersionReference();

        assertTrue(acsvrl.length >= 2);
        assertTrue(contains(acsvrl, Constructors.createAbsoluteCodingSchemeVersionReference(AUTO_URN, AUTO_VERSION)));
        assertTrue(contains(acsvrl, Constructors.createAbsoluteCodingSchemeVersionReference(PARTS_URN, PARTS_VERSION)));
    }

    private boolean contains(AbsoluteCodingSchemeVersionReference[] acsvr, AbsoluteCodingSchemeVersionReference acsvr2) {
        for (int i = 0; i < acsvr.length; i++) {
            if (acsvr[i].getCodingSchemeURN().equals(acsvr2.getCodingSchemeURN())
                    && acsvr[i].getCodingSchemeVersion().equals(acsvr2.getCodingSchemeVersion())) {
                return true;
            }
        }
        return false;
    }

    private boolean contains(MetadataProperty[] mdp, String urn, String version, String name, String value) {
        for (int i = 0; i < mdp.length; i++) {
            if (mdp[i].getCodingSchemeURI().equals(urn) && mdp[i].getCodingSchemeVersion().equals(version)
                    && mdp[i].getName().equals(name) && mdp[i].getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public void testContainerRestrictionSearch() throws Exception {
        LexBIGServiceMetadata md = ServiceHolder.instance().getLexBIGService().getServiceMetadata();
        md.restrictToValue("obo-text", "LuceneQuery");
        md.restrictToCodingScheme(Constructors.createAbsoluteCodingSchemeVersionReference(AUTO_URN, AUTO_VERSION));

        MetadataProperty[] result = md.resolve().getMetadataProperty();
        assertTrue(result.length == 2);
        assertTrue(contains(result, AUTO_URN, AUTO_VERSION, "format", "OBO-TEXT"));
        assertTrue(contains(result, AUTO_URN, AUTO_VERSION, "download_format", "OBO-TEXT"));

        md = ServiceHolder.instance().getLexBIGService().getServiceMetadata();
        md.restrictToCodingScheme(Constructors.createAbsoluteCodingSchemeVersionReference(AUTO_URN, AUTO_VERSION));
        md.restrictToPropertyParents(new String[] { "core_format" });
        md.restrictToValue("obo-text", "LuceneQuery");
        result = md.resolve().getMetadataProperty();
        assertEquals(1,result.length);
        assertTrue(contains(result, AUTO_URN, AUTO_VERSION, "format", "OBO-TEXT"));

    }

    public void testPropertyRestrictionSearch() throws Exception {
        LexBIGServiceMetadata md = ServiceHolder.instance().getLexBIGService().getServiceMetadata();
        md.restrictToValue("obo-text", "LuceneQuery");
        md.restrictToCodingScheme(Constructors.createAbsoluteCodingSchemeVersionReference(AUTO_URN, AUTO_VERSION));

        MetadataProperty[] result = md.resolve().getMetadataProperty();
        assertTrue(result.length == 2);
        assertTrue(contains(result, AUTO_URN, AUTO_VERSION, "format", "OBO-TEXT"));
        assertTrue(contains(result, AUTO_URN, AUTO_VERSION, "download_format", "OBO-TEXT"));

        md = ServiceHolder.instance().getLexBIGService().getServiceMetadata();
        md.restrictToCodingScheme(Constructors.createAbsoluteCodingSchemeVersionReference(AUTO_URN, AUTO_VERSION));
        md.restrictToProperties(new String[] { "format" });
        md.restrictToValue("obo-text", "LuceneQuery");
        result = md.resolve().getMetadataProperty();
        assertTrue(result.length == 1);
        assertTrue(contains(result, AUTO_URN, AUTO_VERSION, "format", "OBO-TEXT"));

    }

    public void testCodingSchemeRestrictionSearch() throws Exception {
        LexBIGServiceMetadata md = ServiceHolder.instance().getLexBIGService().getServiceMetadata();
        md.restrictToValue("English", "LuceneQuery");
        MetadataProperty[] result = md.resolve().getMetadataProperty();

        HashSet<String> temp = new HashSet<String>();
        for (int i = 0; i < result.length; i++) {
            temp.add(result[i].getCodingSchemeURI() + ":" + result[i].getCodingSchemeVersion());
        }

        // should be more than 1 unique code system.
        assertTrue(temp.size() >= 2);

        // should contain this
        assertTrue(temp.contains(PARTS_URN + ":" + PARTS_VERSION));

        // now do the restriction, and retest.

        md = ServiceHolder.instance().getLexBIGService().getServiceMetadata();
        md.restrictToValue("English", "LuceneQuery");
        md.restrictToCodingScheme(Constructors.createAbsoluteCodingSchemeVersionReference(PARTS_URN, PARTS_VERSION));
        result = md.resolve().getMetadataProperty();

        temp = new HashSet<String>();
        for (int i = 0; i < result.length; i++) {
            temp.add(result[i].getCodingSchemeURI() + ":" + result[i].getCodingSchemeVersion());
        }

        // should be more than 1 unique code system.
        assertTrue(temp.size() == 1);

        // should contain this
        assertTrue(temp.contains(PARTS_URN + ":" + PARTS_VERSION));
    }

    public void testValueRestriction() throws Exception {
        LexBIGServiceMetadata md = ServiceHolder.instance().getLexBIGService().getServiceMetadata();
        md.restrictToValue(".*animaldiversity.*", "RegExp");

        MetadataProperty[] result = md.resolve().getMetadataProperty();
        assertTrue(result.length >= 3);
        assertTrue(contains(result, PARTS_URN, PARTS_VERSION, "homepage", "http://www.animaldiversity.org"));
    }
}