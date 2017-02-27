@echo off
REM Loads Value Set Definition content, provided in LexGrid canonical xml format.
REM
REM Options:
REM   -in,--input <uri> URI or path specifying location of the source file.
REM   -v, --validate <int> Perform validation of the candidate
REM         resource without loading data.  
REM         Supported levels of validation include:
REM         0 = Verify document is well-formed
REM         1 = Verify document is valid
REM Example: LoadValueSetDefinition -in "file:///path/to/file.xml"
REM
java -Xmx1300m -Djava.awt.headless=true -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.lexgrid.valuesets.admin.LoadValueSetDefinition %*