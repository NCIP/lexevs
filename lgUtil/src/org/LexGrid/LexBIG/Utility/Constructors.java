
package org.LexGrid.LexBIG.Utility;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Text;

/**
 * Class to ease creating common objects. This can be used client side or server
 * side.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class Constructors {
    /**
     * Create a LocalNameList out of a set of Strings.
     */
    public static LocalNameList createLocalNameList(String[] entries) {
        if (entries == null) {
            return null;
        }
        LocalNameList list = new LocalNameList();
        for (int i = 0; i < entries.length; i++) {
            list.addEntry(entries[i]);
        }
        return list;
    }
    
    /**
     * Create a Text object.
     */
    public static Text createText(String content) {
        Text text = new Text();
        text.setContent(content);
        return text;
    }

    /**
     * Create an EntityDescription object.
     */
    public static EntityDescription createEntityDescription(String description) {
        EntityDescription ed = new EntityDescription();
        ed.setContent(description);
        return ed;
    }

    /**
     * Create a "PRODUCTION" tag
     */
    public static CodingSchemeVersionOrTag createProductionTag() {
        CodingSchemeVersionOrTag tag = new CodingSchemeVersionOrTag();
        tag.setTag(LBConstants.KnownTags.PRODUCTION.toString());
        return tag;
    }

    /**
     * Create an AbsoluteCodingSchemeReference
     */
    public static AbsoluteCodingSchemeVersionReference createAbsoluteCodingSchemeVersionReference(String urn,
            String version) {
        AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
        acsvr.setCodingSchemeURN(urn);
        acsvr.setCodingSchemeVersion(version);
        return acsvr;
    }

    public static AbsoluteCodingSchemeVersionReference createAbsoluteCodingSchemeVersionReference(
            CodingSchemeSummary summary) {
        AbsoluteCodingSchemeVersionReference result = new AbsoluteCodingSchemeVersionReference();
        result.setCodingSchemeURN(summary.getCodingSchemeURI());
        result.setCodingSchemeVersion(summary.getRepresentsVersion());
        return result;
    }

    /**
     * Create a LocalNameList from a single entry.
     */
    public static LocalNameList createLocalNameList(String name) {
        if (name == null) {
            return null;
        } else {
            return createLocalNameList(new String[] { name });
        }
    }

    /**
     * Create a NameAndValueList from a single name.
     */
    public static NameAndValueList createNameAndValueList(String name) {
        if (name == null) {
            return null;
        } else {
            return createNameAndValueList(new String[] { name }, null);
        }
    }

    /**
     * Create a NameAndValue from a name / val combination.
     */
    public static NameAndValue createNameAndValue(String name, String value) {
        NameAndValue cr = new NameAndValue();
        cr.setContent(value);
        cr.setName(name);
        return cr;
    }

    /**
     * Create a NameAndValueList from multiple names.
     */
    public static NameAndValueList createNameAndValueList(String[] names) {
        return createNameAndValueList(names, null);
    }

    /**
     * Create a NameAndValueList from a set of names. Assign the same value to
     * each.
     */
    public static NameAndValueList createNameAndValueList(String[] names, String value) {
        if (names == null) {
            return null;
        }
        NameAndValueList list = new NameAndValueList();
        for (int i = 0; i < names.length; i++) {
            NameAndValue nv = new NameAndValue();
            nv.setContent(value);
            nv.setName(names[i]);
            list.addNameAndValue(nv);
        }
        return list;
    }

    /**
     * Create a NameAndValueList from a name and a value pair. Returns null if
     * the supplied name is empty.
     */
    public static NameAndValueList createNameAndValueList(String name, String value) {
        if (name == null || name.length() == 0) {
            return null;
        }
        NameAndValueList list = new NameAndValueList();
        NameAndValue nv = new NameAndValue();
        nv.setContent(value);
        nv.setName(name);
        list.addNameAndValue(nv);
        return list;
    }

    /**
     * Create a ConceptReferenceList from a single concept code.
     */
    public static ConceptReferenceList createConceptReferenceList(String code) {
        if (code == null) {
            return null;
        } else {
            return createConceptReferenceList(new String[] { code }, null);
        }
    }

    /**
     * Create a conceptReferenceList from multiple codes.
     */
    public static ConceptReferenceList createConceptReferenceList(String[] codes) {
        return createConceptReferenceList(codes, null);
    }

    /**
     * Create a ConceptReference from a code / codesystem combination.
     */
    public static ConceptReference createConceptReference(String code, String codeSystem) {
        ConceptReference cr = new ConceptReference();
        cr.setCodingSchemeName(codeSystem);
        cr.setCode(code);
        return cr;
    }
    
    /**
     * Create a ConceptReference from a code / namespace / codesystem combination.
     */
    public static ConceptReference createConceptReference(String code, String namespace, String codeSystem) {
        ConceptReference cr = new ConceptReference();
        cr.setCodingSchemeName(codeSystem);
        cr.setCode(code);
        cr.setCodeNamespace(namespace);
        return cr;
    }

    /**
     * Create a ConceptReferenceList from a single concept / codesystem
     * combination.
     */
    public static ConceptReferenceList createConceptReferenceList(String code, String codeSystem) {
        return createConceptReferenceList(new String[] { code }, codeSystem);
    }
    
    /**
     * Create a ConceptReferenceList from a single concept / namespace/ codesystem
     * combination.
     */
    public static ConceptReferenceList createConceptReferenceList(String code, String namespace, String codeSystem) {
        return createConceptReferenceList(new String[] { code }, namespace, codeSystem);
    }

    /**
     * Create a ConceptReferenceList from a set of codes. Assign the same code
     * system and namespace to each.
     */
    public static ConceptReferenceList createConceptReferenceList(String[] codes, String namespace, String codeSystem) {
        if (codes == null) {
            return null;
        }
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.length; i++) {
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codeSystem);
            cr.setCodeNamespace(namespace);
            cr.setCode(codes[i]);
            list.addConceptReference(cr);
        }
        return list;
    }
    
    /**
     * Create a ConceptReferenceList from a set of codes. Assign the same code
     * system to each.
     */
    public static ConceptReferenceList createConceptReferenceList(String[] codes, String codeSystem) {
        return createConceptReferenceList(codes, null, codeSystem);
    }

    public static CodingSchemeVersionOrTag createCodingSchemeVersionOrTagFromTag(String tag) {
        CodingSchemeVersionOrTag result = new CodingSchemeVersionOrTag();
        result.setTag(tag);
        return result;
    }

    public static CodingSchemeVersionOrTag createCodingSchemeVersionOrTagFromVersion(String version) {
        CodingSchemeVersionOrTag result = new CodingSchemeVersionOrTag();
        result.setVersion(version);
        return result;
    }

    public static CodingSchemeVersionOrTag createCodingSchemeVersionOrTag(String tag, String version) {
        CodingSchemeVersionOrTag result = new CodingSchemeVersionOrTag();
        result.setVersion(version);
        result.setTag(tag);
        return result;
    }

    public static SortOption createSortOption(String sortExtensionName, Boolean ascending) {
        SortOption result = new SortOption();
        result.setExtensionName(sortExtensionName);
        result.setAscending(ascending);
        return result;
    }

    public static SortOptionList createSortOptionList(String[] sortExtensionNames, Boolean[] ascending)
            throws LBParameterException {
        SortOptionList result = new SortOptionList();
        if (sortExtensionNames == null || ascending == null || sortExtensionNames.length != ascending.length) {
            throw new LBParameterException(
                    "Invalid Parameters - sortExtensionNames array must be the same length as the ascending array - and neither can be null");
        }
        for (int i = 0; i < ascending.length; i++) {
            SortOption temp = new SortOption();
            temp.setExtensionName(sortExtensionNames[i]);
            temp.setAscending(ascending[i]);
            result.addEntry(temp);
        }

        return result;
    }

    public static SortOptionList createSortOptionList(String[] sortExtensionNames) {
        SortOptionList result = new SortOptionList();
        if (sortExtensionNames == null) {
            return null;
        }
        for (int i = 0; i < sortExtensionNames.length; i++) {
            SortOption temp = new SortOption();
            temp.setExtensionName(sortExtensionNames[i]);
            temp.setAscending(null);
            result.addEntry(temp);
        }

        return result;
    }
}