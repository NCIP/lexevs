@echo off
REM Loads NCI Metathesaurus content, provided as a collection of RRF files in a
REM single directory.
REM
REM Options:
REM   -in,--input <uri> URI or path of the directory containing the NLM files
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
REM Example: LoadMetaBatch -in "file:///path/to/directory/"
REM          LoadMetaBatch -in "file:///path/to/directory/" -meta "file:///path/to/metadata.xml" -metao 
REM
java -Xmx6000m -XX:PermSize=256m -cp "..\runtime/lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.LoadMetaBatchWithMetadata %*