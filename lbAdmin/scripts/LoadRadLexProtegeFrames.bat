@echo off
REM URI or path specifying location of the pprj file.
REM	Imports from a RadLex xml file to a LexBIG repository.
REM	Requires that the pprj file be configured with reference to
REM	a RadLex xml file as follows:
REM	([radlex_ProjectKB_Instance_66] of  String
REM		(name "source_file_name")
REM		"(string_value "radlex.xml"))" );
REM Example: java org.LexGrid.LexBIG.admin.LoadRadLexProtegeFrames
REM   -in,--input &lt;uri&gt; URI or path specifying location of the source file
REM   -v, --validate &lt;int&gt; Perform validation of the candidate
REM         resource without loading data.  If specified, the '-a' and '-t'
REM         options are ignored.  Supported levels of validation include:
REM         0 = Verify document is well-formed
REM         1 = Verify document is valid
REM   -a, --activate ActivateScheme on successful load; if unspecified the vocabulary is loaded but not activated
REM   -t, --tag &lt;id&gt; An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign. 
REM 
REM Example: java -Xmx800m -cp lgRuntime.jar -Djava.ext.dirs=../runtime/sqlDrivers
REM  org.LexGrid.LexBIG.admin.LoadRadLexProtegeFrames -in "file:///path/to/RadLex.pprj" -a
REM -or-
REM  org.LexGrid.LexBIG.admin.LoadRadLexProtegeFrames -in "file:///path/to/RadLex.pprj" -v 0
REM
java -Xmx1000m  -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.LoadRadLexProtegeFrames %*