# Exports content from the repository to a file in the Open Biomedical
# Ontologies (OBO) format.
#
# Options:
#   -out,--output <uri> URI or path of the directory to contain the
#        resulting OBO file.  The file name will be automatically derived
#        from the coding scheme name.   
#   -u,--urn <name> URN or local name of the coding scheme to export.
#   -v,--version <id> The assigned tag/label or absolute version
#        identifier of the coding scheme.
#   -f,--force If specified, allows the destination file to be
#        overwritten if present.
# 
# Note: If the coding scheme and version values are unspecified,
# a list of available coding schemes will be presented for
# user selection.
#
# Example: ExportOBO -out "file:///path/to/dir" -f
# Example: ExportOBO -out "file:///path/to/dir" -u "urn:lsid:bioontology.org:fungal_anatomy" -v "UNASSIGNED" -f
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.ExportOBO $@
