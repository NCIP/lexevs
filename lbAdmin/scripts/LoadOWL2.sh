# Loads an OWL file. You can provide a manifest file to configure coding scheme
# meta data.
#
# Options:
#   -in,--input <uri> URI or path specifying location of the source file
#   -mf,--manifest <uri> URI or path specifying location of the manifest file
#   -lp,--loaderPrefs<uri> URI or path specifying location of the loader preference file
#   -v, --validate <int> Perform validation of the candidate
#         resource without loading data.  If specified, the '-a' and '-t'
#         options are ignored.  Supported levels of validation include:
#         0 = Verify document is well-formed
#         1 = Verify document is valid
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
# Example: LoadOWL2 -in "file:///path/to/somefile.owl" -a
#          LoadOWL2 -in "file:///path/to/somefile.owl" -v 0
#          LoadOWL2 -in "file:///path/to/somefile.owl" -a -meta "file:///path/to/metadata.xml" -metao
#
java -Xmx3000m -XX:MaxPermSize=256M -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.LoadOWL2 $@
