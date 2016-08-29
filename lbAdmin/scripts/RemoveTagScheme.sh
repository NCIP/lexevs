# Removes a tag ID (e.g. 'PRODUCTION' or 'TEST') from a coding scheme URN and version.
# 
# Options:
#   -u,--urn <urn> URN uniquely identifying the code system.
#   -v,--version <id> Version identifier.
#  
# Note: If the URN and version values are unspecified, a
# list of available coding schemes will be presented for
# user selection.
#
# Example: RemoveTagScheme -u "urn:oid:2.16.840.1.113883.3.26.1.1" -v "05.09e"
# 
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.RemoveTagScheme $@
