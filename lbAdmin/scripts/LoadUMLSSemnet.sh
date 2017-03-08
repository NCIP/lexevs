# Loads the UMLS Semantic Network, provided as a collection of files in a
# single directory.  The following files are expected to be provided from the
# National Library of Medicine (NLM) distribution:
#   - LICENSE.txt (text from distribution terms and conditions)
#   - SRFIL.txt (File Description)
#   - SRFIL.txt (Field Description)
#   - SRDEF.txt (Basic information about the Semantic Types and Relations)
#   - SRSTR.txt (Structure of the Network)
#   - SRSTRE1.txt (Fully inherited set of Relations (UIs))
#   - SRSTRE2.txt (Fully inherited set of Relations (Names))
#   - SU.txt (Unit Record)
# These files can be downloaded from the NLM web site at
# http://semanticnetwork.nlm.nih.gov/Download/index.html.
#
# Options:
#   -in,--input <uri> URI or path of the directory containing the NLM files
#   -v, --validate <int> Perform validation of the candidate
#         resource without loading data.  If specified, the '--a' and '-t'
#         options are ignored.  Supported levels of validation include:
#         0 = Verify the existence of each required file
#   -a, --activate ActivateScheme on successful load; if unspecified the
#         vocabulary is loaded but not activated.
#   -t, --tag <id> An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign.
#	-il,--InheritanceLevel <int> If specified, indicates the extent of inherited relationships to import.  
#		  0 = none; 1 = all; 2 = all except is_a (default).
#		  All direct relationships are imported, regardless of option.
#
# Example: LoadUMLSSemnet -in "file:///path/to/directory/" -a
#          LoadUMLSSemnet -in "file:///path/to/directory/" -v 0
#
java -Xmx1500m -Djava.awt.headless=true -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.LoadUMLSSemnet $@
