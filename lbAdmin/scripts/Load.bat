@echo off
REM A generic loader script that allows for the selection of a Loader Extension.
REM
REM Options:
REM   -in,--input <uri> URI or path specifying location of the source file
REM   -l, --loader <name> LexGrid Load Extension name
REM   -mf,--manifest <uri> URI or path specifying location of the manifest file
REM   -a, --activate ActivateScheme on successful load; if unspecified the vocabulary is loaded but not activated
REM   -t, --tag <id> An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign. 
REM
REM Example: LoadText -in "file:///path/to/file.txt" -a
REM
java -Xmx1000m -Djava.awt.headless=true -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.Load %*