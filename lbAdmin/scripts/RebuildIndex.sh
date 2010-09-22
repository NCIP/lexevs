# Rebuilds indexes associated with the specified coding scheme.
#
# Options:
#   -u,--urn <urn> URN uniquely identifying the code system.
#   -v,--version <id> Version identifier.
#   -i,--index <name> Name of the index extension to rebuild (if absent, rebuilds all built-in indices and named extensions).
#   -f,--force Force clear (no confirmation).
# 
# Note: If the URN and version values are unspecified, a
# list of available coding schemes will be presented for
# user selection.
#
# Example: RebuildIndex -i "myindex"
# Example: RebuildIndex -u "urn:oid:2.16.840.1.113883.3.26.1.1" -v "05.09e" -i "myindex"
#
java -Xmx800m -XX:PermSize=256m -cp "../runtime/lbPatch.jar:../runtime/lbRuntime.jar" org.LexGrid.LexBIG.admin.RebuildIndex $@
