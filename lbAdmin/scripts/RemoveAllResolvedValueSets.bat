@echo off
REM WARNING:  REMOVES ALL RESOLVED VALUE SETS. 
REM
REM Example: ./RemoveAllResolvedValueSets.sh
REM
java -Xmx3000m -cp "../runtime/lbPatch.jar;../runtime-components/extLib/*" org.lexgrid.valuesets.admin.RemoveAllResolvedValueSets %*
