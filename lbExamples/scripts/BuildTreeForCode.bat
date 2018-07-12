@echo off
REM Attempts to provide a tree, based on a focus code, that includes the
REM following information:
REM 
REM - All paths from the hierarchy root to one or more focus codes.
REM - Immediate children of every node in path to root
REM - Indicator to show whether any unexpanded node can be further expanded
REM 
REM This example accepts two parameters... The first parameter is required, and
REM must contain at least one code in a comma-delimited list. A tree is produced
REM for each code. Time to produce the tree for each code is printed in
REM milliseconds. In order to factor out costs of startup and shutdown, resolving
REM multiple codes may offer a better overall estimate performance.
REM 
REM The second parameter is optional, and can indicate the identifier of a
REM supported hierarchy for the coding scheme to navigate when resolving
REM child nodes. If not provided, "is_a" is assumed.
REM 
REM Note: This example is written to handle sources that define the supported
REM hierarchy in a uni-directional fashion.  In particular, it does not
REM support the Metathesaurus, which can define hierarchical relationships
REM using both forward and backward navigation.  For examples showing
REM techniques to navigate the Metathesaurus, please refer to other examples
REM such as BuildTreeForMetaCodeBySource, ListHierarchyMetaBySource and
REM FindUMLSContextsForCUI.
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.BuildTreeForCode %*