# Removes a coding scheme based on unique URN and version.
#
# Options:
#   -u,--urn <urn> URN uniquely identifying the code system.
#
# Note: If the URN and version values are unspecified, a
# list of available coding schemes will be presented for
# user selection.
#
# Example: RemoveValueSetDefinition
# Example: RemoveValueSetDefinition -u http://evs.nci.nih.gov/Valueset/C100110
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.lexgrid.valuesets.admin.RemoveValueSetDefinition $@
