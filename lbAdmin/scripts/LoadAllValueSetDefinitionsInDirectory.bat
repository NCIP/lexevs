@echo off
REM Loads All LexGrid XML formatted Value Set Definitions in a given directory
REM
REM Options:
REM   -in, Path to directory
REM        
REM Example: LoadAllValueSetDefinitionsInDirectory -in /path/to/directory
REM
java -Xmx3000m -cp "../runtime/lbPatch.jar;../runtime-components/extLib/*" org.lexgrid.valuesets.admin.LoadAllValueSetDefinitionsInDirectory %*

