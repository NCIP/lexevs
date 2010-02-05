# Exports content from the repository to a file in the Open Biomedical
# Ontologies (OBO) format.
#
# Options:
#   -u,--urn <name> URN or local name of the coding scheme to transfer.
#   -v,--version <id> The assigned tag/label or absolute version
#        identifier of the coding scheme.
# 
# Note: If the URN and version values are unspecified, a
# list of available coding schemes will be presented for
# user selection.
#
# Example: TransferScheme
# Example: TransferScheme -u "NCI Thesaurus" -v "6.06"
#
java -cp "../runtime/lbPatch.jar:../runtime/lbRuntime.jar" org.LexGrid.LexBIG.admin.TransferScheme $@
