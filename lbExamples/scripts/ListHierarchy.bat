@echo off
REM Example showing how to determine and display an unsorted list of root and
REM subsumed nodes, up to a specified depth, for hierarchical relationships.
REM 
REM This program accepts two parameters:
REM
REM The first parameter indicates the depth to display for
REM the hierarchy.  If 1, nodes immediately subsumed by the root are displayed.
REM If 2, grandchildren are displayed, etc.  If absent or < 0, a default
REM depth of 3 is assumed.
REM 
REM The second parameter optionally indicates a specific hierarchy to navigate.
REM If provided, this must match a registered identifier in the coding scheme
REM supported hierarchy metadata.  If left unspecified, all hierarchical
REM associations are navigated.  If an incorrect value is specified, a list of
REM supported values will be output for future reference.  
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
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.ListHierarchy %*