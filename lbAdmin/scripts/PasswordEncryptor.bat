@echo off
REM Encrypts the given password.  This can be used in lbconfig.props
REM for the database password and will be automatically decrypted on
REM start-up
REM
REM Argument:
REM   -p,--password password for encryption.
REM
REM NOTE: The encrypted password will be placed @ lbAdmin/password.txt
REM
REM Example: PasswordEncryptor -p lexgrid
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.PasswordEncryptor %*