@echo off
REM Example showing how to find all endpoints of a named association
REM for which the given term matches as source or target.
REM
REM Example: FindRelatedNodesForTermAndAssoc "disorder" "Disease_May_Have_Finding"
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.FindRelatedNodesForTermAndAssoc %*