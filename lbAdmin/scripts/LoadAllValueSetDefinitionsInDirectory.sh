#!/bin/bash
# Loads All LexGrid XML formatted Value Set Definitions in a given directory
#
# Options:
#   -in, Path to directory
#        
# Example: LoadAllValueSetDefinitionsInDirectory -in /path/to/directory
#
java -Xmx3000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.lexgrid.valuesets.admin.LoadAllValueSetDefinitionsInDirectory $@

