@echo off
REM Loads a file in LexGrid Text format.
REM
REM Options:
REM   -in,--input <uri> URI or path specifying location of the source file
REM   -d, --delimiter <character> defaults to tab the character used to delimit pair
REM         or triple components and the nesting.
REM   -mf,--manifest <uri> URI or path specifying location of the manifest file
REM   -v, --validate <int> Perform validation of the candidate
REM         resource without loading data.  If specified, the '-a' and '-t'
REM         options are ignored.  Supported levels of validation include:
REM         0 = Verify document is valid
REM   -a, --activate ActivateScheme on successful load; if unspecified the vocabulary is loaded but not activated
REM   -t, --tag <id> An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign. 
REM
REM Example: LoadText -in "file:///path/to/file.txt" -a
REM        LoadText -in "file:///path/to/file.txt" -v 0
REM
java -Xmx1000m -Djava.awt.headless=true -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.LoadText %*