# Example showing how to determine and display paths from a given
# concept back to defined root nodes through any hierarchies registered
# for the coding scheme.
#
# This program accepts one parameter (required), indicating the code
# to evaluate.
# 
# BACKGROUND: From a database perspective, LexBIG stores relationships
# internally in a forward direction, source to target.  Due to differences
# in source formats, however, a wide variety of associations may be
# used ('PAR', 'CHD', 'isa', 'hasSubtype', etc).  In addition, the
# direction of navigation may vary ('isa' expands in a reverse direction
# whereas 'hasSubtype' expands in a forward direction.
# 
# The intent of the getHierarchy* methods on the LexBIGServiceConvenienceMethods
# interface is to simplify the process of hierarchy discovery and navigation.
# These methods significantly reduce the need to understand conventions for root
# nodes, associations, and direction of navigation for a specific source format.
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.ListHierarchyPathToRoot $@
