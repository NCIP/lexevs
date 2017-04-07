# Removes a resolved ValueSet based on coding scheme and version that was used for its resolution.
#
# Options:
#   -l, List of coding scheme versions to match when removing the ResolvedValueSet.
#   -f,--force Force de-activation and removal without confirmation.
# 
#
# Example: RemoveVSResolvedFromCodingSchemes  -l -l \"source.coding.scheme.uri::version1, second.source.uri::version2\" -f
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.lexgrid.valuesets.admin.RemoveVSResolvedFromCodingSchemes $@
