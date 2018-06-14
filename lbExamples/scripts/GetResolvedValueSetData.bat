@echo off
REM Various data extraction techniques for Resolved Value Sets
REM
REM Example: GetResolvedValueSetData
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.GetResolvedValueSetData %*