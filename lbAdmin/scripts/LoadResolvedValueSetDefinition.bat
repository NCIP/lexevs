@echo off
REM Loads Value Set Definition content, provided in LexGrid canonical xml format.
REM
REM Options:
REM   -u, The valueset definition URI to use 
REM   -l, The list of coding schemes to revolve against. The format is codingschemeName::version
REM   -csVersionTag    The tag to use for resolving coding scheme
REM        
REM Example: LoadRevolvedValueSetDefinition LoadResolvedValueSetDefinition -u \"Automobiles:valuesetDefinitionURI\" -l \"Automobiles::version1, GM::version2\" -csVersionTag \"production\" ""
REM
java -Xmx1500m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*"  org.lexgrid.valuesets.admin.LoadResolvedValueSetDefinition %*