@echo off
REM Example showing how to find if a coded concept belongs to a value set. The program
REM accepts one parameter : The concept code 
REM
REM A list of value Set Definition available in the system will be displayed. User can choose
REM a particular value set to check the concept membership.
REM
REM A list of Code System present in the system will also be displayed. User can select which 
REM Code System Version to be used to resolve Value Set Definition.
REM 
REM Example: CheckForConceptInValueSet "C123456"
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.CheckForConceptInValueSet %*