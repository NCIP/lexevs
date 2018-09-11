@echo off
REM
REM WARNING: Removes all resolved value set coding schemes without discrimination
REM
REM Options
REM   -f,--force Force removing all value set definitions (no confirmation).
REM 
REM Example: RemoveAllResolvedValueSets.bat
REM Example: RemoveAllResolvedValueSets.bat -f
REM
java -Xmx3000m -cp "../runtime/lbPatch.jar;../runtime-components/extLib/*" org.lexgrid.valuesets.admin.RemoveAllValueSetDefinitions %*
