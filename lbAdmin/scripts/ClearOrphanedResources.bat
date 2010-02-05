@echo off
REM Clean up orphaned resources - databases and indexes.
REM 
REM Options:
REM -li,--listIndexes List all unused indexes.
REM -ldb,--listDatabases List all unused databases (with matching prefix).
REM -ri,--removeIndex <name> Remove the (unused) index with the given name.
REM -rdb,--removeDatabase <name> Remove the (unused) database with the given name.
REM -a,--all Remove all unreferenced indexes and databases (with matching prefix).
REM
REM Example: ClearOrphanedResources -li
REM
java -cp "..\runtime\lbPatch.jar;..\runtime\lbRuntime.jar" org.LexGrid.LexBIG.admin.ClearOrphanedResources %*