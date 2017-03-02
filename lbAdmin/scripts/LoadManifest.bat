@echo off
REM Load manifest data onto the codingscheme based on unique URN and version.
REM Options:
REM Example: java org.LexGrid.LexBIG.admin.LoadManifest
REM  -u,--urn "urn"; URN uniquely identifying the code system.
REM  -v,--version "versionId"; Version identifier.
REM  -mf,--manifest "manifest"; location of manifest xml file.
REM 
REM Note: If the URN and version values are unspecified, a
REM list of available coding schemes will be presented for
REM user selection.
REM 
REM Example:	LoadManifest -u \"urn:oid:2.16.840.1.113883.3.26.1.1\" -v \"05.09e\" -mf \"file://path//to//manifest.xml\""
REM				LoadManifest -mf \"file://path//to//manifest.xml\"
java -Xmx1300m -Djava.awt.headless=true -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.LoadManifest %*