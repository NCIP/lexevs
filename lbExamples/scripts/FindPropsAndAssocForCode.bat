@echo off
REM Example showing how to find concept properties and associations based on a code.
REM
REM Example: FindPropsAndAssocForCode "C25762"
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.FindPropsAndAssocForCode %*