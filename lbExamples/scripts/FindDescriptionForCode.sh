# Example showing how to find the entity description assigned
# to a specific code.  The program accepts one parameter, the
# entity code.
#
# Example: FindDescriptionForCode "C43652"
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.FindDescriptionForCode $@
