# Loads optional XML-based metadata to be associated with an existing
# coding scheme.
# 
# Options
#   -u,--urn &lt;name&gt; URN uniquely identifying the code system.
#   -v,--version &lt;id&gt; Version identifier.
#   -in,--input &lt;uri&gt; URI or path specifying location of the XML file.
#   -v, --validate &lt;int&gt; Perform validation of the input file
#         without loading data.  If specified, the '-f', and '-o'
#         options are ignored.  Supported levels of validation include:
#         0 = Verify document is valid
#   -o, --overwrite If specified, existing metadata for the code system
#         will be erased. Otherwise, new metadata will be appended to
#         existing metadata (if present).   Requires user confirmation.
#   -f,--force Force overwrite (no confirmation).
# 
# Note: If the URN and version values are unspecified, a
# list of available coding schemes will be presented for
# user selection.
#
#  Example: LoadMetadata -in "file:///path/to/file.xml"
#  Example: LoadMetadata -in "file:///path/to/file.xml" -o
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.LoadMetadata $@
