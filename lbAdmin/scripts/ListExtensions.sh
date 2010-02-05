# Deactivates a coding scheme based on unique URN and version.
# 
# Options:
#   -u,--urn <urn> URN uniquely identifying the code system.
#   -v,--version <id> Version identifier.
#   -d,--date <MM/DD/yyyy,HH:MM:SS> Date and time for activation to take effect; immediate if not specified.
#   -f,--force Force activation (no confirmation).
# 
# Example: DeactivateScheme -u "urn:oid:2.16.840.1.113883.3.26.1.1" -v "05.09e" -d "01/31/2099,12:00:00"
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime/lbRuntime.jar" org.LexGrid.LexBIG.admin.ListExtensions $@
