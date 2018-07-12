@echo off
REM Example showing a simple scoring algorithm that evaluates a
REM provided term against available terms in a code system.  A cutoff
REM percentage can optionally be provided.
REM
REM Example: ScoreTerm "some term to evaluate"
REM Example: ScoreTerm "some term to evaluate" 25%
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.ScoreTerm %*