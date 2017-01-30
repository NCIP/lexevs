# WARNING:  REMOVES ALL RESOLVED VALUE SETS
#
#
# Example: ./RemoveAllResolvedValueSets.sh
#
java -Xmx3000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.lexgrid.valuesets.admin.RemoveAllResolvedValueSets $@
