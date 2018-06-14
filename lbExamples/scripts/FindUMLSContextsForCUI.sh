# NOTE: This example is intended to run only against code systems
# representing the entire Metathesaurus.  It depends on the presence of
# concepts based on CUIs and the import of source and hierarchical
# definitions as property and association qualifiers.  These
# attributes are only populated by the NCI Meta loader.
# 
# Example displays explicitly asserted source hierarchies (based on
# import of MRHIER HCD) for a CUI.  The program takes a single argument
# (a UMLS CUI), prompts for the code system to query in the LexGrid
# repository, and displays the HCD-based context relationships
# along with other details.
# 
# Note that this example does not print intra-CUI associations (links
# that might exist between individual terms on a single concept) or 
# hierarchies not explicitly tagged by HCD.
# 
# The selected code system must represent the full Metathesaurus.
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.FindUMLSContextsForCUI $@
