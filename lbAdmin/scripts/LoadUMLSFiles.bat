@echo off
REM Loads UMLS content, provided as a collection of RRF files in a
REM single directory.  Files may comprise the entire UMLS distribution
REM or pruned via the MetamorphoSys tool.  A complete list of
REM source vocabularies is available online at
REM http://www.nlm.nih.gov/research/umls/metaa1.html.
REM
REM Options:
REM   -in,--input <uri> URI or path of the directory containing the NLM files
REM   -mf,--manifest Manifest Location
REM   -lp,--Loader Preferences XML File Location
REM   -s,--sources Comma-delimited list of source vocabularies to load.
REM         If absent, all available vocabularies are loaded.
REM   -v, --validate <int> Perform validation of the candidate
REM         resource without loading data.  If specified, the '-a' and
REM         't' options are ignored.  Supported levels of validation include:
REM         0 = Verify the existence of each required file
REM   -a, --activate ActivateScheme on successful load; if unspecified the
REM         vocabulary is loaded but not activated.
REM   -t, --tag &lt;id&gt; An optional tag ID (e.g. 'PRODUCTION' or 'TEST')
REM         to assign.
REM
REM Example: LoadUMLSFiles -in "file:///path/to/directory/" -s "ICD9CM_2005,ICD9CM_2006" -a
REM          LoadUMLSFiles -in "file:///path/to/directory/" -v 0
REM
java -Xmx1300m -cp "..\runtime\lbPatch.jar;..\runtime\lbRuntime.jar" org.LexGrid.LexBIG.admin.LoadUMLSFiles %*