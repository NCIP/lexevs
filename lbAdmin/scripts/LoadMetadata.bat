@echo off
REM Loads optional XML-based metadata to be associated with an existing
REM coding scheme.
REM 
REM Options
REM   -u,--urn &lt;name&gt; URN uniquely identifying the code system.
REM   -v,--version &lt;id&gt; Version identifier.
REM   -in,--input &lt;uri&gt; URI or path specifying location of the XML file.
REM   -v, --validate &lt;int&gt; Perform validation of the input file
REM         without loading data.  If specified, the '-f', and '-o'
REM         options are ignored.  Supported levels of validation include:
REM         0 = Verify document is valid
REM   -o, --overwrite If specified, existing metadata for the code system
REM         will be erased. Otherwise, new metadata will be appended to
REM         existing metadata (if present).  Requires user confirmation.
REM   -f,--force Force overwrite (no confirmation).
REM 
REM Note: If the URN and version values are unspecified, a
REM list of available coding schemes will be presented for
REM user selection.
REM
REM  Example: LoadMetadata -in "file:///path/to/file.xml"
REM  Example: LoadMetadata -in "file:///path/to/file.xml" -o
REM
java -Xmx1000m -Djava.awt.headless=true -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.LoadMetadata %*