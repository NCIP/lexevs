# Example showing how to determine and display an unsorted list of root and
# subsumed nodes, up to a specified depth, for hierarchical relationships.
# It is primarily geared towards the NCI Metathesaurus source.
# 
# Example: ListSubsumptionMetaBySource 1 "MDR"
#
# Hierarchies are navigated primarily through the UMLS-defined PAR (has parent)
# and CHD (has child) relationships.
# 
# This program accepts two parameters.  The first indicates the depth
# to display hierarchical relations.  If 0, only the root nodes are displayed.
# If 1, nodes immediately subsumed by the root are also displayed, etc.
# If < 0, a default depth of 0 is assumed.
# 
# The second parameter must provide the source abbreviation (SAB) of the
# Metathesaurus source to be evaluated (e.g. ICD9CM, MDR, SNOMEDCT).
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.ListHierarchyMetaBySource $@
