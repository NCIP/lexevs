#  Loads All Value Set Definitions for a given uri as resolved coding scheme
#
# Options:
#   -u, Uri uniquely identifying the coding scheme to resolve from
#	defaults to http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#
#        
# Example: LoadAllDefinitionsToResolvedValueSet  -uri "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#" 
#
java -Xmx3000m -Djava.awt.headless=true -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*"  org.lexgrid.valuesets.admin.LoadAllDefinitionsToResolvedValueSet $@
