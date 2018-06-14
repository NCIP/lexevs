@echo off
REM Example showing how to find if one value set is a subset of another. 
REM
REM A list of value Set Definition available in the system will be displayed for selection.
REM 
REM Example: CheckForValueSetSubSet
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.CheckForValueSetSubSet %*