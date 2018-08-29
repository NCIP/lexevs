# Loads a file specified in the Open Biomedical Ontologies (OBO) format.
#
# Options:
#   -in,--input <uri> URI or path specifying location of the source file
#   -mf,--manifest <uri> URI or path specifying location of the manifest file
#   -v, --validate <int> Perform validation of the candidate
#         resource without loading data.  If specified, the '-a' and '-t'
#         options are ignored.  Supported levels of validation include:
#         0 = Verify document is valid
#   -a, --activate ActivateScheme on successful load; if unspecified the
#         vocabulary is loaded but not activated.
#   -t, --tag <id> An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign.
#   -meta --meatadata input &lt;uri&gt; URI or path specifying location of the metadata source file.  
#        metadata is applied to the code system and code system version being loaded.
#   -metav  --validate metadata &lt;int&gt; Perform validation of the metadata source file
#         without loading data.  Supported levels of validation include:
#         0 = Verify document is valid
#         metadata is validated against the code system and code system version being loaded.
#   -metao, --overwrite If specified, existing metadata for the code system
#         will be erased. Otherwise, new metadata will be appended to
#         existing metadata (if present).  
#   -metaf,--force Force overwrite (no confirmation).
#
# Example: LoadOBO -in "file:///path/to/file.obo" -a
#          LoadOBO -in "file:///path/to/file.obo" -v 0
#          LoadOBO -in "file:///path/to/file.obo" -a -meta "file:///path/to/metadata.xml" -metao
#
java -Xmx1000m -Djava.awt.headless=true -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.LoadOBO $@
