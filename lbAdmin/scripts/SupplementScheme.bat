@echo off
REM Utility to register, unregister, and list Coding Scheme Supplements
REM 
REM
REM  Usage: SupplementScheme  [-r] [-u] [-l]
REM     [-parentUri parentUri]
REM     [-parentVersion parentVersion]
REM		[-supplementUri supplementUri] 
REM     [-supplementVersion supplementVersion] 
REM  -r                                    Register Coding Scheme as a Supplement
REM  -u                                    Unregister Coding Scheme as a Supplement
REM  -l,--list                             List Supplements
REM  -parentUri <parentUri>                Parent URI.
REM  -parentVersion <parentVersion>        Parent Version.
REM  -supplementUri <supplementUri>        Supplement URI.
REM  -supplementVersion <supplementVersion>Supplement Version.
REM 
REM  Example: SupplementScheme -u -parentUri "urn:oid:2.16.840.1.113883.3.26.1.1" -parentVersion "05.09e" -supplementUri "http://supplement.ontology.org" -supplementVersion "1.0.1" 
REM 
java -Xmx500m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.SupplementScheme %*