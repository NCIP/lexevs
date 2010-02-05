# Loads the NCI MetaThesaurus, provided as a collection of RRF files.
#
# Options:
#   -in,--input <uri> URI or path of the directory containing the RRF files; must be in URI format.
#   -mf,--manifest Manifest Location
#   -lp,--Loader Preferences XML File Location
#   -v, --validate <int> Perform validation of the candidate
#         resource without loading data.  If specified, the '-a' and '-t'
#         options are ignored.  Supported levels of validation include:
#         0 = Verify the existence and format (up to 100 lines) of each required file
#   -rr,--rootRecalc If specified, indicates that only root nodes are to be
#         reevaluated for an already loaded source (if present).
#   -a, --activate ActivateScheme on successful load; if unspecified the
#         vocabulary is loaded but not activated.
#   -t, --tag <id> An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign.
#
# Example: LoadNCIMeta -in "file:///path/to/directory/" -a
#        LoadNCIMeta -in "file:///path/to/directory/" -v 0
#
java -Xmx1500m -cp "../runtime/lbPatch.jar:../runtime/lbRuntime.jar" org.LexGrid.LexBIG.admin.LoadNCIMeta $@
