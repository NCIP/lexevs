@echo off
REM Resume a UMLS load. Loads will usually be restartable if they fail due to an
REM error. The loader will keep all loaded content and restart at the point of failure.
REM
REM Options:
REM   -in,--input <uri> URI or path of the directory containing the NLM files
REM   -uri,--uri of vocabulary to resume.
REM   -version,--version of vocabulary to resume.
REM
REM Example: ResumeMetaBatch -in "file:///path/to/directory/" -uri "urn:123.4" -version "2.0"
REM
java -Xmx1600m -XX:PermSize=256m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.lexgrid.loader.meta.launch.MetaBatchLoaderLauncher %*
