@echo off
REM Builds indexes associated with the source asserted 
REM value sets of the specified coding scheme.
REM 
REM Example: java -Xmx512m -cp lgRuntime.jar
REM  org.LexGrid.LexBIG.admin.BuildAssertedValueSetIndex
REM  -u &quot;urn:oid:2.16.840.1.113883.3.26.1.1&quot; -v &quot;05.09e&quot;
REM 
REM Note: If the URN and version values are unspecified, a
REM list of available coding schemes will be presented for
REM user selection.
REM 
REM
REM Example: BuildAssertedValueSetIndex -u "urn:oid:2.16.840.1.113883.3.26.1.1" -v "05.09e"
REM
java -Xmx800m -XX:PermSize=256m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.BuildAssertedValueSetIndex %*