# CleanUpMetadata
#	Clean up metadata entries that are orphaned.
#
# Options:
#
# -h, --help, Prints usage information
#
# -f,--force, Force removal without prompting of metadata that doesn't have a corresponding coding scheme entry(orphaned).
#
#
# Example:
#
# CleanUpMetadata -r 
java -Xmx800m -XX:PermSize=256m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.CleanUpMetadataLauncher $@