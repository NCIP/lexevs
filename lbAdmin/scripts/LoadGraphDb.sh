# Loads a set of graphs derived from the associations of the target vocabulary.
#
# Options:
#   -in,--input URI of coding scheme source for target graphs
#   -v, --      Version of terminology source from which to load graphs
#
# Example: LoadGraphDatabase -in "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#" -v "18.05b"
#
java -Xmx1000m -Djava.awt.headless=true -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.LoadGraphDatabase $@
