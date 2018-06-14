# Attempts to provide a tree, based on a focus code, that includes the
# following information:
# 
# - All paths from the hierarchy root to one or more focus codes.
# - Immediate children of every node in path to root
# - Indicator to show whether any unexpanded node can be further expanded
# 
# This example accepts two parameters... The first parameter is required, and
# must contain at least one code in a comma-delimited list. A tree is produced
# for each code. Time to produce the tree for each code is printed in
# milliseconds. In order to factor out costs of startup and shutdown, resolving
# multiple codes may offer a better overall estimate performance.
# 
# The second parameter is optional, and can indicate the identifier of a
# supported hierarchy for the coding scheme to navigate when resolving
# child nodes. If not provided, "is_a" is assumed.
# 
# Note: This example is written to handle sources that define the supported
# hierarchy in a uni-directional fashion.  In particular, it does not
# support the Metathesaurus, which can define hierarchical relationships
# using both forward and backward navigation.  For examples showing
# techniques to navigate the Metathesaurus, please refer to other examples
# such as BuildTreeForMetaCodeBySource, ListHierarchyMetaBySource and
# FindUMLSContextsForCUI.
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.BuildTreeForCode $@
