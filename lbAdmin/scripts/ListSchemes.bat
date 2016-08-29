@echo off
REM List all currently registered vocabularies.
REM Options:
REM   -b,--brief List only coding scheme name, version, urn, and tags (default).
REM   -f,--full  List full detail for each scheme.
REM 
REM Example: ListSchemes
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.ListSchemes %*