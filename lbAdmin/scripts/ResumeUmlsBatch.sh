# Resume a UMLS load. Loads will usually be restartable if they fail due to an
# error. The loader will keep all loaded content and restart at the point of failure.
#
# Options:
#   -in,--input <uri> URI or path of the directory containing the NLM files
#   -s,--source vocabulary to resume.
#   -uri,--uri of vocabulary to resume.
#   -version,--version of vocabulary to resume.
#
# Example: ResumeUmlsBatch.sh -in "file:///path/to/directory/" -s "PSY" -uri "urn:123.4" -version "2.0"
#
java -Xmx800m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.lexgrid.loader.umls.launch.UmlsBatchLoaderLauncher $@
