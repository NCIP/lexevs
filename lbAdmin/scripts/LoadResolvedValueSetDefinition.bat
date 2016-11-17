@echo off
REM
REM
REM Load the resolved ValueSet Definition 
REM
REM Options:
REM   -u, The valueset definition URI to use 
REM   -l, The list of coding schemes to revolve against. The format is codingschemeName::version
REM   -csSourceTag, Resolves against scheme bearing this tag, eg: development, production" 
REM   -vsTag, User defined Tag to apply to the resulting resolved value set scheme  
REM   -vsVersion, Optional user defined version that can be applied to the resolved value set scheme
REM        
REM Example: LoadRevolvedValueSetDefinition LoadResolvedValueSetDefinition -u "Automobiles:valuesetDefinitionURI" 
REM -l "Automobiles::version1, GM::version2" -csVersionTag "production" -vsTag "PRODUCTION" -vsVersion "MyVersion"
REM
java -Xmx1500m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*"  org.lexgrid.valuesets.admin.LoadResolvedValueSetDefinition %*