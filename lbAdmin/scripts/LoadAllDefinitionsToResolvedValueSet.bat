@echo off
REM Loads All Value Set Definitions for a given uri as resolved coding scheme
REM
REM Options:
REM   -u, Uri uniquely identifying the coding scheme from which to resolve
REM	defaults to http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#
REM        
REM Example: LoadAllDefinitionsToResolvedValueSet  -uri "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#" 
REM
java -Xmx3000m -cp "../runtime/lbPatch.jar;../runtime-components/extLib/*"  org.lexgrid.valuesets.admin.LoadAllDefinitionsToResolvedValueSet %*
