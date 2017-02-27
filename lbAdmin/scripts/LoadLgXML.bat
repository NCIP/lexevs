@echo off
REM Loads a vocabulary file, provided in LexGrid canonical xml format.
REM
REM Options:
REM   -in,--input <uri> URI or path specifying location of the source file.
REM   -v, --validate <int> Perform validation of the candidate
REM         resource without loading data.  If specified, the '-a' and '-t'
REM         options are ignored.  Supported levels of validation include:
REM         0 = Verify document is well-formed
REM         1 = Verify document is valid
REM   -a, --activate ActivateScheme on successful load; if unspecified the
REM         vocabulary is loaded but not activated.
REM   -t, --tag <id> An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign.
REM
REM Example: LoadLgXML -in "file:///path/to/file.xml" -a
REM        LoadLgXML -in "file:///path/to/file.xml" -v 0
REM
java -Xmx1000m -Djava.awt.headless=true -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.LoadLgXML %*