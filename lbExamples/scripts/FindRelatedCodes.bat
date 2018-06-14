@echo off
REM Example showing how to find all concepts codes related to another code
REM with distance 1. 
REM
REM Example: FindRelatedCodes "C25762" "hasSubtype"
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.FindRelatedCodes %*