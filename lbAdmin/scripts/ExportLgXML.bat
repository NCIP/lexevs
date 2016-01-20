@echo off
REM ExportLgXML
REM	Exports content from the repository to a file in the LexGrid canonical XML format.
REM
REM    usage: ExportLgXML [-xc] [-an null] [-xall] -v null -u name -out uri [-xa] [-f] 
REM    -an,--associationsName Export associations with this name. Only valid with export type 'xa' 
REM    -f,--force If specified, allows the destination file to be overwritten if present. 
REM   -out,--output <uri> URI or path of the directory to contain the resulting XML file. The file name will be automatically derived from the coding scheme name. 
REM    -u,--urn <name> URN or local name of the coding scheme to export. 
REM    -v,--version The assigned tag/label or absolute version identifier of the coding scheme. 
REM    -xa,--exportAssociationsType of export: export only associations. 
REM    -xall,--exportAll Type of export: export all content. Default behavior. 
REM   -xc,--exportConcepts Type of export: export only concepts. 
REM
REM Example: ExportLgXML -out "file:///path/to/dir" -u "NCI Thesaurus" -v "05.06e" -f 
REM
REM Note: If the coding scheme and version values are unspecified,
REM a list of available coding schemes will be presented for
REM user selection.
REM
java -Xmx1500m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.ExportLgXML %*