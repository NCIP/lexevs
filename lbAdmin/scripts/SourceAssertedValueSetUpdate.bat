@echo off
REM Loads all source asserted Value Sets from a given source
REM
REM Options:
REM
REM    -cs  -codingScheme - Name of Coding Scheme that asserts values sets
REM     default "NCI_Thesaurus"
REM     
REM     -pv --pversion - Previous version of the coding scheme. Optional 
REM     default none
REM     
REM     -cv --cversion - Version of the coding scheme. User must input a current version
REM     default none;
REM     
REM     -a --association - Relationship name asserted by the codingScheme 
REM     default association = "Concept_In_Subset";
REM     
REM     -t --target - Target to Source resolution. 
REM     default target = "true";
REM    
REM     -uri --uri - Base uri to build the value set coding scheme uri upon 
REM    defaul uri = "http://evs.nci.nih.gov/valueset/";
REM     
REM     -o --owner - Owner of the value set assertion 
REM     default owner="NCI";
REM     
REM     -s --sourceName - Gives the name of the property to resolve the source value against 
REM     default source = "Contributing_Source";
REM 
REM     -cd --conceptDomainName - Gives the name of the property to resolve the concept domain value against 
REM     defaultconceptDomainIndicator = "Semantic_Type";
REM     
REM     -sUri --schemeUri - Scheme Uri for the coding scheme containing the value set updates 
REM     defaultschemeUri = NCItSourceAssertedValueSetUpdateService.NCIT_URI;
REM        
REM Example: SourceAssertedValueSetUpdate.sh -cv "17.04d"
REM
java -Xmx3000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.SourceAssertedValueSetUpdateLauncher %*
