@echo off
REM Provides basic profiling of a coding scheme concepts and relations (e.g.
REM number of concepts and relation depth).
REM 
REM Options:
REM   -u,--urn <urn> URN uniquely identifying the code system.
REM   -v,--version <id> Code system version identifier.
REM   -r,--relation <relation> Optional name of a relation to profile;
REM        defaults to 'hasSubtype' if not specified.
REM  
REM Note: If the URN and version values are unspecified, a
REM list of available coding schemes will be presented for
REM user selection.
REM
REM Example: ProfileScheme
REM Example: ProfileScheme -u "urn:oid:2.16.840.1.113883.3.26.1.1" -v "05.09e" -r "hasSubtype"
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.ProfileScheme %*