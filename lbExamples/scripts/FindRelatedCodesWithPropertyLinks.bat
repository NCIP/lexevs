@echo off
REM Example showing how to find all concepts codes related to another code
REM with distance 1. 
REM
REM Example: FindRelatedCodesWithPropertyLinks "C25762"
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.FindRelatedCodesWithPropertyLinks %*