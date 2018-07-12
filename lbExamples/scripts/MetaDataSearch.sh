# Example attempting to query metadata stored for a code system.
# HL7 RIM sample database must be loaded for this example.
# Use the LoadSampleMetaDataData.bat to load the required 
# code system and metadata.
#
# Example: MetaDataSearch "test string"
# Example "test string" input: localName,  codingSchemeURI     
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.MetaDataSearch $@
