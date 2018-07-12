@echo off
REM NOTE: This example is intended to run only against code systems
REM representing the entire Metathesaurus.  It depends on the presence of
REM concepts based on CUIs and the import of source and hierarchical
REM definitions as property and association qualifiers.  These
REM attributes are only populated by the NCI Meta loader.
REM 
REM Example displays explicitly asserted source hierarchies (based on
REM import of MRHIER HCD) for a CUI.  The program takes a single argument
REM (a UMLS CUI), prompts for the code system to query in the LexGrid
REM repository, and displays the HCD-based context relationships
REM along with other details.
REM 
REM Note that this example does not print intra-CUI associations (links
REM that might exist between individual terms on a single concept) or 
REM hierarchies not explicitly tagged by HCD.
REM 
REM The selected code system must represent the full Metathesaurus.
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.FindUMLSContextsForCUI %*