
package org.LexGrid.util.sql.lgTables;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Constants like the insert statements for the SQL tables.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma </A>
 * @author <A HREF="mailto:stancl.craig@mayo.edu">Craig Stancl</A>
 */
public class SQLTableConstants {
    public SQLTableConstants(String version, String tablePrefix) {
        version_ = version;
        tablePrefix_ = tablePrefix;
        init();
    }

    // numbers a just keys... variable names represent all tables.
    public static final String CODING_SCHEME = "1";
    public static final String CODING_SCHEME_MULTI_ATTRIBUTES = "2";
    public static final String CODING_SCHEME_SUPPORTED_ATTRIBUTES = "3";
    public static final String ENTITY = "4";
    public static final String ENTITY_PROPERTY = "6";
    public static final String ENTITY_PROPERTY_MULTI_ATTRIBUTES = "7";
    public static final String RELATION = "8";
    public static final String RELATION_MULTI_ATTRIBUTES = "9";
    public static final String ASSOCIATION = "10";
    public static final String ENTITY_ASSOCIATION_TO_ENTITY = "11";
    public static final String ENTITY_ASSOCIATION_TO_DATA = "12";
    public static final String ENTITY_ASSOCIATION_TO_E_QUALS = "13";
    public static final String ENTITY_ASSOCIATION_TO_D_QUALS = "14";
    public static final String ENTITY_PROPERTY_LINKS = "15";
    public static final String LEXGRID_TABLE_META_DATA = "16";
    // optional
    public static final String ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE = "17";

    // (this was in a different place before - now its new (and optional) in 1.5
    public static final String NCI_THESAURUS_HISTORY = "18";
    public static final String CONCEPT_HISTORY = "36";
    // new in 1.5 (optional)
    public static final String SYSTEM_RELEASE = "19";
    // new in 1.5 (optional)
    public static final String SYSTEM_RELEASE_REFS = "23";
    // new in 1.5 (optional)
    public static final String CODING_SCHEME_VERSIONS = "24";
    // new in 1.5
    public static final String CODING_SCHEME_PROP = "21";
    // new in 1.5
    public static final String CODING_SCHEME_PROP_MULTI_ATTRIB = "22";

    // concept_multi_attributes no longer exists starting with version 1.5
    public static final String CONCEPT_MULTI_ATTRIBUTES = "5";
    // concept_property_qualifiers no longer exists starting with version 1.5
    public static final String CONCEPT_PROPERTY_QUALIFIERS = "20";

    //Temporary tables for RRFs when loading UMLS
    public static final String MRCONSO = "100";
    public static final String MRDOC = "110";
    public static final String MRREL = "120";
    public static final String MRSAB = "130";
    public static final String MRRANK = "140";
    public static final String MRDEF = "150";
    public static final String MRSTY = "160";
    public static final String MRSAT = "170";
    public static final String MRHIER = "180";

    // Table Names (Old and New Both) May 2008
    public static final String TBL_CODING_SCHEME = "codingScheme";
    public static final String TBL_CODING_SCHEME_MULTI_ATTRIBUTES = "codingSchemeMultiAttrib";
    public static final String TBL_CODING_SCHEME_SUPPORTED_ATTRIBUTES = "codingSchemeSupportedAttrib";
    public static final String TBL_CONCEPT = "concept";
    public static final String TBL_CONCEPT_PROPERTY = "conceptProperty";
    public static final String TBL_ENTITY = "entity";
    public static final String TBL_ENTITY_PROPERTY = "entityProperty";
    public static final String TBL_ENTITY_PROPERTY_MULTI_ATTRIBUTES = "entityPropertyMultiAttrib";
    public static final String TBL_ENTITY_TYPE = "entityType";
    public static final String TBL_ENTRY_TYPE = "entryType";

    public static final String TBL_CONCEPT_PROPERTY_MULTI_ATTRIBUTES = "conceptPropertyMultiAttrib";
    public static final String TBL_INSTANCE = "instance";
    public static final String TBL_MAPPING = "mapping";
    public static final String TBL_PROPERTY = "property";
    public static final String TBL_RELATION = "relation";
    public static final String TBL_RELATION_MULTI_ATTRIBUTES = "relationMultiAttrib";
    public static final String TBL_ASSOCIATION = "association";
    public static final String TBL_ENTITY_ASSOCIATION_TO_ENTITY = "entityAssnsToEntity";
    public static final String TBL_ENTITY_ASSOCIATION_TO_DATA = "entityAssnsToData";
    public static final String TBL_ENTITY_ASSOCIATION_TO_E_QUALS = "entityAssnsToEQuals";
    public static final String TBL_ENTITY_ASSOCIATION_TO_D_QUALS = "entityAssnsToDQuals";
    public static final String TBL_ENTITY_PROPERTY_LINKS = "entityPropertyLinks";
    public static final String TBL_ENTRY_STATE = "entryState";

    public static final String TBL_CONCEPT_ASSOCIATION_TO_CONCEPT = "conceptAssnsToConcept";
    public static final String TBL_CONCEPT_ASSOCIATION_TO_DATA = "conceptAssnsToData";
    public static final String TBL_CONCEPT_ASSOCIATION_TO_C_QUALS = "conceptAssnsToCQuals";
    public static final String TBL_CONCEPT_ASSOCIATION_TO_D_QUALS = "conceptAssnsToDQuals";
    public static final String TBL_CONCEPT_HISTORY = "conceptHistory";
    public static final String TBL_CONCEPT_PROPERTY_LINKS = "conceptPropertyLinks";

    public static final String TBL_LEXGRID_TABLE_META_DATA = "lexGridTableMetaData";
    public static final String TBL_ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE = "entityAssnsToEntityTr";
    public static final String TBL_CONCEPT_ASSOCIATION_TO_CONCEPT_TRANSITIVE = "conceptAssnsToConceptTr";
    public static final String TBL_CODING_SCHEME_PROP = "codingSchemeProp";
    public static final String TBL_CODING_SCHEME_PROP_MULTI_ATTRIB = "codingSchemePropMultiAttrib";
    public static final String TBL_SYSTEM_RELEASE = "systemRelease";
    public static final String TBL_SYSTEM_RELEASE_REFS = "systemReleaseRefs";
    public static final String TBL_CODING_SCHEME_VERSIONS = "codingSchemeVersions";
    public static final String TBL_NCI_THESAURUS_HISTORY = "nciThesHist";

    // New constants introduced in LexOWL
    public static final String INSTANCE = "40";

    public static final String ENTITY_TYPE = "50";
    public static final String ENTRY_STATE = "55";
    public static final String ENTRY_STATE_TYPE_ASSOCIATION = "association";
    public static final String ENTRY_STATE_TYPE_ATTRIBUTE = "attribute";
    public static final String ENTRY_STATE_TYPE_CODINGSCHEME = "codingScheme";
    public static final String ENTRY_STATE_TYPE_CODINGSCHEMEPROP = "codingSchemeProp";
    public static final String ENTRY_STATE_TYPE_ENTITY = "entity";
    public static final String ENTRY_STATE_TYPE_ENTITYPROPERTY = "entityProperty";
    public static final String ENTRY_STATE_TYPE_ENTITYASSNSTODATA = "entityAssnsToData";
    public static final String ENTRY_STATE_TYPE_ENTITYASSNSTOENTITY = "entityAssnsToEntity";
    public static final String ENTRY_STATE_TYPE_MAPPING = "mapping";
    public static final String ENTRY_STATE_TYPE_PICKLIST = "pickList";
    public static final String ENTRY_STATE_TYPE_PICKLISTENTRY = "plEntry";
    public static final String ENTRY_STATE_TYPE_PROPERTY = "property";
    public static final String ENTRY_STATE_TYPE_VALUEDOMAIN = "valueSetDefinition";    
    public static final String ENTRY_STATE_TYPE_VALUEDOMAINENTRY = "vdEntry";

    private Hashtable insertStatements = new Hashtable();
    private Hashtable updateStatements = new Hashtable();

    private Hashtable tableNames = new Hashtable();

    private String version_ = "";
    String tablePrefix_;

    // Convenience Variables
    public final String NOCOLUMNNAME = " TABLE-COLUMN-NOT-SET ";
    public final String BLANK = "";
    public String codingSchemeNameOrId = NOCOLUMNNAME;
    public String containerNameOrContainerDC = NOCOLUMNNAME;
    public String associationNameOrId = NOCOLUMNNAME;
    public String entityCodeOrAssociationId = NOCOLUMNNAME;
    public String entityCodeOrAssociationName = NOCOLUMNNAME;
    public String entityCodeOrId = NOCOLUMNNAME;
    public String isDefinedOrNothing = NOCOLUMNNAME;
    public String isInferredOrNothing = NOCOLUMNNAME;
    public String entityCodeOrEntityId = NOCOLUMNNAME;
    public String sourceCSIdOrEntityCodeNS = NOCOLUMNNAME;
    public String sourceEntityCodeOrId = NOCOLUMNNAME;
    public String targetCSIdOrEntityCodeNS = NOCOLUMNNAME;
    public String targetEntityCodeNSOrCSId = NOCOLUMNNAME;
    public String targetEntityCodeOrId = NOCOLUMNNAME;
    public String idOrDataId = NOCOLUMNNAME;
    public String inverseOrInverseId = NOCOLUMNNAME;
    public String propertyOrPropertyName = NOCOLUMNNAME;
    public String formatOrPresentationFormat = NOCOLUMNNAME;
    public String urnOruri = NOCOLUMNNAME;
    public String releaseURNOrreleaseURI = NOCOLUMNNAME;
    public String registeredNameOrCSURI = NOCOLUMNNAME;

    // conditionals
    public String entityTypeColDef = NOCOLUMNNAME;
    public String sourceTypeColDef = NOCOLUMNNAME;
    public String targetTypeColDef = NOCOLUMNNAME;
    public String entityType = NOCOLUMNNAME;
    public static final String TBLCOL_APPROXNUMCONCEPTS = "approxNumConcepts";
    public static final String TBLCOL_ASSOCIATION = "association";
    public static final String TBLCOL_ASSOCIATIONID = "associationId";
    public static final String TBLCOL_ASSOCIATIONINSTANCEID = "associationInstanceId";
    public static final String TBLCOL_ASSOCIATIONNAME = "associationName";
    public static final String TBLCOL_ATTRIBUTEVALUE = "attributeValue";
    public static final String TBLCOL_BASEDONRELEASE = "basedOnRelease";
    public static final String TBLCOL_CHANGEAGENT = "changeAgent";
    public static final String TBLCOL_CHANGEDOCUMENTATION = "changeDocumentation";
    public static final String TBLCOL_CHANGEINSTRUCTIONS = "changeInstructions";
    public static final String TBLCOL_CHANGETYPE = "changeType";
    public static final String TBLCOL_CHANGEINSTRUCTION = "changeInstruction";
    public static final String TBLCOL_CODINGSCHEME = "codingScheme";
    public static final String TBLCOL_CODINGSCHEMEKEY = "codingSchemeKey";
    public static final String TBLCOL_CODINGSCHEMENAME = "codingSchemeName";
    public static final String TBLCOL_CODINGSCHEMEREFERENCE = "codingSchemeReference";
    public static final String TBLCOL_CODINGSCHEMEURI = "codingSchemeURI";
    public static final String TBLCOL_CODINGSCHEMEID = "codingSchemeId";
    public static final String TBLCOL_COMPLETEDOMAIN = "completeDomain";
    public static final String TBLCOL_CONCEPTNAME = "conceptName";
    public static final String TBLCOL_CONCEPTSTATUS = "conceptStatus";
    public static final String TBLCOL_CONTAINERDC = "containerDC";
    public static final String TBLCOL_CONTAINERNAME = "containerName";
    public static final String TBLCOL_COPYRIGHT = "copyright";
    public static final String TBLCOL_DATAID = "dataId";
    public static final String TBLCOL_DATAVALUE = "dataValue";
    public static final String TBLCOL_DEFAULTENTITYCODENAMESPACE = "defaultEntityCodeNamespace";
    public static final String TBLCOL_DEFAULTCODINGSCHEME = "defaultCodingScheme";
    public static final String TBLCOL_DEFAULTLANGUAGE = "defaultLanguage";
    public static final String TBLCOL_DEFAULTSORTORDER = "defaultSortOrder";
    public static final String TBLCOL_DEGREEOFFIDELITY = "degreeOfFidelity";
    public static final String TBLCOL_DEPENDENTVALUE = "dependentValue";
    public static final String TBLCOL_DEPRECATED = "deprecated";
    public static final String TBLCOL_DESCRIPTION = "description";
    public static final String TBLCOL_DOMAINCONCEPTID = "domainConceptId";
    public static final String TBLCOL_EDITACTION = "editAction";
    public static final String TBLCOL_EDITDATE = "editDate";
    public static final String TBLCOL_EDITORDER = "editOrder";
    public static final String TBLCOL_EFFECTIVEDATE = "effectiveDate";
    public static final String TBLCOL_ENTITYCODE = "entityCode";
    public static final String TBLCOL_ENTITYCODENAMESPACE = "entityCodeNamespace";
    public static final String TBLCOL_ENTITYDESCRIPTION = "entityDescription";
    public static final String TBLCOL_ENTITYID = "entityId";
    public static final String TBLCOL_ENTITYTYPE = "entityType";
    public static final String TBLCOL_ENTRYORDER = "entryOrder";
    public static final String TBLCOL_ENTRYSTATEID = "entryStateId";
    public static final String TBLCOL_ENTRYID = "entryId";
    public static final String TBLCOL_ENTRYTYPE = "entryType";
    public static final String TBLCOL_EXPIRATIONDATE = "expirationDate";
    public static final String TBLCOL_FIRSTRELEASE = "firstRelease";
    public static final String TBLCOL_FORMALNAME = "formalName";
    public static final String TBLCOL_FORMAT = "format";
    public static final String TBLCOL_FOREIGNENTRYID = "foreignEntryId";
    public static final String TBLCOL_FORWARDNAME = "forwardName";
    public static final String TBLCOL_ID = "id";
    public static final String TBLCOL_IDVALUE = "idValue";
    public static final String TBLCOL_INCLUDE = "include";
    public static final String TBLCOL_INCLUDECHILDREN = "includeChildren";
    public static final String TBLCOL_INCLUDESVALUEDOMAIN = "includesValueDomain";
    public static final String TBLCOL_INVERSE = "inverse";
    public static final String TBLCOL_INVERSEID = "inverseId";
    public static final String TBLCOL_ISACTIVE = "isActive";
    public static final String TBLCOL_ISANONYMOUS = "isAnonymous";
    public static final String TBLCOL_ISANTIREFLEXIVE = "isAntiReflexive";
    public static final String TBLCOL_ISANTISYMMETRIC = "isAntiSymmetric";
    public static final String TBLCOL_ISANTITRANSITIVE = "isAntiTransitive";
    public static final String TBLCOL_ISCOMPLETE = "isComplete";
    public static final String TBLCOL_ISDEFAULT = "isDefault";
    public static final String TBLCOL_ISDEFINED = "isDefined";
    public static final String TBLCOL_ISDEFINING = "isDefining";
    public static final String TBLCOL_ISFUNCTIONAL = "isFunctional";
    public static final String TBLCOL_ISINFERRED = "isInferred";
    public static final String TBLCOL_ISNATIVE = "isNative";
    public static final String TBLCOL_ISNAVIGABLE = "isNavigable";
    public static final String TBLCOL_ISPREFERRED = "isPreferred";
    public static final String TBLCOL_ISREFLEXIVE = "isReflexive";
    public static final String TBLCOL_ISREVERSEFUNCTIONAL = "isReverseFunctional";
    public static final String TBLCOL_ISSELECTABLE = "isSelectable";
    public static final String TBLCOL_ISSYMMETRIC = "isSymmetric";
    public static final String TBLCOL_ISTRANSITIVE = "isTransitive";
    public static final String TBLCOL_ISTRANSLATIONASSOCIATION = "isTranslationAssociation";
    public static final String TBLCOL_LANGUAGE = "language";
    public static final String TBLCOL_LEAFONLY = "leafOnly";
    public static final String TBLCOL_LINK = "link";
    public static final String TBLCOL_LOCALID = "localId";
    public static final String TBLCOL_MATCHIFNOCONTEXT = "matchIfNoContext";
    public static final String TBLCOL_MODIFIEDINRELEASE = "modifiedInRelease";
    public static final String TBLCOL_MULTIATTRIBUTESKEY = "multiAttributesKey";
    public static final String TBLCOL_OPERATOR = "operator";
    public static final String TBLCOL_OWNER = "owner";
    public static final String TBLCOL_PICKLISTENTRYID = "pickListEntryId";
    public static final String TBLCOL_PICKLISTID = "pickListId";
    public static final String TBLCOL_PICKTEXT = "pickText";
    public static final String TBLCOL_PLENTRYID = "plEntryId";
    public static final String TBLCOL_PRESENTATIONFORMAT = "presentationFormat";
    public static final String TBLCOL_PREVENTRYSTATEID = "prevEntryStateId";
    public static final String TBLCOL_PREVREVISIONID = "prevRevisionId";
    public static final String TBLCOL_PROPERTY = "property";
    public static final String TBLCOL_PROPERTYID = "propertyId";
    public static final String TBLCOL_PROPERTYNAME = "propertyName";
    public static final String TBLCOL_PROPERTYTYPE = "propertyType";
    public static final String TBLCOL_PROPERTYVALUE = "propertyValue";
    public static final String TBLCOL_QUALIFIERNAME = "qualifierName";
    public static final String TBLCOL_QUALIFIERTYPE = "qualifierType";
    public static final String TBLCOL_QUALIFIERVALUE = "qualifierValue";
    public static final String TBLCOL_REFERENCEASSOCIATION = "referenceAssociation";
    public static final String TBLCOL_REFERENCECODE = "referenceCode";
    public static final String TBLCOL_REFERENCENAME = "referenceName";
    public static final String TBLCOL_REFERENCETYPE = "referenceType";
    public static final String TBLCOL_REFERENCEENTRYID = "referenceEntryId";
    public static final String TBLCOL_REGISTEREDNAME = "registeredName";
    public static final String TBLCOL_RELATIONNAME = "relationName";
    public static final String TBLCOL_RELATIVEORDER = "relativeOrder";
    public static final String TBLCOL_RELEASEAGENCY = "releaseAgency";
    public static final String TBLCOL_RELEASEDATE = "releaseDate";
    public static final String TBLCOL_RELEASEID = "releaseId";
    public static final String TBLCOL_RELEASETYPE = "releaseType";
    public static final String TBLCOL_RELEASEURN = "releaseURN";
    public static final String TBLCOL_RELEASEURI = "releaseURI";
    public static final String TBLCOL_REPRESENTATIONALFORM = "representationalForm";
    public static final String TBLCOL_REPRESENTSREALMORCONTEXT = "represensRealmOrContext";
    public static final String TBLCOL_REPRESENTSVALUEDOMAIN = "representsValueDomain";
    public static final String TBLCOL_REPRESENTSVALUEDOMAINVERSION = "representsValueDomainVersion";
    public static final String TBLCOL_REPRESENTSVERSION = "representsVersion";
    public static final String TBLCOL_REVERSENAME = "reverseName";
    public static final String TBLCOL_REVISIONDATE = "revisionDate";
    public static final String TBLCOL_REVISIONID = "revisionId";
    public static final String TBLCOL_REVISIONRELEASEURI = "revisionReleaseURI";
    public static final String TBLCOL_RULEORDER = "ruleOrder";
    public static final String TBLCOL_SOURCECODINGSCHEMEID = "sourceCodingSchemeId";
    public static final String TBLCOL_SOURCECODINGSCHEMENAME = "sourceCodingSchemeName";
    public static final String TBLCOL_SOURCEENTITYCODE = "sourceEntityCode";
    public static final String TBLCOL_SOURCEENTITYCODENAMESPACE = "sourceEntityCodeNamespace";
    public static final String TBLCOL_SOURCEID = "sourceId";
    public static final String TBLCOL_SOURCEPROPERTYID = "sourcePropertyId";
    public static final String TBLCOL_SOURCETYPE = "sourceType";
    public static final String TBLCOL_STATUS = "status";
    public static final String TBLCOL_SUPPORTEDATTRIBUTETAG = "supportedAttributeTag";
    public static final String TBLCOL_TARGETCODINGSCHEME = "targetCodingScheme";
    public static final String TBLCOL_TARGETCODINGSCHEMEID = "targetCodingSchemeId";
    public static final String TBLCOL_TARGETCODINGSCHEMENAME = "targetCodingSchemeName";
    public static final String TBLCOL_TARGETTOSOURCE = "targetToSource";
    public static final String TBLCOL_TARGETENTITYCODE = "targetEntityCode";
    public static final String TBLCOL_TARGETENTITYCODENAMESPACE = "targetEntityCodeNamespace";
    public static final String TBLCOL_TARGETID = "targetId";
    public static final String TBLCOL_TARGETPROPERTYID = "targetPropertyId";
    public static final String TBLCOL_TARGETTYPE = "targetType";
    public static final String TBLCOL_TESTSUBSUMPTION = "testSubsumption";
    public static final String TBLCOL_TRANSITIVECLOSURE = "transitiveClosure";
    public static final String TBLCOL_TYPENAME = "typeName";
    public static final String TBLCOL_URN = "urn";
    public static final String TBLCOL_URI = "uri";
    public static final String TBLCOL_VAL1 = "val1";
    public static final String TBLCOL_VAL2 = "val2";
    public static final String TBLCOL_VALUE = "value";
    public static final String TBLCOL_VALUEDOMAIN = "valueDomain";
    public static final String TBLCOL_VALUEDOMAINENTRYID = "valueDomainEntryId";
    public static final String TBLCOL_VALUEDOMAINNAME = "valueDomainName";
    public static final String TBLCOL_VALUEDOMAINREFERENCE = "valueDomainReference";
    public static final String TBLCOL_VALUEDOMAINURI = "valueDomainURI";
    public static final String TBLCOL_VERSION = "version";
    public static final String TBLCOL_VERSIONDATE = "versionDate";
    public static final String TBLCOL_VERSIONORDER = "versionOrder";

    // Some fixed values that are used in table columns
    public static final String TBLCOLVAL_PRESENTATION = "presentation";
    public static final String TBLCOLVAL_DEFINITION = "definition";
    public static final String TBLCOLVAL_COMMENT = "comment";
    public static final String TBLCOLVAL_INSTRUCTION = "instruction";
    public static final String TBLCOLVAL_PROPERTY = "property";
    public static final String TBLCOLVAL_PROPERTYNAME = "propertyname";
    public static final String TBLCOLVAL_SOURCE = "source";
    public static final String TBLCOLVAL_USAGECONTEXT = "usageContext";
    public static final String TBLCOLVAL_QUALIFIER = "qualifier";
    public static final String TBLCOLVAL_LOCALNAME = "localName";
    public static final String TBLCOLVAL_HIERARCHY = "hierarchy";
    public static final String TBLCOLVAL_SUPPTAG_ASSOCIATION = "Association";
    public static final String TBLCOLVAL_SUPPTAG_ASSOCIATIONQUALIFIER = "AssociationQualifier";
    public static final String TBLCOLVAL_SUPPTAG_CODINGSCHEME = "CodingScheme";
    public static final String TBLCOLVAL_SUPPTAG_CONCEPTSTATUS = "ConceptStatus";
    public static final String TBLCOLVAL_SUPPTAG_CONTAINERNAME = "ContainerName";
    public static final String TBLCOLVAL_SUPPTAG_CONTEXT = "Context";
    public static final String TBLCOLVAL_SUPPTAG_DATATYPE = "DataType";
    public static final String TBLCOLVAL_SUPPTAG_DEGREEOFFIDELITY = "DegreeOfFidelity";
    public static final String TBLCOLVAL_SUPPTAG_ENTITYTYPE = "EntityType";
    public static final String TBLCOLVAL_SUPPTAG_FORMAT = "Format";
    public static final String TBLCOLVAL_SUPPTAG_HIERARCHY = "Hierarchy";
    public static final String TBLCOLVAL_SUPPTAG_LANGUAGE = "Language";
    public static final String TBLCOLVAL_SUPPTAG_NAMESPACE = "Namespace";
    public static final String TBLCOLVAL_SUPPTAG_PROPERTY = "Property";
    public static final String TBLCOLVAL_SUPPTAG_PROPERTYLINK = "PropertyLink";
    public static final String TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIER = "PropertyQualifier";
    public static final String TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIERTYPE = "PropertyQualifierType";
    public static final String TBLCOLVAL_SUPPTAG_PROPERTYTYPE = "PropertyType";
    public static final String TBLCOLVAL_SUPPTAG_REPRESENTATIONALFORM = "RepresentationalForm";
    public static final String TBLCOLVAL_SUPPTAG_ROLEGROUP = "RoleGroup";
    public static final String TBLCOLVAL_SUPPTAG_SORTORDER = "SortOrder";
    public static final String TBLCOLVAL_SUPPTAG_SOURCE = "Source";
    public static final String TBLCOLVAL_SUPPTAG_SOURCEROLE = "SourceRole";
    public static final String TBLCOLVAL_SUPPTAG_CONCEPTDOMAIN = "ConceptDomain";
    public static final String TBLCOLVAL_SUPPTAG_STATUS = "Status";

    public static final String TBLCOLVAL_DC_CODINGSCHEMES = "codingSchemes";
    public static final String TBLCOLVAL_DC_CONCEPTS = "concepts";
    public static final String TBLCOLVAL_DC_RELATIONS = "relations";
    public static final String TBLCOLVAL_DC_MAPPINGS = "mappings";
    public static final String TBLCOLVAL_DC_VALUEDOMAINS = "valueDomains";

    public static final String TBLCOLVAL_STATUS_ACTIVE = "Active";

    public static final String TBLCOLVAL_TEXTUALPRESENTATION = "textualPresentation";
    public static final String TBLCOLVAL_HASSUBTYPE_ASSOCIATION = "hasSubtype";
    public static final String TBLCOLVAL_ISA_ASSOCIATION = "isA";

    public static final String TBLCOLVAL_FORMAT_TXT_PLAIN = "text/plain";
    public static final String TBLCOLVAL_FORMAT_TXT_HTML = "text/html";
    public static final String TBLCOLVAL_FORMAT_TXT_XML = "text/xml";

    public static final String ENTITYTYPE_CONCEPT = "concept";
    public static final String ENTITYTYPE_ASSOCIATION = "association";
    public static final String ENTITYTYPE_INSTANCE = "instance";

    public static final String TBLCOLVAL_MISSING = "MISSING";
    /**
     * Initialize statements
     */

    public Properties newNames = new Properties();

    private void initConvenienceVariables() {

        newNames.setProperty(TBLCOL_CODINGSCHEMEID, TBLCOL_CODINGSCHEMENAME);
        newNames.setProperty(TBLCOL_ASSOCIATIONID, TBLCOL_ASSOCIATIONNAME);
        newNames.setProperty(TBLCOL_ASSOCIATION, TBLCOL_ASSOCIATIONNAME);
        newNames.setProperty(TBLCOL_CONTAINERDC, TBLCOL_CONTAINERNAME);
        newNames.setProperty(TBLCOL_INVERSEID, TBLCOL_INVERSEID);
        newNames.setProperty(TBLCOL_ID, TBLCOL_ENTITYCODE);
        newNames.setProperty(TBLCOL_ENTITYID, TBLCOL_ENTITYCODE);
        newNames.setProperty(TBLCOL_ASSOCIATIONID, TBLCOL_ENTITYCODE);
        newNames.setProperty(TBLCOL_PROPERTYNAME, TBLCOL_PROPERTYNAME);
        newNames.setProperty(TBLCOL_TARGETID, TBLCOL_TARGETENTITYCODE);
        newNames.setProperty(TBLCOL_SOURCEID, TBLCOL_SOURCEENTITYCODE);
        newNames.setProperty(TBLCOL_SOURCECODINGSCHEMEID, TBLCOL_SOURCEENTITYCODENAMESPACE);
        newNames.setProperty(TBLCOL_TARGETCODINGSCHEMEID, TBLCOL_TARGETENTITYCODENAMESPACE);
        newNames.setProperty(TBLCOL_FORMAT, TBLCOL_FORMAT);
        newNames.setProperty(TBLCOL_URN, TBLCOL_URI);
        newNames.setProperty(TBLCOL_RELEASEURN, TBLCOL_RELEASEURI);
        newNames.setProperty(TBLCOL_REGISTEREDNAME, TBLCOL_CODINGSCHEMEURI);

        codingSchemeNameOrId = getCorrectColumnName(TBLCOL_CODINGSCHEMEID);
        containerNameOrContainerDC = getCorrectColumnName(TBLCOL_CONTAINERDC);
        associationNameOrId = getCorrectColumnName(TBLCOL_ID, TBLCOL_ASSOCIATIONNAME);
        entityCodeOrAssociationId = getCorrectColumnName(TBLCOL_ASSOCIATIONID);
        entityCodeOrAssociationName = getCorrectColumnName(TBLCOL_ASSOCIATIONNAME, TBLCOL_ENTITYCODE);
        entityCodeOrId = getCorrectColumnName(TBLCOL_ID);
        isDefinedOrNothing = getCorrectColumnName(BLANK, TBLCOL_ISDEFINED);
        isInferredOrNothing = getCorrectColumnName(BLANK, TBLCOL_ISINFERRED);
        entityCodeOrEntityId = getCorrectColumnName(TBLCOL_ENTITYID, TBLCOL_ENTITYCODE);
        sourceCSIdOrEntityCodeNS = getCorrectColumnName(TBLCOL_SOURCECODINGSCHEMEID);
        sourceEntityCodeOrId = getCorrectColumnName(TBLCOL_SOURCEID);
        targetCSIdOrEntityCodeNS = getCorrectColumnName(TBLCOL_TARGETCODINGSCHEMEID);
        targetEntityCodeOrId = getCorrectColumnName(TBLCOL_TARGETID);
        idOrDataId = getCorrectColumnName(TBLCOL_DATAID, TBLCOL_DATAID);
        inverseOrInverseId = getCorrectColumnName(TBLCOL_INVERSEID);
        propertyOrPropertyName = getCorrectColumnName(TBLCOL_PROPERTYNAME);
        formatOrPresentationFormat = getCorrectColumnName(TBLCOL_FORMAT);
        urnOruri = getCorrectColumnName(TBLCOL_URN);
        releaseURNOrreleaseURI = getCorrectColumnName(TBLCOL_RELEASEURN);
        registeredNameOrCSURI = getCorrectColumnName(TBLCOL_REGISTEREDNAME);

        // conditionals
        entityTypeColDef = supports2009Model() ? " " : (" ^" + TBLCOL_ENTITYTYPE + "^ {limitedText}(15) default '"
                + ENTITYTYPE_CONCEPT + "' NOT NULL,");

        sourceTypeColDef = supports2009Model() ? " " : (" ^" + TBLCOL_SOURCETYPE + "^ {limitedText}(15) default '"
                + ENTITYTYPE_CONCEPT + "' NOT NULL,");

        targetTypeColDef = supports2009Model() ? " " : (" ^" + TBLCOL_TARGETTYPE + "^ {limitedText}(15) default '"
                + ENTITYTYPE_CONCEPT + "' NOT NULL,");

        entityType = TBLCOL_ENTITYTYPE;
    }

    public void init() {
        initConvenienceVariables();

        if (supports2009Model()) {
            // New 2009/01 LexGrid schema introduced with 1.7 or higher (Jan/Feb 2009)
            tableNames.put(CODING_SCHEME, TBL_CODING_SCHEME);
            tableNames.put(CODING_SCHEME_MULTI_ATTRIBUTES, TBL_CODING_SCHEME_MULTI_ATTRIBUTES);
            tableNames.put(CODING_SCHEME_SUPPORTED_ATTRIBUTES, TBL_CODING_SCHEME_SUPPORTED_ATTRIBUTES);
            tableNames.put(ENTITY, TBL_ENTITY);
            tableNames.put(ENTITY_TYPE, TBL_ENTITY_TYPE);
            tableNames.put(INSTANCE, TBL_INSTANCE);
            tableNames.put(ENTITY_PROPERTY, TBL_ENTITY_PROPERTY);
            tableNames.put(ENTITY_PROPERTY_MULTI_ATTRIBUTES, TBL_ENTITY_PROPERTY_MULTI_ATTRIBUTES);
            tableNames.put(RELATION, TBL_RELATION);
            tableNames.put(RELATION_MULTI_ATTRIBUTES, TBL_RELATION_MULTI_ATTRIBUTES);
            tableNames.put(ASSOCIATION, TBL_ASSOCIATION);
            tableNames.put(ENTITY_ASSOCIATION_TO_ENTITY, TBL_ENTITY_ASSOCIATION_TO_ENTITY);
            tableNames.put(ENTITY_ASSOCIATION_TO_DATA, TBL_ENTITY_ASSOCIATION_TO_DATA);
            tableNames.put(ENTITY_ASSOCIATION_TO_E_QUALS, TBL_ENTITY_ASSOCIATION_TO_E_QUALS);
            tableNames.put(ENTITY_ASSOCIATION_TO_D_QUALS, TBL_ENTITY_ASSOCIATION_TO_D_QUALS);
            tableNames.put(ENTITY_PROPERTY_LINKS, TBL_ENTITY_PROPERTY_LINKS);
            tableNames.put(ENTRY_STATE, TBL_ENTRY_STATE);
            tableNames.put(LEXGRID_TABLE_META_DATA, TBL_LEXGRID_TABLE_META_DATA);
            tableNames.put(ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE, TBL_ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE);
            tableNames.put(CODING_SCHEME_PROP, TBL_CODING_SCHEME_PROP);
            tableNames.put(CODING_SCHEME_PROP_MULTI_ATTRIB, TBL_CODING_SCHEME_PROP_MULTI_ATTRIB);
            tableNames.put(SYSTEM_RELEASE, TBL_SYSTEM_RELEASE);
            tableNames.put(SYSTEM_RELEASE_REFS, TBL_SYSTEM_RELEASE_REFS);
            tableNames.put(CODING_SCHEME_VERSIONS, TBL_CODING_SCHEME_VERSIONS);
            tableNames.put(NCI_THESAURUS_HISTORY, TBL_NCI_THESAURUS_HISTORY);
            tableNames.put(CONCEPT_HISTORY, TBL_CONCEPT_HISTORY);
        } else {
            // Older version 2008/01 LexGrid schema introduced with 1.6 or
            // higher (April/May 2008)
            tableNames.put(CODING_SCHEME, TBL_CODING_SCHEME);
            tableNames.put(CODING_SCHEME_MULTI_ATTRIBUTES, TBL_CODING_SCHEME_MULTI_ATTRIBUTES);
            tableNames.put(CODING_SCHEME_SUPPORTED_ATTRIBUTES, TBL_CODING_SCHEME_SUPPORTED_ATTRIBUTES);
            tableNames.put(ENTITY, TBL_CONCEPT);
            tableNames.put(INSTANCE, TBL_INSTANCE);
            tableNames.put(ENTITY_PROPERTY, TBL_ENTITY_PROPERTY);
            tableNames.put(ENTITY_PROPERTY_MULTI_ATTRIBUTES, TBL_ENTITY_PROPERTY_MULTI_ATTRIBUTES);
            tableNames.put(RELATION, TBL_RELATION);
            tableNames.put(RELATION_MULTI_ATTRIBUTES, TBL_RELATION_MULTI_ATTRIBUTES);
            tableNames.put(ASSOCIATION, TBL_ASSOCIATION);
            tableNames.put(ENTITY_ASSOCIATION_TO_ENTITY, TBL_ENTITY_ASSOCIATION_TO_ENTITY);
            tableNames.put(ENTITY_ASSOCIATION_TO_DATA, TBL_ENTITY_ASSOCIATION_TO_DATA);
            tableNames.put(ENTITY_ASSOCIATION_TO_E_QUALS, TBL_ENTITY_ASSOCIATION_TO_E_QUALS);
            tableNames.put(ENTITY_ASSOCIATION_TO_D_QUALS, TBL_ENTITY_ASSOCIATION_TO_D_QUALS);
            tableNames.put(ENTITY_PROPERTY_LINKS, TBL_ENTITY_PROPERTY_LINKS);
            tableNames.put(LEXGRID_TABLE_META_DATA, TBL_LEXGRID_TABLE_META_DATA);
            tableNames.put(ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE, TBL_ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE);
            tableNames.put(CODING_SCHEME_PROP, TBL_CODING_SCHEME_PROP);
            tableNames.put(CODING_SCHEME_PROP_MULTI_ATTRIB, TBL_CODING_SCHEME_PROP_MULTI_ATTRIB);
            tableNames.put(SYSTEM_RELEASE, TBL_SYSTEM_RELEASE);
            tableNames.put(SYSTEM_RELEASE_REFS, TBL_SYSTEM_RELEASE_REFS);
            tableNames.put(CODING_SCHEME_VERSIONS, TBL_CODING_SCHEME_VERSIONS);
            tableNames.put(NCI_THESAURUS_HISTORY, TBL_NCI_THESAURUS_HISTORY);
            tableNames.put(CONCEPT_HISTORY, TBL_CONCEPT_HISTORY);
            tableNames.put(ENTRY_STATE, TBL_ENTRY_STATE);
        }

        insertStatements.put(CODING_SCHEME, "INSERT INTO " + getTableName(CODING_SCHEME) + " ("
                + TBLCOL_CODINGSCHEMENAME + ", " + TBLCOL_CODINGSCHEMEURI + ", " + TBLCOL_REPRESENTSVERSION + ", "
                + TBLCOL_FORMALNAME + ", " + TBLCOL_DEFAULTLANGUAGE + ", " + TBLCOL_APPROXNUMCONCEPTS + ", "
                + TBLCOL_ISACTIVE + ", " + TBLCOL_ENTRYSTATEID + ", " + TBLCOL_RELEASEURI + ", "
                + TBLCOL_ENTITYDESCRIPTION + ", " + TBLCOL_COPYRIGHT + ") VALUES (?,?,?,?,?,?,?,?,?,?,?)");

        insertStatements.put(CODING_SCHEME_MULTI_ATTRIBUTES, "INSERT INTO "
                + getTableName(CODING_SCHEME_MULTI_ATTRIBUTES) + " (" + TBLCOL_CODINGSCHEMENAME + ", "
                + TBLCOL_TYPENAME + ", " + TBLCOL_ATTRIBUTEVALUE + ", " + TBLCOL_VAL1 + ", " + TBLCOL_VAL2
                + ") VALUES (?,?,?,?,?)");

        insertStatements.put(CODING_SCHEME_SUPPORTED_ATTRIBUTES, "INSERT INTO "
                + getTableName(CODING_SCHEME_SUPPORTED_ATTRIBUTES) + " (" + TBLCOL_CODINGSCHEMENAME + ", "
                + TBLCOL_SUPPORTEDATTRIBUTETAG + ", " + TBLCOL_ID + ", " + TBLCOL_URI + ", " + TBLCOL_IDVALUE + ", "
                + TBLCOL_VAL1 + ", " + TBLCOL_VAL2 + ") VALUES (?,?,?,?,?,?,?)");

        // Table Changed in 2008 Model
        insertStatements.put(ENTITY, "INSERT INTO " + getTableName(ENTITY) + " (" + TBLCOL_CODINGSCHEMENAME + ", "
                + TBLCOL_ENTITYCODENAMESPACE + ", " + TBLCOL_ENTITYCODE + ", " + TBLCOL_ISDEFINED + ", "
                + TBLCOL_ISANONYMOUS + ", " + TBLCOL_ISACTIVE + ", " + TBLCOL_ENTRYSTATEID + ", "
                + TBLCOL_ENTITYDESCRIPTION + ") VALUES (?,?,?,?,?,?,?,?)");

        insertStatements.put(ENTRY_STATE, "INSERT INTO " + getTableName(ENTRY_STATE) + " (" + TBLCOL_ENTRYSTATEID
                + ", " + TBLCOL_ENTRYTYPE + ", " + TBLCOL_OWNER + ", " + TBLCOL_STATUS + ", " + TBLCOL_EFFECTIVEDATE
                + ", " + TBLCOL_EXPIRATIONDATE + ", " + TBLCOL_REVISIONID + ", " + TBLCOL_PREVREVISIONID + ", "
                + TBLCOL_CHANGETYPE + ", " + TBLCOL_RELATIVEORDER +  ", " + TBLCOL_PREVENTRYSTATEID + ") VALUES (?,?,?,?,?,?,?,?,?,?,?)");

        // Table Changed in 2008 Model
        insertStatements.put(ENTITY_PROPERTY, "INSERT INTO " + getTableName(ENTITY_PROPERTY) + " ("
                + TBLCOL_CODINGSCHEMENAME + ", " + TBLCOL_ENTITYCODENAMESPACE + ", " + TBLCOL_ENTITYCODE + ", "
                + TBLCOL_PROPERTYID + ", " + TBLCOL_PROPERTYTYPE + ", " + TBLCOL_PROPERTYNAME + ", " + TBLCOL_LANGUAGE
                + ", " + TBLCOL_FORMAT + ", " + TBLCOL_ISPREFERRED + ", " + TBLCOL_DEGREEOFFIDELITY + ", "
                + TBLCOL_MATCHIFNOCONTEXT + ", " + TBLCOL_REPRESENTATIONALFORM + ", " + TBLCOL_ISACTIVE + ", "
                + TBLCOL_ENTRYSTATEID + ", " + TBLCOL_PROPERTYVALUE + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        // Table Changed in 2008 Model
        insertStatements.put(ENTITY_PROPERTY_MULTI_ATTRIBUTES, "INSERT INTO "
                + getTableName(ENTITY_PROPERTY_MULTI_ATTRIBUTES) + " (" + TBLCOL_CODINGSCHEMENAME + ", "
                + TBLCOL_ENTITYCODENAMESPACE + ", " + TBLCOL_ENTITYCODE + ", " + TBLCOL_PROPERTYID + ", "
                + TBLCOL_TYPENAME + ", " + TBLCOL_QUALIFIERTYPE + ", " + TBLCOL_ATTRIBUTEVALUE + ", " + TBLCOL_VAL1
                + ", " + TBLCOL_VAL2 + ") VALUES (?,?,?,?,?,?,?,?,?)");

        // Table Changed in 2008 Model
        insertStatements.put(RELATION, "INSERT INTO " + getTableName(RELATION) + " (" + TBLCOL_CODINGSCHEMENAME + ", "
                + TBLCOL_CONTAINERNAME + ", " + TBLCOL_ISNATIVE + ", " + TBLCOL_ENTITYDESCRIPTION
                + ") VALUES (?, ?, ?, ?)");

        // Table Changed in 2008 Model
        insertStatements.put(ASSOCIATION, "INSERT INTO " + getTableName(ASSOCIATION) + " (" + TBLCOL_CODINGSCHEMENAME
                + ", " + TBLCOL_CONTAINERNAME + ", " + TBLCOL_ENTITYCODENAMESPACE + ", " + TBLCOL_ENTITYCODE + ", " + TBLCOL_ASSOCIATIONNAME + ", "
                + TBLCOL_FORWARDNAME + ", " + TBLCOL_REVERSENAME + ", " + TBLCOL_INVERSEID + ", " + TBLCOL_ISNAVIGABLE
                + ", " + TBLCOL_ISTRANSITIVE + ", " + TBLCOL_ISANTITRANSITIVE + ", " + TBLCOL_ISSYMMETRIC + ", "
                + TBLCOL_ISANTISYMMETRIC + ", " + TBLCOL_ISREFLEXIVE + ", " + TBLCOL_ISANTIREFLEXIVE + ", "
                + TBLCOL_ISFUNCTIONAL + ", " + TBLCOL_ISREVERSEFUNCTIONAL + ", " + TBLCOL_ENTRYSTATEID + ", " 
                + TBLCOL_ENTITYDESCRIPTION + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        // Table Changed in 2008 Model
        insertStatements.put(ENTITY_ASSOCIATION_TO_ENTITY, "INSERT INTO " + getTableName(ENTITY_ASSOCIATION_TO_ENTITY)
                + " (" + TBLCOL_CODINGSCHEMENAME + ", " + TBLCOL_CONTAINERNAME + ", " 
                + TBLCOL_ENTITYCODENAMESPACE + ", " + TBLCOL_ENTITYCODE + ", "
                + TBLCOL_SOURCEENTITYCODENAMESPACE + ", " + TBLCOL_SOURCEENTITYCODE + ", "
                + TBLCOL_TARGETENTITYCODENAMESPACE + ", " + TBLCOL_TARGETENTITYCODE + ", " + TBLCOL_MULTIATTRIBUTESKEY
                + ", " + TBLCOL_ASSOCIATIONINSTANCEID + ", " + TBLCOL_ISDEFINING + ", " + TBLCOL_ISINFERRED + ", "
                + TBLCOL_ISACTIVE + ", " + TBLCOL_ENTRYSTATEID + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        // Table Changed in 2008 Model
        insertStatements.put(ENTITY_ASSOCIATION_TO_DATA, "INSERT INTO " + getTableName(ENTITY_ASSOCIATION_TO_DATA)
                + " (" + TBLCOL_CODINGSCHEMENAME + ", " + TBLCOL_CONTAINERNAME + ", " 
                + TBLCOL_ENTITYCODENAMESPACE + ", " + TBLCOL_ENTITYCODE + ", "
                + TBLCOL_SOURCEENTITYCODENAMESPACE + ", " + TBLCOL_SOURCEENTITYCODE + ", " + TBLCOL_MULTIATTRIBUTESKEY
                + ", " + TBLCOL_ASSOCIATIONINSTANCEID + ", " + TBLCOL_ISDEFINING + ", " + TBLCOL_ISINFERRED + ", "
                + TBLCOL_ISACTIVE + ", " + TBLCOL_ENTRYSTATEID + ", " + TBLCOL_DATAVALUE
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        // Table Changed in 2008 Model
        insertStatements.put(ENTITY_ASSOCIATION_TO_E_QUALS, "INSERT INTO "
                + getTableName(ENTITY_ASSOCIATION_TO_E_QUALS) + " (" + TBLCOL_CODINGSCHEMENAME + ", "
                + TBLCOL_MULTIATTRIBUTESKEY + ", " + TBLCOL_QUALIFIERNAME + ", " + TBLCOL_QUALIFIERVALUE
                + ") VALUES (?, ?, ?, ?)");

        // Table Changed in 2008 Model
        insertStatements.put(ENTITY_ASSOCIATION_TO_D_QUALS, "INSERT INTO "
                + getTableName(ENTITY_ASSOCIATION_TO_D_QUALS) + " (" + TBLCOL_CODINGSCHEMENAME + ", "
                + TBLCOL_MULTIATTRIBUTESKEY + ", " + TBLCOL_QUALIFIERNAME + ", " + TBLCOL_QUALIFIERVALUE
                + ") VALUES (?, ?, ?, ?)");

        // Table Changed in 2008 Model
        insertStatements.put(ENTITY_PROPERTY_LINKS, "INSERT INTO " + getTableName(ENTITY_PROPERTY_LINKS) + " ("
                + TBLCOL_CODINGSCHEMENAME + ", " + TBLCOL_ENTITYCODENAMESPACE + ", " + TBLCOL_ENTITYCODE + ", "
                + TBLCOL_SOURCEPROPERTYID + ", " + TBLCOL_LINK + ", " + TBLCOL_TARGETPROPERTYID
                + ") VALUES (?, ?, ?, ?, ?, ?)");

        // Table Changed in 2008 Model
        insertStatements.put(ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE, "INSERT INTO "
                + getTableName(ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE) + " (" + TBLCOL_CODINGSCHEMENAME + ", "
                + TBLCOL_CONTAINERNAME + ", " + TBLCOL_ENTITYCODENAMESPACE + ", "
                + TBLCOL_ENTITYCODE + ", " + TBLCOL_SOURCEENTITYCODENAMESPACE + ", "
                + TBLCOL_SOURCEENTITYCODE + ", " + TBLCOL_TARGETENTITYCODENAMESPACE + ", " + TBLCOL_TARGETENTITYCODE
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

        // Table Changed in 2008 Model
        insertStatements.put(CODING_SCHEME_PROP, "INSERT INTO " + getTableName(CODING_SCHEME_PROP) + " ("
                + TBLCOL_CODINGSCHEMENAME + ", " + TBLCOL_PROPERTYID + ", " + TBLCOL_PROPERTYNAME + ", "
                + TBLCOL_LANGUAGE + ", " + TBLCOL_FORMAT + ", " + TBLCOL_ISACTIVE + ", " + TBLCOL_ENTRYSTATEID + ", "
                + TBLCOL_PROPERTYVALUE + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

        // Table Changed in 2008 Model
        insertStatements.put(NCI_THESAURUS_HISTORY, "INSERT INTO " + getTableName(NCI_THESAURUS_HISTORY) + " ("
                + entityCodeOrEntityId + ", " + TBLCOL_CONCEPTNAME + ", " + TBLCOL_EDITACTION + ", " + TBLCOL_EDITDATE
                + ", " + TBLCOL_REFERENCECODE + ", " + TBLCOL_REFERENCENAME + ") VALUES (?, ?, ?, ?, ?, ?)");

        insertStatements.put(CONCEPT_HISTORY, "INSERT INTO " + getTableName(CONCEPT_HISTORY) + " ("
                + entityCodeOrEntityId + ", " + TBLCOL_CONCEPTNAME + ", " + TBLCOL_EDITACTION + ", " + TBLCOL_EDITDATE
                + ", " + TBLCOL_REFERENCECODE + ", " + TBLCOL_REFERENCENAME + ") VALUES (?, ?, ?, ?, ?, ?)");

        insertStatements.put(CODING_SCHEME_PROP_MULTI_ATTRIB, "INSERT INTO "
                + getTableName(CODING_SCHEME_PROP_MULTI_ATTRIB) + " (" + TBLCOL_CODINGSCHEMENAME + ", "
                + TBLCOL_PROPERTYID + ", " + TBLCOL_TYPENAME + ", " + TBLCOL_ATTRIBUTEVALUE + ", " + TBLCOL_VAL1 + ", "
                + TBLCOL_VAL2 + ") VALUES (?, ?, ?, ?, ?, ?)");

        insertStatements.put(CODING_SCHEME_VERSIONS, "INSERT INTO " + getTableName(CODING_SCHEME_VERSIONS) + " ("
                + TBLCOL_CODINGSCHEMENAME + ", " + TBLCOL_CODINGSCHEMEKEY + ", " + TBLCOL_VERSION + ", "
                + TBLCOL_ISCOMPLETE + ", " + TBLCOL_VERSIONDATE + ", " + TBLCOL_EFFECTIVEDATE + ", "
                + TBLCOL_RELEASEURN + ", " + TBLCOL_ENTITYDESCRIPTION + ", " + TBLCOL_CHANGEDOCUMENTATION + ", "
                + TBLCOL_CHANGEINSTRUCTIONS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        insertStatements.put(LEXGRID_TABLE_META_DATA, "INSERT INTO " + getTableName(LEXGRID_TABLE_META_DATA) + " ("
                + TBLCOL_VERSION + ", " + TBLCOL_DESCRIPTION + ") VALUES (?, ?)");
        
        

        if (supports2009Model()) {
            insertStatements.put(ENTITY_TYPE, "INSERT INTO " + getTableName(ENTITY_TYPE) + " ("
                    + TBLCOL_CODINGSCHEMENAME + ", " + TBLCOL_ENTITYCODENAMESPACE + ", " + TBLCOL_ENTITYCODE + ", "
                    + TBLCOL_ENTITYTYPE + ") VALUES (?,?,?,?)");
            
            insertStatements.put(SYSTEM_RELEASE, "INSERT INTO " + getTableName(SYSTEM_RELEASE) + " ("
                    + TBLCOL_RELEASEID + ", " + TBLCOL_RELEASEURI + ", " + TBLCOL_BASEDONRELEASE + ", "
                    + TBLCOL_RELEASEDATE + ", " + TBLCOL_RELEASEAGENCY + ", " + TBLCOL_ENTITYDESCRIPTION
                    + ") VALUES (?, ?, ?, ?, ?, ?)");
        } else {
            insertStatements.put(SYSTEM_RELEASE, "INSERT INTO " + getTableName(SYSTEM_RELEASE) + " ("
                    + TBLCOL_RELEASEID + ", " + TBLCOL_RELEASEURN + ", " + TBLCOL_BASEDONRELEASE + ", "
                    + TBLCOL_RELEASEDATE + ", " + TBLCOL_RELEASEAGENCY + ", " + TBLCOL_ENTITYDESCRIPTION
                    + ") VALUES (?, ?, ?, ?, ?, ?)");
        }

        insertStatements.put(SYSTEM_RELEASE_REFS, "INSERT INTO " + getTableName(SYSTEM_RELEASE_REFS) + " ("
                + TBLCOL_RELEASEID + ", " + TBLCOL_RELEASETYPE + ", " + TBLCOL_REFERENCETYPE + ", " + TBLCOL_VERSION
                + ", " + TBLCOL_LOCALID + ", " + TBLCOL_URN + ") VALUES (?, ?, ?, ?, ?, ?)");

        

        if (supports2009Model()) {
            updateStatements.put(SYSTEM_RELEASE, "UPDATE " + getTableName(SYSTEM_RELEASE) + " SET " + TBLCOL_RELEASEURI
                    + "= ?, " + TBLCOL_BASEDONRELEASE + "= ?, " + TBLCOL_RELEASEDATE + "= ?, " + TBLCOL_RELEASEAGENCY
                    + "= ?, " + TBLCOL_ENTITYDESCRIPTION + "= ? WHERE " + TBLCOL_RELEASEID + " = ?");
        } else {
            updateStatements.put(SYSTEM_RELEASE, "UPDATE " + getTableName(SYSTEM_RELEASE) + " SET " + TBLCOL_RELEASEURN
                    + "= ?, " + TBLCOL_BASEDONRELEASE + "= ?, " + TBLCOL_RELEASEDATE + "= ?, " + TBLCOL_RELEASEAGENCY
                    + "= ?, " + TBLCOL_ENTITYDESCRIPTION + "= ? WHERE " + TBLCOL_RELEASEID + " = ?");
        }

        updateStatements.put(ENTITY, "UPDATE " + getTableName(ENTITY) + " SET " + TBLCOL_FIRSTRELEASE + "= ?, "
                + TBLCOL_MODIFIEDINRELEASE + "= ?, " + TBLCOL_DEPRECATED + "= ?, " + TBLCOL_ISACTIVE + " = ?, "
                + (supports2009Model() ? (TBLCOL_ISDEFINED + " = ?, " + TBLCOL_ISINFERRED + " = ?, ") : "")
                + TBLCOL_CONCEPTSTATUS + " = ?, " + TBLCOL_ISANONYMOUS + " = ?, " + TBLCOL_ENTITYDESCRIPTION
                + " = ? WHERE " + codingSchemeNameOrId + " = ? AND " + entityCodeOrId + "= ?");
    }

    /**
     * Returns pre-defined statements
     * 
     * @return statements
     */
    public String[] getTableKeys() {
        Enumeration temp = insertStatements.keys();
        String[] keys = new String[insertStatements.size()];
        int i = 0;
        while (temp.hasMoreElements()) {
            keys[i++] = (String) temp.nextElement();
        }
        return keys;
    }

    /**
     * Returns a specific statement based on a key
     * 
     * @param key
     *            the search key
     * @return the sql statement
     */
    public String getInsertStatementSQL(String key) {
        String t = (String) insertStatements.get(key);
        if (t == null) {
            throw new RuntimeException("Insert statement not available for the table '" + key + "'");
        } else {
            return t;
        }
    }

    /**
     * Returns a specific statement based on a key
     * 
     * @param key
     *            the search key
     * @return the sql statement
     */
    public String getUpdateStatementSQL(String key) {
        return (String) updateStatements.get(key);
    }

    public String getTableName(String key) {
        String t = (String) tableNames.get(key);
        if (t == null) {
            throw new RuntimeException("The table name for'" + key + "' is not available");
        }
        if (tablePrefix_ != null && tablePrefix_.length() > 0) {
            return tablePrefix_ + t;
        } else {
            return t;
        }
    }

    public boolean supports2009Model() {
        if (version_.equals("1.7") || version_.equals("1.8")) {
            return true;
        } else {
            return false;
        }
    }

    public String getVersion() {
        return version_;
    }

    public String getCorrectColumnName(String existing, String changed) {
        return (supports2009Model() ? changed : existing);
    }

    // Get New Column Name for the tables, if changed in new model
    public String getCorrectColumnName(String existingName) {
        if (existingName == null)
            return null;

        if (supports2009Model() && (newNames.containsKey(existingName)))
            return newNames.getProperty(existingName);

        return existingName;
    }

    public String getTablePrefix() {
        return tablePrefix_;
    }
}