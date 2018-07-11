# Example showing how to get all incoming concepts for a given association and code
#
# Example: GetIncomingConcepts "C14225" "Gene_Found_In_Organism"
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.GetAllIncomingConceptsForAssociation $@