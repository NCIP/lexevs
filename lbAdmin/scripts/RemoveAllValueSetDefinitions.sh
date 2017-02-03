# WARNING: Removes all value set definitions without discrimination
#
#
# Example: ./RemoveAllValueSetDefinitions.sh
#
java -Xmx3000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.lexgrid.valuesets.admin.RemoveAllValueSetDefinitions $@
