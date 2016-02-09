# Removes a resolved ValueSet based on coding scheme and version that was used for its resolution.
#
# Options:
#   -l, The resolved value set and the list of coding scheme(s) that were used for resolution (to match for removal). 
#       The format is codingschemeUri::version. Values separated by a comma.
#   -f,--force Force de-activation and removal without confirmation.
# 
#
# Example: RemoveResolvedValueSet  -l \"resolved-valuesetUri::version1, codingschemeUri1::version1\" -f
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.lexgrid.valuesets.admin.RemoveResolvedValueSet $@
