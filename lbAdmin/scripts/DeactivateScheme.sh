# Deactivates a coding scheme based on unique URN and version.
# 
# Options:
#   -u,--urn <urn> URN uniquely identifying the code system.
#   -v,--version <id> Version identifier.
#   -d,--date <yyyy-MM-dd,HH:mm:ss> Date and time for deactivation to take effect; immediate if not specified.
#   -f,--force Force activation (no confirmation).
# 
# Note: If the URN and version values are unspecified, a
# list of available coding schemes will be presented for
# user selection.
#
# Example: DeactivateScheme
# Example: DeactivateScheme -u "urn:oid:2.16.840.1.113883.3.26.1.1" -v "05.09e" -d "01/31/2099,12:00:00"
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.DeactivateScheme $@
