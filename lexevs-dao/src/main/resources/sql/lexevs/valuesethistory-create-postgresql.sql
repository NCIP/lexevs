--SQLWAYS_EVAL# 
--SQLWAYS_EVAL# Enterprise Architect Version 7.5.850
--SQLWAYS_EVAL# Tuesday, 01 June, 2010 
--SQLWAYS_EVAL# Oracle 
--SQLWAYS_EVAL# 




--SQLWAYS_EVAL# 
CREATE TABLE @PREFIX@h_valueSetDefinition
(
   valueSetDefGuid      VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# identifier for the value set def. 
   valueSetDefURI       VARCHAR(250) NOT NULL,    --SQLWAYS_EVAL# given value set definition. 
   valueSetDefName      VARCHAR(250) NOT NULL,    --SQLWAYS_EVAL# given value set definition, if any. 
   defaultCodingScheme  VARCHAR(50),    --SQLWAYS_EVAL# the primary coding scheme from which the value set definition is drawn. defaultCodingScheme must match a local id of a supportedCodingScheme in the mapping section. 
   conceptDomain        VARCHAR(200),
   description          TEXT,    --SQLWAYS_EVAL# the content of the value set definition. 
   releaseGuid          VARCHAR(36),    --SQLWAYS_EVAL# release in which the given value set definition is loaded. This field is a null able field as a value set definition can be loaded alone, with out a system release. 
   isActive             BOOLEAN,    --SQLWAYS_EVAL# to indicate the given value set definition is active or not. 
   owner                VARCHAR(250),    --SQLWAYS_EVAL# the resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent. 
   status               VARCHAR(50),    --SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above. 
   effectiveDate        TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid. 
   expirationDate       TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid. 
   entryStateGuid       VARCHAR(36) NOT NULL    --SQLWAYS_EVAL# to the entry state details of the value domain. 
) WITH OIDS;

COMMENT ON TABLE @PREFIX@h_valueSetDefinition IS 'SQLWAYS_EVAL# value set definitions. A value set definition can be a simple description with no associated value set entries, or it can consist of one or more definitionEntries that resolve to an enumerated list of entityCodes when applied to one or more codingScheme versions.';
COMMENT ON COLUMN @PREFIX@h_valueSetDefinition.valueSetDefGuid      IS 'SQLWAYS_EVAL# for the value set def.';
COMMENT ON COLUMN @PREFIX@h_valueSetDefinition.valueSetDefURI       IS 'SQLWAYS_EVAL# value set definition.';
COMMENT ON COLUMN @PREFIX@h_valueSetDefinition.valueSetDefName      IS 'SQLWAYS_EVAL# value set definition, if any.';
COMMENT ON COLUMN @PREFIX@h_valueSetDefinition.defaultCodingScheme  IS 'SQLWAYS_EVAL# primary coding scheme from which the value set definition is drawn. defaultCodingScheme must match a local id of a supportedCodingScheme in the mapping section.';
COMMENT ON COLUMN @PREFIX@h_valueSetDefinition.description          IS 'SQLWAYS_EVAL# the content of the value set definition.';
COMMENT ON COLUMN @PREFIX@h_valueSetDefinition.releaseGuid          IS 'SQLWAYS_EVAL# release in which the given value set definition is loaded. This field is a null able field as a value set definition can be loaded alone, with out a system release.';
COMMENT ON COLUMN @PREFIX@h_valueSetDefinition.isActive             IS 'SQLWAYS_EVAL# indicate the given value set definition is active or not.';
COMMENT ON COLUMN @PREFIX@h_valueSetDefinition.owner                IS 'SQLWAYS_EVAL# resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent.';
COMMENT ON COLUMN @PREFIX@h_valueSetDefinition.status               IS 'SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above.';
COMMENT ON COLUMN @PREFIX@h_valueSetDefinition.effectiveDate        IS 'SQLWAYS_EVAL# that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid.';
COMMENT ON COLUMN @PREFIX@h_valueSetDefinition.expirationDate       IS 'SQLWAYS_EVAL# that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid.';
COMMENT ON COLUMN @PREFIX@h_valueSetDefinition.entryStateGuid       IS 'SQLWAYS_EVAL# to the entry state details of the value domain.';

CREATE TABLE @PREFIX@h_vsdEntry
(
   vsdEntryGuid             VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# identifier for the value set definition. 
   valueSetDefGuid          VARCHAR(36) NOT NULL,
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
   propertyMatchValue       TEXT,
   matchAlgorithm           VARCHAR(250),
   format                   VARCHAR(50),    --SQLWAYS_EVAL# Ex. XML, blob, text etc. 
   isActive                 BOOLEAN,    --SQLWAYS_EVAL# to indicate the given vsdEntry is active or not. 
   owner                    VARCHAR(250),    --SQLWAYS_EVAL# the resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent. 
   status                   VARCHAR(50),    --SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above. 
   effectiveDate            TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid. 
   expirationDate           TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid. 
   entryStateGuid           VARCHAR(36) NOT NULL    --SQLWAYS_EVAL# to the entry state details of the plEntry. 
) WITH OIDS;

COMMENT ON TABLE @PREFIX@h_vsdEntry IS 'SQLWAYS_EVAL# entries of a value set definition. A reference to an entry code, a coding scheme or another value set definition along with the instructions about how the reference is applied. Definition entrys are applied in entryOrder, with each successive entry either adding to or subtracting from the final set of entity codes.';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.vsdEntryGuid             IS 'SQLWAYS_EVAL# for the value set definition.';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.ruleOrder                IS 'SQLWAYS_EVAL# of the definition entry within the definition as well as the relative order in which this entry should be applied';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.operator                 IS 'SQLWAYS_EVAL# apply to this entry.  Default: OR.';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.codingSchemeReference    IS 'SQLWAYS_EVAL# all of the entity codes in a given coding scheme.';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.valueSetDefReference     IS 'SQLWAYS_EVAL# the set of codes defined in another value set definition.';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.entityCode               IS 'SQLWAYS_EVAL# being referenced.';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.entityCodeNamespace      IS 'SQLWAYS_EVAL# of the namespace of the entityCode. entityCodeNamespace must match a local id of a supportedNamespace in the corresponding mappings section.  If omitted, the URI of the defaultCodingScheme will be used as the URI of the entity code.';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.leafOnly                 IS 'SQLWAYS_EVAL# is supplied and referenceAssociation is defined as transitive, include all entity codes that are "leaves" in  transitive closure of referenceAssociation as applied to entity code. Default: false';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.referenceAssociation     IS 'SQLWAYS_EVAL# of an association that appears in the native relations collection in the default coding scheme.  This association is used to describe a set of entity codes. If absent, only the entityCode itself is included in this definition.';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.targetToSource           IS 'SQLWAYS_EVAL# is supplied, navigate from entityCode as the association target to the corresponding sources. If transitiveClosure is true and the referenceAssociation is transitive, include all the ancestors in the list rather than just the direct "parents" (sources).';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.transitiveClosure        IS 'SQLWAYS_EVAL# is supplied and referenceAssociation is defined as transitive, include all entity codes that belong to  transitive closure of referenceAssociation as applied to entity code. Default: false';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.propertyRefCodingScheme  IS 'SQLWAYS_EVAL# to check property reference.';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.format                   IS 'SQLWAYS_EVAL# Ex. XML, blob, text etc.';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.isActive                 IS 'SQLWAYS_EVAL# indicate the given vsdEntry is active or not.';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.owner                    IS 'SQLWAYS_EVAL# resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent.';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.status                   IS 'SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above.';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.effectiveDate            IS 'SQLWAYS_EVAL# that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid.';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.expirationDate           IS 'SQLWAYS_EVAL# that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid.';
COMMENT ON COLUMN @PREFIX@h_vsdEntry.entryStateGuid           IS 'SQLWAYS_EVAL# to the entry state details of the plEntry.';

CREATE TABLE @PREFIX@h_vsMultiAttrib
(
   vsMultiAttribGuid  VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# identifier for multi attributes entries. 
   referenceGuid      VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# unique identifier of the model object to which the multi attribute belongs to.  
   referenceType      VARCHAR(50) NOT NULL,    --SQLWAYS_EVAL# object to which a given multi attribute belongs to.  
   attributeType      VARCHAR(30) NOT NULL,    --SQLWAYS_EVAL# attribute stored. Typically "qualifier" or "source" or "usage context". 
   attributeValue     VARCHAR(250) NOT NULL,    --SQLWAYS_EVAL# multi attributes. 
   subRef             VARCHAR(250),    --SQLWAYS_EVAL# id or other localized information within the source. Used to make a source reference more precise.  
   role               VARCHAR(250),    --SQLWAYS_EVAL# of the source for this particular resource (e.g. author, distributor). 
   entryStateGuid     VARCHAR(36)    --SQLWAYS_EVAL# to the entry state details of the value domain Multi attributes. 
) WITH OIDS;

COMMENT ON TABLE @PREFIX@h_vsMultiAttrib IS 'SQLWAYS_EVAL# mulit attributes for value domain and pick list model objects(valueSetDef, pickListDef and pickListEntryNode).';
COMMENT ON COLUMN @PREFIX@h_vsMultiAttrib.vsMultiAttribGuid  IS 'SQLWAYS_EVAL# for multi attributes entries.';
COMMENT ON COLUMN @PREFIX@h_vsMultiAttrib.referenceGuid      IS 'SQLWAYS_EVAL# identifier of the model object to which the multi attribute belongs to. ';
COMMENT ON COLUMN @PREFIX@h_vsMultiAttrib.referenceType      IS 'SQLWAYS_EVAL# object to which a given multi attribute belongs to. ';
COMMENT ON COLUMN @PREFIX@h_vsMultiAttrib.attributeType      IS 'SQLWAYS_EVAL# attribute stored. Typically "qualifier" or "source" or "usage context".';
COMMENT ON COLUMN @PREFIX@h_vsMultiAttrib.attributeValue     IS 'SQLWAYS_EVAL# attributes.';
COMMENT ON COLUMN @PREFIX@h_vsMultiAttrib.subRef             IS 'SQLWAYS_EVAL# id or other localized information within the source. Used to make a source reference more precise. ';
COMMENT ON COLUMN @PREFIX@h_vsMultiAttrib.role               IS 'SQLWAYS_EVAL# of the source for this particular resource (e.g. author, distributor).';
COMMENT ON COLUMN @PREFIX@h_vsMultiAttrib.entryStateGuid     IS 'SQLWAYS_EVAL# to the entry state details of the value domain Multi attributes.';

CREATE TABLE @PREFIX@h_vsPickList
(
   vsPickListGuid                VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# identifier for the pick list. 
   pickListId                    VARCHAR(50) NOT NULL,    --SQLWAYS_EVAL# that uniquely names this list within the context of the collection. 
   representsValueSetDefinition  VARCHAR(250) NOT NULL,    --SQLWAYS_EVAL# value set definition that is represented by this pick list. 
   completeSet                   BOOLEAN,    --SQLWAYS_EVAL# this pick list should represent all of the entries in the value set definition.  Any active entry codes that aren't in the specific pick list entries are added to the end, using the designations identified by the defaultLanguage, defaultSortOrder and defaultPickContext.  Default: false 
   defaultEntityCodeNamespace    VARCHAR(50),    --SQLWAYS_EVAL# the namespace to which the entry codes in this list belong. defaultEntityCodeNamespace must match a local id of a supportedNamespace in the mappings section. 
   defaultLanguage               VARCHAR(32),    --SQLWAYS_EVAL# of the language that is used to generate the text of this pick list if not otherwise specified. Note that this language does NOT necessarily have any coorelation with the language of a pickListEntry itself or the language of the target user. defaultLanguage must match a local id of a supportedLanguage in the supportedAttributes section. 
   defaultSortOrder              VARCHAR(50),    --SQLWAYS_EVAL# of a sort order that is used as the default in the definition of the pick list. 
   description                   TEXT,    --SQLWAYS_EVAL# the content of the pick list. 
   releaseGuid                   VARCHAR(36),    --SQLWAYS_EVAL# release in which the given pick list is loaded. This field is a null able field as a pick list can be loaded alone, with out a system release. 
   isActive                      BOOLEAN,    --SQLWAYS_EVAL# to indicate the given pick list is active or not. 
   owner                         VARCHAR(250),    --SQLWAYS_EVAL# the resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent. 
   status                        VARCHAR(50),    --SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above. 
   effectiveDate                 TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid. 
   expirationDate                TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid. 
   entryStateGuid                VARCHAR(36) NOT NULL    --SQLWAYS_EVAL# to the entry state details of the pick list. 
) WITH OIDS;

COMMENT ON TABLE @PREFIX@h_vsPickList IS 'SQLWAYS_EVAL# Pick List entries. Pick List is an ordered list of entity codes and corresponding presentations drawn from a value set definition.';
COMMENT ON COLUMN @PREFIX@h_vsPickList.vsPickListGuid                IS 'SQLWAYS_EVAL# for the pick list.';
COMMENT ON COLUMN @PREFIX@h_vsPickList.pickListId                    IS 'SQLWAYS_EVAL# uniquely names this list within the context of the collection.';
COMMENT ON COLUMN @PREFIX@h_vsPickList.representsValueSetDefinition  IS 'SQLWAYS_EVAL# value set definition that is represented by this pick list.';
COMMENT ON COLUMN @PREFIX@h_vsPickList.completeSet                   IS 'SQLWAYS_EVAL# this pick list should represent all of the entries in the value set definition.  Any active entry codes that aren''t in the specific pick list entries are added to the end, using the designations identified by the defaultLanguage, defaultSortOrder and defaultPickContext.  Default: false';
COMMENT ON COLUMN @PREFIX@h_vsPickList.defaultEntityCodeNamespace    IS 'SQLWAYS_EVAL# namespace to which the entry codes in this list belong. defaultEntityCodeNamespace must match a local id of a supportedNamespace in the mappings section.';
COMMENT ON COLUMN @PREFIX@h_vsPickList.defaultLanguage               IS 'SQLWAYS_EVAL# of the language that is used to generate the text of this pick list if not otherwise specified. Note that this language does NOT necessarily have any coorelation with the language of a pickListEntry itself or the language of the target user. defaultLanguage must match a local id of a supportedLanguage in the supportedAttributes section.';
COMMENT ON COLUMN @PREFIX@h_vsPickList.defaultSortOrder              IS 'SQLWAYS_EVAL# of a sort order that is used as the default in the definition of the pick list.';
COMMENT ON COLUMN @PREFIX@h_vsPickList.description                   IS 'SQLWAYS_EVAL# the content of the pick list.';
COMMENT ON COLUMN @PREFIX@h_vsPickList.releaseGuid                   IS 'SQLWAYS_EVAL# release in which the given pick list is loaded. This field is a null able field as a pick list can be loaded alone, with out a system release.';
COMMENT ON COLUMN @PREFIX@h_vsPickList.isActive                      IS 'SQLWAYS_EVAL# indicate the given pick list is active or not.';
COMMENT ON COLUMN @PREFIX@h_vsPickList.owner                         IS 'SQLWAYS_EVAL# resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent.';
COMMENT ON COLUMN @PREFIX@h_vsPickList.status                        IS 'SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above.';
COMMENT ON COLUMN @PREFIX@h_vsPickList.effectiveDate                 IS 'SQLWAYS_EVAL# that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid.';
COMMENT ON COLUMN @PREFIX@h_vsPickList.expirationDate                IS 'SQLWAYS_EVAL# that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid.';
COMMENT ON COLUMN @PREFIX@h_vsPickList.entryStateGuid                IS 'SQLWAYS_EVAL# to the entry state details of the pick list.';

CREATE TABLE @PREFIX@h_vsPLEntry
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
   pickText             TEXT,    --SQLWAYS_EVAL# represents this node in the pick list. Some business rules may require that this string match a presentation associated with the entityCode. 
   isActive             BOOLEAN,    --SQLWAYS_EVAL# to indicate the given PL entry is active or not. 
   owner                VARCHAR(250),    --SQLWAYS_EVAL# the resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent. 
   status               VARCHAR(50),    --SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above. 
   effectiveDate        TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid. 
   expirationDate       TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid. 
   entryStateGuid       VARCHAR(36) NOT NULL    --SQLWAYS_EVAL# to the entry state details of the pick list entry. 
) WITH OIDS;

COMMENT ON TABLE @PREFIX@h_vsPLEntry IS 'SQLWAYS_EVAL# entries of a Pick List. An inclusion (logical model object : pickListEntry) or exclusion (logical model object : pickListEntryExclusion) in a pick list definition.';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.vsPLEntryGuid        IS 'SQLWAYS_EVAL# for the pick list entry.';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.vsPickListGuid       IS 'SQLWAYS_EVAL# the corresponding pick list definition.';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.plEntryId            IS 'SQLWAYS_EVAL# of the pick list node within the list.';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.entityCodeNamespace  IS 'SQLWAYS_EVAL# of the namespace of the entity code if different than the pickListDefinition defaultEntityCodeNamespace. entityCodeNamespace must match a local id of a supportedNamespace in the mappings section.';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.entityCode           IS 'SQLWAYS_EVAL# that this entry represents, if any. If not present, this entry is not selectable.';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.entryOrder           IS 'SQLWAYS_EVAL# of this entry in the list.  If absent, this entry follows any ordered entries, but is unordered beyond that';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.isDefault            IS 'SQLWAYS_EVAL# this is the default entry for the supplied language and context.';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.matchIfNoContext     IS 'SQLWAYS_EVAL# this entry can be used if no contexts are supplied, even though pickContext ispresent.';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.propertyId           IS 'SQLWAYS_EVAL# associated with the entityCode and entityCodeNamespace that the pickText was derived from.  If absent, the pick text can be anything. Some terminologies may have business rules requiring this attribute to be present.';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.language             IS 'SQLWAYS_EVAL# of the language to be used when the application/user supplies a selection language matches. If absent, this matches all languages. language must match a local id od of a supportedLanguage in the mappings section.';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.include              IS 'SQLWAYS_EVAL# indicate the given PL entry node has to be included or not when the pick list is resolved.';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.pickText             IS 'SQLWAYS_EVAL# this node in the pick list. Some business rules may require that this string match a presentation associated with the entityCode.';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.isActive             IS 'SQLWAYS_EVAL# indicate the given PL entry is active or not.';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.owner                IS 'SQLWAYS_EVAL# resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent.';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.status               IS 'SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above.';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.effectiveDate        IS 'SQLWAYS_EVAL# that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid.';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.expirationDate       IS 'SQLWAYS_EVAL# that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid.';
COMMENT ON COLUMN @PREFIX@h_vsPLEntry.entryStateGuid       IS 'SQLWAYS_EVAL# to the entry state details of the pick list entry.';

CREATE TABLE @PREFIX@h_vsProperty
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
   propertyValue         TEXT NOT NULL,    --SQLWAYS_EVAL# the property associated with this particular resource.  Note that "text" may be any type, including a URI, html fragment, etc. 
   isActive              BOOLEAN,    --SQLWAYS_EVAL# to indicate the given property is active or not. 
   owner                 VARCHAR(250),    --SQLWAYS_EVAL# the resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent. 
   status                VARCHAR(50),    --SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above. 
   effectiveDate         TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid. 
   expirationDate        TIMESTAMP,    --SQLWAYS_EVAL# time that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid. 
   entryStateGuid        VARCHAR(36) NOT NULL    --SQLWAYS_EVAL# to the entry state details of the given property. 
) WITH OIDS;

COMMENT ON TABLE @PREFIX@h_vsProperty IS 'SQLWAYS_EVAL# collection of properties belonging to value set and pick list definitions. ';
COMMENT ON COLUMN @PREFIX@h_vsProperty.vsPropertyGuid        IS 'SQLWAYS_EVAL# for the property.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.referenceGuid         IS 'SQLWAYS_EVAL# object to which a given property belongs to.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.referenceType         IS 'SQLWAYS_EVAL# object to which a given property belongs to. Namely, entity, codingScheme etc.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.propertyId            IS 'SQLWAYS_EVAL# of this particular propert/resource/value instance.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.propertyType          IS 'SQLWAYS_EVAL# element that this property represents.  As an example, the codingScheme "copyright" attribute could be represented by a property with a propertyType that mapped to lgCS:copyRight. Must match a local id of a supportedPropertyType in the corresponding mappings section.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.propertyName          IS 'SQLWAYS_EVAL# that defines the meaning of this particular property entry. Must match a local id of a supportedProperty in the corresponding supportedAttributes section.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.language              IS 'SQLWAYS_EVAL# of the language of the property value. Must match a local id of a supportedLanguage in the corresponding mappings section. If omitted, and language is applicable to this property, the defaultLanguage of the surrounding resource is used.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.format                IS 'SQLWAYS_EVAL# of the property value.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.isPreferred           IS 'SQLWAYS_EVAL# if the text meets the selection criteria, it should be the preferred form. For a given language there should be only one preferred presentation.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.matchIfNoContext      IS 'SQLWAYS_EVAL# this presentation is valid in a acontextual setting - that it is always valid in the given language.  Default: true  if there are no property usageContexts, false otherwise.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.degreeOfFidelity      IS 'SQLWAYS_EVAL# that states how closely a term approximates the intended meaning of an entry code. degreeOfFidelity must match a local id of a supportedDegreeOfFidelity in the corresponding mappings section.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.representationalForm  IS 'SQLWAYS_EVAL# that states how the term represents the concept (abbrev, acronym, etc.) representationalForm must match a local id of a representationalForm in the corresponding mappings section.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.propertyValue         IS 'SQLWAYS_EVAL# property associated with this particular resource.  Note that "text" may be any type, including a URI, html fragment, etc.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.isActive              IS 'SQLWAYS_EVAL# indicate the given property is active or not.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.owner                 IS 'SQLWAYS_EVAL# resource. The specific semantics of owner is defined by the busniess rules of the implementor, including the rules of the owner field is absent.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.status                IS 'SQLWAYS_EVAL# associated with the particular resource. The semantics and business rules of entryStatus are defined by the containing system, but there needs to be a mapping into isActive above.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.effectiveDate         IS 'SQLWAYS_EVAL# that this resource is considered to be active.  To be considered active, isActive must be true, and the temporal context of the operation must be greater than effectiveDate. If omitted, all temporal contexts are considered to be valid.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.expirationDate        IS 'SQLWAYS_EVAL# that this resource is considered to become inActive.  To be considered active, isActive must be true, and the temporal context of the operation must be less than expirationDate.  If omitted, all temporal contexts are considered to be valid.';
COMMENT ON COLUMN @PREFIX@h_vsProperty.entryStateGuid        IS 'SQLWAYS_EVAL# to the entry state details of the given property.';

CREATE TABLE @PREFIX@h_vsPropertyMultiAttrib
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
) WITH OIDS;

COMMENT ON TABLE @PREFIX@h_vsPropertyMultiAttrib IS 'SQLWAYS_EVAL# property mulit attributes like qualifiers, sources and usage contexts for a given property.';
COMMENT ON COLUMN @PREFIX@h_vsPropertyMultiAttrib.vsPropMultiAttribGuid  IS 'SQLWAYS_EVAL# for multi attributes entries.';
COMMENT ON COLUMN @PREFIX@h_vsPropertyMultiAttrib.vsPropertyGuid         IS 'SQLWAYS_EVAL# the corresponding property.';
COMMENT ON COLUMN @PREFIX@h_vsPropertyMultiAttrib.attributeType          IS 'SQLWAYS_EVAL# attribute stored. Typically "qualifier" or "source" or "usage context".';
COMMENT ON COLUMN @PREFIX@h_vsPropertyMultiAttrib.attributeId            IS 'SQLWAYS_EVAL# qualifier. In case of source and usage context value will be null.';
COMMENT ON COLUMN @PREFIX@h_vsPropertyMultiAttrib.attributeValue         IS 'SQLWAYS_EVAL# attributes.';
COMMENT ON COLUMN @PREFIX@h_vsPropertyMultiAttrib.subRef                 IS 'SQLWAYS_EVAL# id or other localized information within the source. Used to make a source reference more precise. ';
COMMENT ON COLUMN @PREFIX@h_vsPropertyMultiAttrib.role                   IS 'SQLWAYS_EVAL# of the source for this particular resource (e.g. author, distributor).';
COMMENT ON COLUMN @PREFIX@h_vsPropertyMultiAttrib.qualifierType          IS 'SQLWAYS_EVAL# of a property.';
COMMENT ON COLUMN @PREFIX@h_vsPropertyMultiAttrib.entryStateGuid         IS 'SQLWAYS_EVAL# to the entry state details of the value domain property Multi attributes.';


--SQLWAYS_EVAL# Key Constraints 
ALTER TABLE @PREFIX@h_valueSetDefinition ADD CONSTRAINT PK_H_VALUESETDEF 
PRIMARY KEY(entryStateGuid);

ALTER TABLE @PREFIX@h_vsdEntry ADD CONSTRAINT PK_H_VSDENTRY 
PRIMARY KEY(entryStateGuid);

ALTER TABLE @PREFIX@h_vsPickList ADD CONSTRAINT PK_H_VSPICKLIST 
PRIMARY KEY(entryStateGuid);

ALTER TABLE @PREFIX@h_vsPLEntry ADD CONSTRAINT PK_H_VSPLENTRY 
PRIMARY KEY(entryStateGuid);

ALTER TABLE @PREFIX@h_vsProperty ADD CONSTRAINT PK_H_VSPROPERTY 
PRIMARY KEY(entryStateGuid);


--SQLWAYS_EVAL# 
CREATE INDEX IDX_H_VSDNAME ON @PREFIX@h_valueSetDefinition
(valueSetDefName);

CREATE INDEX IDX_H_VSDURI ON @PREFIX@h_valueSetDefinition
(valueSetDefURI);

CREATE INDEX IDX_H_VALUESETDEFGUID ON @PREFIX@h_vsdEntry
(valueSetDefGuid);

CREATE INDEX IDX_H_VSDENT_ENTITYCODE ON @PREFIX@h_vsdEntry
(entityCode);

CREATE INDEX IDX_H_PICKLISTID ON @PREFIX@h_vsPickList
(pickListId);

CREATE INDEX IDX_H_REPRESENTSVSD ON @PREFIX@h_vsPickList
(representsValueSetDefinition);

CREATE INDEX IDX_H_VSPICKLISTGUID ON @PREFIX@h_vsPLEntry
(vsPickListGuid);

CREATE INDEX IDX_H_ENTITYCODE ON @PREFIX@h_vsPLEntry
(entityCode);

CREATE INDEX IDX_H_VSPROPERTY ON @PREFIX@h_vsProperty
(referenceGuid, 
propertyId, 
propertyName);

CREATE INDEX IDX_H_VSPROPERTYGUID ON @PREFIX@h_vsPropertyMultiAttrib
(vsPropertyGuid);


--SQLWAYS_EVAL# Key Constraints 
ALTER TABLE @PREFIX@h_valueSetDefinition ADD CONSTRAINT FK_H_VALUESETDEF_SYSTEMRELEASE 
FOREIGN KEY(releaseGuid) REFERENCES @PREFIX@systemRelease(releaseGuid);

ALTER TABLE @PREFIX@h_valueSetDefinition ADD CONSTRAINT FK_H_VALUESETDEF_VALUESETDEFIN 
FOREIGN KEY(valueSetDefGuid) REFERENCES @PREFIX@valueSetDefinition(valueSetDefGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@h_vsdEntry ADD CONSTRAINT FK_H_VSDENTRY_VSDENTRY 
FOREIGN KEY(vsdEntryGuid) REFERENCES @PREFIX@vsdEntry(vsdEntryGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@h_vsMultiAttrib ADD CONSTRAINT FK_H_VSMULTIATTR_VSMULTIATTRIB 
FOREIGN KEY(vsMultiAttribGuid) REFERENCES @PREFIX@vsMultiAttrib(vsMultiAttribGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@h_vsPickList ADD CONSTRAINT FK_H_VSPICKLIST_SYSTEMRELEASE 
FOREIGN KEY(releaseGuid) REFERENCES @PREFIX@systemRelease(releaseGuid);

ALTER TABLE @PREFIX@h_vsPickList ADD CONSTRAINT FK_H_VSPICKLIST_VSPICKLIST 
FOREIGN KEY(vsPickListGuid) REFERENCES @PREFIX@vsPickList(vsPickListGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@h_vsPLEntry ADD CONSTRAINT FK_H_VSPLENTRY_VSPLENTRY 
FOREIGN KEY(vsPLEntryGuid) REFERENCES @PREFIX@vsPLEntry(vsPLEntryGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@h_vsProperty ADD CONSTRAINT FK_H_VSPROPERTY_VSPROPERTY 
FOREIGN KEY(vsPropertyGuid) REFERENCES @PREFIX@vsProperty(vsPropertyGuid)
ON DELETE CASCADE;

ALTER TABLE @PREFIX@h_vsPropertyMultiAttrib ADD CONSTRAINT FK_H_VSPROPERTYM_VSPROPERTYMUL 
FOREIGN KEY(vsPropMultiAttribGuid) REFERENCES @PREFIX@vsPropertyMultiAttrib(vsPropMultiAttribGuid)
ON DELETE CASCADE;