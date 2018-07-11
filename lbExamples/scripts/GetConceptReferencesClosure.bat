@echo off
REM Example showing how use a direct transitive closure for both ancestors and 
REM descendants of a given term
REM
REM Example: GetConceptReferencesClosure "C3262" "subClassOf"
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.GetConceptReferencesClosure %*