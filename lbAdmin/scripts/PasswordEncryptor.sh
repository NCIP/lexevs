# Encrypts the given password.
#
# Argument:
#   -p,--password password for encryption.
#
# Example: PasswordEncryptor -p lexgrid
#
java -Xmx1500m -cp "../runtime/lbPatch.jar:../runtime/lbRuntime.jar" org.LexGrid.LexBIG.admin.PasswordEncryptor $@
