# Loads NCI Metathesaurus content, provided as a collection of RRF files in a
# single directory.
#
# Options:
#   -in,--input <uri> URI or path of the directory containing the NLM files
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
# Example: LoadMetaBatch -in "file:///path/to/directory/"
#          LoadMetaBatch -in "file:///path/to/directory/" -meta "file:///path/to/metadata.xml" -metao
#
java -Xmx6000m -XX:PermSize=256m -Djava.awt.headless=true -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.LoadMetaBatchWithMetadata $@
