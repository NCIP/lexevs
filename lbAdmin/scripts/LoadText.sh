# Loads a file in LexGrid Text format.
#
# Options:
#   -in,--input <uri> URI or path specifying location of the source file
#   -d, --delimiter <character> defaults to tab the character used to delimit pair
#         or triple components and the nesting.
#   -mf,--manifest <uri> URI or path specifying location of the manifest file
#   -v, --validate <int> Perform validation of the candidate
#         resource without loading data.  If specified, the '-a' and '-t'
#         options are ignored.  Supported levels of validation include:
#         0 = Verify document is valid
#   -a, --activate ActivateScheme on successful load; if unspecified the
#         vocabulary is loaded but not activated.
#   -t, --tag <id> An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign.
#
# Example: LoadText -in "file:///path/to/file.txt" -a
#        LoadText -in "file:///path/to/file.txt" -v 0
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.LoadText $@
