--SQLWAYS_EVAL# 
--SQLWAYS_EVAL# Enterprise Architect Version 7.5.850
--SQLWAYS_EVAL# Tuesday, 01 June, 2010 
--SQLWAYS_EVAL# Oracle 
--SQLWAYS_EVAL# 




--SQLWAYS_EVAL# 
CREATE TABLE @PREFIX@registry
(
   registryGuid         VARCHAR(36) NOT NULL,    --SQLWAYS_EVAL# identifier of the resource loaded. 
   resourceURI          VARCHAR(250),    --SQLWAYS_EVAL# loaded into lexEVS system. 
   resourceVersion      VARCHAR(50),    --SQLWAYS_EVAL# version identifier that this particular instance represents. 
   resourceType         VARCHAR(50),    --SQLWAYS_EVAL# registry entry represents. Ex. CodingScheme, ValueDomain, PickList, History etc. 
   dbURI                VARCHAR(250),    --SQLWAYS_EVAL# that contains this image 
   dbName               VARCHAR(50),    --SQLWAYS_EVAL#  
   prefix               VARCHAR(20),    --SQLWAYS_EVAL# (e.g. database prefix, HTML sub-page, etc. that represents this particular instance within the URL 
   stagingPrefix        VARCHAR(20),
   status               VARCHAR(50),    --SQLWAYS_EVAL#  - the state of this registry entry. Note that the activation, deactivation and status entries do not have any relationship with the effectiveDate, expirationDate and isActive fields in the coding scheme itself.  In particular, it is possible to mark an "inactive" coding scheme as "active" (i.e. accessible) in the registry itself. 
   tag                  VARCHAR(50),    --SQLWAYS_EVAL# test, etc.) associated with this release 
   lastUpdateDate       TIMESTAMP,    --SQLWAYS_EVAL# a record was last changed. 
   activationDate       TIMESTAMP,    --SQLWAYS_EVAL# a given entry will (or has) become active.  If absent, the record is considered inactive. 
   deactivationDate     TIMESTAMP,    --SQLWAYS_EVAL# a given record was (or will be) deactivated. 
   baseRevision         VARCHAR(50),    --SQLWAYS_EVAL# id of the resource when the revision record was created 
   fixedAtRevision      VARCHAR(50),    --SQLWAYS_EVAL# version is "fixed" at this revision.  If the coding scheme isn't "at" the same revision, rollback must be done to reach this.  If absent,  this version is represented by the latest revision of the coding scheme, whatever it may be. 
   isLocked             BOOLEAN,    --SQLWAYS_EVAL#  updates are not allowed in the coding scheme. Default: false 
   dbSchemaVersion      VARCHAR(50),    --SQLWAYS_EVAL# database schema structure. 
   dbSchemaDescription  VARCHAR(255)    --SQLWAYS_EVAL# version description. 
) WITH OIDS;

COMMENT ON COLUMN @PREFIX@registry.registryGuid         IS 'SQLWAYS_EVAL# of the resource loaded.';
COMMENT ON COLUMN @PREFIX@registry.resourceURI          IS 'SQLWAYS_EVAL# loaded into lexEVS system.';
COMMENT ON COLUMN @PREFIX@registry.resourceVersion      IS 'SQLWAYS_EVAL# identifier that this particular instance represents.';
COMMENT ON COLUMN @PREFIX@registry.resourceType         IS 'SQLWAYS_EVAL# entry represents. Ex. CodingScheme, ValueDomain, PickList, History etc.';
COMMENT ON COLUMN @PREFIX@registry.dbURI                IS 'SQLWAYS_EVAL# that contains this image';
COMMENT ON COLUMN @PREFIX@registry.dbName               IS 'SQLWAYS_EVAL# ';
COMMENT ON COLUMN @PREFIX@registry.prefix               IS 'SQLWAYS_EVAL# (e.g. database prefix, HTML sub-page, etc. that represents this particular instance within the URL';
COMMENT ON COLUMN @PREFIX@registry.status               IS 'SQLWAYS_EVAL#  - the state of this registry entry. Note that the activation, deactivation and status entries do not have any relationship with the effectiveDate, expirationDate and isActive fields in the coding scheme itself.  In particular, it is possible to mark an "inactive" coding scheme as "active" (i.e. accessible) in the registry itself.';
COMMENT ON COLUMN @PREFIX@registry.tag                  IS 'SQLWAYS_EVAL# test, etc.) associated with this release';
COMMENT ON COLUMN @PREFIX@registry.lastUpdateDate       IS 'SQLWAYS_EVAL# a record was last changed.';
COMMENT ON COLUMN @PREFIX@registry.activationDate       IS 'SQLWAYS_EVAL# given entry will (or has) become active.  If absent, the record is considered inactive.';
COMMENT ON COLUMN @PREFIX@registry.deactivationDate     IS 'SQLWAYS_EVAL# given record was (or will be) deactivated.';
COMMENT ON COLUMN @PREFIX@registry.baseRevision         IS 'SQLWAYS_EVAL# of the resource when the revision record was created';
COMMENT ON COLUMN @PREFIX@registry.fixedAtRevision      IS 'SQLWAYS_EVAL# version is "fixed" at this revision.  If the coding scheme isn''t "at" the same revision, rollback must be done to reach this.  If absent,  this version is represented by the latest revision of the coding scheme, whatever it may be.';
COMMENT ON COLUMN @PREFIX@registry.isLocked             IS 'SQLWAYS_EVAL#  updates are not allowed in the coding scheme. Default: false';
COMMENT ON COLUMN @PREFIX@registry.dbSchemaVersion      IS 'SQLWAYS_EVAL# database schema structure.';
COMMENT ON COLUMN @PREFIX@registry.dbSchemaDescription  IS 'SQLWAYS_EVAL# version description.';

CREATE TABLE @PREFIX@registryMetaData
(
   id                        INTEGER,
   lastUpdateTime            TIMESTAMP,
   lastUsedDBIdentifer       VARCHAR(50),
   lastUsedHistoryIdentifer  VARCHAR(50)
) WITH OIDS;



--SQLWAYS_EVAL# Key Constraints 
ALTER TABLE @PREFIX@registry ADD CONSTRAINT PK_REGISTRY 
PRIMARY KEY(registryGuid);


--SQLWAYS_EVAL# 
ALTER TABLE @PREFIX@registry
ADD CONSTRAINT UQ_REGISTRY UNIQUE(resourceURI,resourceVersion,resourceType);