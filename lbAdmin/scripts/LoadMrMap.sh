# Loads a mappings file(s), provided in UMLS RRF format.
#
# Options:
# -inMap,--input <uri> URI or path specifying location of the MRMAP source file.
# -inSat,--input <uri> URI or path specifying location of the MRSAT source file. 
#
# Example: LoadMrMap -inMap "file:///path/to/MRMAP.RRF -inSat "file:///path/to/MRSAT.RRF"
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.LoadMrMap $@