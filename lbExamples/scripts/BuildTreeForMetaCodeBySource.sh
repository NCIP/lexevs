# NOTE: This example is intended to run only against code systems
# representing the entire Metathesaurus.  It depends on the presence of
# concepts based on CUIs and the import of source and hierarchical
# definitions as property and association qualifiers.  These
# attributes are only populated by the NCI Meta loader.
# 
# Attempts to provide a tree, based on a focus code, that includes the
# following information:
# 
# - All paths from the hierarchy root to one or more focus codes.
# - Immediate children of every node in path to root
# - Indicator to show whether any unexpanded node can be further expanded
# 
# This example accepts two parameters... The first parameter is required, and
# must contain at least one code in a comma-delimited list. A tree is produced
# for each code. Time to produce the tree for each code is printed in
# milliseconds. In order to factor out costs of startup and shutdown, resolving
# multiple codes may offer a better overall estimate performance.
# 
# The second parameter is also required.  It should provide a
# source abbreviation (SAB) used to constrain the relationships navigated
# to a single Meta source.  If not specified, all sources are navigated.
# 
# Note that this example does not print intra-CUI associations (links
# that might exist between individual terms on a single concept).
# 
# The selected code system must represent the full Metathesaurus.
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.BuildTreeForMetaCodeBySource $@
