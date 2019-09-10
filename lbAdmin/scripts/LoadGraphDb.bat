@echo off
REM Loads a set of graphs derived from the associations of the target vocabulary.
REM
REM Options:
REM   -in,--input URI for coding scheme source in LexEVS
REM   -v  	version of coding scheme
REM
REM Example: LoadGraphDatabase -in "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#" -v "18.05b"
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.LoadGraphDatabase %*