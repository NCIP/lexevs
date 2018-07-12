@echo off
REM Example showing how to find the entity description assigned
REM to a specific code.  The program accepts one parameter, the
REM entity code.
REM
REM Example: FindDescriptionForCode "C43652"
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.FindDescriptionForCode %*