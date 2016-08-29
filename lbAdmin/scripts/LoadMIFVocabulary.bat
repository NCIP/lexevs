@echo off
REM Loads from an the HL7 Vocabulary mif file.
REM 
REM
REM
REM Options:
REM   -in,--input <uri> URI or path specifying location of the source file
REM   -a, --activate ActivateScheme on successful load; if unspecified the vocabulary is loaded but not activated
REM   -t, --tag <id> An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign. 
REM
REM Example: LoadMIFVocabulary -in "file:///path/to/somefile.mif" -a
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.LoadMIFVocabulary %*