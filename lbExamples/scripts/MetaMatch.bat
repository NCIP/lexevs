@echo off
REM Example attempting to approximate some characteristics of the
REM Metaphrase search algorithm.  However, full Metaphrase compatibility
REM is not anticipated.
REM
REM Example: MetaMatch "test term"
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.MetaMatch %*