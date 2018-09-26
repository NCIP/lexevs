@echo off
REM Loads UMLS content, provided as a collection of RRF files in a
REM single directory.  Files may comprise the entire UMLS distribution
REM or pruned via the MetamorphoSys tool.  A complete list of
REM source vocabularies is available online at
REM http://www.nlm.nih.gov/research/umls/metaa1.html.
REM
REM Options:
REM   -in,--input <uri> URI or path of the directory containing the NLM files. Path string must be preceded by "file:",
REM   -s,--source vocabularies to load.
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
REM Example: LoadUMLSFiles -in "file:///path/to/directory/" -s "PSY"
REM          LoadUMLSBatch -in "file:///path/to/directory/" -s "PSY" -meta "file:///path/to/metadata.xml" -metao 
REM
java -Xmx8000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.LoadUmlsBatchWithMetadata %*