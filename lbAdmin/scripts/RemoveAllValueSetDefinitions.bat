@echo off
REM WARNING: Removes all resolved value set coding schemes without discrimination
REM
REM
REM Example: RemoveAllResolvedValueSets.bat
REM
java -Xmx3000m -cp "../runtime/lbPatch.jar;../runtime-components/extLib/*" org.lexgrid.valuesets.admin.RemoveAllValueSetDefinitions %*
