CREATE TABLE valueSetDefinition
(
	valueSetDefGuid      VARCHAR2(36) NOT NULL,
	valueSetDefURI       VARCHAR2(250) NOT NULL,
	valueSetDefName      VARCHAR2(250) NOT NULL,
	defaultCodingScheme  VARCHAR2(50),
	conceptDomain        VARCHAR2(200),
	description          CLOB,
	releaseGuid          VARCHAR2(36),
	isActive             CHAR(1),
	owner                VARCHAR2(250),
	status               VARCHAR2(50),
	effectiveDate        TIMESTAMP,
	expirationDate       TIMESTAMP,
	entryStateGuid       VARCHAR2(36)
)
;


CREATE TABLE vsdEntry
(
	vsdEntryGuid             VARCHAR2(36) NOT NULL,
	valueSetDefGuid          VARCHAR2(36) NOT NULL,
	ruleOrder                NUMBER(18) NOT NULL,
	operator                 VARCHAR2(15) NOT NULL,
	codingSchemeReference    VARCHAR2(50),
	valueSetDefReference     VARCHAR2(250),
	entityCode               VARCHAR2(200),
	entityCodeNamespace      VARCHAR2(50),
	leafOnly                 CHAR(1),
	referenceAssociation     VARCHAR2(50),
	targetToSource           CHAR(1),
	transitiveClosure        CHAR(1),
	propertyRefCodingScheme  VARCHAR2(50),
	propertyName             VARCHAR(50),
	propertyMatchValue       CLOB,
	matchAlgorithm           VARCHAR2(250),
	format                   VARCHAR2(50),
	isActive                 CHAR(1),
	owner                    VARCHAR2(250),
	status                   VARCHAR2(50),
	effectiveDate            TIMESTAMP,
	expirationDate           TIMESTAMP,
	entryStateGuid           VARCHAR2(36)
)
;


CREATE TABLE vsEntryState
(
	entryStateGuid      VARCHAR2(36) NOT NULL,
	entryGuid           VARCHAR2(36) NOT NULL,
	entryType           VARCHAR2(50),
	changeType          VARCHAR2(15),
	relativeOrder       NUMBER(18),
	revisionGuid        VARCHAR2(36),
	prevRevisionGuid    VARCHAR2(36),
	prevEntryStateGuid  VARCHAR2(36)
)
;


CREATE TABLE vsMultiAttrib
(
	vsMultiAttribGuid  VARCHAR2(36) NOT NULL,
	referenceGuid      VARCHAR2(36) NOT NULL,
	referenceType      VARCHAR2(50),
	attributeType      VARCHAR2(30) NOT NULL,
	attributeId        VARCHAR2(50),
	attributeValue     VARCHAR2(250) NOT NULL,
	subRef             VARCHAR2(250),
	role               VARCHAR2(250),
	entryStateGuid     VARCHAR2(36)
)
;


CREATE TABLE vsPickList
(
	vsPickListGuid              VARCHAR2(36) NOT NULL,
	pickListId                  VARCHAR2(50) NOT NULL,
	representsValueSetDef       VARCHAR2(250) NOT NULL,
	completeSet                 CHAR(1),
	defaultEntityCodeNamespace  VARCHAR2(50),
	defaultLanguage             VARCHAR2(50),
	defaultSortOrder            VARCHAR2(50),
	description                 CLOB,
	releaseGuid                 VARCHAR2(36),
	isActive                    CHAR(1),
	owner                       VARCHAR2(250),
	status                      VARCHAR2(50),
	effectiveDate               TIMESTAMP,
	expirationDate              TIMESTAMP,
	entryStateGuid              VARCHAR2(36)
)
;


CREATE TABLE vsPLEntry
(
	vsPLEntryGuid        VARCHAR2(36) NOT NULL,
	vsPickListGuid       VARCHAR2(36) NOT NULL,
	plEntryId            VARCHAR2(50) NOT NULL,
	entityCodeNamespace  VARCHAR2(50),
	entityCode           VARCHAR2(200) NOT NULL,
	entryOrder           NUMBER(18),
	isDefault            CHAR(1),
	matchIfNoContext     CHAR(1),
	propertyId           VARCHAR2(50),
	include              CHAR(1),
	pickText             CLOB,
	isActive             CHAR(1),
	owner                VARCHAR2(250),
	status               VARCHAR2(50),
	effectiveDate        TIMESTAMP,
	expirationDate       TIMESTAMP,
	entryStateGuid       VARCHAR2(36)
)
;


CREATE TABLE vsProperty
(
	vsPropertyGuid        VARCHAR2(36) NOT NULL,
	referenceGuid         VARCHAR2(36) NOT NULL,
	referenceType         VARCHAR2(50) NOT NULL,
	propertyId            VARCHAR2(50),
	propertyType          VARCHAR2(15),
	propertyName          VARCHAR2(50) NOT NULL,
	language              VARCHAR2(32),
	format                VARCHAR2(50),
	isPreferred           CHAR(1),
	matchIfNoContext      CHAR(1),
	degreeOfFidelity      VARCHAR2(50),
	representationalForm  VARCHAR2(50),
	propertyValue         CLOB NOT NULL,
	isActive              CHAR(1) NOT NULL,
	owner                 VARCHAR2(250),
	status                VARCHAR2(50),
	effectiveDate         TIMESTAMP,
	expirationDate        TIMESTAMP,
	entryStateGuid        VARCHAR2(36) NOT NULL
)
;


CREATE TABLE vsPropertyMultiAttrib
(
	vsPropMultiAttribGuid  VARCHAR2(36) NOT NULL,
	vsPropertyGuid         VARCHAR2(36) NOT NULL,
	attributeType          VARCHAR2(30) NOT NULL,
	attributeId            VARCHAR2(50),
	attributeValue         VARCHAR2(250) NOT NULL,
	subRef                 VARCHAR2(250),
	role                   VARCHAR2(250),
	entryStateGuid         VARCHAR2(36)
)
;


CREATE TABLE vsSupportedAttrib
(
	vsSuppAttribGuid        VARCHAR2(36) NOT NULL,
	referenceGuid           VARCHAR2(36),
	supportedAttributeTag   VARCHAR2(30) NOT NULL,
	id                      VARCHAR2(250) NOT NULL,
	uri                     VARCHAR2(250),
	idValue                 VARCHAR2(250),
	rootCode                VARCHAR2(250),
	isForwardNavigable      CHAR(1),
	isImported              CHAR(1),
	equivalentCodingScheme  VARCHAR2(250),
	assemblyRule            VARCHAR2(250)
)
;



ALTER TABLE valueSetDefinition ADD CONSTRAINT PK_valueSetDef 
	PRIMARY KEY (valueSetDefGuid)
;

ALTER TABLE vsdEntry ADD CONSTRAINT PK_vsdEntry 
	PRIMARY KEY (vsdEntryGuid)
;

ALTER TABLE vsEntryState ADD CONSTRAINT PK_vsEntryState 
	PRIMARY KEY (entryStateGuid)
;

ALTER TABLE vsMultiAttrib ADD CONSTRAINT PK_vsMultiAttrib 
	PRIMARY KEY (vsMultiAttribGuid)
;

ALTER TABLE vsPickList ADD CONSTRAINT PK_vsPickList 
	PRIMARY KEY (vsPickListGuid)
;

ALTER TABLE vsPLEntry ADD CONSTRAINT PK_vsPLEntry 
	PRIMARY KEY (vsPLEntryGuid)
;

ALTER TABLE vsProperty ADD CONSTRAINT PK_vsProperty 
	PRIMARY KEY (vsPropertyGuid)
;

ALTER TABLE vsPropertyMultiAttrib ADD CONSTRAINT PK_vsPropertyMultiAttrib 
	PRIMARY KEY (vsPropMultiAttribGuid)
;

ALTER TABLE vsSupportedAttrib ADD CONSTRAINT PK_vsMapping 
	PRIMARY KEY (vsSuppAttribGuid)
;


CREATE INDEX idx_vsdName ON valueSetDefinition
(valueSetDefName ASC)
;

CREATE INDEX idx_vsdURI ON valueSetDefinition
(valueSetDefURI ASC)
;

ALTER TABLE vsdEntry
	ADD CONSTRAINT UQ_vsdEntry_valueSetDefGuid UNIQUE (valueSetDefGuid)
;

ALTER TABLE vsdEntry
	ADD CONSTRAINT UQ_vsdEntry UNIQUE (ruleOrder)
;

CREATE INDEX idx_valueSetDefGuid ON vsdEntry
(valueSetDefGuid ASC)
;

CREATE INDEX idx_vsdEnt_entityCode ON vsdEntry
(entityCode ASC)
;

CREATE INDEX idx_pickListId ON vsPickList
(pickListId ASC)
;

CREATE INDEX idx_representsVSD ON vsPickList
(representsValueSetDef ASC)
;

ALTER TABLE vsPLEntry
	ADD CONSTRAINT UQ_vsPLEntry_vsPickListGuid UNIQUE (vsPickListGuid)
;

ALTER TABLE vsPLEntry
	ADD CONSTRAINT UQ_vsPLEntry UNIQUE (plEntryId)
;

CREATE INDEX idx_vsPickListGuid ON vsPLEntry
(vsPickListGuid ASC)
;

CREATE INDEX idx_entityCode ON vsPLEntry
(entityCode ASC)
;

CREATE INDEX idx_vsProperty ON vsProperty
(referenceGuid ASC, propertyId ASC, propertyName ASC)
;

ALTER TABLE vsProperty
	ADD CONSTRAINT UQ_vsProperty UNIQUE (referenceGuid, propertyId, propertyName)
;

CREATE INDEX idx_vsPropertyGuid ON vsPropertyMultiAttrib
(vsPropertyGuid ASC)
;

ALTER TABLE vsSupportedAttrib
	ADD CONSTRAINT UQ_vsMapping UNIQUE (referenceGuid, supportedAttributeTag, id)
;

CREATE INDEX idx_saReferenceGuid ON vsSupportedAttrib
(referenceGuid ASC)
;


ALTER TABLE valueSetDefinition ADD CONSTRAINT FK_vsd_releaseGuid 
	FOREIGN KEY (releaseGuid) REFERENCES systemRelease (releaseGuid)
;

ALTER TABLE vsdEntry ADD CONSTRAINT FK_vsdEnt_vsdGuid 
	FOREIGN KEY (valueSetDefGuid) REFERENCES valueSetDefinition (valueSetDefName)
ON DELETE CASCADE
;

ALTER TABLE vsEntryState ADD CONSTRAINT FK_vsES_prevEntryStateGuid 
	FOREIGN KEY (prevEntryStateGuid) REFERENCES vsEntryState (entryStateGuid)
;

ALTER TABLE vsPickList ADD CONSTRAINT FK_vsdPL_releaseGuid 
	FOREIGN KEY (releaseGuid) REFERENCES systemRelease (releaseGuid)
;

ALTER TABLE vsPLEntry ADD CONSTRAINT FK_vsPLEnt_vsPLGuid 
	FOREIGN KEY (vsPickListGuid) REFERENCES vsPickList (representsValueSetDef)
ON DELETE CASCADE
;

ALTER TABLE vsPropertyMultiAttrib ADD CONSTRAINT FK_vsProperty 
	FOREIGN KEY (vsPropertyGuid) REFERENCES vsProperty (referenceGuid, propertyId, propertyName)
ON DELETE CASCADE
;
