@echo off
REM Loads UMLS content, provided as a collection of RRF files in a
REM single directory.  Files may comprise the entire UMLS distribution
REM or pruned via the MetamorphoSys tool.  A complete list of
REM source vocabularies is available online at
REM http://www.nlm.nih.gov/research/umls/metaa1.html.
REM
REM Options:
REM   -in,--input <uri> URI or path of the directory containing the NLM files
REM   -s,--source vocabularies to load.
REM
REM Example: LoadUMLSFiles -in "file:///path/to/directory/" -s "PSY"
REM
java -Xmx500m -cp "../runtime/lbPatch.jar;../runtime/lbRuntime.jar;../runtime/lib/args4j-2.0.12.jar;../runtime/SpringBatchLoader-1.0-bootstrap.jar" org.lexgrid.loader.umls.launch.UmlsBatchLoaderLauncher %*