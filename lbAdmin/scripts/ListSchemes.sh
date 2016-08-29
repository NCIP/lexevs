# List all currently registered vocabularies.
# Options:
#   -b,--brief List only coding scheme name, version, urn, and tags (default).
#   -f,--full  List full detail for each scheme.
# 
# Example: ListSchemes
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.ListSchemes $@
