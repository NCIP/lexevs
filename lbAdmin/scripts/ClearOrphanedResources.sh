# Clean up orphaned resources - databases and indexes.
# 
# Options:
# -li,--listIndexes List all unused indexes.
# -ldb,--listDatabases List all unused databases (with matching prefix).
# -ri,--RemoveIndex <name> Remove the (unused) index with the given name.
# -rdb,--RemoveDatabase <name> Remove the (unused) database with the given name.
# -a,--all Remove all unreferenced indexes and databases (with matching prefix).
#
# Example: ClearOrphanedResources -li
#
java -cp "../runtime/lbPatch.jar:../runtime/lbRuntime.jar" org.LexGrid.LexBIG.admin.ClearOrphanedResources $@