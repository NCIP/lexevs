@echo off
REM Example showing how to list concepts with presentation text
REM that 'sounds like' a specified value.
REM
REM Example: SoundsLike "hart ventrickl"
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.SoundsLike %*