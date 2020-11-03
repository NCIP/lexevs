# Loads MEDRT UMLS content, provided as a collection of RRF files in a
# single directory.  Files must comprise the entire UMLS distribution.  This
# will not work with a UMLS extraction of MEDRT
#
# Options:
#   -in,--input <uri> URI or path of the directory containing the NLM files.
#   Path string must be preceded by "file:",
#
# Example: LoadUMLSMEDRTBatch -in "file:///path/to/directory/"
#
java -Xmx8000m -Djava.awt.headless=true -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.lexgrid.loader.umls.launch.MedRtBatchLoaderLauncher $@
