# Rebuilds transitivity table associated with the specified coding scheme.
#
# Options:
#   -u,--urn <urn> URN uniquely identifying the code system.
#   -v,--version <id> Version identifier.
#   -f,--force Force clear (no confirmation).
# 
# Note: If the URN and version values are unspecified, a
# list of available coding schemes will be presented for
# user selection.
#
# Example: RebuildTransitivityTable -u "urn:oid:2.16.840.1.113883.3.26.1.1" -v "05.09e"
#
java -Xmx1500m -XX:PermSize=256m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.RebuildTransitivityTable $@
