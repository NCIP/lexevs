# Example showing how use a direct transitive closure for both ancestors and 
# descendants of a given term
#
# Example: GetConceptReferencesClosure "C3262" "subClassOf"
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.GetConceptReferencesClosure $@