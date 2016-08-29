@echo off
REM Deactivates a coding scheme based on unique URN and version.
REM 
REM Options:
REM   -u,--urn <urn> URN uniquely identifying the code system.
REM   -v,--version <id> Version identifier.
REM   -d,--date <yyyy-MM-dd,HH:mm:ss> Date and time for deactivation to take effect; immediate if not specified.
REM   -f,--force Force deactivation (no confirmation).
REM 
REM Note: If the URN and version values are unspecified, a
REM list of available coding schemes will be presented for
REM user selection.
REM
REM Example: DeactivateScheme -d "01/31/2099,12:00:00"
REM Example: DeactivateScheme -u "urn:oid:2.16.840.1.113883.3.26.1.1" -v "05.09e" -d "01/31/2099,12:00:00"
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.DeactivateScheme %*