# Loads Value Set Definition content, provided in LexGrid canonical xml format.
#
# Options
#   -u, URN uniquely identifying the ValueSet Definition
#   -l, &lt;id&gt; List of coding scheme versions to use for the resolution
#       in format "csuri1::version1, csuri2::version2"
#   -csSourceTag, Resolves against scheme bearing this tag, eg: development, production" 
#   -vsTag, Optional user defined Tag to apply to the resulting resolved value set scheme  
#   -vsVersion, Optional user defined version that can be applied to the resolved value set scheme
#        
# Example: LoadRevolvedValueSetDefinition LoadResolvedValueSetDefinition -u "Automobiles:valuesetDefinitionURI" -l
#  "Automobiles::version1, GM::version2" -csSourceTag "production" -vsTag"PRODUCTION" -vsVersion "MyVersion"
#
java -Xmx1500m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*"  org.lexgrid.valuesets.admin.LoadResolvedValueSetDefinition $@
