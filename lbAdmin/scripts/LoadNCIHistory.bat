@echo off
REM Imports NCI History data to the LexBIG repository.
REM
REM Options:
REM   -in,--input <uri> URI or path specifying location of the history file
REM   -vf,--versionFile <uri> URI or path specifying location of the file
REM         containing version identifiers for the history to be loaded
REM   -v, --validate <int> Perform validation of the candidate
REM         resource without loading data.  If specified, the '-r'
REM         option is ignored.  Supported levels of validation include:
REM         0 = Verify top 10 lines are correctly formatted
REM         1 = Verify correct format for the entire file
REM   -r, --replace If not specified, the provided history file will
REM         be added into the current history database; otherwise the
REM         current database will be replaced by the new content.
REM
REM Example: Example: LoadNCIHistory -in "file:///path/to/history.file" 
REM                 LoadNCIHistory -in "file:///path/to/history.file" -v 0
REM
java -Xmx10000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.LoadNCIHistory %*