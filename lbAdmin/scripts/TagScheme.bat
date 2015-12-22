@echo off
REM Associates a tag ID (e.g. 'PRODUCTION' or 'TEST') with a coding scheme URN and version.
REM
REM Options:
REM   -u,--urn <urn> URN uniquely identifying the code system.
REM   -v,--version <id> Version identifier.
REM   -t,--tag The tag ID (e.g. 'PRODUCTION' or 'TEST') to assign.
REM 
REM Note: If the URN and version values are unspecified, a
REM list of available coding schemes will be presented for
REM user selection.
REM
REM Example: TagScheme -t "TEST"
REM Example: TagScheme -u "urn:oid:2.16.840.1.113883.3.26.1.1" -v "05.09e" -t "TEST"
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.TagScheme %*