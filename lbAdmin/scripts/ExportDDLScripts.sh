# Exports the database create/drop scripts
#
# -db, --databaseType, required=true, usage="Target database type.")
#
# -p, --prefix Prefix to append to all tables
# 
# -o,--out Output directory, a required option
#
# -f, --force Force output directory creation.
#
# Example: ExportDDLScripts -p lb -0 "file:///path/to/dir" -f 
java -Xmx200m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.lexevs.dao.database.operation.tools.ScriptProducingLauncher $@