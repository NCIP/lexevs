# Example showing how to find if a coded concept belongs to a value set. The program
# accepts one parameter : The concept code 
#
# A list of value Set Definition available in the system will be displayed. User can choose
# a particular value set to check the concept membership.
#
# A list of Code System present in the system will also be displayed. User can select which 
# Code System Version to be used to resolve Value Set Definition.
# 
# Example: CheckForConceptInValueSet "C123456"
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.example.CheckForConceptInValueSet $@
