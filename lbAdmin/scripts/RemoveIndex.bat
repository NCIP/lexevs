@echo off
REM Clears an optional named index associated with the specified coding scheme.
REM Note that built-in indices required by the LexBIG runtime cannot be removed.
REM
REM Options
REM   -u,--urn <urn> URN uniquely identifying the code system.
REM   -v,--version <id> Version identifier.
REM   -f,--force Force clear (no confirmation).
REM 
REM Note: If the URN and version values are unspecified, a
REM list of available coding schemes will be presented for
REM user selection.
REM
REM  Example: RemoveIndex -u "urn:oid:2.16.840.1.113883.3.26.1.1" -v "05.09e"
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.RemoveIndex %*