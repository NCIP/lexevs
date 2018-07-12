@echo off
REM Example showing how to find codes matching descriptive text.
REM The program accepts up to two parameters...
REM
REM The first param (required) indicates the text used to search
REM matching descriptions.  Matches are determined through a
REM customized match algorithm, which uses a simple heuristic to
REM try and rank returned values by relevance.
REM 
REM The second param (optional) indicates the type of entity to
REM search.  Possible values include the LexGrid built-in types
REM "concept" and "instance".  Additional supported types can be
REM defined uniquely to a coding scheme.  If provided, this
REM should be a comma-delimited list of types.  If not provided,
REM all entity types are searched.
REM
REM Example: FindCodesForDescription "blood"
REM Example: FindCodesForDescription "breast cancer" "concept"
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.FindCodesForDescription %*