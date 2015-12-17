# CleanUpLuceneIndex
#	Clean up orphaned indexes.
#
# Options:
#
# -h, --help, Prints usage information
#
# -r,--reindex, Reindex any missing indicies.
#
# Note: Lucene Clean Up can only be executed in the default Single Index Mode.
#
# Example:
# 
# CleanUPLuceneIndex -r 
java -Xmx1600m -XX:PermSize=256m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.lexevs.dao.index.operation.tools.CleanUpLuceneIndexLauncher $@