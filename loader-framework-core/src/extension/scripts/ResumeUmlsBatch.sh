# Loads UMLS content, provided as a collection of RRF files in a
# single directory.  Files may comprise the entire UMLS distribution
# or pruned via the MetamorphoSys tool.  A complete list of
# source vocabularies is available online at
# http://www.nlm.nih.gov/research/umls/metaa1.html.
#
# Options:
#   -in,--input <uri> URI or path of the directory containing the NLM files
#   -s,--source vocabulary to resume.
#   -uri,--uri of vocabulary to resume.
#   -version,--version of vocabulary to resume.
#
# Example: LoadUMLSFiles -in "file:///path/to/directory/" -s "PSY" -uri "urn:123.4" -version "2.0"
#
java -Xmx500m -cp "../runtime/lbPatch.jar:../runtime/lbRuntime.jar:../runtime/lib/args4j-2.0.12.jar:../runtime/SpringBatchLoader-1.0-bootstrap.jar" org.lexgrid.loader.umls.launch.UmlsBatchLoaderLauncher $@
