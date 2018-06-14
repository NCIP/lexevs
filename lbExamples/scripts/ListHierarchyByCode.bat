@echo off
REM Example showing how to determine and display the hierarchical relationships
REM for a specific code, ancestors or descendants, within a fixed distance.
REM 
REM This program accepts two parameters, indicating the code and distance.
REM The first parameter is the code (required).  The second parameter is the
REM distance (optional).  If 1, immediate parents and children are
REM displayed.  If 2, grandparents and grandchildren are displayed, etc.
REM If absent or < 0, all paths to root and downstream branches are displayed.
REM 
REM BACKGROUND: From a database perspective, LexBIG stores relationships
REM internally in a forward direction, source to target.  Due to differences
REM in source formats, however, a wide variety of associations may be
REM used ('PAR', 'CHD', 'isa', 'hasSubtype', etc).  In addition, the
REM direction of navigation may vary ('isa' expands in a reverse direction
REM whereas 'hasSubtype' expands in a forward direction.
REM 
REM The intent of the getHierarchy* methods on the LexBIGServiceConvenienceMethods
REM interface is to simplify the process of hierarchy discovery and navigation.
REM These methods significantly reduce the need to understand conventions for root
REM nodes, associations, and direction of navigation for a specific source format.
REM 
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.ListHierarchyByCode %*