@echo off
REM Example showing how to determine and display paths from a given
REM concept back to defined root nodes through any hierarchies registered
REM for the coding scheme.

REM This program accepts one parameter (required), indicating the code
REM to evaluate.
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
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.ListHierarchyPathToRoot %*