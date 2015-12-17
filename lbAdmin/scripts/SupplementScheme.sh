#  Utility to register, unregister, and list Coding Scheme Supplements
#
#  Usage: SupplementScheme  [-r] [-u] [-l]
#     [-parentUri parentUri]
#     [-parentVersion parentVersion]
#		[-supplementUri supplementUri] 
#     [-supplementVersion supplementVersion] 
#  -r                                    Register Coding Scheme as a Supplement
#  -u                                    Unregister Coding Scheme as a Supplement
#  -l,--list                             List Supplements
#  -parentUri <parentUri>                Parent URI.
#  -parentVersion <parentVersion>        Parent Version.
#  -supplementUri <supplementUri>        Supplement URI.
#  -supplementVersion <supplementVersion>Supplement Version.
# 
#  Example: SupplementScheme -u -parentUri "urn:oid:2.16.840.1.113883.3.26.1.1" -parentVersion "05.09e" -supplementUri "http://supplement.ontology.org" -supplementVersion "1.0.1" 
# 
java -Xmx500m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.SupplementScheme $@