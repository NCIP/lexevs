#!/bin/bash
# Loads all source asserted Value Sets from a given source
#
# Options:
#
#
#    -cs, --codingScheme, usage="Name of Coding Scheme that asserts values sets" 
#    
#    -v, --version, usage="Version of the coding scheme."
#    
#    -a, --association, usage="Relationship name asserted by the codingScheme" 
#    
#    -t", --target", usage="Target to Source resolution." 
#    
#    -uri", --uri", usage="Base uri to build the conding scheme uri upon" 
#    
#    -o", --owner", usage="Owener of the value set assertioin" 
#    
#    -s", --sourceName", usage="Gives the name of the property to resolve the source value against" 
#
#    -cd, --conceptDomainName" usage= "Gives the name of the property to resolve the concept domain value against" 
#        
# Example: SourceAssertedValueSetDefinition.sh -v "17.04d"
#
java -Xmx3000m -Djava.awt.headless=true -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.SourceAssertedValueSetDefinitionLauncher $@
