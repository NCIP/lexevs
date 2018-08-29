# Loads a mappings file(s), provided in UMLS RRF format.
#
# Options:
# -inMap,--input <uri> URI or path specifying location of the MRMAP source file.
# -inSat,--input <uri> URI or path specifying location of the MRSAT source file. 
# -meta --meatadata input &lt;uri&gt; URI or path specifying location of the metadata source file.  
#         metadata is applied to the code system and code system version being loaded.
# -metav  --validate metadata &lt;int&gt; Perform validation of the metadata source file
#         without loading data.  Supported levels of validation include:
#         0 = Verify document is valid
#         metadata is validated against the code system and code system version being loaded.
# -metao, --overwrite If specified, existing metadata for the code system
#         will be erased. Otherwise, new metadata will be appended to
#         existing metadata (if present).  
# -metaf,--force Force overwrite (no confirmation).
#
# Example: LoadMrMap -inMap "file:///path/to/MRMAP.RRF -inSat "file:///path/to/MRSAT.RRF"
#          LoadMrMap -inMap "file:///path/to/MRMAP.RRF -inSat "file:///path/to/MRSAT.RRF" -meta "file:///path/to/metadata.xml" -metao
#
java -Xmx1000m -Djava.awt.headless=true -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.LoadMrMap $@