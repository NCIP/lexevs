@echo off
REM Loads a mappings file(s), provided in UMLS RRF format.
REM
REM Options:
REM   -inMap,--input <uri> URI or path specifying location of the MRMAP source file.
REM   -inSat,--input <uri> URI or path specifying location of the MRSAT source file.
REM
REM Example: Example: LoadMrMap -inMap "file:///path/to/MRMAP.RRF -inSat "file:///path/to/MRSAT.RRF"
REM
java -Xmx1000m -Djava.awt.headless=true -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.LoadMrMap %*