# Loads Value Set Definition content, provided in LexGrid canonical xml format.
#
# Options:
#
# Example: java org.lexgrid.valuesets.admin.LoadResolvedValueSetDefinition
#   -u, URN uniquely identifying the ValueSet Definition
#   -l, &lt;id&gt; List of coding scheme versions to use for the resolution
#       in format "csuri1::version1, csuri2::version2"
#   -csSourceTag, Resolves against scheme bearing this tag, eg: development, production" 
#   -vsTag, User defined Tag to apply to the resulting resolved value set scheme  
#        
# Example: LoadRevolvedValueSetDefinition LoadResolvedValueSetDefinition -u "Automobiles:valuesetDefinitionURI" -l "Automobiles::version1, GM::version2" -csVersionTag "production" -vsTag "PRODUCTION" 
#
java -Xmx1500m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*"  org.lexgrid.valuesets.admin.LoadResolvedValueSetDefinition $@
