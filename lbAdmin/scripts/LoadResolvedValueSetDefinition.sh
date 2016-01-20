# Loads Value Set Definition content, provided in LexGrid canonical xml format.
#
# Options:
#   -u, The valueset definition URI to use 
#   -l, The list of coding schemes to revolve against. The format is codingschemeName::version
#   -csVersionTag    The tag to use for resolving coding scheme
#        
# Example: LoadRevolvedValueSetDefinition LoadResolvedValueSetDefinition -u \"Automobiles:valuesetDefinitionURI\" -l \"Automobiles::version1, GM::version2\" -csVersionTag \"production\" ""
#
java -Xmx1500m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*"  org.lexgrid.valuesets.admin.LoadResolvedValueSetDefinition $@
