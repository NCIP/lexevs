--SQLWAYS_EVAL# 
--SQLWAYS_EVAL# Enterprise Architect Version 7.5.850
--SQLWAYS_EVAL# Tuesday, 01 June, 2010 
--SQLWAYS_EVAL# Oracle 
--SQLWAYS_EVAL# 




--SQLWAYS_EVAL# 
CREATE TABLE @PREFIX@associationPredicate
(
   associationPredicateGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifier for a given associationPredicate. 
   relationGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# the corresponding relation container. 
   associationName VARCHAR(100)   NOT NULL    --SQLWAYS_EVAL# of the relation itself.  associationName must match a local id of a supportedAssociationName in the corresponding mappings section. 
);

COMMENT ON TABLE @PREFIX@associationPredicate IS 'SQLWAYS_EVAL# of the relation itself.  associationName must match a local id of a supportedAssociationName in the corresponding mappings section.';
COMMENT ON COLUMN @PREFIX@associationPredicate.associationPredicateGuid  IS 'SQLWAYS_EVAL# for a given associationPredicate.';
COMMENT ON COLUMN @PREFIX@associationPredicate.relationGuid              IS 'SQLWAYS_EVAL# the corresponding relation container.';
COMMENT ON COLUMN @PREFIX@associationPredicate.associationName           IS 'SQLWAYS_EVAL# of the relation itself.  associationName must match a local id of a supportedAssociationName in the corresponding mappings section.';

CREATE TABLE @PREFIX@codingScheme
(
   codingSchemeGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifier for codingScheme entries. 
   codingSchemeName VARCHAR(50)   NOT NULL,    --SQLWAYS_EVAL# used to uniquely reference a coding scheme within the context of a message or container. codingSchemeName must match a local id of a supportedCodingScheme in the mappings table. 
   codingSchemeURI VARCHAR(250)   NOT NULL,    --SQLWAYS_EVAL# URI for a coding scheme. This URI must also be present in the supportedCodingScheme entry that matches the codingSchemeName. 
   representsVersion VARCHAR(50),    --SQLWAYS_EVAL# version of the resource represented by this coding scheme. 
   formalName VARCHAR(250),    --SQLWAYS_EVAL# or common name by which a coding scheme is known. (optional) 
   defaultLanguage VARCHAR(32),    --SQLWAYS_EVAL# of the language that is used in all presentations, descriptions, etc. in this coding scheme if not otherwise specified. defaultLanguage must match a local id of a supportedLanguage in the mappings table. 
   approxNumConcepts BIGINT,    --SQLWAYS_EVAL# number of entries in the lexical portion of this scheme. This is used as a hint for browsers and services. (optional) 
   description CLOB(2G) NOT LOGGED,    --SQLWAYS_EVAL# the content of the coding scheme. 
   copyright CLOB(2G) NOT LOGGED,    --SQLWAYS_EVAL# rights held in and over the resource. Typically, copyright information includes a statement about various property rights associated with the resource, including intellectual property rights. (optional) 
   isActive CHAR(1) DEFAULT '1',    --SQLWAYS_EVAL# to indicate the given coding scheme is active or not. 
   owner VARCHAR(250),    --SQLWAYS_EVAL# the resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent. 
   status VARCHAR(50),    --SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above. 
   effectiveDate TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate.  If omitted, all temporal contexts are considered to be valid. 
   expirationDate TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid. 
   releaseGuid VARCHAR(36),    --SQLWAYS_EVAL# release in which the given coding scheme is loaded. This field is a nullable field as a coding scheme can be loaded alone, with out a system release. 
   entryStateGuid VARCHAR(36)    --SQLWAYS_EVAL# to the entry state details of the coding scheme. 
);

COMMENT ON TABLE @PREFIX@codingScheme IS 'SQLWAYS_EVAL# identifiers and metadata. A resource that makes assertions about a collection of terminological entities.';
COMMENT ON COLUMN @PREFIX@codingScheme.codingSchemeGuid   IS 'SQLWAYS_EVAL# for codingScheme entries.';
COMMENT ON COLUMN @PREFIX@codingScheme.codingSchemeName   IS 'SQLWAYS_EVAL# used to uniquely reference a coding scheme within the context of a message or container. codingSchemeName must match a local id of a supportedCodingScheme in the mappings table.';
COMMENT ON COLUMN @PREFIX@codingScheme.codingSchemeURI    IS 'SQLWAYS_EVAL# for a coding scheme. This URI must also be present in the supportedCodingScheme entry that matches the codingSchemeName.';
COMMENT ON COLUMN @PREFIX@codingScheme.representsVersion  IS 'SQLWAYS_EVAL# of the resource represented by this coding scheme.';
COMMENT ON COLUMN @PREFIX@codingScheme.formalName         IS 'SQLWAYS_EVAL# common name by which a coding scheme is known. (optional)';
COMMENT ON COLUMN @PREFIX@codingScheme.defaultLanguage    IS 'SQLWAYS_EVAL# of the language that is used in all presentations, descriptions, etc. in this coding scheme if not otherwise specified. defaultLanguage must match a local id of a supportedLanguage in the mappings table.';
COMMENT ON COLUMN @PREFIX@codingScheme.approxNumConcepts  IS 'SQLWAYS_EVAL# number of entries in the lexical portion of this scheme. This is used as a hint for browsers and services. (optional)';
COMMENT ON COLUMN @PREFIX@codingScheme.description        IS 'SQLWAYS_EVAL# the content of the coding scheme.';
COMMENT ON COLUMN @PREFIX@codingScheme.copyright          IS 'SQLWAYS_EVAL# rights held in and over the resource. Typically, copyright information includes a statement about various property rights associated with the resource, including intellectual property rights. (optional)';
COMMENT ON COLUMN @PREFIX@codingScheme.isActive           IS 'SQLWAYS_EVAL# indicate the given coding scheme is active or not.';
COMMENT ON COLUMN @PREFIX@codingScheme.owner              IS 'SQLWAYS_EVAL# resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent.';
COMMENT ON COLUMN @PREFIX@codingScheme.status             IS 'SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above.';
COMMENT ON COLUMN @PREFIX@codingScheme.effectiveDate      IS 'SQLWAYS_EVAL# that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate.  If omitted, all temporal contexts are considered to be valid.';
COMMENT ON COLUMN @PREFIX@codingScheme.expirationDate     IS 'SQLWAYS_EVAL# that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be va';
COMMENT ON COLUMN @PREFIX@codingScheme.releaseGuid        IS 'SQLWAYS_EVAL# release in which the given coding scheme is loaded. This field is a nullable field as a coding scheme can be loaded alone, with out a system release.';
COMMENT ON COLUMN @PREFIX@codingScheme.entryStateGuid     IS 'SQLWAYS_EVAL# to the entry state details of the coding scheme.';

CREATE TABLE @PREFIX@csMultiAttrib
(
   csMultiAttribGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifiers for codingScheme multi attribute entries. 
   codingSchemeGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# the corresponding codingScheme. 
   attributeType VARCHAR(30)   NOT NULL,    --SQLWAYS_EVAL# attribute stored. Typically "localName" or "source". 
   attributeValue VARCHAR(250)   NOT NULL,    --SQLWAYS_EVAL# multi attributes. 
   subRef VARCHAR(250),    --SQLWAYS_EVAL# id or other localized information within the source. Used to make a source reference more precise.  
   role VARCHAR(250),    --SQLWAYS_EVAL# of the source for this particular resource (e.g. author, distributor). 
   entryStateGuid VARCHAR(36)    --SQLWAYS_EVAL# to the entry state details. 
);

COMMENT ON TABLE @PREFIX@csMultiAttrib IS 'SQLWAYS_EVAL# localName and source for a given codingScheme.';
COMMENT ON COLUMN @PREFIX@csMultiAttrib.csMultiAttribGuid  IS 'SQLWAYS_EVAL# for codingScheme multi attribute entries.';
COMMENT ON COLUMN @PREFIX@csMultiAttrib.codingSchemeGuid   IS 'SQLWAYS_EVAL# the corresponding codingScheme.';
COMMENT ON COLUMN @PREFIX@csMultiAttrib.attributeType      IS 'SQLWAYS_EVAL# attribute stored. Typically "localName" or "source".';
COMMENT ON COLUMN @PREFIX@csMultiAttrib.attributeValue     IS 'SQLWAYS_EVAL# attributes.';
COMMENT ON COLUMN @PREFIX@csMultiAttrib.subRef             IS 'SQLWAYS_EVAL# id or other localized information within the source. Used to make a source reference more precise. ';
COMMENT ON COLUMN @PREFIX@csMultiAttrib.role               IS 'SQLWAYS_EVAL# of the source for this particular resource (e.g. author, distributor).';
COMMENT ON COLUMN @PREFIX@csMultiAttrib.entryStateGuid     IS 'SQLWAYS_EVAL# to the entry state details.';

CREATE TABLE @PREFIX@csSupportedAttrib
(
   csSuppAttribGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifier for csSupportedAttributes  table. 
   codingSchemeGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# the corresponding codingScheme. 
   supportedAttributeTag VARCHAR(30)   NOT NULL,    
   id VARCHAR(250)   NOT NULL,    --SQLWAYS_EVAL# for the supported item 
   uri VARCHAR(250),    --SQLWAYS_EVAL# uri 
   idValue VARCHAR(250),    --SQLWAYS_EVAL# supported attribute (most cases it is same as the id) 
   associationNames VARCHAR(250),    --SQLWAYS_EVAL# for supported Hierarchy. 
   rootCode VARCHAR(250),     
   isForwardNavigable CHAR(1),  
   isImported CHAR(1),     
   equivalentCodingScheme VARCHAR(250),    --SQLWAYS_EVAL# value for supportedNamespace. The local identifier of a supportedCodingScheme that correspondes with this namespace.   In many models, the namespace used to qualify a concept code has a direct correlation to the coding scheme itself. If codingSchemeName is present, it asserts that the defining information about entryCodes in this namespace can be found in the corresponding coding scheme. 
   assemblyRule VARCHAR(250),    --SQLWAYS_EVAL# value for supportedSource. Rule for combining source-specific information such as page numbers, sections and the like with the source URL. Syntax: [ID] - names the identifier. Everything else is literal. 
   assnCodingScheme VARCHAR(250),    --SQLWAYS_EVAL# of coding scheme this association is defined. 
   assnNamespace VARCHAR(250),    --SQLWAYS_EVAL# of enitycodenamespace this association is defined. 
   assnEntityCode VARCHAR(200),    --SQLWAYS_EVAL# this association is defined. 
   propertyType VARCHAR(50)    --SQLWAYS_EVAL# this mapping belongs to. Valid values are 'property','comment','definition' and 'presentation'. 
);

CREATE TABLE @PREFIX@entity
(
   entityGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifier for the entities. 
   codingSchemeGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# the corresponding codingScheme. 
   entityCode VARCHAR(200)   NOT NULL,    --SQLWAYS_EVAL# code within coding system 
   entityCodeNamespace VARCHAR(50)   NOT NULL,    --SQLWAYS_EVAL# of the namespace of the entityCode. entityCodeNamespace must match a local id of a supportedNamespace in the corresponding mapping section.  If omitted, the URI of the defaultCodingScheme will be used as the namespace for the entity code. 
   isDefined CHAR(1),    --SQLWAYS_EVAL# this entityCode is considered to be completely defined (i.e. necessary and sufficient) within the context of the containing code system. False means that only the necessary components are present.  If omitted, the state of the entityCode definition is not known. 
   isAnonymous CHAR(1),    --SQLWAYS_EVAL# the entityCode is synthetic, and doesn't actually exist in the namespace.  isAnonymous is used for synthetic top and bottom nodes as well as blank or anonymous inner class definitions.  Default: False 
   description CLOB(2G) NOT LOGGED,    --SQLWAYS_EVAL# the entity. 
   isActive CHAR(1) DEFAULT '1',    --SQLWAYS_EVAL# to indicate the given entity is active or not. 
   owner VARCHAR(250),    --SQLWAYS_EVAL# the resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent. 
   status VARCHAR(50),    --SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above. 
   effectiveDate TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate.  If omitted, all temporal contexts are considered to be valid. 
   expirationDate TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid. 
   entryStateGuid VARCHAR(36),    --SQLWAYS_EVAL# to the entry state details of the given entity. 
   forwardName VARCHAR(250),    --SQLWAYS_EVAL# of a defined association. 
   reverseName VARCHAR(250),    --SQLWAYS_EVAL# of a defined association. 
   isTransitive CHAR(1),
   isNavigable CHAR(1)
);

COMMENT ON TABLE @PREFIX@entity IS 'SQLWAYS_EVAL# entity related information. A set of lexical assertions about the intended meaning of a particular entity code.';
COMMENT ON COLUMN @PREFIX@entity.entityGuid           IS 'SQLWAYS_EVAL# for the entities.';
COMMENT ON COLUMN @PREFIX@entity.codingSchemeGuid     IS 'SQLWAYS_EVAL# the corresponding codingScheme.';
COMMENT ON COLUMN @PREFIX@entity.entityCode           IS 'SQLWAYS_EVAL# within coding system';
COMMENT ON COLUMN @PREFIX@entity.entityCodeNamespace  IS 'SQLWAYS_EVAL# of the namespace of the entityCode. entityCodeNamespace must match a local id of a supportedNamespace in the corresponding mapping section.  If omitted, the URI of the defaultCodingScheme will be used as the namespace for the entity code';
COMMENT ON COLUMN @PREFIX@entity.isDefined            IS 'SQLWAYS_EVAL# this entityCode is considered to be completely defined (i.e. necessary and sufficient) within the context of the containing code system. False means that only the necessary components are present.  If omitted, the state of the entityCode ';
COMMENT ON COLUMN @PREFIX@entity.isAnonymous          IS 'SQLWAYS_EVAL# the entityCode is synthetic, and doesn''t actually exist in the namespace.  isAnonymous is used for synthetic top and bottom nodes as well as blank or anonymous inner class definitions.  Default: False';
COMMENT ON COLUMN @PREFIX@entity.description          IS 'SQLWAYS_EVAL# the entity.';
COMMENT ON COLUMN @PREFIX@entity.isActive             IS 'SQLWAYS_EVAL# indicate the given entity is active or not.';
COMMENT ON COLUMN @PREFIX@entity.owner                IS 'SQLWAYS_EVAL# resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent.';
COMMENT ON COLUMN @PREFIX@entity.status               IS 'SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above.';
COMMENT ON COLUMN @PREFIX@entity.effectiveDate        IS 'SQLWAYS_EVAL# that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate.  If omitted, all temporal contexts are considered to be valid.';
COMMENT ON COLUMN @PREFIX@entity.expirationDate       IS 'SQLWAYS_EVAL# that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be va';
COMMENT ON COLUMN @PREFIX@entity.entryStateGuid       IS 'SQLWAYS_EVAL# to the entry state details of the given entity.';
COMMENT ON COLUMN @PREFIX@entity.forwardName          IS 'SQLWAYS_EVAL# a defined association.';
COMMENT ON COLUMN @PREFIX@entity.reverseName          IS 'SQLWAYS_EVAL# a defined association.';

CREATE TABLE @PREFIX@entityAssnQuals
(
   entityAssnQualsGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifier for the association qualifier. 
   referenceGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifier of  the triple to which the qualifier belongs to.. 
   qualifierName VARCHAR(50)   NOT NULL,    --SQLWAYS_EVAL# of a qualifier. Must be in supportedAssociationQualifier. 
   qualifierValue VARCHAR(250),    --SQLWAYS_EVAL# with this qualifier, if any. 
   entryStateGuid VARCHAR(36)
);

COMMENT ON TABLE @PREFIX@entityAssnQuals IS 'SQLWAYS_EVAL# association instance qualifiers.';
COMMENT ON COLUMN @PREFIX@entityAssnQuals.entityAssnQualsGuid  IS 'SQLWAYS_EVAL# for the association qualifier.';
COMMENT ON COLUMN @PREFIX@entityAssnQuals.referenceGuid        IS 'SQLWAYS_EVAL# of  the triple to which the qualifier belongs to..';
COMMENT ON COLUMN @PREFIX@entityAssnQuals.qualifierName        IS 'SQLWAYS_EVAL# of a qualifier. Must be in supportedAssociationQualifier.';
COMMENT ON COLUMN @PREFIX@entityAssnQuals.qualifierValue       IS 'SQLWAYS_EVAL# with this qualifier, if any.';

CREATE TABLE @PREFIX@entityAssnsToData
(
   entityAssnsDataGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifier for the association instance. 
   associationPredicateGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# the corresponding associationPredicate. 
   sourceEntityCode VARCHAR(200)   NOT NULL,    --SQLWAYS_EVAL# of the source entity. 
   sourceEntityCodeNamespace VARCHAR(50)   NOT NULL,    --SQLWAYS_EVAL# the namespace of the sourceEntityCode. sourceEntityCodeNamespace must match a local id of a supportedNamespace in the corresponding mappings section.  If omitted, the URI of sourceEntityCode namespace is the codingSchemeURI of the containing coding scheme. 
   associationInstanceId VARCHAR(50),    --SQLWAYS_EVAL# uniqely names this entry within the context of the associationInstance. 
   isDefining CHAR(1),    --SQLWAYS_EVAL# this association instance is considered to be part of the definition of source entity, false means that it is an "accidental" characteristic. If omitted (null), this information is not known. 
   isInferred CHAR(1),    --SQLWAYS_EVAL# this association instance isn't asserted in the scheme, but is inferred by a classifier.  Default: false 
   dataValue CLOB(2G) NOT LOGGED,    --SQLWAYS_EVAL# value 
   isActive CHAR(1),    --SQLWAYS_EVAL# to indicate the given instance is active or not. 
   owner VARCHAR(250),    --SQLWAYS_EVAL# the resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent. 
   status VARCHAR(50),    --SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above. 
   effectiveDate TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid. 
   expirationDate TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid. 
   entryStateGuid VARCHAR(36)    --SQLWAYS_EVAL# to the entry state details of the given triple. 
);

COMMENT ON TABLE @PREFIX@entityAssnsToData IS 'SQLWAYS_EVAL# the instance of an association, source being an entity and target being data.';
COMMENT ON COLUMN @PREFIX@entityAssnsToData.entityAssnsDataGuid        IS 'SQLWAYS_EVAL# for the association instance.';
COMMENT ON COLUMN @PREFIX@entityAssnsToData.associationPredicateGuid   IS 'SQLWAYS_EVAL# the corresponding associationPredicate.';
COMMENT ON COLUMN @PREFIX@entityAssnsToData.sourceEntityCode           IS 'SQLWAYS_EVAL# of the source entity.';
COMMENT ON COLUMN @PREFIX@entityAssnsToData.sourceEntityCodeNamespace  IS 'SQLWAYS_EVAL# namespace of the sourceEntityCode. sourceEntityCodeNamespace must match a local id of a supportedNamespace in the corresponding mappings section.  If omitted, the URI of sourceEntityCode namespace is the codingSchemeURI of the containin';
COMMENT ON COLUMN @PREFIX@entityAssnsToData.associationInstanceId      IS 'SQLWAYS_EVAL# uniqely names this entry within the context of the associationInstance.';
COMMENT ON COLUMN @PREFIX@entityAssnsToData.isDefining                 IS 'SQLWAYS_EVAL# this association instance is considered to be part of the definition of source entity, false means that it is an "accidental" characteristic. If omitted (null), this information is not known.';
COMMENT ON COLUMN @PREFIX@entityAssnsToData.isInferred                 IS 'SQLWAYS_EVAL# this association instance isn''t asserted in the scheme, but is inferred by a classifier.  Default: false';
COMMENT ON COLUMN @PREFIX@entityAssnsToData.dataValue                  IS 'SQLWAYS_EVAL# value';
COMMENT ON COLUMN @PREFIX@entityAssnsToData.isActive                   IS 'SQLWAYS_EVAL# indicate the given instance is active or not.';
COMMENT ON COLUMN @PREFIX@entityAssnsToData.owner                      IS 'SQLWAYS_EVAL# resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent.';
COMMENT ON COLUMN @PREFIX@entityAssnsToData.status                     IS 'SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above.';
COMMENT ON COLUMN @PREFIX@entityAssnsToData.effectiveDate              IS 'SQLWAYS_EVAL# that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid.';
COMMENT ON COLUMN @PREFIX@entityAssnsToData.expirationDate             IS 'SQLWAYS_EVAL# that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be va';
COMMENT ON COLUMN @PREFIX@entityAssnsToData.entryStateGuid             IS 'SQLWAYS_EVAL# to the entry state details of the given triple.';

CREATE TABLE @PREFIX@entityAssnsToEntity
(
   entityAssnsGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifier for the association instance. 
   associationPredicateGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# the corresponding associationPredicate. 
   sourceEntityCode VARCHAR(200)   NOT NULL,    --SQLWAYS_EVAL# of the source entity. 
   sourceEntityCodeNamespace VARCHAR(50)   NOT NULL,    --SQLWAYS_EVAL# the namespace of the sourceEntityCode. sourceEntityCodeNamespace must match a local id of a supportedNamespace in the corresponding mappings section.  If omitted, the URI of sourceEntityCode namespace is the codingSchemeURI of the containing coding scheme. 
   targetEntityCode VARCHAR(200)   NOT NULL,    --SQLWAYS_EVAL# of the target entity. 
   targetEntityCodeNamespace VARCHAR(50)   NOT NULL,    --SQLWAYS_EVAL# the namespace of the targetEntityCode. targetEntityCodeNamespace must match a local id of a supportedNamespace in the corresponding mappings section.  If omitted, the URI of targetEntityCode namespace is the codingSchemeURI of the containing coding scheme. 
   associationInstanceId VARCHAR(50),    --SQLWAYS_EVAL# assigned to the particular relation, from, to triple. 
   isDefining CHAR(1),    --SQLWAYS_EVAL# this association instance is considered to be part of the definition of source entity, false means that it is an "accidental" characteristic. If omitted, this information is not known. 
   isInferred CHAR(1),    --SQLWAYS_EVAL# this association instance isn't asserted in the scheme, but is inferred by a classifier.  Default: false 
   isActive CHAR(1),    --SQLWAYS_EVAL# to indicate the given instance is active or not. 
   owner VARCHAR(250),    --SQLWAYS_EVAL# the resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent. 
   status VARCHAR(50),    --SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above. 
   effectiveDate TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid. 
   expirationDate TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid. 
   entryStateGuid VARCHAR(36)    --SQLWAYS_EVAL# to the entry state details of the given triple. 
);

COMMENT ON TABLE @PREFIX@entityAssnsToEntity IS 'SQLWAYS_EVAL# the instance of an association, both source and target being entities. ';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntity.entityAssnsGuid            IS 'SQLWAYS_EVAL# for the association instance.';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntity.associationPredicateGuid   IS 'SQLWAYS_EVAL# the corresponding associationPredicate.';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntity.sourceEntityCode           IS 'SQLWAYS_EVAL# of the source entity.';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntity.sourceEntityCodeNamespace  IS 'SQLWAYS_EVAL# namespace of the sourceEntityCode. sourceEntityCodeNamespace must match a local id of a supportedNamespace in the corresponding mappings section.  If omitted, the URI of sourceEntityCode namespace is the codingSchemeURI of the containin';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntity.targetEntityCode           IS 'SQLWAYS_EVAL# of the target entity.';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntity.targetEntityCodeNamespace  IS 'SQLWAYS_EVAL# namespace of the targetEntityCode. targetEntityCodeNamespace must match a local id of a supportedNamespace in the corresponding mappings section.  If omitted, the URI of targetEntityCode namespace is the codingSchemeURI of the containin';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntity.associationInstanceId      IS 'SQLWAYS_EVAL# assigned to the particular relation, from, to triple.';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntity.isDefining                 IS 'SQLWAYS_EVAL# this association instance is considered to be part of the definition of source entity, false means that it is an "accidental" characteristic. If omitted, this information is not known.';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntity.isInferred                 IS 'SQLWAYS_EVAL# this association instance isn''t asserted in the scheme, but is inferred by a classifier.  Default: false';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntity.isActive                   IS 'SQLWAYS_EVAL# indicate the given instance is active or not.';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntity.owner                      IS 'SQLWAYS_EVAL# resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent.';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntity.status                     IS 'SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above.';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntity.effectiveDate              IS 'SQLWAYS_EVAL# that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid.';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntity.expirationDate             IS 'SQLWAYS_EVAL# that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be va';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntity.entryStateGuid             IS 'SQLWAYS_EVAL# to the entry state details of the given triple.';

CREATE TABLE @PREFIX@entityAssnsToEntityTr
(
   entityAssnsTrGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifier for the transitive relation. 
   associationPredicateGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# the corresponding association. 
   sourceEntityCode VARCHAR(200)   NOT NULL,    --SQLWAYS_EVAL# of the source entity. 
   sourceEntityCodeNamespace VARCHAR(50)   NOT NULL,    --SQLWAYS_EVAL# the namespace of the sourceEntityCode. sourceEntityCodeNamespace must match a local id of a supportedNamespace in the corresponding mappings section.  If omitted, the URI of sourceEntityCode namespace is the codingSchemeURI of the containing coding scheme. 
   targetEntityCode VARCHAR(200)   NOT NULL,    --SQLWAYS_EVAL# of the target entity. 
   targetEntityCodeNamespace VARCHAR(50)   NOT NULL    --SQLWAYS_EVAL# the namespace of the targetEntityCode. targetEntityCodeNamespace must match a local id of a supportedNamespace in the corresponding mappings section.  If omitted, the URI of targetEntityCode namespace is the codingSchemeURI of the containing coding scheme. 
);

COMMENT ON TABLE @PREFIX@entityAssnsToEntityTr IS 'SQLWAYS_EVAL# transitive relation details associated with the triple.';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntityTr.entityAssnsTrGuid          IS 'SQLWAYS_EVAL# for the transitive relation.';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntityTr.associationPredicateGuid   IS 'SQLWAYS_EVAL# the corresponding association.';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntityTr.sourceEntityCode           IS 'SQLWAYS_EVAL# of the source entity.';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntityTr.sourceEntityCodeNamespace  IS 'SQLWAYS_EVAL# namespace of the sourceEntityCode. sourceEntityCodeNamespace must match a local id of a supportedNamespace in the corresponding mappings section.  If omitted, the URI of sourceEntityCode namespace is the codingSchemeURI of the containin';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntityTr.targetEntityCode           IS 'SQLWAYS_EVAL# of the target entity.';
COMMENT ON COLUMN @PREFIX@entityAssnsToEntityTr.targetEntityCodeNamespace  IS 'SQLWAYS_EVAL# namespace of the targetEntityCode. targetEntityCodeNamespace must match a local id of a supportedNamespace in the corresponding mappings section.  If omitted, the URI of targetEntityCode namespace is the codingSchemeURI of the containin';

CREATE TABLE @PREFIX@entityType
(
   entityGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# the guid of entity table. 
   entityType VARCHAR(50)   NOT NULL    --SQLWAYS_EVAL# 
);

COMMENT ON TABLE @PREFIX@entityType IS 'SQLWAYS_EVAL# type of the entity being loaded into entity table. Primarily concept, instance and association. But the entity types are not restricted just to these three types.';
COMMENT ON COLUMN @PREFIX@entityType.entityGuid  IS 'SQLWAYS_EVAL# the guid of entity table.';
COMMENT ON COLUMN @PREFIX@entityType.entityType  IS 'Type of entity.';

CREATE TABLE @PREFIX@entryState
(
   entryStateGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifier for the entry state. 
   entryGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifier of the object being revised.  
   entryType VARCHAR(50)   NOT NULL,    --SQLWAYS_EVAL# able object being revised. 
   changeType VARCHAR(15)   NOT NULL,    --SQLWAYS_EVAL# that occurred between this state and the previous. 
   relativeOrder BIGINT   NOT NULL,    --SQLWAYS_EVAL# order that this state change should be applied within the context of the containing revision. 
   revisionGuid VARCHAR(36),    --SQLWAYS_EVAL# identifier of the revision in which the version able object is being revised. 
   prevRevisionGuid VARCHAR(36),    --SQLWAYS_EVAL# identifier of the revision in which the version able object was previously revised. 
   prevEntryStateGuid VARCHAR(36)    --SQLWAYS_EVAL# identifier of the previous entry state for the given version able object. 
);

COMMENT ON TABLE @PREFIX@entryState IS 'SQLWAYS_EVAL# entryState details of the codingScheme(s). Represents a change that occurred between the current state of the versionable entry and an immediately preceeding state of the same entry.';
COMMENT ON COLUMN @PREFIX@entryState.entryStateGuid      IS 'SQLWAYS_EVAL# for the entry state.';
COMMENT ON COLUMN @PREFIX@entryState.entryGuid           IS 'SQLWAYS_EVAL# identifier of the object being revised. ';
COMMENT ON COLUMN @PREFIX@entryState.entryType           IS 'SQLWAYS_EVAL# able object being revised.';
COMMENT ON COLUMN @PREFIX@entryState.changeType          IS 'SQLWAYS_EVAL# that occurred between this state and the previous.';
COMMENT ON COLUMN @PREFIX@entryState.relativeOrder       IS 'SQLWAYS_EVAL# that this state change should be applied within the context of the containing revision.';
COMMENT ON COLUMN @PREFIX@entryState.revisionGuid        IS 'SQLWAYS_EVAL# of the revision in which the version able object is being revised.';
COMMENT ON COLUMN @PREFIX@entryState.prevRevisionGuid    IS 'SQLWAYS_EVAL# of the revision in which the version able object was previously revised.';
COMMENT ON COLUMN @PREFIX@entryState.prevEntryStateGuid  IS 'SQLWAYS_EVAL# of the previous entry state for the given version able object.';

CREATE TABLE @PREFIX@property
(
   propertyGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifier for a given property. 
   referenceGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# object to which a given property belongs to. 
   referenceType VARCHAR(50)   NOT NULL,    --SQLWAYS_EVAL# object to which a given property belongs to. Ex: entity, codingScheme etc. 
   propertyId VARCHAR(50),    --SQLWAYS_EVAL# of the property within the context of the coded entry 
   propertyType VARCHAR(15),    --SQLWAYS_EVAL# element that a given property represents.  As an example, the codingScheme "copyright" attribute could be represented by a property with a propertyType that mapped to lgCS:copyRight. Must match a local id of a supportedPropertyType in the corresponding mappings section. 
   propertyName VARCHAR(250)   NOT NULL,    --SQLWAYS_EVAL# that defines the meaning of this particular property entry. Must match a local id of a supportedProperty in the corresponding mapping section. 
   "LANGUAGE" VARCHAR(32),     
   format VARCHAR(50),    --SQLWAYS_EVAL# of the property value. 
   isPreferred CHAR(1),    --SQLWAYS_EVAL# if the text meets the selection criteria, it should be the preferred form. For a given language there should be only one preferred presentation. 
   matchIfNoContext CHAR(1),    --SQLWAYS_EVAL# this presentation is valid in a acontextual setting - that it is always valid in the given language.  Default: true  if there are no property usageContexts, false otherwise. 
   degreeOfFidelity VARCHAR(50),    --SQLWAYS_EVAL# that states how closely a term approximates the intended meaning of an entry code. degreeOfFidelity must match a local id of a supportedDegreeOfFidelity in the corresponding mappings section. 
   representationalForm VARCHAR(50),    --SQLWAYS_EVAL# that states how the term represents the concept (abbrev, acronym, etc.) representationalForm must match a local id of a representationalForm in the corresponding mappings section. 
   propertyValue CLOB(2G) NOT LOGGED   NOT NULL,    --SQLWAYS_EVAL# the property associated with this particular resource.  Note that "text" may be any type, including a URI, html fragment, etc. 
   isActive CHAR(1) DEFAULT '1',    --SQLWAYS_EVAL# to indicate the given property is active or not. 
   owner VARCHAR(250),    --SQLWAYS_EVAL# the resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent. 
   status VARCHAR(50),    --SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above. 
   effectiveDate TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate.  If omitted, all temporal contexts are considered to be valid. 
   expirationDate TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid. 
   entryStateGuid VARCHAR(36)    --SQLWAYS_EVAL# to the entry state details of the given property. 
);


CREATE TABLE @PREFIX@propertyLinks
(
   propertyLinksGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifier for a given property link. 
   entityGuid VARCHAR(36)   NOT NULL,
   sourcePropertyGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# the source property guid. (The identifier of the first property in the link.) 
   link VARCHAR(250)   NOT NULL,    --SQLWAYS_EVAL# of the type of link between properties.  (Examples include acronymFor, abbreviationOf, spellingVariantOf, etc. Must be in supportedPropertyLink) propertyLink must match a local id of a supportedPropertyLink in the corresponding mapping section. 
   targetPropertyGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# the target property guid. (The identifier of the second property in the link.) 
   entryStateGuid VARCHAR(36)
);

COMMENT ON TABLE @PREFIX@propertyLinks IS 'SQLWAYS_EVAL# link between two properties.';
COMMENT ON COLUMN @PREFIX@propertyLinks.propertyLinksGuid   IS 'SQLWAYS_EVAL# for a given property link.';
COMMENT ON COLUMN @PREFIX@propertyLinks.sourcePropertyGuid  IS 'SQLWAYS_EVAL# the source property guid. (The identifier of the first property in the link.)';
COMMENT ON COLUMN @PREFIX@propertyLinks.link                IS 'SQLWAYS_EVAL# of the type of link between properties.  (Examples include acronymFor, abbreviationOf, spellingVariantOf, etc. Must be in supportedPropertyLink) propertyLink must match a local id of a supportedPropertyLink in the corresponding mapping sec';
COMMENT ON COLUMN @PREFIX@propertyLinks.targetPropertyGuid  IS 'SQLWAYS_EVAL# the target property guid. (The identifier of the second property in the link.)';

CREATE TABLE @PREFIX@propertyMultiAttrib
(
   propMultiAttribGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifier for multi attributes entries. 
   propertyGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# the corresponding property. 
   attributeType VARCHAR(30)   NOT NULL,    --SQLWAYS_EVAL# attribute stored. Typically "qualifier" or "source" or "usage context". 
   attributeId VARCHAR(50),    --SQLWAYS_EVAL# qualifier. In case of source and usage context value will be null. 
   attributeValue VARCHAR(250),    --SQLWAYS_EVAL# multi attributes. 
   subRef VARCHAR(250),    --SQLWAYS_EVAL# id or other localized information within the source. Used to make a source reference more precise.  
   role VARCHAR(250),    --SQLWAYS_EVAL# of the source for this particular resource (e.g. author, distributor). 
   qualifierType VARCHAR(250),    --SQLWAYS_EVAL# qualifier 
   entryStateGuid VARCHAR(36)    --SQLWAYS_EVAL# to the entry state. 
);

COMMENT ON TABLE @PREFIX@propertyMultiAttrib IS 'SQLWAYS_EVAL# property mulit attributes like qualifiers, sources and usage contexts of a given property.';
COMMENT ON COLUMN @PREFIX@propertyMultiAttrib.propMultiAttribGuid  IS 'SQLWAYS_EVAL# for multi attributes entries.';
COMMENT ON COLUMN @PREFIX@propertyMultiAttrib.propertyGuid         IS 'SQLWAYS_EVAL# the corresponding property.';
COMMENT ON COLUMN @PREFIX@propertyMultiAttrib.attributeType        IS 'SQLWAYS_EVAL# attribute stored. Typically "qualifier" or "source" or "usage context".';
COMMENT ON COLUMN @PREFIX@propertyMultiAttrib.attributeId          IS 'SQLWAYS_EVAL# qualifier. In case of source and usage context value will be null.';
COMMENT ON COLUMN @PREFIX@propertyMultiAttrib.attributeValue       IS 'SQLWAYS_EVAL# attributes.';
COMMENT ON COLUMN @PREFIX@propertyMultiAttrib.subRef               IS 'SQLWAYS_EVAL# id or other localized information within the source. Used to make a source reference more precise. ';
COMMENT ON COLUMN @PREFIX@propertyMultiAttrib.role                 IS 'SQLWAYS_EVAL# of the source for this particular resource (e.g. author, distributor).';
COMMENT ON COLUMN @PREFIX@propertyMultiAttrib.qualifierType        IS 'SQLWAYS_EVAL# qualifier';
COMMENT ON COLUMN @PREFIX@propertyMultiAttrib.entryStateGuid       IS 'SQLWAYS_EVAL# to the entry state.';

CREATE TABLE @PREFIX@relation
(
   relationGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifier for a given relation. 
   codingSchemeGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# the corresponding coding scheme. 
   containerName VARCHAR(50)   NOT NULL,    --SQLWAYS_EVAL# of a collection of associations. Required if there is one or more collection of relations in a coding scheme. Must be in supportedContainerName. 
   isMapping CHAR(1),    --SQLWAYS_EVAL# that a set of associations represents a mapping between two terminologies if value is true. 
   representsVersion VARCHAR(50),
   sourceCodingScheme VARCHAR(50),    --SQLWAYS_EVAL# in associationInstance, this is the default source codingScheme. 
   sourceCodingSchemeVersion VARCHAR(50),    --SQLWAYS_EVAL# in associationInstance, this is the default source codingScheme version. 
   targetCodingScheme VARCHAR(50),    --SQLWAYS_EVAL# in associationInstance, this is the default target codingScheme. 
   targetCodingSchemeVersion VARCHAR(50),    --SQLWAYS_EVAL# in associationInstance, this is the default source codingScheme version. 
   description CLOB(2G) NOT LOGGED,    --SQLWAYS_EVAL# the relation container. 
   isActive CHAR(1),    --SQLWAYS_EVAL# to indicate the given relation is active or not. 
   owner VARCHAR(250),    --SQLWAYS_EVAL# the resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent. 
   status VARCHAR(50),    --SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entry Status are defined by the containing system, but there needs to be a mapping into isActive above. 
   effectiveDate TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate.  If omitted, all temporal contexts are considered to be valid. 
   expirationDate TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid. 
   entryStateGuid VARCHAR(36)    --SQLWAYS_EVAL# to the entry state details of the given relation. 
);

COMMENT ON TABLE @PREFIX@relation IS 'SQLWAYS_EVAL# relations that represent a particular point of view or community.';
COMMENT ON COLUMN @PREFIX@relation.relationGuid               IS 'SQLWAYS_EVAL# for a given relation.';
COMMENT ON COLUMN @PREFIX@relation.codingSchemeGuid           IS 'SQLWAYS_EVAL# the corresponding coding scheme.';
COMMENT ON COLUMN @PREFIX@relation.containerName              IS 'SQLWAYS_EVAL# of a collection of associations. Required if there is one or more collection of relations in a coding scheme. Must be in supportedContainerName.';
COMMENT ON COLUMN @PREFIX@relation.isMapping                  IS 'SQLWAYS_EVAL# a set of associations represents a mapping between two terminologies if value is true.';
COMMENT ON COLUMN @PREFIX@relation.sourceCodingScheme         IS 'SQLWAYS_EVAL# in associationInstance, this is the default source codingScheme.';
COMMENT ON COLUMN @PREFIX@relation.sourceCodingSchemeVersion  IS 'SQLWAYS_EVAL# in associationInstance, this is the default source codingScheme version.';
COMMENT ON COLUMN @PREFIX@relation.targetCodingScheme         IS 'SQLWAYS_EVAL# in associationInstance, this is the default target codingScheme.';
COMMENT ON COLUMN @PREFIX@relation.targetCodingSchemeVersion  IS 'SQLWAYS_EVAL# in associationInstance, this is the default source codingScheme version.';
COMMENT ON COLUMN @PREFIX@relation.description                IS 'SQLWAYS_EVAL# the relation container.';
COMMENT ON COLUMN @PREFIX@relation.isActive                   IS 'SQLWAYS_EVAL# indicate the given relation is active or not.';
COMMENT ON COLUMN @PREFIX@relation.owner                      IS 'SQLWAYS_EVAL# resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent.';
COMMENT ON COLUMN @PREFIX@relation.status                     IS 'SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entry Status are defined by the containing system, but there needs to be a mapping into isActive above.';
COMMENT ON COLUMN @PREFIX@relation.effectiveDate              IS 'SQLWAYS_EVAL# that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate.  If omitted, all temporal contexts are considered to be valid.';
COMMENT ON COLUMN @PREFIX@relation.expirationDate             IS 'SQLWAYS_EVAL# that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be va';
COMMENT ON COLUMN @PREFIX@relation.entryStateGuid             IS 'SQLWAYS_EVAL# to the entry state details of the given relation.';

CREATE TABLE @PREFIX@revision
(
   revisionGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifier for revision table entry. 
   releaseGuid VARCHAR(36),    --SQLWAYS_EVAL# the GUID of systemRelease table. 
   revisionId VARCHAR(50)   NOT NULL,    --SQLWAYS_EVAL# of a given revision supplied by the source. The order of the revision is determined by the revAppliedDate.  
   changeAgent VARCHAR(50),    --SQLWAYS_EVAL# of the source(s) that participated in this particular change. changeAgent must match a local id of a supportedSource in the corresponding mappings section. 
   revisionDate TIMESTAMP,    --SQLWAYS_EVAL# for which this version is operative (considered commited).  
   revAppliedDate TIMESTAMP   NOT NULL,    --SQLWAYS_EVAL# time on which the revision is applied into the lexEVS system. 
   editOrder BIGINT,    --SQLWAYS_EVAL# order that this revision is to be applied if in a systemRelease.  
   changeInstructions CLOB(2G) NOT LOGGED,    --SQLWAYS_EVAL# readable set of instructions on how to apply this change 
   description CLOB(2G) NOT LOGGED    --SQLWAYS_EVAL# the revision contents. 
);

COMMENT ON TABLE @PREFIX@revision IS 'SQLWAYS_EVAL# of state changes that define the transformation of a set of resources from one consistent state to another.';
COMMENT ON COLUMN @PREFIX@revision.revisionGuid        IS 'SQLWAYS_EVAL# for revision table entry.';
COMMENT ON COLUMN @PREFIX@revision.releaseGuid         IS 'SQLWAYS_EVAL# the GUID of systemRelease table.';
COMMENT ON COLUMN @PREFIX@revision.revisionId          IS 'SQLWAYS_EVAL# of a given revision supplied by the source. The order of the revision is determined by the revAppliedDate. ';
COMMENT ON COLUMN @PREFIX@revision.changeAgent         IS 'SQLWAYS_EVAL# of the source(s) that participated in this particular change. changeAgent must match a local id of a supportedSource in the corresponding mappings section.';
COMMENT ON COLUMN @PREFIX@revision.revisionDate        IS 'SQLWAYS_EVAL# which this version is operative (considered commited). ';
COMMENT ON COLUMN @PREFIX@revision.revAppliedDate      IS 'SQLWAYS_EVAL# on which the revision is applied into the lexEVS system.';
COMMENT ON COLUMN @PREFIX@revision.editOrder           IS 'SQLWAYS_EVAL# that this revision is to be applied if in a systemRelease. ';
COMMENT ON COLUMN @PREFIX@revision.changeInstructions  IS 'SQLWAYS_EVAL# readable set of instructions on how to apply this change';
COMMENT ON COLUMN @PREFIX@revision.description         IS 'SQLWAYS_EVAL# the revision contents.';

CREATE TABLE @PREFIX@systemRelease
(
   releaseGuid VARCHAR(36)   NOT NULL,    --SQLWAYS_EVAL# identifier for systemRelease table. 
   releaseURI VARCHAR(250)   NOT NULL,    --SQLWAYS_EVAL# URI of a given systemRelease. 
   releaseId VARCHAR(50),    --SQLWAYS_EVAL# to a given  release by the release agency. 
   releaseDate TIMESTAMP   NOT NULL,    --SQLWAYS_EVAL# release data and time of a given release. 
   basedOnRelease VARCHAR(250),    --SQLWAYS_EVAL# release that logically preceeds this release. 
   releaseAgency VARCHAR(250),    --SQLWAYS_EVAL# agency responsible for this release. 
   description CLOB(2G) NOT LOGGED    --SQLWAYS_EVAL# the release contents. 
);

COMMENT ON TABLE @PREFIX@systemRelease IS 'SQLWAYS_EVAL# information about the different releases applied to the lexGrid system (if any). A collection of coding schemes, value domains, pick lists and/or revision records that are released as a unit. This table is common across database. It doesn';
COMMENT ON COLUMN @PREFIX@systemRelease.releaseGuid     IS 'SQLWAYS_EVAL# for systemRelease table.';
COMMENT ON COLUMN @PREFIX@systemRelease.releaseURI      IS 'SQLWAYS_EVAL# of a given systemRelease.';
COMMENT ON COLUMN @PREFIX@systemRelease.releaseId       IS 'SQLWAYS_EVAL# to a given  release by the release agency.';
COMMENT ON COLUMN @PREFIX@systemRelease.releaseDate     IS 'SQLWAYS_EVAL# data and time of a given release.';
COMMENT ON COLUMN @PREFIX@systemRelease.basedOnRelease  IS 'SQLWAYS_EVAL# release that logically preceeds this release.';
COMMENT ON COLUMN @PREFIX@systemRelease.releaseAgency   IS 'SQLWAYS_EVAL# agency responsible for this release.';
COMMENT ON COLUMN @PREFIX@systemRelease.description     IS 'SQLWAYS_EVAL# the release contents.';


--SQLWAYS_EVAL# Key Constraints 
ALTER TABLE @PREFIX@associationPredicate ADD CONSTRAINT PK_ASSOCPRED 
PRIMARY KEY(associationPredicateGuid);

ALTER TABLE @PREFIX@codingScheme ADD CONSTRAINT PK_CODINGSCHEME 
PRIMARY KEY(codingSchemeGuid);

ALTER TABLE @PREFIX@csMultiAttrib ADD CONSTRAINT PK_CSMULTATTRGUID 
PRIMARY KEY(csMultiAttribGuid);

ALTER TABLE @PREFIX@csSupportedAttrib ADD CONSTRAINT PK_MAPPINGGUID 
PRIMARY KEY(csSuppAttribGuid);

ALTER TABLE @PREFIX@entity ADD CONSTRAINT PK_ENTITY 
PRIMARY KEY(entityGuid);

ALTER TABLE @PREFIX@entityAssnQuals ADD CONSTRAINT PK_ENTIASSNQLS 
PRIMARY KEY(entityAssnQualsGuid);

ALTER TABLE @PREFIX@entityAssnsToData ADD CONSTRAINT PK_ENTIASSNSDATA 
PRIMARY KEY(entityAssnsDataGuid);

ALTER TABLE @PREFIX@entityAssnsToEntity ADD CONSTRAINT PK_ENTIASSNSTOENTI 
PRIMARY KEY(entityAssnsGuid);

ALTER TABLE @PREFIX@entityAssnsToEntityTr ADD CONSTRAINT PK_ENTIASSNSENTITR 
PRIMARY KEY(entityAssnsTrGuid);

ALTER TABLE @PREFIX@entryState ADD CONSTRAINT PK_ENTRYSTATE 
PRIMARY KEY(entryStateGuid);

ALTER TABLE @PREFIX@property ADD CONSTRAINT PK_PROPERTY 
PRIMARY KEY(propertyGuid);

ALTER TABLE @PREFIX@propertyLinks ADD CONSTRAINT PK_PROPLINKSGUID 
PRIMARY KEY(propertyLinksGuid);

ALTER TABLE @PREFIX@propertyMultiAttrib ADD CONSTRAINT PK_PROPMULTATTGUID 
PRIMARY KEY(propMultiAttribGuid);

ALTER TABLE @PREFIX@relation ADD CONSTRAINT PK_RELATION 
PRIMARY KEY(relationGuid);

ALTER TABLE @PREFIX@revision ADD CONSTRAINT PK_REVISION 
PRIMARY KEY(revisionGuid);

ALTER TABLE @PREFIX@systemRelease ADD CONSTRAINT PK_SYSTEMRELEASE 
PRIMARY KEY(releaseGuid);


--SQLWAYS_EVAL# 
ALTER TABLE @PREFIX@associationPredicate
ADD CONSTRAINT UQ_ASSOCIATION UNIQUE(relationGuid,associationName);

CREATE INDEX IDX_CSURI ON @PREFIX@codingScheme
(codingSchemeURI ASC);

CREATE INDEX IDX_CSURIVERSION ON @PREFIX@codingScheme
(codingSchemeURI ASC, 
representsVersion ASC);

CREATE INDEX IDX_CSNAME ON @PREFIX@codingScheme
(codingSchemeName ASC);

CREATE INDEX IDX_CSNAMEVERSION ON @PREFIX@codingScheme
(codingSchemeName ASC, 
representsVersion ASC);

CREATE INDEX IDX_CSMULTIATTRIB ON @PREFIX@csMultiAttrib
(codingSchemeGuid ASC, 
attributeType ASC);

ALTER TABLE @PREFIX@csSupportedAttrib
ADD CONSTRAINT UQ_MAPPING UNIQUE(codingSchemeGuid,supportedAttributeTag,id);

CREATE INDEX IDX_ENTITY ON @PREFIX@entity
(codingSchemeGuid ASC, 
entityCode ASC);

CREATE INDEX IDX_ENTITYNS ON @PREFIX@entity
(codingSchemeGuid ASC, 
entityCode ASC, 
entityCodeNamespace ASC);

--ALTER TABLE @PREFIX@entityAssnQuals
--ADD CONSTRAINT UQ_ENTIASSNQUALS UNIQUE(referenceGuid,qualifierName,qualifierValue);

ALTER TABLE @PREFIX@entityAssnsToData
ADD CONSTRAINT UQ_ENTASDATA_SRC UNIQUE(sourceEntityCode,sourceEntityCodeNamespace);

CREATE INDEX IDX_ENTASTODATA_SOURCE ON @PREFIX@entityAssnsToData
(associationPredicateGuid ASC, 
sourceEntityCode ASC);

CREATE INDEX IDX_ENTASTOENT_SOURCE ON @PREFIX@entityAssnsToEntity
(associationPredicateGuid ASC, 
sourceEntityCode ASC);

CREATE INDEX IDX_ENTASTOENT_SOURCENS ON @PREFIX@entityAssnsToEntity
(associationPredicateGuid ASC, 
sourceEntityCode ASC, 
sourceEntityCodeNamespace ASC);

CREATE INDEX IDX_ENTASTOENT_TARGET ON @PREFIX@entityAssnsToEntity
(associationPredicateGuid ASC, 
targetEntityCode ASC);

CREATE INDEX IDX_ENTASTOENT_TARGETNS ON @PREFIX@entityAssnsToEntity
(associationPredicateGuid ASC, 
targetEntityCode ASC, 
targetEntityCodeNamespace ASC);

ALTER TABLE @PREFIX@entityAssnsToEntityTr
ADD CONSTRAINT UQ_SRCTGTCOMBO UNIQUE(sourceEntityCode,sourceEntityCodeNamespace,targetEntityCode,targetEntityCodeNamespace, 
associationPredicateGuid);

CREATE INDEX IDX_ENTASTOENTTR_SOURCE ON @PREFIX@entityAssnsToEntityTr
(associationPredicateGuid ASC, 
sourceEntityCode ASC, 
sourceEntityCodeNamespace ASC);

CREATE INDEX IDX_ENTASTOENTTR_TARGET ON @PREFIX@entityAssnsToEntityTr
(associationPredicateGuid ASC, 
targetEntityCode ASC, 
targetEntityCodeNamespace ASC);

CREATE INDEX IDX_REFERENCEGUID ON @PREFIX@property
(referenceGuid ASC);

ALTER TABLE @PREFIX@propertyLinks
ADD CONSTRAINT UQ_PROPLINKS UNIQUE(sourcePropertyGuid,link,targetPropertyGuid);

CREATE INDEX IDX_SOURCEPROPERTYGUID ON @PREFIX@propertyLinks
(sourcePropertyGuid ASC);

CREATE INDEX IDX_TARGETPROPERTYGUID ON @PREFIX@propertyLinks
(targetPropertyGuid ASC);

CREATE INDEX IDX_PROPERTYMULTIATTRIB ON @PREFIX@propertyMultiAttrib
(propertyGuid ASC);


--SQLWAYS_EVAL# Key Constraints 
ALTER TABLE @PREFIX@associationPredicate ADD CONSTRAINT FK_ASSPRED_REL 
FOREIGN KEY(relationGuid) REFERENCES @PREFIX@relation(relationGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@codingScheme ADD CONSTRAINT FK_CS_RELEASEGUID 
FOREIGN KEY(releaseGuid) REFERENCES @DEFAULT_PREFIX@systemRelease(releaseGuid);

ALTER TABLE @PREFIX@csMultiAttrib ADD CONSTRAINT FK_CSMULTI_CSGUID 
FOREIGN KEY(codingSchemeGuid) REFERENCES @PREFIX@codingScheme(codingSchemeGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@csSupportedAttrib ADD CONSTRAINT FK_MAP_CSGUID 
FOREIGN KEY(codingSchemeGuid) REFERENCES @PREFIX@codingScheme(codingSchemeGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@entity ADD CONSTRAINT FK_ENT_CSGUID 
FOREIGN KEY(codingSchemeGuid) REFERENCES @PREFIX@codingScheme(codingSchemeGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@entityAssnsToData ADD CONSTRAINT FK_ENASDATA_ASGUID 
FOREIGN KEY(associationPredicateGuid) REFERENCES @PREFIX@associationPredicate(associationPredicateGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@entityAssnsToEntity ADD CONSTRAINT FK_ENASEN_ASGUID 
FOREIGN KEY(associationPredicateGuid) REFERENCES @PREFIX@associationPredicate(associationPredicateGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@entityAssnsToEntityTr ADD CONSTRAINT FK_ENASENR_ASGUID 
FOREIGN KEY(associationPredicateGuid) REFERENCES @PREFIX@associationPredicate(associationPredicateGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@entityType ADD CONSTRAINT FK_ETYPE_ENGUID 
FOREIGN KEY(entityGuid) REFERENCES @PREFIX@entity(entityGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@entryState ADD CONSTRAINT FK_ES_PREESTGUID 
FOREIGN KEY(prevEntryStateGuid) REFERENCES @PREFIX@entryState(entryStateGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@entryState ADD CONSTRAINT FK_ES_REVGUID 
FOREIGN KEY(revisionGuid) REFERENCES @DEFAULT_PREFIX@revision(revisionGuid);

ALTER TABLE @PREFIX@entryState ADD CONSTRAINT FK_ES_PREREVGUID 
FOREIGN KEY(prevRevisionGuid) REFERENCES @DEFAULT_PREFIX@revision(revisionGuid);

ALTER TABLE @PREFIX@propertyLinks ADD CONSTRAINT FK_PLKS_SPROGUID 
FOREIGN KEY(sourcePropertyGuid) REFERENCES @PREFIX@property(propertyGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@propertyLinks ADD CONSTRAINT FK_PLKS_TPROGUID 
FOREIGN KEY(targetPropertyGuid) REFERENCES @PREFIX@property(propertyGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@propertyMultiAttrib ADD CONSTRAINT FK_PMA_PROGUID 
FOREIGN KEY(propertyGuid) REFERENCES @PREFIX@property(propertyGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@relation ADD CONSTRAINT FK_REL_CS 
FOREIGN KEY(codingSchemeGuid) REFERENCES @PREFIX@codingScheme(codingSchemeGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@revision ADD CONSTRAINT FK_REV_REGUID 
FOREIGN KEY(releaseGuid) REFERENCES @PREFIX@systemRelease(releaseGuid);