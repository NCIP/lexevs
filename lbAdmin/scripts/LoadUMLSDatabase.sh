# Loads UMLS content, provided as a collection of RRF files in a
# single directory.  Files may comprise the entire UMLS distribution
# or pruned via the MetamorphoSys tool.  A complete list of
# source vocabularies is available online at
# http://www.nlm.nih.gov/research/umls/metaa1.html.
#
# Options:
#   -in,--input <uri> Location of the source database. Typically this is
#         specified in the form of a URL that indicates the database
#         server, port, name, and optional properties.
#   -u,--uid User ID for authenticated access, if required and not
#         specified as part of the input URL.
#   -p,--pwd Password for authenticated access, if required and not
#         specified as part of the input URL.
#   -d,--driver Name of the JDBC driver to use when accessing the database.
#   -s,--sources Comma-delimited list of source vocabularies to load.
#         If absent, all available vocabularies are loaded.
#   -v, --validate <int> Perform validation of the candidate resource
#         without loading data.  If specified, the '-ef', -a' and '-t'
#         options are ignored. Supported levels of validation include:
#         0 = Verify the existence of each required file
#   -a, --activate ActivateScheme on successful load; if unspecified the
#         vocabulary is loaded but not activated.
#   -t, --tag &lt;id&gt; An optional tag ID (e.g. 'PRODUCTION' or 'TEST')
#         to assign.
#
# Example: LoadUMLSDatabase -in "jdbc:postgresql://localhost:5432/lexgrid"
#          -d "org.postgresql.Driver" -u "myDatabaseUser" -p "myPassword"
#          -s "ICD9CM_2005,ICD9CM_2006" -a
#          LoadUMLSDatabase -in "jdbc:postgresql://localhost:5432/lexgrid"
#          -d "org.postgresql.Driver" -u "myDatabaseUser" -p "myPassword"
#          -v 0
#
java -Xmx1500m -cp "../runtime/lbPatch.jar:../runtime/lbRuntime.jar" org.LexGrid.LexBIG.admin.LoadUMLSDatabase $@
