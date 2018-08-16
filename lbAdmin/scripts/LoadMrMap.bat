@echo off
REM Loads a mappings file(s), provided in UMLS RRF format.
REM
REM Options:
REM   -inMap,--input <uri> URI or path specifying location of the MRMAP source file.
REM   -inSat,--input <uri> URI or path specifying location of the MRSAT source file.
REM   -meta --meatadata input &lt;uri&gt; URI or path specifying location of the metadata source file.  
REM        metadata is applied to the code system and code system version being loaded.
REM   -metav  --validate metadata &lt;int&gt; Perform validation of the metadata source file
REM         without loading data.  Supported levels of validation include:
REM         0 = Verify document is valid
REM         metadata is validated against the code system and code system version being loaded.
REM   -metao, --overwrite If specified, existing metadata for the code system
REM         will be erased. Otherwise, new metadata will be appended to
REM         existing metadata (if present).  
REM   -metaf,--force Force overwrite (no confirmation).
REM
REM Example: LoadMrMap -inMap "file:///path/to/MRMAP.RRF -inSat "file:///path/to/MRSAT.RRF"
REM          LoadMrMap -inMap "file:///path/to/MRMAP.RRF -inSat "file:///path/to/MRSAT.RRF" -meta "file:///path/to/metadata.xml" -metao
REM     
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.LoadMrMap %*