# Loads NCI Metathesaurus content, provided as a collection of RRF files in a
# single directory.
#
# Options:
#   -in,--input <uri> URI or path of the directory containing the NLM files
#
# Example: LoadMetaBatch -in "file:///path/to/directory/"
#
java -Xmx2000m -XX:PermSize=256m -Djava.awt.headless=true -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.lexgrid.loader.meta.launch.MetaBatchLoaderLauncher $@
