# Loads UMLS content, provided as a collection of RRF files in a
# single directory.  Files may comprise the entire UMLS distribution
# or pruned via the MetamorphoSys tool.  A complete list of
# source vocabularies is available online at
# http://www.nlm.nih.gov/research/umls/metaa1.html.
#
# Options:
#   -in,--input <uri> URI or path of the directory containing the NLM files. Path string must be preceded by "file:",
#   -s,--source vocabularies to load.
#
# Example: LoadUMLSBatch -in "file:///path/to/directory/" -s "PSY"
#
java -Xmx8000m -Djava.awt.headless=true -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.lexgrid.loader.umls.launch.UmlsBatchLoaderLauncher $@
