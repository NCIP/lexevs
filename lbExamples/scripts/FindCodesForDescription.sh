# Example showing how to find codes matching descriptive text.
# The program accepts up to two parameters...
#
# The first param (required) indicates the text used to search
# matching descriptions.  Matches are determined through a
# customized match algorithm, which uses a simple heuristic to
# try and rank returned values by relevance.
# 
# The second param (optional) indicates the type of entity to
# search.  Possible values include the LexGrid built-in types
# "concept" and "instance".  Additional supported types can be
# defined uniquely to a coding scheme.  If provided, this
# should be a comma-delimited list of types.  If not provided,
# all entity types are searched.
#
# Example: FindCodesForDescription "blood"
# Example: FindCodesForDescription "breast cancer" "concept"
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.FindCodesForDescription $@
