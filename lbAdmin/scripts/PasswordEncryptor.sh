# Encrypts the given password.  This can be used in lbconfig.props
# for the database password and will be automatically decrypted on
# start-up
#
# Argument:
#   -p,--password password for encryption.
#
# Example: PasswordEncryptor -p lexgrid
#
# NOTE: The encrypted password will be placed @ lbAdmin/password.txt
#
java -Xmx1500m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.LexGrid.LexBIG.admin.PasswordEncryptor $@
