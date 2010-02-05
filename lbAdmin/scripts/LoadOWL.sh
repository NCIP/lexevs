# Loads an OWL file. You can provide a manifest file to configure coding scheme
# meta data.
#
# Options:
#   -in,--input <uri> URI or path specifying location of the source file
#   -mf,--manifest <uri> URI or path specifying location of the manifest file
#   -ms,--memorySetting <int> If specified, indicates the profile
#         used to tune memory/performance tradeoffs. Options are:
#         1 = Faster/more memory (holds OWL in memory)
#         2 = Slower/less memory (cache OWL to database)
#   -v, --validate <int> Perform validation of the candidate
#         resource without loading data.  If specified, the '-a' and '-t'
#         options are ignored.  Supported levels of validation include:
#         0 = Verify document is well-formed
#         1 = Verify document is valid
#   -a, --activate ActivateScheme on successful load; if unspecified the
#         vocabulary is loaded but not activated.
#   -t, --tag <id> An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign.
#
# Example: LoadOWL -in "file:///path/to/somefile.owl" -a
#          LoadOWL -in "file:///path/to/somefile.owl" -v 0
#
java -Xmx3000m -XX:MaxPermSize=256M -cp "../runtime/lbPatch.jar:../runtime/lbRuntime.jar" org.LexGrid.LexBIG.admin.LoadOWL $@
