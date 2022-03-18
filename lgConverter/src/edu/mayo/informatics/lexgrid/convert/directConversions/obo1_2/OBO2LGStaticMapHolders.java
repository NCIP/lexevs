
package edu.mayo.informatics.lexgrid.convert.directConversions.obo1_2;

import java.util.Hashtable;
import java.util.List;
import java.util.TreeSet;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedDataType;
import org.LexGrid.naming.SupportedLanguage;
import org.LexGrid.naming.SupportedNamespace;

import edu.mayo.informatics.resourcereader.obo.OBOConstants;
import edu.mayo.informatics.resourcereader.obo.OBOContents;
import edu.mayo.informatics.resourcereader.obo.OBOHeader;
import edu.mayo.informatics.resourcereader.obo.OBOResourceReader;
import edu.mayo.informatics.resourcereader.obo.OBOTerms;

public class OBO2LGStaticMapHolders {
    private static Hashtable<String, String> properties = new Hashtable<String, String> ();
    private static Hashtable<String, String> association = new Hashtable<String, String>();
    private static Hashtable<String, String> sources = new Hashtable<String, String> ();


    private LgMessageDirectorIF messages_;

    public OBO2LGStaticMapHolders() {
    }

    public OBO2LGStaticMapHolders(LgMessageDirectorIF messages) {
        messages_ = messages;
    }

    private void prepareSupportedLanguages(List<SupportedLanguage> suppLang) {
        try {
            SupportedLanguage lang = new SupportedLanguage();
            lang.setLocalId(OBO2LGConstants.LANG_ENGLISH);
            lang.setUri(OBO2LGConstants.LANG_ENGLISH_URN);
            suppLang.add(lang);
        } catch (Exception e) {
            messages_.error("Failed while setting supported languages...", e);
        }
    }

    private void prepareSupportedCodingScheme(CodingScheme csclass) {
        try {
            List<SupportedCodingScheme> suppCodingScheme = csclass.getMappings().getSupportedCodingSchemeAsReference();
            SupportedCodingScheme scs = new SupportedCodingScheme();
            scs.setLocalId(csclass.getCodingSchemeName());
            scs.setUri(csclass.getCodingSchemeURI());
            scs.setIsImported(true);
            suppCodingScheme.add(scs);
        } catch (Exception e) {
            messages_.error("Failed while setting supported codingScheme...", e);
        }
    }

    private void prepareSupportedNamespace(CodingScheme csclass) {
        try {
            List<SupportedNamespace> suppCodingScheme = csclass.getMappings().getSupportedNamespaceAsReference();
            SupportedNamespace scs = new SupportedNamespace();
            scs.setLocalId(csclass.getCodingSchemeName());
            scs.setUri(csclass.getCodingSchemeURI());           
            suppCodingScheme.add(scs);
        } catch (Exception e) {
            messages_.error("Failed while setting supported codingScheme...", e);
        }
    }    
    private void prepareSupportedFormats(List<SupportedDataType> supportedFormats) {
        SupportedDataType fmt = new SupportedDataType();
        fmt.setLocalId(OBO2LGConstants.PLAIN_FORMAT);
        fmt.setUri(OBO2LGConstants.PLAIN_FORMAT_URN);
        supportedFormats.add(fmt);
    }

    public CodingScheme getOBOCodingScheme(OBOResourceReader oboRdr, String inputFileNameWithoutExt) {

        CodingScheme csclass = null;

        try {
            if (oboRdr == null) {
                messages_.fatalAndThrowException("Failed to create Coding Scheme Node bcause of OBO File Reader!");
                return csclass;
            }

            csclass = new CodingScheme();
            Mappings mappings = new Mappings();
            csclass.setMappings(mappings);

            // String csName = inputFileNameWithoutExt;
            String csName = "";
            OBOHeader oboHeader = oboRdr.getResourceHeader(false);
            OBOContents oboContents = oboRdr.getContents(false, false);
            OBOTerms terms = oboContents.getOBOTerms();
            TreeSet<String> nameSpaceSet = terms.getNameSpaceSet();
            if (OBO2LGUtils.isNull(csName) && nameSpaceSet.size() == 1)
                csName = nameSpaceSet.first().toString();

            if (OBO2LGUtils.isNull(csName) && !OBO2LGUtils.isNull(oboHeader.getDefaultNameSpace()))
                csName = oboHeader.getDefaultNameSpace();

            if (OBO2LGUtils.isNull(csName) && !OBO2LGUtils.isNull(terms.getTermPrefix()))
                csName = terms.getTermPrefix();

            if (OBO2LGUtils.isNull(csName))
                csName = inputFileNameWithoutExt;

            csName = csName.trim();
            if (csName.length() > 50) {
                csName= csName.substring(0, 49);
            }

            csclass.setCodingSchemeName(csName);
            csclass.setFormalName(csName);
            csclass.setCodingSchemeURI(OBO2LGConstants.BIOONTOLOGY_LSID_PREFIX + csName);
            OBO2LGConstants.OBO_URN = OBO2LGConstants.BIOONTOLOGY_LSID_PREFIX + csName + OBO2LGConstants.URN_DELIM;
            csclass.setDefaultLanguage(OBO2LGConstants.LANG_ENGLISH);

            if (!OBO2LGUtils.isNull(oboHeader.getOntologyVersion()))
                csclass.setRepresentsVersion(oboHeader.getOntologyVersion());
            else
                csclass.setRepresentsVersion(OBOConstants.UNASSIGNED_LABEL);

            csclass.getLocalNameAsReference().add(csName);
            csclass.setApproxNumConcepts(terms.getMembersCount());
            // csclass.setIsNative(new Boolean(true));
            EntityDescription ed= new EntityDescription();
            ed.setContent(oboHeader.getRemarks());
            csclass.setEntityDescription(ed);

            List<SupportedLanguage> supportedLanguages = csclass.getMappings().getSupportedLanguageAsReference();
            prepareSupportedLanguages(supportedLanguages);

            List<SupportedDataType> supportedFormats = csclass.getMappings().getSupportedDataTypeAsReference();
            prepareSupportedFormats(supportedFormats);

            prepareSupportedCodingScheme(csclass);
            prepareSupportedNamespace(csclass);
        } catch (Exception e) {
            messages_.error("Failed while preparing for Coding Scheme Class", e);
        }

        return csclass;
    }

    public static Hashtable<String, String>  getFixedProperties() {
        properties.clear();
        properties.put(OBO2LGConstants.PROPERTY_COMMENT, OBO2LGConstants.PROPERTY_COMMENT);
        properties.put(OBO2LGConstants.PROPERTY_DEFINITION, OBO2LGConstants.PROPERTY_DEFINITION);
        properties.put(OBO2LGConstants.PROPERTY_TEXTPRESENTATION, OBO2LGConstants.PROPERTY_TEXTPRESENTATION);
        properties.put(OBO2LGConstants.PROPERTY_SYNONYM, OBO2LGConstants.PROPERTY_SYNONYM);
        properties.put(OBO2LGConstants.PROPERTY_ALTID, OBO2LGConstants.PROPERTY_ALTID);

        return properties;
    }


    public static Hashtable<String, String> getFixedSources() {
        sources.clear();
        return sources;
    }



    public static Hashtable<String, String> getFixedAssociations() {
        association.clear();
        // association.put(OBO2EMFConstants.ASSOCIATION_HASSUBTYPE,
        // OBO2EMFConstants.ASSOCIATION_HASSUBTYPE);

        return association;
    }
}