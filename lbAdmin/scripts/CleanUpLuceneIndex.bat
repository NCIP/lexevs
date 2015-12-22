@echo off
REM CleanUpLuceneIndex
REM	Clean up orphaned indexes.
REM
REM Options:
REM
REM -h, --help, Prints usage information
REM
REM -r,--reindex, Reindex any missing indicies.
REM
REM Note: Lucene Clean Up can only be executed in the default Single Index Mode.
REM
REM Example:
REM
REM CleanUPLuceneIndex -r 
java -Xmx800m -XX:PermSize=256m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.lexevs.dao.index.operation.tools.CleanUpLuceneIndexLauncher %*