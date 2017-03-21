# Loads UMLS History
#
# Options:
#   -in,--input <uri> URI or path specifying location of the source file
#   -v, --validate <int> Perform validation of the candidate
#         resource without loading data.  If specified, the '-a' and '-t'
#         options are ignored.  Supported levels of validation include:
#         0 = Verify document is valid
#   -a, --activate ActivateScheme on successful load; if unspecified the
#         vocabulary is loaded but not activated.
#   -r, --replace Replace exisiting file. 
#   -t, --tag <id> An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign.
#
# Example: LoadUMLSHistory -in "file:///path/to/META.folder" -r
#          LoadUMLSHistory -in "file:///path/to/META.folder" -v 0
#
java -Xmx1000m -Djava.awt.headless=true -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.LoadUMLSHistory $@