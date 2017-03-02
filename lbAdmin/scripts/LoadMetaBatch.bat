@echo off
REM Loads NCI Metathesaurus content, provided as a collection of RRF files in a
REM single directory.
REM
REM Options:
REM   -in,--input <uri> URI or path of the directory containing the NLM files
REM
REM Example: LoadMetaBatch -in "file:///path/to/directory/"
REM
java -Xmx1600m -XX:PermSize=256m -Djava.awt.headless=true -cp "..\runtime/lbPatch.jar;..\runtime-components\extLib\*" org.lexgrid.loader.meta.launch.MetaBatchLoaderLauncher %*