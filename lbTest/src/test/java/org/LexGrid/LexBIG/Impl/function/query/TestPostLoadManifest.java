
package org.LexGrid.LexBIG.Impl.function.query;

import java.io.File;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedAssociationQualifier;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedContext;
import org.LexGrid.naming.SupportedDataType;
import org.LexGrid.naming.SupportedDegreeOfFidelity;
import org.LexGrid.naming.SupportedHierarchy;
import org.LexGrid.naming.SupportedLanguage;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.SupportedPropertyLink;
import org.LexGrid.naming.SupportedPropertyQualifier;
import org.LexGrid.naming.SupportedRepresentationalForm;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.naming.SupportedStatus;
import org.lexevs.system.ResourceManager;

public class TestPostLoadManifest extends LexBIGServiceTestCase {

    final static String testID = "TEST_POST_LOAD_MANIFEST";

    final static String URN = "http://www.co-ode.org/ontologies/pizza/pizza.owl";
    final static String VERSION = "2008";
    public final static String NEW_URN = "urn:oid:2.16.840.1.113883.6.10:Post.Pizza.test";
    public final static String NEW_VERSION = "1.11";

    final static String XMLLOADER = "LexGridLoader";
    final static String METADATALOADER = "MetaDataLoader";

    final static String PIZZAMANIFESTFILE = "resources/testData/pizza-Manifest.xml";
    final static String PIZZAONTOFILE = "resources/testData/pizza.xml";
    final static String PIZZAMANIFESTPOST = "resources/testData/pizza-Manifest-post.xml";

    private AbsoluteCodingSchemeVersionReference absCSV_ = new AbsoluteCodingSchemeVersionReference();
    
    private LexBIGService lbs;
    private LexBIGServiceManager lbsm;
    
    @Override
    protected String getTestID() {
        return testID;
    }
    
    public void setUp() throws LBException {
        lbs = ServiceHolder.instance().getLexBIGService();
        lbsm = lbs.getServiceManager(null);
    }

    /**
     * Modify the pizza ontology after the initial load using manifest file.
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testPostLoadManifestWithPizza() throws InterruptedException, LBException {
        LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm.getLoader(XMLLOADER);

        loader.setCodingSchemeManifestURI(new File(PIZZAMANIFESTFILE).toURI());
        loader.load(new File(PIZZAONTOFILE).toURI(), true, false);

        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        MetaData_Loader mdLoader = (MetaData_Loader) lbsm.getLoader(METADATALOADER);
        AbsoluteCodingSchemeVersionReference codingSchemeURNVersion = new AbsoluteCodingSchemeVersionReference();
        codingSchemeURNVersion.setCodingSchemeURN(URN);
        codingSchemeURNVersion.setCodingSchemeVersion(VERSION);

        try {
            mdLoader.loadLexGridManifest(new File(PIZZAMANIFESTPOST).toURI(), codingSchemeURNVersion, false, false);
        } catch (LBException e) {
           fail(e.getMessage());
        }

        assertTrue(mdLoader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(mdLoader.getStatus().getErrorsLogged().booleanValue());
    }

    public void testLoadedOntologyForCodingSchemeAttrib() throws LBException {
        String internalVersion = null;
        String internalCSName = null;

        absCSV_.setCodingSchemeURN(NEW_URN);
        absCSV_.setCodingSchemeVersion(NEW_VERSION);

        internalVersion = ResourceManager.instance().getInternalVersionStringForTag(absCSV_.getCodingSchemeURN(), null);
        internalCSName = ResourceManager.instance().getInternalCodingSchemeNameForUserCodingSchemeName(
                absCSV_.getCodingSchemeURN(), internalVersion);

        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(NEW_VERSION);
        CodingScheme codingScheme = lbs.resolveCodingScheme(internalCSName, versionOrTag);

        assertNotNull(codingScheme);

        String entiryDesc = codingScheme.getEntityDescription().getContent();
        String formalName = codingScheme.getFormalName();
        String regName = codingScheme.getCodingSchemeURI();
        String defLang = codingScheme.getDefaultLanguage();
        String repVersion = codingScheme.getRepresentsVersion();
        Text copyRight = codingScheme.getCopyright();

        assertTrue("Error in Entity Description.", "2008 version of Pizza ontology".equalsIgnoreCase(entiryDesc));
        assertFalse("Error in Formal Name.", "Pizza Vocabulary".equalsIgnoreCase(formalName));
        assertFalse("Error in Registered Name.", "http://www.co-ode.org/ontologies/pizza/pizza.owl#"
                .equalsIgnoreCase(regName));
        assertFalse("Error in Default Language.", "ka".equalsIgnoreCase(defLang));
        assertFalse("Error in Represents Version.", "2008".equalsIgnoreCase(repVersion));
        assertFalse("Error in Copy Right.", "Pizza, 2008".equalsIgnoreCase(copyRight.getContent()));
    }

    public void testLoadedOntologyForSupportedAttribs() throws LBException {
        String internalVersion = null;
        String internalCSName = null;

        absCSV_.setCodingSchemeURN(NEW_URN);
        absCSV_.setCodingSchemeVersion(NEW_VERSION);

        internalVersion = ResourceManager.instance().getInternalVersionStringForTag(absCSV_.getCodingSchemeURN(), null);
        internalCSName = ResourceManager.instance().getInternalCodingSchemeNameForUserCodingSchemeName(
                absCSV_.getCodingSchemeURN(), internalVersion);

        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(NEW_VERSION);

        CodingScheme codingScheme = lbs.resolveCodingScheme(internalCSName, versionOrTag);
        assertNotNull(codingScheme);

        /* SupportedLanguage */
        SupportedLanguage[] suppLang = codingScheme.getMappings().getSupportedLanguage();
        for (int i = 0; i < suppLang.length; i++) {
            if (suppLang[i].getLocalId().equalsIgnoreCase("ka")) {
                assertFalse("Error in SupportedLanguage.", "urn:oid:1.3.6:KANNADA".equalsIgnoreCase(suppLang[i]
                        .getUri()));
            }
        }

        /* SupportedFormat */
        SupportedDataType[] suppForm = codingScheme.getMappings().getSupportedDataType();
        boolean testPlainPostExist = false;
        for (int i = 0; i < suppForm.length; i++) {
            if (suppForm[i].getLocalId().equalsIgnoreCase("text_plain_post")) {
                testPlainPostExist = true;
            }
        }
        assertTrue("'text_plain_post' not added.", testPlainPostExist);

        /* SupportedProperty */
        SupportedProperty[] suppProp = codingScheme.getMappings().getSupportedProperty();
        for (int i = 0; i < suppProp.length; i++) {
            if (suppProp[i].getLocalId().equalsIgnoreCase("property1")) {
                assertFalse("Error in SupportedProperty.", "urn:oid:1.3.6.1.4.1.2114.108.1.6.59:property111_post"
                        .equalsIgnoreCase(suppProp[i].getUri()));
                assertTrue("Error in SupportedProperty.", "urn:oid:1.3.6:PROPERTY1".equals(suppProp[i].getUri()));
            }
        }

        /* SupportedCodingScheme */
        SupportedCodingScheme[] suppCodingScheme = codingScheme.getMappings().getSupportedCodingScheme();
        for (int i = 0; i < suppCodingScheme.length; i++) {
            if (suppCodingScheme[i].getLocalId().equalsIgnoreCase("Expendable Pizza")) {
                assertTrue("Error in SupportedCodingScheme.", "urn:oid:11.11.0.50".equalsIgnoreCase(suppCodingScheme[i]
                        .getUri()));
            }
        }

        /* SupportedSource */
        SupportedSource[] suppSource = codingScheme.getMappings().getSupportedSource();
        for (int i = 0; i < suppSource.length; i++) {
            if (suppSource[i].getLocalId().equalsIgnoreCase("SOURCE1")) {
                assertTrue("Error in SupportedSource.", "urn:oid:1.3.8:source1"
                        .equalsIgnoreCase(suppSource[i].getUri()));
            }
        }

        /* SupportedAssociation */
        SupportedAssociation[] suppAssociation = codingScheme.getMappings().getSupportedAssociation();
        for (int i = 0; i < suppAssociation.length; i++) {
            if (suppAssociation[i].getLocalId().equalsIgnoreCase("Role_Has_Range")) {
                assertTrue("Error in SupportedAssociation.", "post-content".equalsIgnoreCase(suppAssociation[i]
                        .getContent()));
            }
        }

        /* SupportedContext */
        SupportedContext[] suppContext = codingScheme.getMappings().getSupportedContext();
        for (int i = 0; i < suppContext.length; i++) {
            if (suppContext[i].getLocalId().equalsIgnoreCase("Context1")) {
                assertTrue("Error in SupportedContext.", "urn:oid:1.3.6".equalsIgnoreCase(suppContext[i].getUri()));
            }
        }

        /* SupportedAssociationQualifier */
        SupportedAssociationQualifier[] suppAssocQualifier = codingScheme.getMappings()
                .getSupportedAssociationQualifier();
        for (int i = 0; i < suppAssocQualifier.length; i++) {
            if (suppAssocQualifier[i].getLocalId().equalsIgnoreCase("AssociationQualifier")) {
                assertTrue("Error in SupportedAssociationQualifier.",
                        "urn:oid:1.3.6.1.4.1.2114.108.1.6.61:AssociationQualifier"
                                .equalsIgnoreCase(suppAssocQualifier[i].getUri()));
            } else if (suppAssocQualifier[i].getLocalId().equalsIgnoreCase("")) {
                assertTrue("Error in SupportedAssociationQualifier.", "urn:oid:1.3.6.1.4.1.2114.108.1.6.61:Context2"
                        .equalsIgnoreCase(suppAssocQualifier[i].getUri()));
            }
        }

        /* SupportedConceptStatus */
        SupportedStatus[] suppConceptStatus = codingScheme.getMappings().getSupportedStatus();
        for (int i = 0; i < suppConceptStatus.length; i++) {
            if (suppConceptStatus[i].getLocalId().equalsIgnoreCase("cONCEPTsTATUS")) {
                assertTrue("Error in SupportedConceptStatus.", "urn:oid:1.3.6.1.4.1.2114.108.1.6.96:cONCEPTsTATUS"
                        .equalsIgnoreCase(suppConceptStatus[i].getUri()));
            }
        }

        /* SupportedRepresentationalForm */
        SupportedRepresentationalForm[] suppRepForm = codingScheme.getMappings().getSupportedRepresentationalForm();
        for (int i = 0; i < suppRepForm.length; i++) {
            if (suppRepForm[i].getLocalId().equalsIgnoreCase("RepresentationForm")) {
                assertFalse("Error in SupportedRepresentationalForm urn.",
                        "urn:oid:2.16.840.1.113883.6.10:RepresentationForm-post".equalsIgnoreCase(suppRepForm[i]
                                .getUri()));
                assertFalse("Error in SupportedRepresentationalForm content.", "supportedRepresentationalForm"
                        .equalsIgnoreCase(suppRepForm[i].getContent()));
            }
        }

        /* SupportedPropertyLink */
        SupportedPropertyLink[] suppPropLink = codingScheme.getMappings().getSupportedPropertyLink();
        for (int i = 0; i < suppPropLink.length; i++) {
            if (suppPropLink[i].getLocalId().equalsIgnoreCase("Propertylink")) {
                assertFalse("Error in SupportedPropertyLink urn.", "urn:oid:2.16.840.1.113883.6.10:Propertylink"
                        .equalsIgnoreCase(suppPropLink[i].getUri()));
                assertTrue("Error in SupportedPropertyLink content.", "supportedPropertylink"
                        .equalsIgnoreCase(suppPropLink[i].getContent()));
            }
        }

        /* SupportedDegreeOfFidelity */
        SupportedDegreeOfFidelity[] suppDegreeOfFidelity = codingScheme.getMappings().getSupportedDegreeOfFidelity();
        for (int i = 0; i < suppDegreeOfFidelity.length; i++) {
            if (suppDegreeOfFidelity[i].getLocalId().equalsIgnoreCase("DegreeOfFidelity")) {
                assertTrue("Error in SupportedDegreeOfFidelity content.", "supportedDegreeOfFidelity"
                        .equalsIgnoreCase(suppDegreeOfFidelity[i].getContent()));
            }
        }

        /* SupportedPropertyQualifier */
        SupportedPropertyQualifier[] suppPropQual = codingScheme.getMappings().getSupportedPropertyQualifier();
        boolean suppPropQualPresent = false;
        for (int i = 0; i < suppPropQual.length; i++) {
            if (suppPropQual[i].getLocalId().equalsIgnoreCase("PropertyQualifier")) {
                suppPropQualPresent = true;
            }
        }
        assertTrue("Error in SupportedPropertyQualifier", suppPropQualPresent);

        /* SupportedHierarchy */
        SupportedHierarchy[] suppHier = codingScheme.getMappings().getSupportedHierarchy();
        for (int i = 0; i < suppHier.length; i++) {
            if (suppHier[i].getLocalId().equalsIgnoreCase("is_a")
                    && suppHier[i].getAssociationNames().equals(new String[] { "hasSubtype" })
                    && suppHier[i].getRootCode().equals("@")) {
                assertTrue("Error in SupportedHierarchy content.", "supportedHierarchy-post"
                        .equalsIgnoreCase(suppHier[i].getContent()));
            }
        }
    }
}