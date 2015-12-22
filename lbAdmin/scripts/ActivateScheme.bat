@echo off
REM Activates a coding scheme based on unique URN and version.
REM 
REM User sees menu of coding schemes to activate and inputs number
REM interface requests activation confirmation as Y or N
REM 
REM Otherwise user may also run a direct command line command as follows
REM Options:
REM   -u,--urn <urn> URN uniquely identifying the code system.
REM   -v,--version <id> Version identifier.
REM   -f,--force Force activation (no confirmation).
REM 
REM Note: If the URN and version values are unspecified, a
REM list of available coding schemes will be presented for
REM user selection.
REM
REM Example: ActivateScheme
REM Example: ActivateScheme -u "urn:oid:2.16.840.1.113883.3.26.1.1" -v "05.09e"
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.ActivateScheme %*