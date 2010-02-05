# Loads UMLS content, provided as a collection of RRF files in a
# single directory.  Files may comprise the entire UMLS distribution
# or pruned via the MetamorphoSys tool.  A complete list of
# source vocabularies is available online at
# http://www.nlm.nih.gov/research/umls/metaa1.html.
#
# Options:
#   -in,--input <uri> URI or path of the directory containing the NLM files
#   -mf,--manifest Manifest Location
#   -lp,--Loader Preferences XML File Location
#   -s,--sources Comma-delimited list of source vocabularies to load.
#        If absent, all available vocabularies are loaded.
#   -v, --validate <int> Perform validation of the candidate
#        resource without loading data.  If specified, the '-a' and '-t'
#        options are ignored.  Supported levels of validation include:
#        0 = Verify the existence of each required file
#   -a, --activate ActivateScheme on successful load; if unspecified the
#        vocabulary is loaded but not activated.
#   -t, --tag &lt;id&gt; An optional tag ID (e.g. 'PRODUCTION' or 'TEST')
#         to assign.
#
# Example: LoadUMLSFiles -in "file:///path/to/directory/" -s "ICD9CM_2005,ICD9CM_2006" -a
#          LoadUMLSFiles -in "file:///path/to/directory/" -v 0
#
java -Xmx1500m -cp "../runtime/lbPatch.jar:../runtime/lbRuntime.jar" org.LexGrid.LexBIG.admin.LoadUMLSFiles $@
