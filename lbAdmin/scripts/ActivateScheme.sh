# Activates a coding scheme based on unique URN and version.
# 
# User sees menu of coding schemes to activate and inputs number
# interface requests activation confirmation as Y or N
# 
# Otherwise user may also run a direct command line command as follows
# Options:
#   -u,--urn <urn> URN uniquely identifying the code system.
#   -v,--version <id> Version identifier.
#   -f,--force Force activation (no confirmation).
#  
# Note: If the URN and version values are unspecified, a
# list of available coding schemes will be presented for
# user selection.
#
# Example: ActivateScheme
# Example: ActivateScheme -u "urn:oid:2.16.840.1.113883.3.26.1.1" -v "05.09e"
# 
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.ActivateScheme $@
