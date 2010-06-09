CREATE TABLE @PREFIX@nciHistSystemRelease ( 
	releaseGuid VARCHAR(36) NOT NULL,
	codingSchemeUri VARCHAR(36) NOT NULL,
	releaseId VARCHAR(50) NOT NULL,
	releaseURI VARCHAR(250) NOT NULL,
	basedOnRelease VARCHAR(250),
	releaseDate TIMESTAMP NOT NULL,
	releaseAgency varchar(250) NOT NULL,
	description description CLOB(2G) NOT LOGGED
)
;

CREATE TABLE @PREFIX@nciHist ( 
	ncitHistGuid VARCHAR(36) NOT NULL,
	releaseGuid VARCHAR(36) NOT NULL,
	entityCode VARCHAR(100) NOT NULL,
	conceptName VARCHAR NOT NULL,
	editAction VARCHAR(10) NOT NULL,
	editDate TIMESTAMP NOT NULL,
	referenceCode VARCHAR(100),
	referenceName description CLOB(2G) NOT LOGGED
)
;

ALTER TABLE @PREFIX@nciHistSystemRelease ADD CONSTRAINT @PREFIX@PK_nciHistSystemRelease 
	PRIMARY KEY (releaseGuid)
;

ALTER TABLE @PREFIX@nciHist ADD CONSTRAINT PK_nciHist 
	PRIMARY KEY (ncitHistGuid)
;

ALTER TABLE @PREFIX@nciHist ADD CONSTRAINT @PREFIX@FK_nciHist
	FOREIGN KEY (releaseGuid) REFERENCES @PREFIX@nciHistSystemRelease (releaseGuid)
ON DELETE CASCADE
;

--  Create Indexes 
CREATE INDEX idx_ncitHist
ON @PREFIX@nciHist (entityCode, editAction)
;

CREATE INDEX idx_editDate
ON @PREFIX@nciHist (editDate)
;

CREATE INDEX idx_referenceCode
ON @PREFIX@nciHist (referenceCode)
;

ALTER TABLE @PREFIX@systemRelease
	ADD CONSTRAINT UQ_ncih_sysRel UNIQUE (releaseId, releaseURI)
;

CREATE INDEX idx_releaseId
ON @PREFIX@systemRelease (releaseId)
;

CREATE INDEX idx_releaseDate
ON @PREFIX@systemRelease (releaseDate)
;