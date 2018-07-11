@echo off
REM Example showing how to determine and display an unsorted list of root and
REM subsumed nodes, up to a specified depth, for hierarchical relationships.
REM It is primarily geared towards the NCI Metathesaurus source.
REM 
REM Example: ListSubsumptionMetaBySource 1 "MDR"
REM
REM Hierarchies are navigated primarily through the UMLS-defined PAR (has parent)
REM and CHD (has child) relationships.
REM 
REM This program accepts two parameters.  The first indicates the depth
REM to display hierarchical relations.  If 0, only the root nodes are displayed.
REM If 1, nodes immediately subsumed by the root are also displayed, etc.
REM If < 0, a default depth of 0 is assumed.
REM 
REM The second parameter must provide the source abbreviation (SAB) of the
REM Metathesaurus source to be evaluated (e.g. ICD9CM, MDR, SNOMEDCT).
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.ListHierarchyMetaBySource %*