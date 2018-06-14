# Example showing how to find all concepts codes related to another code
# with distance 1. 
#
# Example: FindRelatedCodes "C25762" "hasSubtype"
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.FindRelatedCodes $@
