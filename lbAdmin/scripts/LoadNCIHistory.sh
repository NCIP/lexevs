# Imports NCI History data to the LexBIG repository.
#
# Options:
#   -in,--input <uri> URI or path specifying location of the history file
#   -vf,--versionFile <uri> URI or path specifying location of the file
#         containing version identifiers for the history to be loaded
#   -v, --validate <int> Perform validation of the candidate
#         resource without loading data.  If specified, the '-r'
#         option is ignored.  Supported levels of validation include:
#         0 = Verify top 10 lines are correctly formatted
#         1 = Verify correct format for the entire file
#   -r, --replace If not specified, the provided history file will
#         be added into the current history database; otherwise the
#         current database will be replaced by the new content.
#
# Example: Example: LoadNCIHistory -in "file:///path/to/history.file" 
#                 LoadNCIHistory -in "file:///path/to/history.file" -v 0
#
java -Xmx1000m -Djava.awt.headless=true -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.LoadNCIHistory $@
