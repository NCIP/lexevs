# Example showing how to determine and display the hierarchical relationships
# for a specific code, ancestors or descendants, within a fixed distance.
# 
# This program accepts two parameters, indicating the code and distance.
# The first parameter is the code (required).  The second parameter is the
# distance (optional).  If 1, immediate parents and children are
# displayed.  If 2, grandparents and grandchildren are displayed, etc.
# If absent or < 0, all paths to root and downstream branches are displayed.
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
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.ListHierarchyByCode $@
