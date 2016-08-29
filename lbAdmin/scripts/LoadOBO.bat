@echo off
REM Loads a file specified in the Open Biomedical Ontologies (OBO) format.
REM
REM Options:
REM   -in,--input <uri> URI or path specifying location of the source file
REM   -mf,--manifest <uri> URI or path specifying location of the manifest file
REM   -v, --validate <int> Perform validation of the candidate
REM         resource without loading data.  If specified, the '-a' and '-t'
REM         options are ignored.  Supported levels of validation include:
REM         0 = Verify document is valid
REM   -a, --activate ActivateScheme on successful load; if unspecified the vocabulary is loaded but not activated
REM   -t, --tag <id> An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign. 
REM
REM Example: LoadOBO -in "file:///path/to/file.obo" -a
REM        LoadOBO -in "file:///path/to/file.obo" -v 0
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.LoadOBO %*