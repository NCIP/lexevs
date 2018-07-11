# Provides basic profiling of a coding scheme concepts and relations (e.g.
# number of concepts and relation depth).
# 
# Options:
#   -u,--urn <urn> URN uniquely identifying the code system.
#   -v,--version <id> Code system version identifier.
#   -r,--relation <relation> Optional name of a relation to profile;
#        defaults to 'hasSubtype' if not specified.
#  
# Note: If the URN and version values are unspecified, a
# list of available coding schemes will be presented for
# user selection.
#
# Example: ProfileScheme
# Example: ProfileScheme -u "urn:oid:2.16.840.1.113883.3.26.1.1" -v "05.09e" -r "hasSubtype"
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.ProfileScheme $@
