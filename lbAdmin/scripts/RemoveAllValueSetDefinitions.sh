# WARNING: Removes all value set definitions without discrimination
#
# Options
#   -f,--force Force removing all value set definitions (no confirmation).
#
# Example: ./RemoveAllValueSetDefinitions.sh
# Example: ./RemoveAllValueSetDefinitions.sh -f
#
java -Xmx3000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.lexgrid.valuesets.admin.RemoveAllValueSetDefinitions $@
