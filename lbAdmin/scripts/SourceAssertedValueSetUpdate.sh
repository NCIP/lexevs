#!/bin/bash
# Loads all source asserted Value Sets from a given source
#
# Options:
#
#    -cs  -codingScheme - Name of Coding Scheme that asserts values sets
#     default "NCI_Thesaurus"
#     
#     -pv --pversion - Previous version of the coding scheme. Optional 
#     default none
#     
#     -cv --cversion - Version of the coding scheme. User must input a current version
#     default none;
#     
#     -a --association - Relationship name asserted by the codingScheme 
#     default association = "Concept_In_Subset";
#     
#     -t --target - Target to Source resolution. 
#     default target = "true";
#    
#     -uri --uri - Base uri to build the value set coding scheme uri upon 
#    defaul uri = "http://evs.nci.nih.gov/valueset/";
#     
#     -o --owner - Owner of the value set assertion 
#     default owner="NCI";
#     
#     -s --sourceName - Gives the name of the property to resolve the source value against 
#     default source = "Contributing_Source";
# 
#     -cd --conceptDomainName - Gives the name of the property to resolve the concept domain value against 
#     defaultconceptDomainIndicator = "Semantic_Type";
#     
#     -sUri --schemeUri - Scheme Uri for the coding scheme containing the value set updates 
#     defaultschemeUri = NCItSourceAssertedValueSetUpdateService.NCIT_URI;
#        
# Example: SourceAssertedValueSetUpdate.sh -cv "17.04d"
#
java -Xmx3000m -Djava.awt.headless=true -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.SourceAssertedValueSetUpdateLauncher $@
