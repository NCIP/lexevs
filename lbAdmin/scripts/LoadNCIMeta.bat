@echo off
REM Loads the NCI MetaThesaurus, provided as a collection of RRF files.
REM
REM Options:
REM   -in,--input <uri> URI or path of the directory containing the RRF files
REM   -mf,--manifest Manifest Location
REM   -lp,--Loader Preferences XML File Location
REM   -v, --validate <int> Perform validation of the candidate
REM         resource without loading data.  If specified, the '-a' and '-t'
REM         options are ignored.  Supported levels of validation include:
REM         0 = Verify the existence and format (up to 100 lines) of each required file
REM   -rr,--rootRecalc If specified, indicates that only root nodes are to be
REM         reevaluated for an already loaded source (if present).
REM   -a, --activate ActivateScheme on successful load; if unspecified the
REM         vocabulary is loaded but not activated.
REM   -t, --tag <id> An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign.
REM
REM Example: LoadNCIMeta -in "file:///path/to/directory/" -a
REM        LoadNCIMeta -in "file:///path/to/directory/" -v 0
REM
java -Xmx1300m -cp "..\runtime\lbPatch.jar;..\runtime\lbRuntime.jar" org.LexGrid.LexBIG.admin.LoadNCIMeta %*