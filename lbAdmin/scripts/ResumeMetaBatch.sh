# Resume a UMLS load. Loads will usually be restartable if they fail due to an
# error. The loader will keep all loaded content and restart at the point of failure.
#
# Options:
#   -in,--input <uri> URI or path of the directory containing the NLM files
#   -uri,--uri of vocabulary to resume.
#   -version,--version of vocabulary to resume.
#
# Example: ResumeMetaBatch -in "file:///path/to/directory/" -s "PSY" -uri "urn:123.4" -version "2.0"
#
java -Xmx1600m -XX:PermSize=256m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.lexgrid.loader.meta.launch.MetaBatchLoaderLauncher $@
