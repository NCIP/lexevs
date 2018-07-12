# Example showing how to determine and display an unsorted list of root and
# subsumed nodes, up to a specified depth, for hierarchical relationships.
# 
# This program accepts two parameters:
#
# The first parameter indicates the depth to display for
# the hierarchy.  If 1, nodes immediately subsumed by the root are displayed.
# If 2, grandchildren are displayed, etc.  If absent or < 0, a default
# depth of 3 is assumed.
# 
# The second parameter optionally indicates a specific hierarchy to navigate.
# If provided, this must match a registered identifier in the coding scheme
# supported hierarchy metadata.  If left unspecified, all hierarchical
# associations are navigated.  If an incorrect value is specified, a list of
# supported values will be output for future reference.  
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
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.ListHierarchy $@
