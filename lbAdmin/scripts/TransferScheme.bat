@echo off
REM Exports content from the repository to a file in the Open Biomedical
REM Ontologies (OBO) format.
REM
REM Options:
REM   -u,--urn <name> URN or local name of the coding scheme to transfer.
REM   -v,--version <id> The assigned tag/label or absolute version
REM        identifier of the coding scheme.
REM 
REM Note: If the URN and version values are unspecified, a
REM list of available coding schemes will be presented for
REM user selection.
REM
REM Example: TransferScheme
REM Example: TransferScheme -u "NCI Thesaurus" -v "6.06"
REM
java -cp "..\runtime\lbPatch.jar;..\runtime\lbRuntime.jar" org.LexGrid.LexBIG.admin.TransferScheme %*