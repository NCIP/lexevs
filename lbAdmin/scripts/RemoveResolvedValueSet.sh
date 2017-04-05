# Removes a resolved ValueSet 
#
# Options:
#   -l, The list of resolved value sets to remove, separated by comma. 
#		format "resolvedValueSetUri1::version1, resolvedValueSetUri2::version2,...".
#   -f,--force Force de-activation and removal without confirmation.
# 
#
# Example: RemoveResolvedValueSet  -l \"resolvedValueSetUri1::version1, resolvedValueSetUri2::version2\" -f
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.lexgrid.valuesets.admin.RemoveResolvedValueSet $@
