# Example showing a simple scoring algorithm that evaluates a
# provided term against available terms in a code system.  A cutoff
# percentage can optionally be provided.
#
# Example: ScoreTerm "some term to evaluate"
# Example: ScoreTerm "some term to evaluate" 25%
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.ScoreTerm $@
