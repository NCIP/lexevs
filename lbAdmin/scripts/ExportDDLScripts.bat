@echo off
REM Exports the database create/drop scripts
REM
REM -db, --databaseType, required=true, usage="Target database type.")
REM
REM -p, --prefix Prefix to append to all tables
REM
REM -o,--out Output directory, a required option
REM
REM -f, --force Force output directory creation.
REM
REM Example: ExportDDLScripts -p lb -0 "file:///path/to/dir" -f 
java -Xmx200m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.lexevs.dao.database.operation.tools.ScriptProducingLauncher %*