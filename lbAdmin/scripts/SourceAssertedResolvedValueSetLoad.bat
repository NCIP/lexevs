@echo off
REM  Loads all source asserted Value Sets from a given source
REM
REM Options:
REM
REM    -cs, --codingScheme, usage="Name of Coding Scheme that asserts values sets" 
REM    
REM    -v, --version, usage="Version of the coding scheme."
REM    
REM    -a, --association, usage="Relationship name asserted by the codingScheme") 
REM    
REM    -t", --target", usage="Target to Source resolution.") 
REM    
REM    -uri", --uri", usage="Base uri to build the conding scheme uri upon") 
REM    
REM    -o", --owner", usage="Owener of the value set assertioin") 
REM    
REM    -s", --sourceName", usage="Gives the name of the property to resolve the source value against") 
REM        
REM Example: SourceAssertedResolvedValueSetLoad.sh -v "17.04d"
REM
java -Xmx3000m -Djava.awt.headless=true -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.SourceAssertedResolvedValueSetBatchLauncher %*
