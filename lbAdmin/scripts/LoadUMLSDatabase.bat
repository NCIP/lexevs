@echo off
REM Loads UMLS content, provided as a collection of RRF files in a
REM single directory.  Files may comprise the entire UMLS distribution
REM or pruned via the MetamorphoSys tool.  A complete list of
REM source vocabularies is available online at
REM http://www.nlm.nih.gov/research/umls/metaa1.html.
REM
REM Options:
REM   -in,--input <uri> Location of the source database. Typically this is
REM         specified in the form of a URL that indicates the database
REM         server, port, name, and optional properties.
REM   -u,--uid User ID for authenticated access, if required and not
REM         specified as part of the input URL.
REM   -p,--pwd Password for authenticated access, if required and not
REM         specified as part of the input URL.
REM   -d,--driver Name of the JDBC driver to use when accessing the database.
REM   -s,--sources Comma-delimited list of source vocabularies to load.
REM         If absent, all available vocabularies are loaded.
REM   -v, --validate <int> Perform validation of the candidate resource
REM         without loading data.  If specified, the '-a' and '-t'
REM         options are ignored. Supported levels of validation include:
REM         0 = Verify the existence of each required file
REM   -a, --activate ActivateScheme on successful load; if unspecified the
REM         vocabulary is loaded but not activated.
REM   -t, --tag &lt;id&gt; An optional tag ID (e.g. 'PRODUCTION' or 'TEST')
REM         to assign.
REM
REM Example: LoadUMLSDatabase -in "jdbc:postgresql://localhost:5432/lexgrid"
REM          -d "org.postgresql.Driver" -u "myDatabaseUser" -p "myPassword"
REM          -s "ICD9CM_2005,ICD9CM_2006" -a
REM         LoadUMLSDatabase -in "jdbc:postgresql://localhost:5432/lexgrid"
REM          -d "org.postgresql.Driver" -u "myDatabaseUser" -p "myPassword"
REM          -v 0
REM
java -Xmx1300m -cp "..\runtime\lbPatch.jar;..\runtime\lbRuntime.jar" org.LexGrid.LexBIG.admin.LoadUMLSDatabase %*