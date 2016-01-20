@echo off
REM Removes a resolved ValueSet based on coding scheme and version that was used for its resolution.
REM
REM Options:
REM   -l, The list of coding schemes that were used for resolution to match for removal. The format is codingschemeURI::version
REM   -f,--force Force deactivation and removal without confirmation.
REM 
REM
REM Example: RemoveResolvedValueSet  -l \"codingschemeUri1::version1, codingschemeUri2::version2\" -f
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.lexgrid.valuesets.admin.RemoveResolvedValueSet %*