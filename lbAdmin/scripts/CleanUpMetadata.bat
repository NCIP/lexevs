@echo off
REM CleanUpMetadata
REM	Clean up metadata entries that are orphaned.
REM
REM Options:
REM
REM -h, --help, Prints usage information
REM
REM -f,--force, Force removal without prompting of metadata that doesn't have a corresponding coding scheme entry(orphaned).
REM
REM
REM Example:
REM
REM CleanUpMetadata -r 
java -Xmx800m -XX:PermSize=256m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.CleanUpMetadata %*