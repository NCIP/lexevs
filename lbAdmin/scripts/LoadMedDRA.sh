# Loads a file specified in the Medical Dictionary for Regulatory Activities (MedDRA) format.
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
#   -cui  Uri to MRCONSO.RRF from the NCI Metathesaurus allows loading of CUI's
#         for a given term. (Slows load time considerably)
#
# Example: LoadMedDRA -in "file:///path/to/file.asc" -a
#        LoadMedDRA -in "file:///path/to/file.asc" -v 0
#        LoadMedDRA  -in "file:///path/to/file.asc" -cui "file:///path/to/MRCONSO.RRF
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.LoadMedDRA $@
