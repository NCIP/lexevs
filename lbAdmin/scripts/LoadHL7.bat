@echo off
REM Loads from an HL7RIM MS Access database. Meta data for contained coding schemes loaded separately
REM 
REM
REM
REM Options:
REM   -in,--input <uri> URI or path specifying location of the source file
REM   -mf,--input <uri> URI or path specifying location of the manifest file.
REM   -lp,--input <uri> URI or path specifying location of the loader preference file.
REM   -v, --validate <int> Perform validation of the candidate
REM         resource without loading data.  If specified, the '-a' and '-t'
REM         options are ignored.  Supported levels of validation include:
REM         0 = Verify document is well-formed
REM         1 = Verify document is valid
REM   -a, --activate ActivateScheme on successful load; if unspecified the vocabulary is loaded but not activated
REM   -t, --tag <id> An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign. 
REM
REM Example: LoadHL7RIM -in "file:///path/to/somefile.mdb" -a
REM          LoadHL7RIM -in "file:///path/to/somefile.mdb" -v 0
REM
java -Xmx1000m -Djava.awt.headless=true -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.LoadHL7RIM %*