@echo off
REM Loads NCI Metathesaurus content, provided as a collection of RRF files in a
REM single directory.
REM
REM Options:
REM   -in,--input <uri> URI or path of the directory containing the NLM files
REM
REM Example: LoadMetaBatch -in "file:///path/to/directory/"
REM
java -Xmx800m -cp "../runtime/lbPatch.jar;../runtime/lbRuntime.jar" org.lexgrid.loader.meta.launch.MetaBatchLoaderLauncher %*