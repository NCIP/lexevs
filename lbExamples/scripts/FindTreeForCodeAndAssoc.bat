@echo off
REM Example showing how to determine a branch of associations, starting from a
REM specific concept code.
REM
REM Example: FindTreeForCodeAndAssoc "C25762" "hasSubtype"
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.FindTreeForCodeAndAssoc %*