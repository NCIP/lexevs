--SQLWAYS_EVAL# 
--SQLWAYS_EVAL# Enterprise Architect Version 7.5.850
--SQLWAYS_EVAL# Tuesday, 01 June, 2010 
--SQLWAYS_EVAL# Oracle 
--SQLWAYS_EVAL# 




--SQLWAYS_EVAL# 
CREATE TABLE @PREFIX@valueSetDefinition
(
   valueSetDefGuid      VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# identifier for the value set def. 
   valueSetDefURI       VARCHAR(250) NOT NULL,    --SQLWAYS_EVAL# given value set definition. 
   valueSetDefName      VARCHAR(250) NOT NULL,    --SQLWAYS_EVAL# given value set definition, if any. 
   defaultCodingScheme  VARCHAR(50),    --SQLWAYS_EVAL# the primary coding scheme from which the value set definition is drawn. defaultCodingScheme must match a local id of a supportedCodingScheme in the mapping section. 
   conceptDomain        VARCHAR(200),
   description          VARCHAR,    --SQLWAYS_EVAL# the content of the value set definition. 
   releaseGuid          VARCHAR(36),    --SQLWAYS_EVAL# release in which the given value set definition is loaded. This field is a null able field as a value set definition can be loaded alone, with out a system release. 
   isActive             BOOLEAN,    --SQLWAYS_EVAL# to indicate the given value set definition is active or not. 
   owner                VARCHAR(250),    --SQLWAYS_EVAL# the resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent. 
   status               VARCHAR(50),    --SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above. 
   effectiveDate        TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid. 
   expirationDate       TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid. 
   entryStateGuid       VARCHAR(36)    --SQLWAYS_EVAL# to the entry state details of the value domain. 
) ;

CREATE TABLE @PREFIX@vsdEntry
(
   vsdEntryGuid             VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# identifier for the value set definition. 
   valueSetDefGuid          VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# the corresponding value set definition. 
   ruleOrder                BIGINT NOT NULL,    --SQLWAYS_EVAL# of the definition entry within the definition as well as the relative order in which this entry should be applied 
   operator                 VARCHAR(15) NOT NULL,    --SQLWAYS_EVAL# to apply to this entry.  Default: OR. 
   codingSchemeReference    VARCHAR(50),    --SQLWAYS_EVAL# all of the entity codes in a given coding scheme. 
   valueSetDefReference     VARCHAR(250),    --SQLWAYS_EVAL# the set of codes defined in another value set definition. 
   entityCode               VARCHAR(200),    --SQLWAYS_EVAL# being referenced. 
   entityCodeNamespace      VARCHAR(50),    --SQLWAYS_EVAL# of the namespace of the entityCode. entityCodeNamespace must match a local id of a supportedNamespace in the corresponding mappings section.  If omitted, the URI of the defaultCodingScheme will be used as the URI of the entity code. 
   leafOnly                 BOOLEAN,    --SQLWAYS_EVAL# is supplied and referenceAssociation is defined as transitive, include all entity codes that are "leaves" in  transitive closure of referenceAssociation as applied to entity code. Default: false 
   referenceAssociation     VARCHAR(50),    --SQLWAYS_EVAL# of an association that appears in the native relations collection in the default coding scheme.  This association is used to describe a set of entity codes. If absent, only the entityCode itself is included in this definition. 
   targetToSource           BOOLEAN,    --SQLWAYS_EVAL# is supplied, navigate from entityCode as the association target to the corresponding sources. If transitiveClosure is true and the referenceAssociation is transitive, include all the ancestors in the list rather than just the direct "parents" (sources). 
   transitiveClosure        BOOLEAN,    --SQLWAYS_EVAL# is supplied and referenceAssociation is defined as transitive, include all entity codes that belong to  transitive closure of referenceAssociation as applied to entity code. Default: false 
   propertyRefCodingScheme  VARCHAR(50),    --SQLWAYS_EVAL# localId to check property reference. 
   propertyName             VARCHAR(50),
   propertyMatchValue       VARCHAR,
   matchAlgorithm           VARCHAR(250),
   format                   VARCHAR(50),    --SQLWAYS_EVAL# Ex. XML, blob, text etc. 
   isActive                 BOOLEAN,    --SQLWAYS_EVAL# to indicate the given vsdEntry is active or not. 
   owner                    VARCHAR(250),    --SQLWAYS_EVAL# the resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent. 
   status                   VARCHAR(50),    --SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above. 
   effectiveDate            TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid. 
   expirationDate           TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid. 
   entryStateGuid           VARCHAR(36)    --SQLWAYS_EVAL# to the entry state details of the plEntry. 
) ;

CREATE TABLE @PREFIX@vsEntryState
(
   entryStateGuid      VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# identifier for the entry state. 
   entryGuid           VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# identifier of the resource being revised. 
   entryType           VARCHAR(50),    --SQLWAYS_EVAL# able object being revised. 
   changeType          VARCHAR(15),    --SQLWAYS_EVAL# that occurred between current state and the previous. 
   relativeOrder       BIGINT,    --SQLWAYS_EVAL# order that this state change should be applied within the context of the containing revision. 
   revisionGuid        VARCHAR(36),    --SQLWAYS_EVAL# identifier of the revision in which the version able object is being revised. 
   prevRevisionGuid    VARCHAR(36),    --SQLWAYS_EVAL# identifier of the revision in which the version able object was previously revised. 
   prevEntryStateGuid  VARCHAR(36)    --SQLWAYS_EVAL# identifier of the previous entry state for the given version able object. 
) ;


CREATE TABLE @PREFIX@vsMultiAttrib
(
   vsMultiAttribGuid  VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# identifier for multi attributes entries. 
   referenceGuid      VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# unique identifier of the model object to which the multi attribute belongs to.  
   referenceType      VARCHAR(50) NOT NULL,    --SQLWAYS_EVAL# object to which a given multi attribute belongs to.  
   attributeType      VARCHAR(30) NOT NULL,    --SQLWAYS_EVAL# attribute stored. Typically "qualifier" or "source" or "usage context". 
   attributeValue     VARCHAR(250) NOT NULL,    --SQLWAYS_EVAL# multi attributes. 
   subRef             VARCHAR(250),    --SQLWAYS_EVAL# id or other localized information within the source. Used to make a source reference more precise.  
   role               VARCHAR(250),    --SQLWAYS_EVAL# of the source for this particular resource (e.g. author, distributor). 
   entryStateGuid     VARCHAR(36)    --SQLWAYS_EVAL# to the entry state details of the value domain Multi attributes. 
) ;

CREATE TABLE @PREFIX@vsPickList
(
   vsPickListGuid                VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# identifier for the pick list. 
   pickListId                    VARCHAR(50) NOT NULL,    --SQLWAYS_EVAL# that uniquely names this list within the context of the collection. 
   representsValueSetDefinition  VARCHAR(250) NOT NULL,    --SQLWAYS_EVAL# value set definition that is represented by this pick list. 
   completeSet                   BOOLEAN,    --SQLWAYS_EVAL# this pick list should represent all of the entries in the value set definition.  Any active entry codes that aren't in the specific pick list entries are added to the end, using the designations identified by the defaultLanguage, defaultSortOrder and defaultPickContext.  Default: false 
   defaultEntityCodeNamespace    VARCHAR(50),    --SQLWAYS_EVAL# the namespace to which the entry codes in this list belong. defaultEntityCodeNamespace must match a local id of a supportedNamespace in the mappings section. 
   defaultLanguage               VARCHAR(32),    --SQLWAYS_EVAL# of the language that is used to generate the text of this pick list if not otherwise specified. Note that this language does NOT necessarily have any coorelation with the language of a pickListEntry itself or the language of the target user. defaultLanguage must match a local id of a supportedLanguage in the supportedAttributes section. 
   defaultSortOrder              VARCHAR(50),    --SQLWAYS_EVAL# of a sort order that is used as the default in the definition of the pick list. 
   description                   VARCHAR,    --SQLWAYS_EVAL# the content of the pick list. 
   releaseGuid                   VARCHAR(36),    --SQLWAYS_EVAL# release in which the given pick list is loaded. This field is a null able field as a pick list can be loaded alone, with out a system release. 
   isActive                      BOOLEAN,    --SQLWAYS_EVAL# to indicate the given pick list is active or not. 
   owner                         VARCHAR(250),    --SQLWAYS_EVAL# the resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent. 
   status                        VARCHAR(50),    --SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above. 
   effectiveDate                 TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid. 
   expirationDate                TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid. 
   entryStateGuid                VARCHAR(36)    --SQLWAYS_EVAL# to the entry state details of the pick list. 
) ;

CREATE TABLE @PREFIX@vsPLEntry
(
   vsPLEntryGuid        VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# identifier for the pick list entry. 
   vsPickListGuid       VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# the corresponding pick list definition. 
   plEntryId            VARCHAR(50) NOT NULL,    --SQLWAYS_EVAL# of the pick list node within the list. 
   entityCodeNamespace  VARCHAR(50),    --SQLWAYS_EVAL# of the namespace of the entity code if different than the pickListDefinition defaultEntityCodeNamespace. entityCodeNamespace must match a local id of a supportedNamespace in the mappings section. 
   entityCode           VARCHAR(200) NOT NULL,    --SQLWAYS_EVAL# that this entry represents, if any. If not present, this entry is not selectable. 
   entryOrder           BIGINT,    --SQLWAYS_EVAL# of this entry in the list.  If absent, this entry follows any ordered entries, but is unordered beyond that 
   isDefault            BOOLEAN,    --SQLWAYS_EVAL# this is the default entry for the supplied language and context. 
   matchIfNoContext     BOOLEAN,    --SQLWAYS_EVAL# this entry can be used if no contexts are supplied, even though pickContext ispresent. 
   propertyId           VARCHAR(50),    --SQLWAYS_EVAL# identifier associated with the entityCode and entityCodeNamespace that the pickText was derived from.  If absent, the pick text can be anything. Some terminologies may have business rules requiring this attribute to be present. 
   language             VARCHAR(32),    --SQLWAYS_EVAL# of the language to be used when the application/user supplies a selection language matches. If absent, this matches all languages. language must match a local id od of a supportedLanguage in the mappings section. 
   include              BOOLEAN,    --SQLWAYS_EVAL# to indicate the given PL entry node has to be included or not when the pick list is resolved. 
   pickText             VARCHAR,    --SQLWAYS_EVAL# represents this node in the pick list. Some business rules may require that this string match a presentation associated with the entityCode. 
   isActive             BOOLEAN,    --SQLWAYS_EVAL# to indicate the given PL entry is active or not. 
   owner                VARCHAR(250),    --SQLWAYS_EVAL# the resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent. 
   status               VARCHAR(50),    --SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above. 
   effectiveDate        TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid. 
   expirationDate       TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid. 
   entryStateGuid       VARCHAR(36)    --SQLWAYS_EVAL# to the entry state details of the pick list entry. 
) ;

CREATE TABLE @PREFIX@vsProperty
(
   vsPropertyGuid        VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# identifier for the property. 
   referenceGuid         VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# object to which a given property belongs to. 
   referenceType         VARCHAR(50) NOT NULL,    --SQLWAYS_EVAL# object to which a given property belongs to. Namely, entity, codingScheme etc. 
   propertyId            VARCHAR(50),    --SQLWAYS_EVAL# of this particular propert/resource/value instance. 
   propertyType          VARCHAR(15),    --SQLWAYS_EVAL# element that this property represents.  As an example, the codingScheme "copyright" attribute could be represented by a property with a propertyType that mapped to lgCS:copyRight. Must match a local id of a supportedPropertyType in the corresponding mappings section. 
   propertyName          VARCHAR(50) NOT NULL,    --SQLWAYS_EVAL# that defines the meaning of this particular property entry. Must match a local id of a supportedProperty in the corresponding supportedAttributes section. 
   language              VARCHAR(32),    --SQLWAYS_EVAL# of the language of the property value. Must match a local id of a supportedLanguage in the corresponding mappings section. If omitted, and language is applicable to this property, the defaultLanguage of the surrounding resource is used. 
   format                VARCHAR(50),    --SQLWAYS_EVAL# of the property value. 
   isPreferred           BOOLEAN,    --SQLWAYS_EVAL# if the text meets the selection criteria, it should be the preferred form. For a given language there should be only one preferred presentation. 
   matchIfNoContext      BOOLEAN,    --SQLWAYS_EVAL# this presentation is valid in a acontextual setting - that it is always valid in the given language.  Default: true  if there are no property usageContexts, false otherwise. 
   degreeOfFidelity      VARCHAR(50),    --SQLWAYS_EVAL# that states how closely a term approximates the intended meaning of an entry code. degreeOfFidelity must match a local id of a supportedDegreeOfFidelity in the corresponding mappings section. 
   representationalForm  VARCHAR(50),    --SQLWAYS_EVAL# that states how the term represents the concept (abbrev, acronym, etc.) representationalForm must match a local id of a representationalForm in the corresponding mappings section. 
   propertyValue         VARCHAR NOT NULL,    --SQLWAYS_EVAL# the property associated with this particular resource.  Note that "text" may be any type, including a URI, html fragment, etc. 
   isActive              BOOLEAN,    --SQLWAYS_EVAL# to indicate the given property is active or not. 
   owner                 VARCHAR(250),    --SQLWAYS_EVAL# the resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent. 
   status                VARCHAR(50),    --SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above. 
   effectiveDate         TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid. 
   expirationDate        TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid. 
   entryStateGuid        VARCHAR(36) NOT NULL    --SQLWAYS_EVAL# to the entry state details of the given property. 
) ;

CREATE TABLE @PREFIX@vsPropertyMultiAttrib
(
   vsPropMultiAttribGuid  VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# identifier for multi attributes entries. 
   vsPropertyGuid         VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# the corresponding property. 
   attributeType          VARCHAR(30) NOT NULL,    --SQLWAYS_EVAL# attribute stored. Typically "qualifier" or "source" or "usage context". 
   attributeId            VARCHAR(50),    --SQLWAYS_EVAL# qualifier. In case of source and usage context value will be null. 
   attributeValue         VARCHAR(250) NOT NULL,    --SQLWAYS_EVAL# multi attributes. 
   subRef                 VARCHAR(250),    --SQLWAYS_EVAL# id or other localized information within the source. Used to make a source reference more precise.  
   role                   VARCHAR(250),    --SQLWAYS_EVAL# of the source for this particular resource (e.g. author, distributor). 
   qualifierType          VARCHAR(250),    --SQLWAYS_EVAL# of a property. 
   entryStateGuid         VARCHAR(36)    --SQLWAYS_EVAL# to the entry state details of the value domain property Multi attributes. 
) ;

CREATE TABLE @PREFIX@vsSupportedAttrib
(
   vsSuppAttribGuid        VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# identifier for vsSupportedMapping table. 
   referenceGuid           VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# identifier of the referencing resource. 
   referenceType           VARCHAR(50) NOT NULL,    --SQLWAYS_EVAL# of the parent object. It could be ValueSetDefinition or PickListDefinition 
   supportedAttributeTag   VARCHAR(30) NOT NULL,    --SQLWAYS_EVAL# attribute. 
   id                      VARCHAR(250) NOT NULL,    --SQLWAYS_EVAL# of this particular attribute 
   uri                     VARCHAR(250),    --SQLWAYS_EVAL# with this particular local value 
   idValue                 VARCHAR(250),    --SQLWAYS_EVAL# supported attribute (most cases it is same as the id) 
   associationNames        VARCHAR(250),
   rootCode                VARCHAR(250),  
   isForwardNavigable      BOOLEAN,   
   isImported              BOOLEAN,  
   equivalentCodingScheme  VARCHAR(250), 
   assemblyRule            VARCHAR(250), 
   assnCodingScheme        VARCHAR(250),
   assnNamespace           VARCHAR(250),
   assnEntityCode          VARCHAR(200),
   propertyType            VARCHAR(50)
) ;


--SQLWAYS_EVAL# Key Constraints 
ALTER TABLE @PREFIX@valueSetDefinition ADD CONSTRAINT PK_VALUESETDEF 
PRIMARY KEY(valueSetDefGuid);

ALTER TABLE @PREFIX@vsdEntry ADD CONSTRAINT PK_VSDENTRY 
PRIMARY KEY(vsdEntryGuid);

ALTER TABLE @PREFIX@vsEntryState ADD CONSTRAINT PK_VSENTRYSTATE 
PRIMARY KEY(entryStateGuid);

ALTER TABLE @PREFIX@vsMultiAttrib ADD CONSTRAINT PK_VSMULTIATTRIB 
PRIMARY KEY(vsMultiAttribGuid);

ALTER TABLE @PREFIX@vsPickList ADD CONSTRAINT PK_VSPICKLIST 
PRIMARY KEY(vsPickListGuid);

ALTER TABLE @PREFIX@vsPLEntry ADD CONSTRAINT PK_VSPLENTRY 
PRIMARY KEY(vsPLEntryGuid);

ALTER TABLE @PREFIX@vsProperty ADD CONSTRAINT PK_VSPROPERTY 
PRIMARY KEY(vsPropertyGuid);

ALTER TABLE @PREFIX@vsPropertyMultiAttrib ADD CONSTRAINT PK_VSPROPERTYMULTIATTRIB 
PRIMARY KEY(vsPropMultiAttribGuid);

ALTER TABLE @PREFIX@vsSupportedAttrib ADD CONSTRAINT PK_VSMAPPING 
PRIMARY KEY(vsSuppAttribGuid);


--SQLWAYS_EVAL# 
CREATE INDEX IDX_VSDNAME ON @PREFIX@valueSetDefinition
(valueSetDefName);

CREATE INDEX IDX_VSDURI ON @PREFIX@valueSetDefinition
(valueSetDefURI);

ALTER TABLE @PREFIX@vsdEntry
ADD CONSTRAINT UQ_VSDENTRY_VALUESETDEFGUID UNIQUE(valueSetDefGuid,vsdEntryGuid);

CREATE INDEX IDX_VALUESETDEFGUID ON @PREFIX@vsdEntry
(valueSetDefGuid);

CREATE INDEX IDX_VSDENT_ENTITYCODE ON @PREFIX@vsdEntry
(entityCode);

CREATE INDEX IDX_PICKLISTID ON @PREFIX@vsPickList
(pickListId);

CREATE INDEX IDX_REPRESENTSVSD ON @PREFIX@vsPickList
(representsValueSetDefinition);

ALTER TABLE @PREFIX@vsPLEntry
ADD CONSTRAINT UQ_VSPLENTRY_VSPICKLISTGUID UNIQUE(vsPickListGuid,plEntryId);

CREATE INDEX IDX_VSPICKLISTGUID ON @PREFIX@vsPLEntry
(vsPickListGuid);

CREATE INDEX IDX_ENTITYCODE ON @PREFIX@vsPLEntry
(entityCode);

CREATE INDEX IDX_VSPROPERTY ON @PREFIX@vsProperty
(referenceGuid, 
propertyId, 
propertyName);

ALTER TABLE @PREFIX@vsProperty
ADD CONSTRAINT UQ_VSPROPERTY UNIQUE(referenceGuid,propertyId,propertyName);

CREATE INDEX IDX_VSPROPERTYGUID ON @PREFIX@vsPropertyMultiAttrib
(vsPropertyGuid);

ALTER TABLE @PREFIX@vsSupportedAttrib
ADD CONSTRAINT UQ_VSMAPPING UNIQUE(referenceGuid,supportedAttributeTag,id);

CREATE INDEX IDX_SAREFERENCEGUID ON @PREFIX@vsSupportedAttrib
(referenceGuid);


--SQLWAYS_EVAL# Key Constraints 
ALTER TABLE @PREFIX@valueSetDefinition ADD CONSTRAINT FK_VSD_RELEASEGUID 
FOREIGN KEY(releaseGuid) REFERENCES @PREFIX@systemRelease(releaseGuid);

ALTER TABLE @PREFIX@vsdEntry ADD CONSTRAINT FK_VSDENTRY_VALUESETDEFINITION 
FOREIGN KEY(valueSetDefGuid) REFERENCES @PREFIX@valueSetDefinition(valueSetDefGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@vsEntryState ADD CONSTRAINT FK_VSES_PREVENTRYSTATEGUID 
FOREIGN KEY(prevEntryStateGuid) REFERENCES @PREFIX@vsEntryState(entryStateGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@vsPickList ADD CONSTRAINT FK_VSDPL_RELEASEGUID 
FOREIGN KEY(releaseGuid) REFERENCES @PREFIX@systemRelease(releaseGuid);

ALTER TABLE @PREFIX@vsPLEntry ADD CONSTRAINT FK_VSPLENTRY_VSPICKLIST 
FOREIGN KEY(vsPickListGuid) REFERENCES @PREFIX@vsPickList(vsPickListGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@vsPropertyMultiAttrib ADD CONSTRAINT FK_VSPROPERTYMULTIA_VSPROPERTY 
FOREIGN KEY(vsPropertyGuid) REFERENCES @PREFIX@vsProperty(vsPropertyGuid)
ON DELETE CASCADE;