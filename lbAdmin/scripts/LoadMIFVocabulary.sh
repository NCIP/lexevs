# Loads from an the HL7 Vocabulary mif file.
#
# Options:
#   -in,--input <uri> URI or path specifying location of the source file.
#   -a, --activate ActivateScheme on successful load; if unspecified the
#         vocabulary is loaded but not activated.
#   -t, --tag <id> An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign.
#
# Example: LoadMIFVocabulary -in "file:///path/to/file.xml" -a
#
java -Xmx1000m -Djava.awt.headless=true -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.LoadMIFVocabulary $@
