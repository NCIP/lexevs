@echo off
REM Loads Value Set Definition content, provided in LexGrid canonical xml format.
REM
REM
REM Load the resolved ValueSet Definition 
REM
REM Options
REM Example: java org.lexgrid.valuesets.admin.LoadResolvedValueSetDefinition
REM  -u, URN uniquely identifying the ValueSet Definition
REM  -l, &lt;id&gt; List of coding scheme versions to use for the resolution
REM      in format "csuri1::version1, csuri2::version2"
REM  -csSourceTag, Resolves against scheme bearing this tag, eg: development, production
REM  -vsTag, User defined Tag to apply to the resulting resolved value set scheme  
REM        
REM Example: LoadRevolvedValueSetDefinition LoadResolvedValueSetDefinition -u "Automobiles:valuesetDefinitionURI\" -l "Automobiles::version1, GM::version2" -csSourceTag "production" -vsTag "PRODUCTION"
REM
java -Xmx1500m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*"  org.lexgrid.valuesets.admin.LoadResolvedValueSetDefinition %*