# Example showing how to resolve a value set definition. 
#
# A list of value Set Definition available in the system will be displayed for selection. 
# Also a list of Code System available in the system will be displayed for selection.
# This code system will be used to resolve value set definition.
#
# A list concepts that are member of value set definition will be displayed.
# 
# Example: ResolveValueSet
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.ResolveValueSet $@
