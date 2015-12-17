# ExportLgXML
#	Exports content from the repository to a file in the LexGrid canonical XML format.
#
#    usage: ExportLgXML [-xc] [-an null] [-xall] -v null -u name -out uri [-xa] [-f] 
#    -an,--associationsName Export associations with this name. Only valid with export type 'xa' 
#    -f,--force If specified, allows the destination file to be overwritten if present. 
#    -out,--output <uri> URI or path of the directory to contain the resulting XML file. The file name will be automatically derived from the coding scheme name. 
#    -u,--urn <name> URN or local name of the coding scheme to export. 
#    -v,--version The assigned tag/label or absolute version identifier of the coding scheme. 
#    -xa,--exportAssociationsType of export: export only associations. 
#    -xall,--exportAll Type of export: export all content. Default behavior. 
#    -xc,--exportConcepts Type of export: export only concepts. 
#
# Example: ExportLgXML -out "file:///path/to/dir" -u "NCI Thesaurus" -v "05.06e" -f 
# Note: If the coding scheme and version values are unspecified,
# a list of available coding schemes will be presented for
# user selection.
#
# Example: ExportLgXML -out "file:///path/to/dir" -f
# Example: ExportLgXML -out "file:///path/to/dir" -u "NCI_Thesaurus" -v "PRODUCTION" -f
#
java -Xmx2000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.ExportLgXML $@
