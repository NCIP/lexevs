# Loads NCI Metathesaurus content, provided as a collection of RRF files in a
# single directory.
#
# Options:
#   -in,--input <uri> URI or path of the directory containing the NLM files
#
# Example: LoadMetaBatch -in "file:///path/to/directory/"
#
java -Xmx2000m -cp "../runtime/lbPatch.jar:../runtime/lbRuntime.jar" org.lexgrid.loader.meta.launch.MetaBatchLoaderLauncher $@
