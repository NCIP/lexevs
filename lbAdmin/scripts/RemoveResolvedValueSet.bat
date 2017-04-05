@echo off
REM Removes a resolved ValueSet
REM
REM Options:
REM   -l, The list of resolved value sets to remove, separated by comma. 
REM		format "resolvedValueSetUri1::version1, resolvedValueSetUri2::version2,...".
REM   -f,--force Force de-activation and removal without confirmation.
REM 
REM
REM Example: RemoveResolvedValueSet  -l \"resolvedValueSetUri1::version1, resolvedValueSetUri2::version2\" -f
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.lexgrid.valuesets.admin.RemoveResolvedValueSet %*