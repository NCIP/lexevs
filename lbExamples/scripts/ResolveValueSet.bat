@echo off
REM Example showing how to resolve a value set definition. 
REM
REM A list of value Set Definition available in the system will be displayed for selection. 
REM
REM Also a list of Code System available in the system will be displayed for selection.
REM This code system will be used to resolve value set definition.
REM
REM A list concepts that are member of value set definition will be displayed.
REM 
REM Example: ResolveValueSet
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.ResolveValueSet %*