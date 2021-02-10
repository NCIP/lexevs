
package edu.mayo.informatics.lexgrid.convert.directConversions.UmlsCommon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.LexOnt.CsmfCodingSchemeName;
import org.LexGrid.LexOnt.CsmfCodingSchemeURI;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.GenericSQLModifier;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.OrderedMapIterator;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import edu.mayo.informatics.lexgrid.convert.utility.Constants;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;

/**
 * Common Code for RRF/UMLS based loaders.
 * 
 * Here are the table use details for the UMLS converter
 * 
 * Used tables:
 * 
 * MRCONSO.RRF MRREL.RRF MRSAB.RRF MRDEF.RRF MRSTY.RRF MRSAT.RRF MRDOC.RRF
 * MRRANK.RRF MRHIER.RRF
 * 
 * Conditionally referenced: MRHIER.RRF (for contextual links)
 * 
 * Unused tables: AMBIGLUI.RRF AMBIGSUI.RRF MRAUI.RRF MRCOC.RRF MRCOLS.RRF
 * MRCUI.RRF MRCXT.RRF MRFILES.RRF MRHIST.RRF MRMAP.RRF MRSMAP.RRF MRXNS_ENG.RRF
 * MRXNW_ENG.RRF MRXW_BAQ.RRF MRXW_CZE.RRF MRXW_DAN.RRF MRXW_DUT.RRF
 * MRXW_ENG.RRF MRXW_FIN.RRF MRXW_FRE.RRF MRXW_GER.RRF MRXW_HEB.RRF MRXW_HUN.RRF
 * MRXW_ITA.RRF MRXW_JPN.RRF MRXW_NOR.RRF MRXW_POR.RRF MRXW_RUS.RRF MRXW_SPA.RRF
 * MRXW_SWE.RRF
 * 
 * 
 * There are a couple of unused tables that I probably should be using - but I'm
 * just not loading that info yet - such as the history. I'm not sure about the
 * map tables - I'll need to look into them more at some future point.
 * 
 * 
 * Here is the breakdown for the NCI MetaThesaurus:
 * 
 * Used tables:
 * 
 * MRCONSO.RRF MRREL.RRF MRSAB.RRF MRDEF.RRF MRSTY.RRF MRRANK.RRF
 * 
 * 
 * Unused tables: MRCOC.RRF MRCOLS.RRF MRDOC.RRF MRFILES.RRF
 * 
 * Here, I think that the only one that I'm not using that I maybe should be is
 * the MRCOC file - I'll have to look at this again at some point.
 * 
 * Differences/issues I can remember (though there may have been more):
 * 
 * While loading from the UMLS: For most terminologies, I could get the Language
 * from the LAT column in the MRCONSO file. In Snomed, I had to do a query on
 * the MRSAT table and pull the language out of the ATV column.
 * 
 * In the UMLS - when we load things, we are loading based on terminology
 * concept codes, not CUI's - so to get the preferred text for a code in a
 * terminology (per language) required reading all presentations for a code from
 * a terminology, and then doing a sort taking into account 4 different columns,
 * each with various values that mean preferred in different terminologies
 * 
 * In the NCI MetaThesaurus, since we are loading it using the CUI's as the
 * concept codes, I can just use the IsPref column to determine the preferred
 * text.
 * 
 * 
 * I think that all of the other differences in the UMLS loader versus the NCI
 * MetaThesaurus loader are due to loading based on CUI's instead of concept
 * codes.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: 9636 $ checked in on $Date: 2008-08-20
 *          16:32:48 +0000 (Wed, 20 Aug 2008) $
 */
@Deprecated
public class UMLSBaseCode {
    protected static Logger log;

    protected Connection umlsConnection_;
    protected Connection umlsConnection2_;
    protected Connection sqlConnection_;

    protected SQLTableUtilities sqlTableUtility_;
    protected SQLTableConstants stc_;
    protected GenericSQLModifier umlsSqlModifier_;
    protected GenericSQLModifier sqlModifier_;

    protected final int batchSize = Constants.mySqlBatchSize;

    protected Hashtable isoMap = new Hashtable();

    protected HashSet loadedAssociations_ = new HashSet();
    protected HashSet supportedCodingSchemes_ = new HashSet();
    protected Association[] supportedAssociations_ = null;
    protected HashSet supportedAssociationQualifiers_ = new HashSet();
    protected HashSet supportedPropertyTypes_ = new HashSet();
    protected HashSet supportedPropertyLinks_ = new HashSet();
    protected HashSet supportedPropertyQualifiers_ = new HashSet();
    protected HashSet supportedSources_ = new HashSet();
    protected HashSet supportedLanguages_ = new HashSet();
    protected HashSet supportedHierarchies_ = new HashSet();
    protected HashSet associationQualifierRUI_ = new HashSet();
    protected HashMap MRRANK_ = new HashMap();
    protected String defaultLanguage_;

    protected Hashtable mrconsoRepresentationalMap_ = new Hashtable();
    protected Hashtable sabToCodeSystem_ = new Hashtable();

    protected PreparedStatement insertIntoCodingScheme;
    protected PreparedStatement insertIntoCodingSchemeMultiAttributes;
    protected PreparedStatement insertIntoEntityProperty;
    protected PreparedStatement insertIntoEntityPropertyMultiAttributes;
    protected PreparedStatement insertIntoEntities;
    protected PreparedStatement insertIntoEntryState;
    protected PreparedStatement insertIntoEntityType;
    protected PreparedStatement insertIntoConceptsMultiAttributes;
    protected PreparedStatement insertIntoRelations;
    protected PreparedStatement insertIntoAssociations;
    protected PreparedStatement insertIntoEntityAssociationsToEntity;
    protected PreparedStatement insertIntoEntityAssociationsToEntityQualifier;
    protected PreparedStatement insertIntoEntityPropertyLinks;

    protected PreparedStatement insertIntoCodingSchemeSupportedAttributes;
    protected PreparedStatement getCodeForCUI_;
    protected PreparedStatement getCodeForCUINoAUI_;
    protected PreparedStatement getSource_;
    protected PreparedStatement getUMLSRoot_;
    protected PreparedStatement getAssocInstance_;
    protected PreparedStatement getAssocQualifier_;
    protected PreparedStatement getAllAssocQualifiersFromRRF_;
    protected PreparedStatement getAssocQualifierFromRRF_;
    protected PreparedStatement getAUIFromPresentation_;
    protected PreparedStatement getPresentationRank_;
    protected PreparedStatement getPropIds_;

    private LRUMap cuiToCodeCache_ = new LRUMap(50000);
    protected LRUMap auiToCodeCache_ = new LRUMap(50000);

    protected LgMessageDirectorIF messages_;
    private MessageDigest md_ = null;

    protected LoaderPreferences loadPrefs_;
    protected URI manifestLocation_;

    public boolean manifestNameChange_ = false;

    protected static int entryStateId_ = 0;

    public class Association implements Comparable {
        public String name;
        public String rrfName;
        public String rrfInverse;
        public String rrfField;
        public String rrfSAB;
        public boolean rrfSourceDirectionalityReversed = false;

        public String toShortString() {
            return new StringBuffer(64).append(rrfSAB == null ? "" : (rrfSAB + ':')).append(rrfName).toString();
        }

        public String toString() {
            return new StringBuffer(256).append("Association name: ").append(name).append("\n\tRRF name: ").append(
                    rrfName).append("\n\tRRF inverse: ").append(rrfInverse).append("\n\tRRF field: ").append(rrfField)
                    .append("\n\tRRF sab: ").append(rrfSAB).append("\n\tRRF directionality reversed: ").append(
                            rrfSourceDirectionalityReversed).toString();
        }

        public boolean isForwardMatch(SABString qualName) {
            return ((StringUtils.isBlank(rrfSAB)) || StringUtils.isBlank(qualName.sab) || rrfSAB
                    .equalsIgnoreCase(qualName.sab))
                    && (StringUtils.isNotBlank(rrfName) && (rrfName.equalsIgnoreCase(qualName.str)));
        }

        public boolean isInverseMatch(SABString qualName) {
            return ((StringUtils.isBlank(rrfSAB)) || StringUtils.isBlank(qualName.sab) || rrfSAB
                    .equalsIgnoreCase(qualName.sab))
                    && (StringUtils.isNotBlank(rrfInverse) && (rrfInverse.equalsIgnoreCase(qualName.str)));
        }

        public int compareTo(Object o) {
            if (!(o instanceof Association))
                return -1;
            int i = 0;
            if (name != null && ((i = name.compareTo(((Association) o).name)) != 0))
                return i;
            if (rrfSAB != null && ((i = rrfSAB.compareTo(((Association) o).rrfSAB)) != 0))
                return i;
            return 0;
        }
    }

    public class AssociationQualification {
        public String qualifierName;
        public String qualifierValue;
        public String codingSchemeName;
        public String pathToRoot;
        public String sourceConceptAUI;
        public String sourceConceptCode;
    }

    public class AssociationSorter implements Comparator {
        public int compare(Object arg0, Object arg1) {
            // Group by field, so that source-defined and umls-defined
            // relations are grouped together. Also, keep items with the
            // same primary name together (potentially differentiated
            // by SAB).
            Association a = (Association) arg0;
            Association b = (Association) arg1;
            int i = -1 * a.rrfField.compareTo(b.rrfField);
            if (i != 0)
                return i;
            return new SABString(a.name, a.rrfSAB).compareTo(new SABString(b.name, b.rrfSAB));
        }
    }

    public class CodeHolder {
        public String code;
        public String codingScheme;

        public CodeHolder() {
            super();
        }

        public CodeHolder(String code, String codingScheme) {
            this();
            this.code = code;
            this.codingScheme = codingScheme;
        }
    }

    public class ConceptPresentationSorter implements Comparator {
        private boolean metaMode = false;

        public ConceptPresentationSorter(boolean NCIMetaThesaurusMode) {
            this.metaMode = NCIMetaThesaurusMode;
        }

        public ConceptPresentationSorter() {
            this.metaMode = false;
        }

        /*
         * This comparator is used for sorting an array of ConceptPresentations.
         * It groups them by language, (default language first) and inside the
         * language groups, it ranks them by their best likleyhood of being the
         * "preferredPresentation"
         */
        public int compare(Object a, Object b) {
            ConceptPresentation aa = (ConceptPresentation) (a);
            ConceptPresentation bb = (ConceptPresentation) (b);

            if (aa.language.equals(defaultLanguage_) && !bb.language.equals(defaultLanguage_)) {
                // aa is default language, bb is not.
                return -1;
            } else if (bb.language.equals(defaultLanguage_) && !aa.language.equals(defaultLanguage_)) {
                // bb is default language, aa is not.
                return 1;
            } else if (!aa.language.equals(bb.language)) {
                // languages not equal to each other, group by languages
                // alphabetically
                return aa.language.compareTo(bb.language);
            } else
            // languages equal each other - now order by which one has the
            // "more preferred" presentation
            {
                // There are several ways we can calculate the order at this
                // point. We can use the "isPref"
                // column, the "TS" and "STT" columns in the MRCONSO RRF file,
                // or a combination of these columns
                // to determine the Preferred Presentation. We can also use the
                // MRRANK file.
                // In all cases, the Presentations will be sorted into language
                // groups, with the default language
                // group first.

                // MRRANK is the overriding factor in choosing which
                // Presentation is "Preferred".

                // Set both priorities to -1 -- we'll determine their actual
                // value later.
                int firstCodePriority = getRankFromMRRANK(aa.source, aa.TTY);
                int secondCodePriority = getRankFromMRRANK(bb.source, bb.TTY);

                // Here is where we do our calculations. In MRRANK, a higher
                // numerical rank value means that
                // the Presentation is Peferred over a lower rank value. We sort
                // them as such.
                // Note here that we only return a value if one priority is
                // higher than the other.
                // If both priorities are the same, we continue calculating
                // using the "isPref" and
                // so on. Also note that just because the MRRANK file exists it
                // doesn't guarantee that
                // we'll find the specific "SAB" and "TTY" we're looking for. In
                // this case, both
                // "firstCodePriority" and "secondCodePriority" will remain -1,
                // equal each other,
                // and processing will continue below.
                if (firstCodePriority > secondCodePriority) {
                    return -1;
                } else if (firstCodePriority < secondCodePriority) {
                    return 1;
                }

                // NCI MetaThesaurus RRF files actually have the isPref column
                // set in a useful fashion, so we can just
                // use that.

                if (metaMode && aa.ISPREF.equals("Y") && !bb.ISPREF.equals("Y")) {
                    // aa has a preferred flag set on the isPref column.
                    return -1;
                } else if (metaMode && bb.ISPREF.equals("Y") && !aa.ISPREF.equals("Y")) {
                    // bb has a preferred flag set on the isPref column.
                    return 1;
                } else {
                    // Not in NCI MetaThesaurus mode, or neither had the flag
                    // set.
                    if ((aa.TTY.equals("PT") || aa.TTY.equals("MH")) && (!bb.TTY.equals("PT") || !bb.TTY.equals("MH"))) {
                        // aa has a preferred flag set on the TTY column, but bb
                        // does not
                        return -1;
                    } else if ((bb.TTY.equals("PT") || bb.TTY.equals("MH"))
                            && (!aa.TTY.equals("PT") || !aa.TTY.equals("MH"))) {
                        // bb has a preferred flag set on the tty column, but aa
                        // does not.
                        return 1;
                    } else {
                        // both or neither have the TTY column set the way I
                        // want, look at the TS and STT columns
                        if ((aa.TS.equals("P") && aa.STT.equals("PF")) && (!bb.TS.equals("P") || !bb.STT.equals("PF"))) {
                            // aa is preferred, bb is not
                            return -1;
                        } else if ((bb.TS.equals("P") && bb.STT.equals("PF"))
                                && (!aa.TS.equals("P") || !aa.STT.equals("PF"))) {
                            // bb is preferred, aa is not.
                            return 1;
                        } else {
                            // both or neither have both columns set the way I
                            // want, what about one column?
                            // favor STT column first
                            if (aa.STT.equals("PF") && !bb.STT.equals("PF")) {
                                // aa preferred, bb not.
                                return -1;
                            } else if (bb.STT.equals("PF") && !aa.STT.equals("PF")) {
                                // bb preferred, aa not
                                return 1;
                            } else if (aa.TS.equals("P") && !bb.TS.equals("P")) {
                                return -1;
                            } else if (bb.TS.equals("P") && !aa.TS.equals("P")) {
                                return 1;
                            } else {
                                // neither is preferred over the other.
                                return 0;
                            }
                        }
                    }
                }
            }
        }
    }

    public class ConceptPresentation {
        public String cui;
        public String representationForm;
        public String STT = "";
        public String TS = "";
        public String value;
        public String TTY = "";
        public String presentationFormat;
        public String conceptCode;
        public String language;
        public String source;
        public String ISPREF = "";
        public String AUI = "";
        public Boolean isPreferred;
    }

    protected void makeConnections(String umlsServer, String umlsDriver, String umlsUserName, String umlsPassword,
            String sqlServer, String sqlDriver, String sqlUserName, String sqlPassword, String tablePrefix,
            boolean enforceIntegrity) throws Exception {
        try {
            umlsConnection_ = DBUtility.connectToDatabase(umlsServer, umlsDriver, umlsUserName, umlsPassword);
            umlsConnection2_ = DBUtility.connectToDatabase(umlsServer, umlsDriver, umlsUserName, umlsPassword);
            sqlConnection_ = DBUtility.connectToDatabase(sqlServer, sqlDriver, sqlUserName, sqlPassword);
        } catch (ClassNotFoundException e) {
            messages_.info("The class you specified for your sql driver could not be found on the path.");
        }

        sqlTableUtility_ = new SQLTableUtilities(sqlConnection_, tablePrefix);
        stc_ = sqlTableUtility_.getSQLTableConstants();

        umlsSqlModifier_ = new GenericSQLModifier(umlsConnection_);
        sqlModifier_ = new GenericSQLModifier(sqlConnection_);

        messages_.info("Creating tables");
        sqlTableUtility_.createDefaultTables();
        if (enforceIntegrity) {
            messages_.info("Creating constraints");
            sqlTableUtility_.createDefaultTableConstraints();
        } else {
            messages_.info("Removing constraints");
            sqlTableUtility_.dropDefaultTableConstraints();
        }

        initLoadStatements();

    }

    /*
     * enter the mappings from MRCONSO TTY codes to the "nice names"
     */
    protected void initMRCONSOTTYMAP() {
        mrconsoRepresentationalMap_.put("FN", "Full Form");
        // TODO - add more of these - see
        // http://www.nlm.nih.gov/research/umls/metab3.html#sb3_0
    }
    
    protected void addToCodingScheme(String codingSchemeName, String registeredName, String representsVersion, String formalName, String language,
            int approxNumConcepts, Boolean isActive, int entryStateId, String releaseURI, String entityDescription, String copyright) throws SQLException{
        
        int i = 1;
        insertIntoCodingScheme.setString(i++, codingSchemeName);
        insertIntoCodingScheme.setString(i++, registeredName);
        insertIntoCodingScheme.setString(i++, representsVersion);
        insertIntoCodingScheme.setString(i++, formalName);
        insertIntoCodingScheme.setString(i++, language);
        insertIntoCodingScheme.setInt(i++, approxNumConcepts);
        DBUtility.setBooleanOnPreparedStatment(insertIntoCodingScheme, i++, isActive); // isActive
        insertIntoCodingScheme.setInt(i++, entryStateId);
        insertIntoCodingScheme.setString(i++, releaseURI); // releaseURI here
        insertIntoCodingScheme.setString(i++, entityDescription);
        insertIntoCodingScheme.setString(i++, copyright);

        insertIntoCodingScheme.executeUpdate();
        
        // we can close this prepared statement as it will be used again.
        insertIntoCodingScheme.close();
    }

    protected void addToEntityProperty(String codingSchemeName, String entityType, String conceptCode,
            String propertyId, String propertyType, String propertyName, String language, String presentationFormat,
            Boolean isPreferred, String degreeOfFidelity, Boolean matchIfNoContext, String representationForm,
            String propertyValue) throws SQLException {
//        int entryId = entryStateId_++;
//        try {
//            addEntryState(entryId, SQLTableConstants.ENTRY_STATE_TYPE_ENTITYPROPERTY, null, null, null, null, null, null,
//                    null, 0);
//        } catch (ObjectAlreadyExistsException e) {
//            // do nothing
//        } catch (InsertException e) {
//            // do nothing
//        }

        int k = 1;
        insertIntoEntityProperty.setString(k++, codingSchemeName);
        insertIntoEntityProperty.setString(k++, codingSchemeName); // entityCodeNamespace
                                                                   // here
        insertIntoEntityProperty.setString(k++, conceptCode);
        insertIntoEntityProperty.setString(k++, propertyId);
        insertIntoEntityProperty.setString(k++, propertyType);
        insertIntoEntityProperty.setString(k++, propertyName);
        insertIntoEntityProperty.setString(k++, language);
        insertIntoEntityProperty.setString(k++, presentationFormat);
        DBUtility.setBooleanOnPreparedStatment(insertIntoEntityProperty, k++, isPreferred);
        insertIntoEntityProperty.setString(k++, degreeOfFidelity);
        DBUtility.setBooleanOnPreparedStatment(insertIntoEntityProperty, k++, matchIfNoContext);
        insertIntoEntityProperty.setString(k++, representationForm);
        DBUtility.setBooleanOnPreparedStatment(insertIntoEntityProperty, k++, null);
        insertIntoEntityProperty.setInt(k++, 0);
        insertIntoEntityProperty.setString(k++, propertyValue == null ? " " : propertyValue);

        insertIntoEntityProperty.executeUpdate();
    }

    protected void addConceptToEntityPropertyMultiAttributes(String codingSchemeName, String entityType,
            String conceptCode, String propertyId, String typeName, String attributeValue, String val1, String val2)
            throws SQLException {
        int k = 1;
        insertIntoEntityPropertyMultiAttributes.setString(k++, codingSchemeName);
        insertIntoEntityPropertyMultiAttributes.setString(k++, codingSchemeName); // entityCodeNamespace
                                                                                  // here
        insertIntoEntityPropertyMultiAttributes.setString(k++, conceptCode);
        insertIntoEntityPropertyMultiAttributes.setString(k++, propertyId);
        insertIntoEntityPropertyMultiAttributes.setString(k++, typeName);
        insertIntoEntityPropertyMultiAttributes.setString(k++, null); // qualifierType
                                                                      // here
        insertIntoEntityPropertyMultiAttributes.setString(k++, attributeValue);
        insertIntoEntityPropertyMultiAttributes.setString(k++, StringUtils.isNotBlank(val1) ? val1 : " ");
        insertIntoEntityPropertyMultiAttributes.setString(k++, val2);

        insertIntoEntityPropertyMultiAttributes.executeUpdate();
    }

    protected void addConceptToConceptsMultiAttributes(String codingSchemeName, String conceptCode,
            String attributeName, String attributeValue) throws SQLException {

        insertIntoConceptsMultiAttributes.setString(1, codingSchemeName);
        insertIntoConceptsMultiAttributes.setString(2, conceptCode);
        insertIntoConceptsMultiAttributes.setString(3, attributeName);
        insertIntoConceptsMultiAttributes.setString(4, attributeValue);

        insertIntoConceptsMultiAttributes.executeUpdate();
    }

    protected void addAssociationToAssociations(String codingSchemeName, String relationName, String association,
            String forwardName, String reverseName, String inverse, Boolean isNavigable, Boolean isTransitive,
            Boolean isAntiTransitive, Boolean isSymmetric, Boolean isAntiSymmetric, Boolean isReflexive,
            Boolean isAntiReflexive, Boolean isFunctional, Boolean isReverseFunctional,
            Boolean isTranslationAssociation, String targetCodingScheme, String entityDescription) throws SQLException {
        int k = 1;
        insertIntoAssociations.setString(k++, codingSchemeName);
        insertIntoAssociations.setString(k++, relationName);
        insertIntoAssociations.setString(k++, codingSchemeName); // assume the Namespace is the same as the CodingScheme name
        insertIntoAssociations.setString(k++, association); // entityCode
        insertIntoAssociations.setString(k++, association); // associationName
        insertIntoAssociations.setString(k++, forwardName);
        insertIntoAssociations.setString(k++, reverseName);
        insertIntoAssociations.setString(k++, inverse);
        DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, isNavigable);
        DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, isTransitive);
        DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, isAntiTransitive);
        DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, isSymmetric);
        DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, isAntiSymmetric);
        DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, isReflexive);
        DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, isAntiReflexive);
        DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, isFunctional);
        DBUtility.setBooleanOnPreparedStatment(insertIntoAssociations, k++, isReverseFunctional);
        insertIntoAssociations.setInt(k++, 0);//entryStateId
        insertIntoAssociations.setString(k++, entityDescription);

        insertIntoAssociations.execute();
    }

    protected void addEntityAssociationToEntity(String codingSchemeName, String sourceCodingSchemeName,
            String sourceEntityType, String sourceConceptCode, String relationName, String association,
            String targetCodingSchemeName, String targetEntityType, String targetConceptCode,
            String multiAttributesKey, Boolean firstVersion, Boolean lastVersion) throws SQLException {
//        int entryId = entryStateId_++;
//        try {
//            addEntryState(entryId, SQLTableConstants.ENTRY_STATE_TYPE_ENTITYASSNSTOENTITY, null, null, null, null, null,
//                    null, null, 0);
//        } catch (ObjectAlreadyExistsException e) {
//            // do nothing
//        } catch (InsertException e) {
//            // do nothing
//        }

        int k = 1;
        insertIntoEntityAssociationsToEntity.setString(k++, codingSchemeName);
        insertIntoEntityAssociationsToEntity.setString(k++, relationName);
        insertIntoEntityAssociationsToEntity.setString(k++, codingSchemeName); // assume the CodingSchemeName is the same as the EnityCodeNamespace
        insertIntoEntityAssociationsToEntity.setString(k++, association); // entityCode
        insertIntoEntityAssociationsToEntity.setString(k++, sourceCodingSchemeName);
        insertIntoEntityAssociationsToEntity.setString(k++, sourceConceptCode);
        insertIntoEntityAssociationsToEntity.setString(k++, targetCodingSchemeName);
        insertIntoEntityAssociationsToEntity.setString(k++, targetConceptCode);
        
        //if the multiattributes key is null, generate one.
        if(StringUtils.isBlank(multiAttributesKey)){
            multiAttributesKey =  this.generateUniqueKey(1);
        }
        
        insertIntoEntityAssociationsToEntity.setString(k++, multiAttributesKey);
        insertIntoEntityAssociationsToEntity.setString(k++, null); // associationInstanceId
        DBUtility.setBooleanOnPreparedStatment(insertIntoEntityAssociationsToEntity, k++, null); // isDefining
        DBUtility.setBooleanOnPreparedStatment(insertIntoEntityAssociationsToEntity, k++, null); // isInferred
        DBUtility.setBooleanOnPreparedStatment(insertIntoEntityAssociationsToEntity, k++, null); // isActive
        insertIntoEntityAssociationsToEntity.setInt(k++, 0);

        insertIntoEntityAssociationsToEntity.execute();
    }

    protected void addEntityAssociationQualifierToEntityAssociation(String codingSchemeName, String multiAttributesKey,
            String qualifierName, String qualifierValue) throws SQLException {
        insertIntoEntityAssociationsToEntityQualifier.setString(1, codingSchemeName);
        insertIntoEntityAssociationsToEntityQualifier.setString(2, multiAttributesKey);
        insertIntoEntityAssociationsToEntityQualifier.setString(3, qualifierName);
        insertIntoEntityAssociationsToEntityQualifier.setString(4, qualifierValue);

        insertIntoEntityAssociationsToEntityQualifier.execute();
    }

    protected void addRelationToRelations(String codingSchemeName, String relationName, Boolean isNative,
            String entityDescription) throws SQLException {

        insertIntoRelations.setString(1, codingSchemeName);
        insertIntoRelations.setString(2, relationName);
        DBUtility.setBooleanOnPreparedStatment(insertIntoRelations, 3, isNative);
        insertIntoRelations.setString(4, entityDescription);

        insertIntoRelations.execute();
    }

    protected void addConceptToConcepts(String codingSchemeName, String conceptCode, Boolean firstRelease,
            Boolean modifiedInRelease, Boolean deprecated, Boolean isActive, Boolean isDefined, Boolean isInferred,
            String conceptStatus, Boolean isAnonymous, String entityDescription) throws SQLException {

        // add entryState details into entryState table
        int entryStateId = entryStateId_++;
        try {
            addEntryState(entryStateId, SQLTableConstants.ENTRY_STATE_TYPE_ENTITY, null, conceptStatus, null, null, null,
                    null, null, 0);
        } catch (SQLException e) {
            log.error("Problem inserting entryState for new code " + conceptCode, e);
            messages_.info("ERROR - Problem inserting entryState for new code " + conceptCode);
        } 

        int k = 1;
        insertIntoEntities.setString(k++, codingSchemeName);
        insertIntoEntities.setString(k++, codingSchemeName); // entityCodeNamespace
                                                             // here
        insertIntoEntities.setString(k++, conceptCode);
        DBUtility.setBooleanOnPreparedStatment(insertIntoEntities, k++, isDefined);
        DBUtility.setBooleanOnPreparedStatment(insertIntoEntities, k++, isAnonymous);
        DBUtility.setBooleanOnPreparedStatment(insertIntoEntities, k++, isActive);
        insertIntoEntities.setInt(k++, entryStateId);
        insertIntoEntities.setString(k++, entityDescription);

        insertIntoEntities.execute();

        // load the type of this entity into the entityType table
        try {
            addEntityType(codingSchemeName, codingSchemeName, conceptCode, SQLTableConstants.ENTITYTYPE_CONCEPT);
        } catch (SQLException e) {
            log.error("Problem inserting entityType for new code " + conceptCode, e);
            messages_.info("ERROR - Problem inserting entityType for new code " + conceptCode);
            return;
        }
    }

    protected void addEntityType(String codingSchemeName, String entityCodeNamespace, String entityCode,
            String entityType) throws SQLException {
        int k = 1;
        insertIntoEntityType.setString(k++, codingSchemeName);
        insertIntoEntityType.setString(k++, (entityCodeNamespace == null ? codingSchemeName : entityCodeNamespace));
        insertIntoEntityType.setString(k++, entityCode);
        insertIntoEntityType.setString(k++, (entityType == null ? SQLTableConstants.ENTITYTYPE_CONCEPT : entityType));

        insertIntoEntityType.execute();
    }

    protected void addEntryState(int entryStateId, String entryType, String owner, String status, String effectiveDate,
            String expirationDate, String revisionId, String prevRevisionId, String changeType, int relativeOrder)
            throws SQLException {
        // Insert only if there is any data.
        if (!StringUtils.isBlank(owner) || !StringUtils.isBlank(status) 
                || effectiveDate != null || expirationDate != null
                || !StringUtils.isBlank(revisionId) || !StringUtils.isBlank(prevRevisionId)
                || !StringUtils.isBlank(changeType))
        {
            int k = 1;
            insertIntoEntryState.setInt(k++, entryStateId);
            insertIntoEntryState.setString(k++, entryType);
            insertIntoEntryState.setString(k++, owner);
            insertIntoEntryState.setString(k++, status);
            insertIntoEntryState.setTimestamp(k++, null);
            insertIntoEntryState.setTimestamp(k++, null);
            insertIntoEntryState.setString(k++, revisionId);
            insertIntoEntryState.setString(k++, prevRevisionId);
            insertIntoEntryState.setString(k++, (changeType != null ? changeType : " "));
            insertIntoEntryState.setInt(k++, relativeOrder);
            
            if(sqlModifier_.getDatabaseType().equals("ACCESS")){
                insertIntoEntryState.setString(k++, null);
            } else {
                insertIntoEntryState.setObject(k++, null, java.sql.Types.BIGINT);
            }
            
            insertIntoEntryState.execute();
        }
    }

    public static Hashtable getIsoMap() throws Exception {
        String fileName = "/UMLS_SAB_ISO_Map.txt";
        Hashtable result = new Hashtable();
        BufferedReader in;
        int lineNo = 1;

        in = new BufferedReader(new InputStreamReader(UMLSBaseCode.class.getResourceAsStream(fileName)));
        String line = in.readLine();
        while (line != null) {
            if (!line.startsWith("#") && line.length() > 0) {
                String[] foo = line.split("=");
                if (foo.length == 2) {
                    result.put(foo[0], foo[1]);
                } else {
                    throw new Exception("Invalid format on line " + lineNo);
                }
            }
            line = in.readLine();
            lineNo++;
        }
        in.close();
        return result;
    }

    /*
     * enter in the mappings of coding schemes to iso codes
     */
    protected void initIsoMap() {
        messages_.info("Loading the SAB -> URN map");

        // expects a file with one entry per line
        // each line looks like:
        // SAB=urn:oid:x.x.x.x
        try {
            isoMap = getIsoMap();
        } catch (Exception e) {
            messages_.info("Problem trying to read the file containing the SAB -> ISO map");
            log.error("Problem trying to read the file containing the SAB -> ISO map", e);
        }
    }

    protected void updateApproxNumberOfConcepts(int count, String codingSchemeName) {
        try {
            messages_.info("Updating concept count");
            PreparedStatement temp = sqlConnection_.prepareStatement("UPDATE "
                    + stc_.getTableName(SQLTableConstants.CODING_SCHEME) + " SET "
                    + SQLTableConstants.TBLCOL_APPROXNUMCONCEPTS + " = ? " + " WHERE "
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ?");
            temp.setInt(1, count);
            temp.setString(2, codingSchemeName);
            temp.executeUpdate();
            temp.close();
        } catch (SQLException e) {
            messages_.error("Problem updating the concept count", e);
            log.error("Problem updating the concept count", e);
        }
    }

    /*
     * make sure it doesn't return null, in case it is missing. This maps either
     * RSABs or VSABS to the iso code.
     */
    protected String getISOString(String UMLSCodingSchemeName) throws SQLException {
        String temp = (String) isoMap.get(UMLSCodingSchemeName);
        if (temp == null) {
            // maybe it was a VSAB instead of an RSAB.
            String RSAB = mapVSABtoRSAB(UMLSCodingSchemeName);
            temp = (String) isoMap.get(RSAB);
            if (temp == null) {
                log.error("Could not get the proper URN for " + UMLSCodingSchemeName);
                messages_.info("Could not get the proper URN for " + UMLSCodingSchemeName);
                temp = "Unknown-" + Math.random();
            }
        }
        return temp;
    }

    protected String mapVSABtoRSAB(String vsab) throws SQLException {
        PreparedStatement getRSAB = umlsConnection_.prepareStatement("SELECT RSAB FROM MRSAB WHERE VSAB = ?");

        getRSAB.setString(1, vsab);

        ResultSet results = getRSAB.executeQuery();
        String result = "";
        if (results.next()) {
            result = results.getString("RSAB");
        }

        results.close();
        getRSAB.close();
        return result;
    }

    /*
     * populate the properties attributes of the
     * codingSchemeSuppportedAttributes table
     */
    protected void loadSupportedPropertyLinks(String codingSchemeName) throws SQLException {
        messages_.info("loading the supported property links");
        String[] temp = (String[]) supportedPropertyLinks_.toArray(new String[supportedPropertyLinks_.size()]);
        for (int i = 0; i < temp.length; i++) {
            insertIntoCodingSchemeSupportedAttributes(codingSchemeName,
                    SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYLINK, temp[i], null, null, null, null);
        }
        messages_.info("loaded " + temp.length + "  supported property links");
    }

    /*
     * populate the properties attributes of the
     * codingSchemeSuppportedAttributes table
     */
    protected void loadSupportedProperties(String codingSchemeName) throws SQLException {
        messages_.info("loading the supported properties");
        String[] temp = (String[]) supportedPropertyTypes_.toArray(new String[supportedPropertyTypes_.size()]);
        for (int i = 0; i < temp.length; i++) {
            insertIntoCodingSchemeSupportedAttributes(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTY,
                    temp[i], null, null, null, null);
        }
        messages_.info("loaded " + temp.length + " properties");
    }

    /*
     * populate the properties attributes of the
     * codingSchemeSuppportedAttributes table
     */
    protected void loadSupportedPropertyQualifiers(String codingSchemeName) throws SQLException {
        messages_.info("loading the supported property qualifiers");
        String[] temp = (String[]) supportedPropertyQualifiers_
                .toArray(new String[supportedPropertyQualifiers_.size()]);
        for (int i = 0; i < temp.length; i++) {
            insertIntoCodingSchemeSupportedAttributes(codingSchemeName,
                    SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIER, temp[i], null, null, null, null);
        }
        messages_.info("loaded " + temp.length + " property qualifiers");
    }

    /*
     * populate the properties attributes of the
     * codingSchemeSuppportedAttributes table
     */
    protected void loadSupportedAssociationQualifiers(String codingSchemeName) throws SQLException {
        messages_.info("loading the supported association qualifiers.");
        String[] temp = (String[]) supportedAssociationQualifiers_.toArray(new String[supportedAssociationQualifiers_
                .size()]);
        for (int i = 0; i < temp.length; i++) {
            insertIntoCodingSchemeSupportedAttributes(codingSchemeName,
                    SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATIONQUALIFIER, temp[i], null, null, null, null);
        }
        messages_.info("loaded " + temp.length + " association qualifiers");
    }

    /*
     * populate the properties attributes of the
     * codingSchemeSuppportedAttributes table
     */
    protected void loadSupportedSources(String codingSchemeName) throws SQLException {
        messages_.info("loading the supported sources");
        String[] temp = (String[]) supportedSources_.toArray(new String[supportedSources_.size()]);
        for (int i = 0; i < temp.length; i++) {
            insertIntoCodingSchemeSupportedAttributes(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE,
                    temp[i], getISOString(temp[i]), null, null, null);

        }
        messages_.info("loaded " + temp.length + " sources");
    }

    /*
     * populate the properties attributes of the
     * codingSchemeSuppportedAttributes table
     */
    protected void loadSupportedLanguages(String codingSchemeName) throws SQLException {
        messages_.info("loading the supported languages");
        String[] temp = (String[]) supportedLanguages_.toArray(new String[supportedLanguages_.size()]);
        for (int i = 0; i < temp.length; i++) {
            // TODO figure out how to get the URN's and put them in here
            insertIntoCodingSchemeSupportedAttributes(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_LANGUAGE,
                    temp[i], null, null, null, null);

        }
        messages_.info("loaded " + temp.length + " languages");
    }

    protected void insertIntoConceptPropertyLinks(String codingSchemeName, String entityType, String conceptCode,
            String sourcePropertyId, String link, String targetPropertyId) throws SQLException {
        insertIntoEntityPropertyLinks.setString(1, codingSchemeName);
        insertIntoEntityPropertyLinks.setString(2, codingSchemeName); // entityCodeNamespace
        insertIntoEntityPropertyLinks.setString(3, conceptCode);
        insertIntoEntityPropertyLinks.setString(4, sourcePropertyId);
        insertIntoEntityPropertyLinks.setString(5, link);
        insertIntoEntityPropertyLinks.setString(6, targetPropertyId);

        insertIntoEntityPropertyLinks.executeUpdate();
    }

    /*
     * Method to do the insertion into the table
     */
    protected void insertIntoCodingSchemeSupportedAttributes(String codingSchemeName, String supportedAttributeTag,
            String id, String urn, String value, String assemblyRule, String agentRole) throws SQLException {
        insertIntoCodingSchemeSupportedAttributes.setString(1, codingSchemeName);
        insertIntoCodingSchemeSupportedAttributes.setString(2, supportedAttributeTag);
        insertIntoCodingSchemeSupportedAttributes.setString(3, id);
        insertIntoCodingSchemeSupportedAttributes.setString(4, urn);
        // If these values are null, turn them into whitespace. This is because
        // they are part of
        // the primary key in the database and they cannot be null.
        insertIntoCodingSchemeSupportedAttributes.setString(5, StringUtils.isNotEmpty(value) ? value : " ");
        insertIntoCodingSchemeSupportedAttributes.setString(6, StringUtils.isNotEmpty(assemblyRule) ? assemblyRule
                : " ");
        insertIntoCodingSchemeSupportedAttributes.setString(7, StringUtils.isNotEmpty(agentRole) ? agentRole : " ");

        insertIntoCodingSchemeSupportedAttributes.executeUpdate();
    }

    /*
     * Create the statements that insert into the tables
     */
    protected void initLoadStatements() throws SQLException {
        insertIntoCodingScheme = sqlConnection_.prepareStatement(stc_
                .getInsertStatementSQL(SQLTableConstants.CODING_SCHEME));

        insertIntoCodingSchemeSupportedAttributes = sqlConnection_.prepareStatement(stc_
                .getInsertStatementSQL(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES));

        insertIntoCodingSchemeMultiAttributes = sqlConnection_.prepareStatement(stc_
                .getInsertStatementSQL(SQLTableConstants.CODING_SCHEME_MULTI_ATTRIBUTES));

        insertIntoEntities = sqlConnection_.prepareStatement(stc_.getInsertStatementSQL(SQLTableConstants.ENTITY));

        insertIntoEntryState = sqlConnection_.prepareStatement(stc_
                .getInsertStatementSQL(SQLTableConstants.ENTRY_STATE));

        insertIntoEntityType = sqlConnection_.prepareStatement(stc_
                .getInsertStatementSQL(SQLTableConstants.ENTITY_TYPE));

        insertIntoEntityProperty = sqlConnection_.prepareStatement(stc_
                .getInsertStatementSQL(SQLTableConstants.ENTITY_PROPERTY));

        insertIntoEntityPropertyMultiAttributes = sqlConnection_.prepareStatement(stc_
                .getInsertStatementSQL(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES));

        insertIntoRelations = sqlConnection_.prepareStatement(stc_.getInsertStatementSQL(SQLTableConstants.RELATION));

        insertIntoAssociations = sqlConnection_.prepareStatement(stc_
                .getInsertStatementSQL(SQLTableConstants.ASSOCIATION));

        insertIntoEntityAssociationsToEntity = sqlConnection_.prepareStatement(stc_
                .getInsertStatementSQL(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY));

        insertIntoEntityAssociationsToEntityQualifier = sqlConnection_.prepareStatement(stc_
                .getInsertStatementSQL(SQLTableConstants.ENTITY_ASSOCIATION_TO_E_QUALS));

        getCodeForCUI_ = umlsConnection_.prepareStatement("SELECT CODE, SAB FROM MRCONSO WHERE CUI = ? AND AUI = ?");

        getCodeForCUINoAUI_ = umlsConnection_
                .prepareStatement("SELECT Distinct CODE, SAB FROM MRCONSO WHERE CUI = ? AND SAB Like ?");

        getSource_ = umlsConnection_.prepareStatement("SELECT EXPL FROM MRDOC WHERE DOCKEY = ? AND VALUE = ?");

        getUMLSRoot_ = umlsConnection_
                .prepareStatement("SELECT CUI, CODE, SAB FROM MRCONSO WHERE CUI IN (SELECT CUI FROM MRCONSO WHERE SAB = 'SRC' AND TTY = 'RHT')");

        getAssocInstance_ = sqlConnection_.prepareStatement(new StringBuffer(256).append("SELECT * FROM ").append(
                stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY)).append(
                " WHERE " + stc_.codingSchemeNameOrId + " = ? AND " + stc_.containerNameOrContainerDC + " = ? AND "
                        + stc_.entityCodeOrAssociationId + " = ?").append(
                " AND " + stc_.sourceCSIdOrEntityCodeNS + " = ? AND " + stc_.sourceEntityCodeOrId + " = ?").append(
                " AND " + stc_.targetCSIdOrEntityCodeNS + " = ? AND " + stc_.targetEntityCodeOrId + " = ?").toString());

        getAssocQualifier_ = sqlConnection_.prepareStatement(new StringBuffer(256).append("SELECT * FROM ").append(
                stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_E_QUALS)).append(
                " WHERE " + stc_.codingSchemeNameOrId + " = ? AND " + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY
                        + " = ? AND " + SQLTableConstants.TBLCOL_QUALIFIERNAME + " = ? AND "
                        + SQLTableConstants.TBLCOL_QUALIFIERVALUE + " = ?").toString());
        // This statement get's the association qualifiers defined in the MRSAT
        // source by the RUI designation
        // It's not the only place where association qualifiers are defined.
        getAllAssocQualifiersFromRRF_ = umlsConnection_
                .prepareStatement("SELECT distinct(METAUI) FROM MRSAT WHERE STYPE = ?");
        // Preset the STYPE to RUI in the query
        getAllAssocQualifiersFromRRF_.setString(1, "RUI");

        getAssocQualifierFromRRF_ = umlsConnection_.prepareStatement("SELECT ATN,ATV FROM MRSAT WHERE METAUI = ?");
        getAUIFromPresentation_ = sqlConnection_.prepareStatement("SELECT AUI FROM MRDEF WHERE CUI = ? AND DEF = ?");

        insertIntoEntityPropertyLinks = sqlConnection_.prepareStatement(stc_
                .getInsertStatementSQL(SQLTableConstants.ENTITY_PROPERTY_LINKS));

        getPresentationRank_ = umlsConnection_.prepareStatement("SELECT RANK, SAB, TTY FROM MRRANK");       

        getPropIds_ = sqlConnection_.prepareStatement(new StringBuffer(256).append("Select " + SQLTableConstants.TBLCOL_PROPERTYID + " from ").append(
                stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES)).append(
                        " where " + stc_.codingSchemeNameOrId + " = ? AND "
                                + stc_.entityCodeOrEntityId + " = ? AND " + SQLTableConstants.TBLCOL_TYPENAME + " = '"
                                + SQLTableConstants.TBLCOLVAL_QUALIFIER + "' AND " + SQLTableConstants.TBLCOL_ATTRIBUTEVALUE
                                + " = '" + "AUI" + "' AND " + SQLTableConstants.TBLCOL_VAL1 + " = ?").toString());
    }

    /**
     * Add context qualifiers to text presentations for matching concepts.
     * 
     * @param code
     *            The code of the concept to qualify.
     * @param aui
     *            The AUI identifying specific context text to be matched.
     * @param codingSchemeName
     *            The coding scheme container the code to be qualified.
     * @param qualifierName
     *            The qualifier name (e.g. 'HCD').
     * @param qualifierValue
     *            The qualifier value.
     * @throws SQLException
     */
    protected void qualifyConceptPresentation(String code, String aui, String codingSchemeName, String qualifierName,
            String qualifierValue) throws SQLException {
        // /////////////////////////////////////////////////////////////////////
        // Note: Separate the RRF and LexGrid queries into separate SQL since
        // content for each may be stored in separate databases (multi-db mode).
        // Syntax (and ability) to query across databases varies by database
        // engine.
        // /////////////////////////////////////////////////////////////////////

        // Locate matching root entry in MRCONSO ...
        PreparedStatement getMRCONSO = umlsConnection2_
                .prepareStatement("SELECT CODE, STR from MRCONSO WHERE MRCONSO.CODE = ? AND MRCONSO.AUI = ?");
        getMRCONSO.setString(1, code);
        getMRCONSO.setString(2, aui);
        try {
            // For each, find matching concept properties (text presentations)
            // and add
            // a property qualifier. Note: multiple qualifiers can exist per
            // term.
            // Note: Oracle was pretty finicky about how we defined the schema
            // to allow
            // for very large property values. Unfortunately it also does not
            // allow
            // query over these values (stored as CLOBS) without use of the
            // DBMS_LOB
            // package functions, and there it appeared we could not do better
            // than a
            // 'startsWith' match. For now, we strike a compromise by allowing
            // the
            // query to be narrower for non-Oracle, but supporting Oracle by
            // iterating
            // through the returned presentations to find a match.
            boolean narrowQuery = !sqlModifier_.getDatabaseType().equals("Oracle");
            StringBuffer stmt = new StringBuffer("SELECT " + SQLTableConstants.TBLCOL_PROPERTYID + ", "
                    + SQLTableConstants.TBLCOL_PROPERTYVALUE + " from ").append(
                    stc_.getTableName(SQLTableConstants.ENTITY_PROPERTY)).append(
                    " WHERE " + stc_.codingSchemeNameOrId + " = '" + codingSchemeName + "' AND "
                            + stc_.entityCodeOrEntityId + " = ? AND " + SQLTableConstants.TBLCOL_PROPERTYTYPE
                            + " = '" + SQLTableConstants.TBLCOLVAL_PRESENTATION + "'");
            if (narrowQuery)
                stmt.append(" AND " + SQLTableConstants.TBLCOL_PROPERTYVALUE + " = ? ");

            PreparedStatement getPropIds = sqlConnection_.prepareStatement(stmt.toString());

            ResultSet mrconso_results = getMRCONSO.executeQuery();
            try {
                while (mrconso_results.next()) {
                    getPropIds.setString(1, mrconso_results.getString(1));
                    String textToMatch = mrconso_results.getString(2);
                    if (narrowQuery)
                        getPropIds.setString(2, textToMatch);

                    ResultSet propId_results = getPropIds.executeQuery();
                    try {
                        while (propId_results.next()) {
                            if (narrowQuery || textToMatch.equals(propId_results.getString(2))) {
                                addConceptToEntityPropertyMultiAttributes(codingSchemeName,
                                        SQLTableConstants.ENTITYTYPE_CONCEPT, code, propId_results.getString(1),
                                        SQLTableConstants.TBLCOLVAL_QUALIFIER, qualifierName, qualifierValue, "");
                                break;
                            }
                        }
                    } catch (SQLException e) {
                        // Warn and continue ...
                        messages_.warn("Unable to register context for code: " + code + " aui: " + aui, e);
                    } finally {
                        propId_results.close();
                    }
                }
            } finally {
                getPropIds.close();
            }
        } finally {
            getMRCONSO.close();
        }
    }

    /**
     * Adds qualification to concepts and associations in the LexGrid
     * repository.
     * 
     * @param aq
     *            Qualification information from the UMLS source.
     * @param constructHCD
     *            Indicates whether artificial context values should be
     *            constructed if not provided in the UMLS information.
     * @param rela
     *            The relationship attribute defined by UMLS (can be empty or
     *            null).
     * @param totalCount
     *            The total number of context links qualified previously.
     * @return The number of contextual links qualified in the repository for
     *         the given UMLS info.
     * @throws SQLException
     */
    protected int loadContext(AssociationQualification aq, boolean constructHCD, String rela, int totalCount)
            throws SQLException {
        // If a context identifier was assigned, use it.
        // If a context identifier is not assigned and the option to construct
        // is enabled,
        // derive one based on the root concept code and path to root AUI
        // values.
        int contextLinks = 0;
        String hcd = aq.qualifierValue;
        if (constructHCD && StringUtils.isBlank(hcd) && StringUtils.isNotBlank(aq.pathToRoot)
                && StringUtils.isNotBlank(aq.sourceConceptCode)) {
            MessageDigest md = getSHA1();
            md.reset();
            md.update(aq.pathToRoot.getBytes());
            hcd = String.valueOf(md.digest(aq.sourceConceptCode.getBytes()));
        }
        if (StringUtils.isBlank(hcd))
            return 0;

        // Iterate through the path to root and determine the codes for
        // participating AUIs. We maintain a LRU cache of AUIs to codes to
        // assist.
        // If the associated code is not in the cache, find and cache it here.
        ListOrderedMap orderedPtrAUIToCode = new ListOrderedMap();

        // Break up the path to root into AUIs ...
        String[] auis = aq.pathToRoot.split("\\.");
        if (auis.length > 0) {
            // Check the cache for each. If not found, perform and cache the
            // AUI to code mapping.
            PreparedStatement getPTRCode = umlsConnection2_.prepareStatement("SELECT CODE FROM MRCONSO WHERE AUI = ?");
            try {
                String nextCode, nextAUI;
                for (int i = 0; i < auis.length; i++) {
                    // Check for registered code in the cache.
                    nextAUI = auis[i];
                    nextCode = (String) auiToCodeCache_.get(nextAUI);

                    // If not cached, perform lookup ...
                    if (nextCode == null) {
                        getPTRCode.setString(1, nextAUI);
                        ResultSet ptrCodes = getPTRCode.executeQuery();
                        int count = 0;
                        try {
                            while (ptrCodes.next()) {
                                count++;
                                nextCode = ptrCodes.getString(1);
                            }
                        } finally {
                            ptrCodes.close();
                        }
                        // If one to one mapping (probably should always be, but
                        // doesn't
                        // hurt to check), add to the cache for quick lookup
                        // later...
                        if (count == 1)
                            auiToCodeCache_.put(nextAUI, nextCode);
                    }

                    // Was it resolved?
                    if (nextCode != null)
                        orderedPtrAUIToCode.put(nextAUI, nextCode);
                }
            } finally {
                getPTRCode.close();
            }
        }
        // Ensure we have included the original AUI to code mapping from the
        // provided UMLS qualification info; inserted last as the root
        // of the path.
        orderedPtrAUIToCode.put(aq.sourceConceptAUI, aq.sourceConceptCode);

        // /////////////////////////////////////////////////////////////////////
        // We have all the participating codes and AUIs.
        // Add context qualifiers to the text presentation of each concept
        // based on code/AUI pairs.
        // /////////////////////////////////////////////////////////////////////
        for (OrderedMapIterator omi = orderedPtrAUIToCode.orderedMapIterator(); omi.hasNext();) {
            omi.next();
            String aui = (String) omi.getKey();
            String code = (String) omi.getValue();
            if (code != null)
                qualifyConceptPresentation(code, aui, aq.codingSchemeName, aq.qualifierName, hcd);
        }

        // /////////////////////////////////////////////////////////////////////
        // At this point we have taken care of all the concept qualifiers.
        // Now find and similarly tag each participating association link
        // between AUIs in the path to root chain.
        // /////////////////////////////////////////////////////////////////////

        // Statements to find LexGrid association to concept mappings.
        // Check source to target (parent association as processed)
        // or target to source (child association as processed).

        // Honor the association specified in the MRHIER entry, if provided.
        // For example, the UMLS 'inverse_isa' is mapped on load to 'hasSubtype'
        // association name; account for that here.
        String assoc = mapRela(rela);

        // If a specific relation attribute (rela) was not provided, consider
        // all relevant
        // hierarchical associations (including UMLS standard or source-specific
        // names).
        String assocParam = StringUtils.isNotBlank(assoc) ? '\'' + assoc + '\''
                : toCommaDelimitedWithQuotes(getHierAssocNames(aq.codingSchemeName));

        // Create statements to navigate both directions (up & down the
        // contextual chain).
        PreparedStatement getRelationship_1 = sqlConnection_.prepareStatement(new StringBuffer("SELECT "
                + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + ", " + stc_.targetEntityCodeOrId + ", "
                + stc_.entityCodeOrAssociationId + ", " + stc_.sourceEntityCodeOrId + " FROM ").append(
                stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY)).append(
                " WHERE " + stc_.sourceEntityCodeOrId + " = ? AND " + stc_.targetEntityCodeOrId + " = ? AND").append(
                " " + stc_.codingSchemeNameOrId + " = ? AND " + stc_.entityCodeOrAssociationId + " IN (").append(
                assocParam).append(")").toString());

        PreparedStatement getRelationship_2 = sqlConnection_.prepareStatement(new StringBuffer("SELECT "
                + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + ", " + stc_.targetEntityCodeOrId + ", "
                + stc_.entityCodeOrAssociationId + ", " + stc_.sourceEntityCodeOrId + " FROM ").append(
                stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY)).append(
                " WHERE " + stc_.targetEntityCodeOrId + " = ? AND " + stc_.sourceEntityCodeOrId + " = ? AND").append(
                " " + stc_.codingSchemeNameOrId + " = ? AND " + stc_.entityCodeOrAssociationId + " IN (").append(
                assocParam).append(")").toString());

        // Statement to update a multi-attributes key for an association
        // mapping.
        PreparedStatement updateMAK = sqlConnection_.prepareStatement(new StringBuffer("UPDATE ").append(
                stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY)).append(
                " SET " + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + " = ? " + " WHERE " + stc_.codingSchemeNameOrId
                        + " = ?").append(
                " AND " + stc_.sourceEntityCodeOrId + " = ? AND " + stc_.targetEntityCodeOrId + " = ?").append(
                " AND " + stc_.entityCodeOrAssociationId + " = ?").toString());

        // Locate and qualify each affected association link with the context ID
        // ...
        try {
            PreparedStatement[] stmts = new PreparedStatement[] { getRelationship_1, getRelationship_2 };
            for (int s = 0; s < stmts.length; s++) {
                PreparedStatement stmt = stmts[s];
                for (int i = orderedPtrAUIToCode.size() - 1; i > 0; i--) {
                    String code = (String) orderedPtrAUIToCode.getValue(i);
                    String codePrev = (String) orderedPtrAUIToCode.getValue(i - 1);
                    stmt.setString(1, code);
                    stmt.setString(2, codePrev);
                    stmt.setString(3, aq.codingSchemeName);

                    ResultSet results = stmt.executeQuery();
                    try {
                        // Iterate through all relevant association links ...
                        while (results.next()) {
                            String multiAttributesKey = results.getString(SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY);
                            String targetConceptCode = results.getString(stc_.targetEntityCodeOrId);
                            String sourceConceptCode = results.getString(stc_.sourceEntityCodeOrId);
                            String association = results.getString(stc_.entityCodeOrAssociationId);

                            // If there is no key to correlate to the
                            // multi-attributes table,
                            // construct and add now.
                            if (multiAttributesKey == null) {
                                StringBuffer key = new StringBuffer().append(System.currentTimeMillis()).append(
                                        (int) Math.floor((Math.random() * 100000))).append(totalCount);
                                multiAttributesKey = key.substring(0, Math.min(50, key.length()));
                                updateMAK.setString(1, multiAttributesKey);
                                updateMAK.setString(2, aq.codingSchemeName);
                                updateMAK.setString(3, sourceConceptCode);
                                updateMAK.setString(4, targetConceptCode);
                                updateMAK.setString(5, association);
                                updateMAK.execute();
                            }

                            // Add a context qualifier to the multi-attributes
                            // table.
                            try {
                                addEntityAssociationQualifierToEntityAssociation(aq.codingSchemeName,
                                        multiAttributesKey, aq.qualifierName, hcd);
                                contextLinks++;
                            } catch (SQLException e) {
                                // Because we qualify all relationships along
                                // the PTR and
                                // the HCD is identical for siblings at the same
                                // PTR some
                                // exceptions with regards to identical keys
                                // will come up.

                                // We try to bypass altogether if the message
                                // indicates duplication.
                                // However, message text can vary based on the
                                // database engine.
                                // Rather than exit in error, log the message
                                // and continue.
                                if (!e.getMessage().contains("Duplicate")) {
                                    messages_.warn("Unable to add context qualifier to association.", e);
                                }
                            }
                        }
                    } finally {
                        results.close();
                    }
                }
            }
        } finally {
            updateMAK.close();
            getRelationship_1.close();
            getRelationship_2.close();
        }
        return contextLinks;
    }

    protected CodeHolder[] mapCUIToCode(String cui, String aui, String sab) throws SQLException {
        if (aui == null || aui.length() == 0) {
            return mapCUIToCodeNoAUI(cui, sab);
        } else {
            return mapCUIToCodeWithAUI(cui, aui);
        }
    }

    private CodeHolder[] mapCUIToCodeNoAUI(String cui, String sab) throws SQLException {
        CodeHolder[] resultToReturn = (CodeHolder[]) cuiToCodeCache_.get(cui + ":null:" + sab);
        if (resultToReturn == null) {
            PreparedStatement statement = null;

            statement = getCodeForCUINoAUI_;
            statement.setString(1, cui);

            if (sab.toLowerCase().equals("rxnorm")) {
                // if we are loading rxnorm, restrict matches to mrconso rows
                // that are from rxnorm.
                statement.setString(2, sab);
            } else {
                // not rxnorm, match from every sab.
                statement.setString(2, "%");
            }

            ArrayList temp = new ArrayList();
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                CodeHolder tempResult = new CodeHolder();
                tempResult.code = results.getString("CODE");
                // there are some concepts that just have "NOCODE" as the
                // code... need to make them unique.
                if (tempResult.code.equals("NOCODE")) {
                    tempResult.code = tempResult.code + "-" + cui;
                }
                tempResult.codingScheme = mapSABToCodeSystemName(results.getString("SAB"));
                temp.add(tempResult);
            }

            results.close();

            if (temp.size() == 0) {
                log.warn("Unable to map CUI to CODE - " + cui + " (no aui) sab - " + sab);
            }

            resultToReturn = (CodeHolder[]) temp.toArray(new CodeHolder[temp.size()]);

            cuiToCodeCache_.put(cui + ":null:" + sab, resultToReturn);
        }

        return resultToReturn;
    }

    protected CodeHolder[] mapCUIToCodeWithAUI(String cui, String aui) throws SQLException {
        CodeHolder result = (CodeHolder) cuiToCodeCache_.get(cui + ":" + aui);
        if (result == null) {
            PreparedStatement statement = null;

            statement = getCodeForCUI_;
            statement.setString(1, cui);
            statement.setString(2, aui);

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                result = new CodeHolder();
                result.code = results.getString("CODE");
                // there are some concepts that just have "NOCODE" as the
                // code... need to make them unique.
                if (result.code.equals("NOCODE")) {
                    result.code = result.code + "-" + cui;
                }
                result.codingScheme = mapSABToCodeSystemName(results.getString("SAB"));

                cuiToCodeCache_.put(cui + ":" + aui, result);
            } else {
                log.warn("Unable to map CUI to CODE - " + cui + " (aui) - " + aui);
            }
            if (results.next()) {
                log.error("INVALID ASSUMPTION - CUI-AUI is not unique");
                log.error("cui " + cui + " aui " + aui);
            }
            results.close();
        }
        return result != null ? new CodeHolder[] { result } : null;
    }

    protected String getSourceVersionString(String fallbackString) {
        String result = fallbackString;
        try {
            getSource_.setString(1, "RELEASE");
            getSource_.setString(2, "umls.release.name");
            ResultSet results = getSource_.executeQuery();

            if (results.next()) {
                result = results.getString("EXPL");
            }
            results.close();
        } catch (SQLException e) {
            // Older versions of the umls did not have the version number in a
            // table.
        }

        return result;
    }

    protected void closeLoadStatements() throws SQLException {
        if (insertIntoCodingScheme != null) {
            insertIntoCodingScheme.close();
        }
        if (insertIntoCodingSchemeMultiAttributes != null) {
            insertIntoCodingSchemeMultiAttributes.close();
        }
        if (insertIntoCodingSchemeSupportedAttributes != null) {
            insertIntoCodingSchemeSupportedAttributes.close();
        }
        if (insertIntoEntities != null) {
            insertIntoEntities.close();
        }
        if (insertIntoEntryState != null) {
            insertIntoEntryState.close();
        }
        if (insertIntoEntityType != null) {
            insertIntoEntityType.close();
        }
        if (insertIntoConceptsMultiAttributes != null) {
            insertIntoConceptsMultiAttributes.close();
        }
        if (insertIntoAssociations != null) {
            insertIntoAssociations.close();
        }
        if (insertIntoEntityAssociationsToEntity != null) {
            insertIntoEntityAssociationsToEntity.close();
        }
        if (insertIntoEntityAssociationsToEntityQualifier != null) {
            insertIntoEntityAssociationsToEntityQualifier.close();
        }
        if (insertIntoEntityProperty != null) {
            insertIntoEntityProperty.close();
        }
        if (insertIntoEntityPropertyMultiAttributes != null) {
            insertIntoEntityPropertyMultiAttributes.close();
        }
        if (insertIntoRelations != null) {
            insertIntoRelations.close();
        }
        if (getCodeForCUI_ != null) {
            getCodeForCUI_.close();
        }
        if (getCodeForCUINoAUI_ != null) {
            getCodeForCUINoAUI_.close();
        }
        if (getSource_ != null) {
            getSource_.close();
        }
        if (getUMLSRoot_ != null) {
            getUMLSRoot_.close();
        }
        if (getAssocInstance_ != null) {
            getAssocInstance_.close();
        }
        if (getAssocQualifier_ != null) {
            getAssocQualifier_.close();
        }
        if (insertIntoEntityPropertyLinks != null) {
            insertIntoEntityPropertyLinks.close();
        }
        if (getAUIFromPresentation_ != null) {
            getAUIFromPresentation_.close();
        }
        if (getAllAssocQualifiersFromRRF_ != null) {
            getAllAssocQualifiersFromRRF_.close();
        }
        if (getAssocQualifierFromRRF_ != null) {
            getAssocQualifierFromRRF_.close();
        }
        if (getPresentationRank_ != null) {
            getPresentationRank_.close();
        }
        if (getPropIds_ != null) {
            getPropIds_.close();
        }
    }

    /*
     * close the sql connections
     */
    protected void closeConnections() throws SQLException {
        closeLoadStatements();
        umlsConnection_.close();
        umlsConnection2_.close();
        sqlConnection_.close();
    }

    // //////////////////////////////////////////////////////////////
    // Code for tracking hierarchical associations used in each
    // container, and according to the direction of navigation from
    // parent to child...
    // //////////////////////////////////////////////////////////////

    /**
     * Inner class defining a string optionally qualified by SAB and providing
     * corresponding equality methods, etc, to support use and comparison in
     * various collections.
     * <p>
     * If the given string is intended to match any SAB, the sab-qualifier is
     * null.
     */
    public class SABString implements Comparable {
        public String str = null;
        public String sab = null;
        private int hash = 0;

        public SABString(String str, String sab) {
            super();
            if (str == null)
                throw new IllegalArgumentException("string cannot be null");
            this.str = str;
            this.sab = sab;
        }

        public int compareTo(Object o) {
            if (!(o instanceof SABString))
                return -1;
            int i = 0;
            if (str != null && ((i = str.compareTo(((SABString) o).str)) != 0))
                return i;
            if (sab != null && ((i = sab.compareTo(((SABString) o).sab)) != 0))
                return i;
            return 0;
        }

        public boolean equals(Object obj) {
            return obj != null && getClass().equals(obj.getClass()) && hashCode() == obj.hashCode();
        }

        public int hashCode() {
            if (hash == 0) {
                hash = 31 + str.hashCode();
                if (sab != null)
                    hash = hash * 31 + sab.hashCode();
            }
            return hash;
        }

        public String toString() {
            return sab == null ? str : (sab + ':' + str);
        }
    }

    private SABString[] hierRelas_ = null;
    private SABString[] hierRels_ = null;
    protected Map schemeToHierNames_ = new HashMap();

    /**
     * Build relations to an artificial root ('@') or tail ('@@') node to assist
     * with navigation of one or more associations. If building a root node,
     * nodes acting as source but not as target are connected to the '@' node.
     * If building a tail node, nodes acting as target but not source are
     * connected to the '@@' node. Therefore, '@' becomes the ultimate source
     * node and '@@' the ultimate target node for navigation.
     * 
     * @param codingSchemeName
     *            The LexGrid code system name being loaded.
     * @param isMeta
     *            True if the LexGrid code system is to represent multiple UMLS
     *            ontologies, acting as a single meta-ontology; false if the
     *            code system represents a single UMLS ontology. If true,
     *            LexGrid concept codes and association references are defined
     *            in terms of UMLS CUI instead of native codes from the source
     *            ontology. In addition, relations for meta distributions are
     *            interpreted as intra-scheme, whereas relations for individual
     *            coding systems are allowed to cross coding scheme boundaries.
     * @param dirNames
     *            The names of UMLS directional relations to be evaluated,
     *            optionally qualified by SAB. Roots are evaluated for
     *            participation as source or target for the given names.
     * @param lgRelationContainerName
     *            The LexGrid relations container defining the associations to
     *            be evaluated, or null to match any relations for the code
     *            system.
     * @throws SQLException
     */
    protected void buildRootNode(String codingSchemeName, boolean isMeta, SABString[] dirNames,
            String lgRelationContainerName) throws SQLException {
        // Log status
        StringBuffer nameInfo = new StringBuffer();
        for (int i = 0; i < dirNames.length; i++) {
            if (i > 0)
                nameInfo.append(',');
            nameInfo.append(dirNames[i].sab != null ? dirNames[i].sab : "<all sabs>").append(':').append(
                    dirNames[i].str);
        }
        messages_.info("Adding root relations for container "
                + (lgRelationContainerName == null ? "<any>" : lgRelationContainerName));
        messages_.info("Based on RRF name(s) = " + nameInfo.toString());

        // Find the possible root entry or entries introduced by the UMLS.
        ResultSet rs = getUMLSRoot_.executeQuery();
        Set qualifiedRoots = new HashSet();
        try {
            while (rs.next())
                qualifiedRoots.add(new SABString(rs.getString(isMeta ? "CUI" : "CODE"), rs.getString("SAB")));
        } finally {
            rs.close();
        }

        // For each possible root, resolve loaded relationships matching the
        // provided name(s) and direction.
        int buildCount = 0;
        for (Iterator roots = qualifiedRoots.iterator(); roots.hasNext();) {

            // Extract the code and SAB from the qualified root entry,
            // and determine the coding scheme used for representation of the
            // SAB within the LexGrid repository.
            SABString root = (SABString) roots.next();
            String rootCodingSchemeName = isMeta ? codingSchemeName : mapSABToCodeSystemName(root.sab);

            // Check each directional association name (also provided as a
            // SAB-qualified string) ...
            for (int i = 0; i < dirNames.length; i++) {
                SABString dirName = dirNames[i];
                String codingSchemeForRRFName = dirName.sab != null ? mapSABToCodeSystemName(dirName.sab) : null;

                // Only check associations matching the loaded coding scheme.
                // For meta, assume all associations are included within bounds
                // of
                // the load, since all SABs are brought in.
                if (isMeta || codingSchemeForRRFName == null || codingSchemeForRRFName.equals(codingSchemeName)) {
                    // Check the requested name against all registered
                    // associations.
                    // Evaluate root nodes for participation as source or
                    // target.
                    for (int j = 0; j < supportedAssociations_.length; j++) {
                        Association assoc = supportedAssociations_[j];
                        boolean matchFwd = assoc.isForwardMatch(dirName);
                        boolean matchRev = assoc.isInverseMatch(dirName);
                        if (matchFwd || matchRev) {
                            for (int fwd = 1; fwd >= 0; fwd--) {
                                boolean resolveFwd = fwd > 0;
                                boolean isRela = "RELA".equalsIgnoreCase(assoc.rrfField);
                                // The registered LexGrid association name
                                // matched a UMLS-defined
                                // directional name. Create the SQL statement to
                                // resolve the root nodes.
                                // If meta and a specific code system was
                                // specified for the association,
                                // we still want to make sure any root
                                // identified actually participates
                                // on behalf of the given scheme. This could
                                // come into play if, for
                                // example, root concepts A and B both
                                // participate in partOf relations,
                                // and the UMLS SAB for A maps partOf (RELA) to
                                // 'PAR' (REL) but the
                                // SAB for B does not. When loading a meta
                                // distribution, the source
                                // defining the association is stored as a
                                // qualifier.
                                String assocTable = stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY);
                                String assocQualTable = stc_
                                        .getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_E_QUALS);
                                StringBuffer sql = new StringBuffer(256);
                                sql.append("SELECT * FROM ").append(assocTable);
                                // Test both directions and reflexive definition
                                // ...
                                if (isMeta && codingSchemeForRRFName != null)
                                    sql.append(" INNER JOIN ").append(assocQualTable).append(" ON ").append(assocTable)
                                            .append("." + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + " = ").append(
                                                    assocQualTable).append(
                                                    "." + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY)
                                                    .append(" AND ")
                                                    .append(assocTable).append(".")
                                                    .append(stc_.codingSchemeNameOrId)
                                                    .append(" = ")
                                                    .append(assocQualTable).append(".")
                                                    .append(stc_.codingSchemeNameOrId);
                                sql.append(" WHERE (").append(
                                        resolveFwd ? " " + assocTable + "." + stc_.codingSchemeNameOrId + " = ? AND "
                                                + stc_.sourceEntityCodeOrId + " = ? AND "
                                                + stc_.sourceCSIdOrEntityCodeNS + " = ? AND "
                                                + stc_.targetCSIdOrEntityCodeNS + " = ?" : " "
                                                + assocTable + "." + stc_.codingSchemeNameOrId + " = ? AND "
                                                + stc_.targetEntityCodeOrId + " = ? AND "
                                                + stc_.targetCSIdOrEntityCodeNS + " = ? AND "
                                                + stc_.sourceCSIdOrEntityCodeNS + " = ?").append(
                                        ") AND (" + stc_.entityCodeOrAssociationId + " = ? OR "
                                                + stc_.entityCodeOrAssociationId + " = ?)");
                                if (lgRelationContainerName != null)
                                    sql.append(" AND " + stc_.containerNameOrContainerDC + " = ?");
                                if (isMeta && dirName.sab != null)
                                    sql.append(" AND ").append(assocQualTable).append(
                                            "." + SQLTableConstants.TBLCOL_QUALIFIERNAME + " = '").append(dirName.sab)
                                            .append("' AND ").append(assocQualTable).append(
                                                    "." + SQLTableConstants.TBLCOL_QUALIFIERVALUE + " = '"
                                                            + SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE + "'");

                                // Prepare the statement and assign parameters
                                // ...
                                PreparedStatement getRootRelations = sqlConnection_.prepareStatement(sqlModifier_
                                        .modifySQL(sql.toString()));
                                int param = 0;
                                getRootRelations.setString(++param, codingSchemeName);
                                getRootRelations.setString(++param, root.str);
                                getRootRelations.setString(++param, rootCodingSchemeName);
                                getRootRelations.setString(++param, codingSchemeName);
                                getRootRelations.setString(++param, assoc.name);
                                getRootRelations.setString(++param, isRela ? mapRela(assoc.rrfInverse)
                                        : assoc.rrfInverse);
                                if (lgRelationContainerName != null)
                                    getRootRelations.setString(++param, lgRelationContainerName);

                                // The query will resolve existing LexGrid
                                // associations from the
                                // UMLS-designated root to corresponding top
                                // nodes.
                                // For each root/topNode combination add a
                                // counterpart substituting
                                // '@' or '@@' in place of the UMLS root.
                                try {
                                    rs = getRootRelations.executeQuery();
                                    while (rs.next()) {
                                        String newRel = rs.getString(stc_.containerNameOrContainerDC);
                                        String newAssoc = rs.getString(stc_.entityCodeOrAssociationId);

                                        String newSrc = resolveFwd ? "@" : rs.getString(stc_.sourceEntityCodeOrId);
                                        String newTgt = resolveFwd ? rs.getString(stc_.targetEntityCodeOrId) : "@@";

                                        // Here we load the Supported
                                        // Hierarchies
                                        // We only want to pay attention to
                                        // 'PAR' and 'CHD'
                                        if (newAssoc.equals("PAR") || newAssoc.equals("CHD")) {

                                            // Next, check to see if the current
                                            // relation has been loaded
                                            // as a Supported Hierarchy. If not,
                                            // load it into the database
                                            // and flag that relation as loaded.
                                            if (!isSupportedHierarchyLoaded(newAssoc)) {
                                                try {
                                                    if (newAssoc.equals("PAR")) {
                                                        insertIntoCodingSchemeSupportedAttributes(codingSchemeName,
                                                                SQLTableConstants.TBLCOLVAL_SUPPTAG_HIERARCHY, "is_a",
                                                                null, newAssoc, "@@", String.valueOf(false));
                                                    } else if (newAssoc.equals("CHD")) {
                                                        insertIntoCodingSchemeSupportedAttributes(codingSchemeName,
                                                                SQLTableConstants.TBLCOLVAL_SUPPTAG_HIERARCHY, "is_a",
                                                                null, newAssoc, "@", String.valueOf(true));
                                                    }
                                                } catch (SQLException e) {
                                                    log
                                                            .warn("Problem inserting Supported Hierarchy. This means that"
                                                                    + " the Supported Hierarchy exists already. If you are doing a recalc"
                                                                    + "of the root node, this indicates that it has been calculated already.");
                                                    messages_
                                                            .warn("Problem inserting Supported Hierarchy. This means that"
                                                                    + " the Supported Hierarchy exists already. If you are doing a recalc"
                                                                    + "of the root node, this indicates that it has been calculated already.");
                                                }
                                            }
                                        }

                                        // Try optimistic insert. On error,
                                        // check for duplicate and reuse
                                        // multiAttributesKey if present. If
                                        // not, throw the original err.
                                        String multiKey = null;
                                        try {
                                            multiKey = isMeta ? generateUniqueKey(new String[] { codingSchemeName,
                                                    codingSchemeName, newSrc, newRel, newAssoc, codingSchemeName,
                                                    newTgt }) : null;
                                            addEntityAssociationToEntity(codingSchemeName, codingSchemeName,
                                                    SQLTableConstants.ENTITYTYPE_CONCEPT, newSrc, newRel, newAssoc,
                                                    codingSchemeName, SQLTableConstants.ENTITYTYPE_CONCEPT, newTgt,
                                                    multiKey, null, null);
                                            buildCount++;
                                        } catch (SQLException sqle) {
                                            // Check for existing association
                                            // (same link may be defined
                                            // for multiple sources in meta,
                                            // etc. If found, extract the
                                            // existing multi-attribute key for
                                            // reuse to add qualifiers.
                                            ResultSet rs2 = null;
                                            getAssocInstance_.setString(1, codingSchemeName);
                                            getAssocInstance_.setString(2, newRel);
                                            getAssocInstance_.setString(3, newAssoc);
                                            getAssocInstance_.setString(4, codingSchemeName);
                                            getAssocInstance_.setString(5, newSrc);
                                            getAssocInstance_.setString(6, codingSchemeName);
                                            getAssocInstance_.setString(7, newTgt);
                                            multiKey = null;
                                            try {
                                                rs2 = getAssocInstance_.executeQuery();
                                                if (rs2.next()) {
                                                    multiKey = rs2
                                                            .getString(SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY);
                                                } else {
                                                    throw sqle;
                                                }
                                            } catch (Exception e) {
                                                log.warn("Association instance not inserted.", sqle);
                                            } finally {
                                                if (rs2 != null)
                                                    rs2.close();
                                            }
                                        }
                                        // If inserting for the meta, ensure
                                        // registration of source
                                        // qualifier. Here we have no alternate
                                        // path to follow
                                        // if an error occurs and the qualifier
                                        // does or does not exist.
                                        // The load will not be terminated on
                                        // failure to load a
                                        // qualifier. On error, log a warning
                                        // and continue.
                                        if (isMeta && multiKey != null && dirName.sab != null)
                                            try {
                                                addEntityAssociationQualifierToEntityAssociation(codingSchemeName,
                                                        multiKey, dirName.sab,
                                                        SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE);
                                            } catch (SQLException sqle) {
                                                ResultSet rs2 = null;
                                                getAssocQualifier_.setString(1, codingSchemeName);
                                                getAssocQualifier_.setString(2, multiKey);
                                                getAssocQualifier_.setString(3, dirName.sab);
                                                getAssocQualifier_.setString(4,
                                                        SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE);
                                                try {
                                                    rs2 = getAssocQualifier_.executeQuery();
                                                    if (!rs2.next())
                                                        throw sqle;
                                                } catch (Exception e) {
                                                    log.warn("Association qualifier not inserted.", sqle);
                                                } finally {
                                                    if (rs2 != null)
                                                        rs2.close();
                                                }
                                            }
                                    }
                                } finally {
                                    if (rs != null)
                                        rs.close();
                                    getRootRelations.close();
                                }
                            }
                        }
                    }
                }
            }
        }
        messages_.info(buildCount + " root relations generated.");
    }

    // Checks if the Supported Hierarchy for a given relation has been loaded
    // into the database. This is necessary because there may be more than one
    // instance when a given relation can be loaded -- this avoids the database
    // error of trying to load duplicate entries
    private boolean isSupportedHierarchyLoaded(String relation) {
        // if the Supported Hierarchy has not been loaded, keep a record of it
        // being loaded and return false.
        if (!supportedHierarchies_.contains(relation)) {
            supportedHierarchies_.add(relation);
            return false;
            // if it has been loaded, return true
        } else {
            return true;
        }
    }

    /**
     * Return the detailed name of the given RRF name; null if not available.
     * 
     * @param rrfName
     *            The UMLS relation name.
     * @param rrfField
     *            The UMLS field (e.g. 'RELA' or 'REL').
     * @return String
     */
    protected String getExpandedForm(String rrfName, String rrfField) throws SQLException {
        String result = null;
        if (!StringUtils.isNotBlank(rrfName)) {
            PreparedStatement getInverse = umlsConnection_
                    .prepareStatement("SELECT EXPL FROM MRDOC WHERE DOCKEY = ? AND value = ? and TYPE = ?");
            try {
                getInverse.setString(1, rrfField);
                getInverse.setString(2, rrfName);
                getInverse.setString(3, "expanded_form");

                ResultSet results = getInverse.executeQuery();
                if (results.next())
                    result = results.getString("EXPL");
                results.close();
            } finally {
                getInverse.close();
            }
        }
        return result;
    }

    /**
     * Relation names in the RRF (RELA field) for hierarchical associations
     * specific to the source distribution.
     * 
     * @return String[]
     * @throws SQLException
     *             If an error occurs resolving from the source.
     */
    protected SABString[] getHierRelas() throws SQLException {
        if (hierRelas_ == null) {
            // Note: Always add isa & inverse_isa; sometimes not mapped by RELA
            // to
            // the standard parent/child (PAR/CHD) RELs (e.g. MeSH maps to
            // 'RN/RB').
            Set names = new HashSet();
            names.addAll(Arrays.asList(getRelationSABs(new String[] { "isa", "inverse_isa" }, "RELA")));
            names.addAll(Arrays.asList(getRelaNames(getHierRels())));
            hierRelas_ = (SABString[]) names.toArray(new SABString[names.size()]);
        }
        return hierRelas_;
    }

    /**
     * Standard names (REL field in the RRF) for hierarchical associations in
     * the UMLS.
     * 
     * @return String[]
     * @throws SQLException
     *             If an error occurs resolving from the source.
     */
    protected SABString[] getHierRels() throws SQLException {
        if (hierRels_ == null) {
            // PAR = hasParent, CHD = hasChild
            // Note: not including RB/RN (relation broader/narrower) since
            // used in contexts that do not always imply subsumption.
            hierRels_ = getRelationSABs(new String[] { "CHD", "PAR" }, "REL");
        }
        return hierRels_;
    }

    /**
     * Returns the names of the hierarchical associations in use for the LexGrid
     * code system of the given name.
     * 
     * @param codeSystemName
     * @return String[]
     * @throws SQLException
     *             If an error occurs resolving from the source.
     */
    protected String[] getHierAssocNames(String codeSystemName) throws SQLException {
        String[] names;
        if ((names = (String[]) schemeToHierNames_.get(codeSystemName)) == null) {
            Set matches = new HashSet();
            String sab = mapCodeSystemNameToSAB(codeSystemName);
            SABString[][] allNames = new SABString[][] { getHierRels(), getHierRelas() };
            for (int i = 0; i < allNames.length; i++) {
                SABString[] candidates = allNames[i];
                for (int j = 0; j < candidates.length; j++) {
                    SABString candidate = candidates[j];
                    if (candidate.sab == null || candidate.sab.equalsIgnoreCase(sab))
                        matches.add(i == 0 ? candidate.str : mapRela(candidate.str));
                }
            }
            names = (String[]) matches.toArray(new String[matches.size()]);
        }
        return names;
    }

    /**
     * Resolve source-assigned relation names corresponding to the given array
     * of standard UMLS relation names (e.g. resolve 'part_of' for 'PAR').
     * 
     * @param relNames
     *            SABString[]
     * @return String[]
     * @throws SQLException
     *             If an error occurs resolving from the source.
     */
    protected SABString[] getRelaNames(SABString[] relNames) throws SQLException {
        GenericSQLModifier gsm = null;
        try {
            gsm = new GenericSQLModifier(umlsConnection2_);
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }

        // Locate matching RELA entries in MRREL file ...
        Collection relas = new HashSet();
        for (int i = 0; i < relNames.length; i++) {
            SABString relName = relNames[i];
            StringBuffer sql = new StringBuffer().append("SELECT DISTINCT RELA from MRREL WHERE REL = '").append(
                    relName.str).append('\'');
            if (relName.sab != null)
                sql.append(" AND SAB = '").append(relName.sab).append('\'');
            PreparedStatement getRELA = umlsConnection2_.prepareStatement(gsm.modifySQL(sql.toString()));
            try {
                ResultSet rs = getRELA.executeQuery();
                String s;
                while (rs.next())
                    if (StringUtils.isNotBlank(s = rs.getString(1)))
                        relas.add(s);
                rs.close();
            } finally {
                getRELA.close();
            }
        }

        // Resolve corresponding SABs for each RELA
        Collection sabStrings = new ArrayList();
        for (Iterator relaIt = relas.iterator(); relaIt.hasNext();) {
            String relaName = (String) relaIt.next();
            StringBuffer sql = new StringBuffer().append("SELECT DISTINCT SAB from MRREL WHERE RELA = '").append(
                    relaName).append('\'');
            PreparedStatement getSABs = umlsConnection2_.prepareStatement(gsm.modifySQL(sql.toString()));
            try {
                ResultSet rs = getSABs.executeQuery();
                try {
                    while (rs.next())
                        sabStrings.add(new SABString(relaName, rs.getString(1)));
                } finally {
                    rs.close();
                }
            } finally {
                getSABs.close();
            }
        }
        return (SABString[]) sabStrings.toArray(new SABString[sabStrings.size()]);
    }

    /**
     * Resolve UMLS source abbreviations corresponding to the given array of
     * source-defined relation names (e.g. resolved from MRREL).
     * 
     * @param rrfNames
     *            String[]
     * @param rrfField
     *            'RELA' or 'REL'.
     * @return SABString[] Array of corresponding SAB-qualified relation names.
     * @throws SQLException
     *             If an error occurs resolving from the source.
     */
    protected SABString[] getRelationSABs(String[] rrfNames, String rrfField) throws SQLException {
        GenericSQLModifier gsm = null;
        try {
            gsm = new GenericSQLModifier(umlsConnection2_);
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }

        // Locate matching RELA entries in MRREL file ...
        Collection sabStrings = new HashSet();
        for (int i = 0; i < rrfNames.length; i++) {
            String rrfName = rrfNames[i];
            StringBuffer sql = new StringBuffer().append("SELECT DISTINCT SAB from MRREL WHERE ").append(rrfField)
                    .append(" = '").append(rrfName).append('\'');
            PreparedStatement getSABs = umlsConnection2_.prepareStatement(gsm.modifySQL(sql.toString()));
            try {
                ResultSet rs = getSABs.executeQuery();
                String s;
                while (rs.next())
                    if (StringUtils.isNotBlank(s = rs.getString(1)))
                        sabStrings.add(new SABString(rrfName, s));
                rs.close();
            } finally {
                getSABs.close();
            }
        }
        return (SABString[]) sabStrings.toArray(new SABString[sabStrings.size()]);
    }

    /**
     * Return the inverse name of the given RRF-based relation; null if not
     * available.
     * 
     * @param rrfName
     *            The UMLS relation name.
     * @param rrfField
     *            'RELA' or 'REL'.
     * @return String
     */
    protected String getRelationInverseName(String rrfName, String rrfField) throws SQLException {
        String result = null;
        if (StringUtils.isNotBlank(rrfName)) {
            PreparedStatement getInverse = umlsConnection_
                    .prepareStatement("SELECT EXPL FROM MRDOC WHERE DOCKEY = ? AND value = ? and TYPE = ?");
            try {
                getInverse.setString(1, rrfField);
                getInverse.setString(2, rrfName);
                getInverse.setString(3, rrfField.toLowerCase() + "_inverse");

                ResultSet results = getInverse.executeQuery();
                if (results.next())
                    result = results.getString("EXPL");
                results.close();
            } finally {
                getInverse.close();
            }
        }
        return result;
    }

    /**
     * Indicates whether the given association is used to represent hierarchical
     * relationships in the source being processed.
     * 
     * @param assoc
     *            Association object to check; not null.
     * @param codingSchemeName
     *            Name of the LexGrid coding scheme containing the association;
     *            null to match any coding scheme.
     * @return true if representing hierarchical relations; false otherwise.
     * @throws SQLException
     *             If an error occurs resolving from the source.
     */
    protected boolean isHierarchicalAssociation(Association assoc, String codingSchemeName) throws SQLException {
        return isHierarchicalName(assoc.rrfName, codingSchemeName)
                || isHierarchicalName(assoc.rrfInverse, codingSchemeName);
    }

    /**
     * Indicates whether the given name is used to represent hierarchical
     * relationships in the source being processed.
     * 
     * @param dirName
     *            Directional name of the association to check.
     * @param codeSystemName
     *            Name of the LexGrid coding scheme containing the association;
     *            null to match any coding scheme.
     * @return true if the name represents hierarchical definitions; false if
     *         not.
     * @throws SQLException
     *             If an error occurs resolving from the source.
     */
    protected boolean isHierarchicalName(String name, String codeSystemName) throws SQLException {
        if (name == null)
            return false;
        SABString[][] allNames = new SABString[][] { getHierRels(), getHierRelas() };

        String sab = codeSystemName != null ? mapCodeSystemNameToSAB(codeSystemName) : null;
        for (int i = 0; i < allNames.length; i++) {
            SABString[] candidates = allNames[i];
            for (int j = 0; j < candidates.length; j++) {
                SABString candidate = candidates[j];
                if (name.equalsIgnoreCase(candidate.str)
                        && (sab == null || candidate.sab == null || candidate.sab.equalsIgnoreCase(sab)))
                    return true;
            }
        }
        return false;
    }

    /**
     * Indicates if the given association matches those currently loaded for the
     * given coding scheme and relations container.
     * 
     * @param assoc
     * @param codingSchemeName
     * @param relationsContainerName
     * @return true if loaded; false if not.
     * @throws SQLException
     */
    protected boolean isLoaded(Association assoc, String codingSchemeName, String relationsContainerName)
            throws SQLException {
        // Note: Do not include sab on this check; evaluating at LexGrid level.
        return isLoaded(assoc.name, codingSchemeName, relationsContainerName);
    }

    /**
     * Indicates if the given association name matches those currently loaded
     * for the given coding scheme and relations container.
     * 
     * @param name
     * @param codingSchemeName
     * @param relationsContainerName
     * @return true if loaded; false if not.
     * @throws SQLException
     */
    protected boolean isLoaded(String name, String codingSchemeName, String relationsContainerName) throws SQLException {
        // Note: markLoaded() key must match ...
        return loadedAssociations_.contains(new StringBuffer(128).append(codingSchemeName).append(
                relationsContainerName).append(name).toString());
    }

    /**
     * Sets an indication that the given association has been loaded for the
     * given coding scheme and relations container.
     * 
     * @param assoc
     * @param codingSchemeName
     * @param relationsContainerName
     * @throws SQLException
     */
    protected void markLoaded(Association assoc, String codingSchemeName, String relationsContainerName)
            throws SQLException {
        markLoaded(assoc.name, codingSchemeName, relationsContainerName);
    }

    /**
     * Sets an indication that the given association has been loaded for the
     * given coding scheme and relations container.
     * 
     * @param name
     * @param codingSchemeName
     * @param relationsContainerName
     * @return true if loaded; false if not.
     * @throws SQLException
     */
    protected void markLoaded(String name, String codingSchemeName, String relationsContainerName) throws SQLException {
        // Note: isLoaded() key must match ...
        loadedAssociations_.add(new StringBuffer(128).append(codingSchemeName).append(relationsContainerName).append(
                name).toString());
    }

    /**
     * Map from the UMLS source abbreviation (SAB) to the coding scheme name
     * used for identification in the LexGrid model.
     * 
     * @param sab
     * @return String
     * @throws SQLException
     */
    protected String mapSABToCodeSystemName(String sab) throws SQLException {
        String result = (String) sabToCodeSystem_.get(sab);
        if (result == null) {
            PreparedStatement getCodingSchemeInfo = umlsConnection_
                    .prepareStatement("SELECT SSN FROM MRSAB WHERE RSAB = ?");
            try {
                getCodingSchemeInfo.setString(1, sab);
                ResultSet results = getCodingSchemeInfo.executeQuery();
                if (results.next()) {
                    result = results.getString("SSN");
                    sabToCodeSystem_.put(sab, result);
                }
                results.close();
            } finally {
                getCodingSchemeInfo.close();
            }
        }
        return result;
    }

    /**
     * Map from the coding scheme name used for identification in the LexGrid
     * model to the UMLS source abbreviation (SAB).
     * 
     * @param codeSystemName
     * @return String
     * @throws SQLException
     */
    protected String mapCodeSystemNameToSAB(String codeSystemName) throws SQLException {
        String result = "";
        PreparedStatement getCodingSchemeInfo = umlsConnection_
                .prepareStatement("SELECT RSAB FROM MRSAB WHERE SSN = ?");
        try {
            getCodingSchemeInfo.setString(1, codeSystemName);
            ResultSet results = getCodingSchemeInfo.executeQuery();
            if (results.next()) {
                result = results.getString("RSAB");
            }
            results.close();
        } finally {
            getCodingSchemeInfo.close();
        }
        return result;
    }

    /**
     * Map the provided information to a new Association and add, if new, add to
     * the given hashtable.
     * 
     * @param rrfName
     *            The name of the relationship as provided by the UMLS.
     * @param rrfSAB
     *            The source abbreviation of the UMLS code system.
     * @param rrfDirFlag
     *            The UMLS directional flag. Y indicates that this is the
     *            direction of the RELA relationship in its source; N indicates
     *            that it is not; otherwise indicates that it is not important
     *            or has not yet been determined. (If blank RELA, we interpret
     *            as 'N', based on empirical review of meta files).
     * @param rrfField
     *            Indicates whether the association name is defined as a
     *            standard UMLS relation ('REL') or source-provided label
     *            ('RELA').
     * @param map
     *            A Map of Associations, keyed by SAB-qualified name.
     * @return The mapped Association.
     * @throws SQLException
     */
    protected Association mapSupportedAssociationsHelper(String rrfName, String rrfSAB, String rrfDirFlag,
            String rrfField, Map map) throws SQLException {
        // Check for an existing Association matching the given info ...
        boolean isRela = "RELA".equalsIgnoreCase(rrfField);
        Association assoc = (Association) map.get(new SABString(isRela ? mapRela(rrfName) : rrfName, rrfSAB));
        if (assoc != null)
            return assoc;

        // Also check reverse direction ...
        String reverseName = getRelationInverseName(rrfName, rrfField);
        if (reverseName.equals(rrfName))
            reverseName = "";
        if (StringUtils.isNotBlank(reverseName))
            assoc = (Association) map.get(new SABString(isRela ? mapRela(reverseName) : reverseName, rrfSAB));
        if (assoc != null)
            return assoc;

        // Not found, define and register now ...
        assoc = new Association();

        boolean isReversed = isRela && !("Y".equalsIgnoreCase(rrfDirFlag));
        assoc.rrfName = rrfName;
        assoc.rrfInverse = reverseName;
        assoc.rrfField = rrfField;
        assoc.rrfSAB = rrfSAB;
        assoc.rrfSourceDirectionalityReversed = isReversed;
        assoc.name = isRela ? mapRela(rrfName) : rrfName;

        map.put(new SABString(assoc.name, rrfSAB), assoc);
        return assoc;
    }

    /**
     * Maps from the given UMLS-provided relationship name to a pre-designated
     * LexGrid association name (e.g. map from 'inverse_isa' to 'hasSubtype'),
     * if available. Otherwise, return the rela unmodified.
     * 
     * @param rrfName
     * @return The mapped value.
     */
    protected String mapRela(String rrfName) {
        return ("inverse_isa".equalsIgnoreCase(rrfName)) ? "hasSubtype" : rrfName;
    }

    /**
     * Converts the given string array to a single string, separating each value
     * by comma and embedding quotes before and after each value.
     * 
     * @param strings
     * @return String
     */
    protected String toCommaDelimitedWithQuotes(String[] strings) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strings.length; i++) {
            if (i > 0)
                sb.append(',');
            sb.append('\'').append(strings[i]).append('\'');
        }
        return sb.toString();
    }

    /**
     * Returns a message digest to be used for fixed-length key generation. s
     */
    protected MessageDigest getSHA1() {
        if (md_ == null)
            try {
                md_ = MessageDigest.getInstance("SHA1");
            } catch (NoSuchAlgorithmException e) {
            }
        return md_;
    }

    /**
     * Generates a unique but reproducible key based on the given base of string
     * values.
     * 
     * @param components
     * @return A unique string based on 20-byte output from a SHA-1 message
     *         digest.
     * @throws SQLException
     */
    protected String generateUniqueKey(String[] basis) {
        MessageDigest md = getSHA1();
        md.reset();
        for (int i = 0; i < basis.length; i++)
            if (basis[i] != null)
                md.update(basis[i].getBytes());
        byte[] bytes = md.digest();
        return String.valueOf(Hex.encodeHex(bytes));
    }

    /**
     * Generates a unique but random key, using the given integer as seed.
     * 
     * @param seed
     * @return String
     */
    protected String generateUniqueKey(int seed) {
        return System.currentTimeMillis() + "" + get5DigitRandom() + "" + seed;
    }

    protected String get5DigitRandom() {
        String result = "";
        while (result.length() != 5) {
            result = "" + (int) Math.floor((Math.random() * 100000));
        }
        return result;
    }

    protected void loadLoaderPreferences() {
        if (loadPrefs_ != null) {
            // not implemented yet
        }
    }

    /**
     * Helper method to check if the Registered name is changed in the Manifest.
     * Because this is done pre-load, we need to check.
     * 
     * @param defaultRegisteredName
     *            The default to return if the manifest does not override it
     * @return The Registered Name
     */
    protected String getRegisteredNameFromManifest(String defaultRegisteredName) {
        if (manifestLocation_ != null) {
            ManifestUtil manifestUtil = new ManifestUtil();
            CodingSchemeManifest manifest = manifestUtil.getManifest(manifestLocation_);
            CsmfCodingSchemeURI registeredName = manifest.getCodingSchemeURI();
            if (registeredName != null) {
                manifestNameChange_ = true;
                return registeredName.getContent();
            }
        }
        return defaultRegisteredName;
    }

    /**
     * Helper method to check if the Coding Scheme Name is changed in the
     * Manifest. Because this is done pre-load, we need to check.
     * 
     * @param defaultCodingSchemeName
     *            The default to return if the manifest does not override it
     * @return The CodingScheme Name
     */
    protected String getCodingSchemeNameFromManifest(String defaultCodingSchemeName) {
        if (manifestLocation_ != null) {
            ManifestUtil manifestUtil = new ManifestUtil();
            CodingSchemeManifest manifest = manifestUtil.getManifest(manifestLocation_);
            CsmfCodingSchemeName codingScheme = manifest.getCodingScheme();
            if (codingScheme != null) {
                manifestNameChange_ = true;
                return codingScheme.getContent();
            }
        }
        return defaultCodingSchemeName;
    }

    protected void loadMRRANK() {
        ResultSet results = null;
        try {
            results = getPresentationRank_.executeQuery();
            while (results.next()) {
                // Turn the resulting rank into an int so we can do some
                // computing with it.
                String rank = results.getString("RANK");
                String source = results.getString("SAB");
                String tty = results.getString("TTY");

                insertValueIntoMRRANK(source, tty, rank);
            }
        } catch (SQLException e) {
            log.warn("Problem Loading MRRANK, it will not be used to calculate Presentation rank.");
            messages_.warn("Problem Loading MRRANK, it will not be used to calculate Presentation rank.");
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (getPresentationRank_ != null) {
                    getPresentationRank_.close();
                }
            } catch (SQLException e) {
                log.warn("Problem Closing SQL Connections.");
                messages_.warn("Problem Closing SQL Connections.");
            }
        }
    }
    
    protected void loadDefaultSupportedNamespace(String codingSchemeName) throws SQLException {
        insertIntoCodingSchemeSupportedAttributes(codingSchemeName, SQLTableConstants.TBLCOLVAL_SUPPTAG_NAMESPACE,
                codingSchemeName, null, codingSchemeName, codingSchemeName, null);
        messages_.info("Loaded default Suppported Namespace for: " + codingSchemeName);
    }
    
    private void insertValueIntoMRRANK(String source, String TTY, String rank) {
        String key = source + ":" + TTY;
        MRRANK_.put(key, rank);
    }

    private int getRankFromMRRANK(String source, String TTY) {
        String key = source + ":" + TTY;
        if (MRRANK_.containsKey(key)) {
            String rank = (String) MRRANK_.get(key);
            return Integer.parseInt(rank);
        } else {
            return -1;
        }
    }

}