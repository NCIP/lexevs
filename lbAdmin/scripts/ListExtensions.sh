# List registered extensions to the LexBIG runtime.
# 
# Options:
#   -a,--all List all extensions (default, override by specifying other options).
#   -i,--index List index extensions.
#   -m,--match List match algorithm extensions.
#   -s,--sort List sort algorithm extensions.
#   -g,--generic List generic extensions.
# 
#  Example: ListExtensions -a
#
java -Xmx1000m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.ListExtensions $@
