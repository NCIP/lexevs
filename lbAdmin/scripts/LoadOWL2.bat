@echo off
REM Loads an OWL file. You can provide a manifest file to configure coding scheme
REM meta data.
REM
REM Options:
REM   -in,--input <uri> URI or path specifying location of the source file
REM   -mf,--manifest <uri> URI or path specifying location of the manifest file
REM   -lp,--loaderPrefs<uri> URI or path specifying location of the loader preference file
REM   -v, --validate <int> Perform validation of the candidate
REM         resource without loading data.  If specified, the '-a' and '-t'
REM         options are ignored.  Supported levels of validation include:
REM         0 = Verify document is well-formed
REM         1 = Verify document is valid
REM   -a, --activate ActivateScheme on successful load; if unspecified the vocabulary is loaded but not activated
REM   -t, --tag <id> An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign. 
REM   -meta --meatadata input &lt;uri&gt; URI or path specifying location of the metadata source file.  
REM        metadata is applied to the code system and code system version being loaded.
REM   -metav  --validate metadata &lt;int&gt; Perform validation of the metadata source file
REM         without loading data.  Supported levels of validation include:
REM         0 = Verify document is valid
REM         metadata is validated against the code system and code system version being loaded.
REM   -metao, --overwrite If specified, existing metadata for the code system
REM         will be erased. Otherwise, new metadata will be appended to
REM         existing metadata (if present).  
REM   -metaf,--force Force overwrite (no confirmation).
REM
REM Example: LoadOWL2 -in "file:///path/to/somefile.owl" -a
REM          LoadOWL2 -in "file:///path/to/somefile.owl" -v 0
REM          LoadOWL2 -in "file:///path/to/somefile.owl" -a -meta "file:///path/to/metadata.xml" -metao
REM
java -Xmx1000m -XX:MaxPermSize=256M -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.LoadOWL2 %*