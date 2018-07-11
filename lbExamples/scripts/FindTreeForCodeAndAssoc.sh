# Example showing how to determine a branch of associations, starting from a
# specific concept code.
#
# Example: FindTreeForCodeAndAssoc "C25762" "hasSubtype"
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.FindTreeForCodeAndAssoc $@
