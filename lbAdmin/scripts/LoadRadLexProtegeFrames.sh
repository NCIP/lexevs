# URI or path specifying location of the pprj file.
# 	Imports from a RadLex xml file to a LexBIG repository.
# 	Requires that the pprj file be configured with reference to
# 	a RadLex xml file as follows:
# 	([radlex_ProjectKB_Instance_66] of  String
# 		(name "source_file_name")
# 		(string_value "radlex.xml"))" );
#
# Example: java org.LexGrid.LexBIG.admin.LoadRadLexProtegeFrames
#   -in,--input &lt;uri&gt; URI or path specifying location of the source file
#   -v, --validate &lt;int&gt; Perform validation of the candidate
#         resource without loading data.  If specified, the '-a' and '-t'
#         options are ignored.  Supported levels of validation include:
#         0 = Verify document is well-formed
#         1 = Verify document is valid
#   -a, --activate ActivateScheme on successful load; if unspecified the vocabulary is loaded but not activated
#   -t, --tag &lt;id&gt; An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign. 
# 
# Example: java -Xmx800m -cp lgRuntime.jar -Djava.ext.dirs=../runtime/sqlDrivers
#  org.LexGrid.LexBIG.admin.LoadRadLexProtegeFrames -in "file:///path/to/RadLex.pprj" -a
# -or-
#  org.LexGrid.LexBIG.admin.LoadRadLexProtegeFrames -in "file:///path/to/RadLex.pprj" -v 0
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.LoadRadLexProtegeFrames $@