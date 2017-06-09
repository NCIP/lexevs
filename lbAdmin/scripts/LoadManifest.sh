# Load manifest data onto the codingscheme based on unique URN and version.
# Options:
# Example: java org.LexGrid.LexBIG.admin.LoadManifest
#  -u,--urn "urn"; URN uniquely identifying the code system.
#  -v,--version "versionId"; Version identifier.
#  -mf,--manifest "manifest"; location of manifest xml file.
# 
# Note: If the URN and version values are unspecified, a
# list of available coding schemes will be presented for
# user selection.
# 
# Example:	LoadManifest -u \"urn:oid:2.16.840.1.113883.3.26.1.1\" -v \"05.09e\" -mf \"file://path//to//manifest.xml\"" 
#			LoadManifest -mf \"file://path//to//manifest.xml\"
java -Xmx1300m -Djava.awt.headless=true -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.LoadManifest $@
