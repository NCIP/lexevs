@echo off
REM Loads MEDRT UMLS content, provided as a collection of RRF files in a
REM single directory.  Files must comprise the entire UMLS distribution.  This
REM will not work with a UMLS extraction of MEDRT
REM
REM Options:
REM   -in,--input <uri> URI or path of the directory containing the NLM files.
REM   Path string must be preceded by "file:",
REM
REM Example: LoadUMLSFiles -in "file:///path/to/directory/"
REM
java -Xmx8000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.lexgrid.loader.umls.launch.MedRtBatchLoaderLauncher %*
