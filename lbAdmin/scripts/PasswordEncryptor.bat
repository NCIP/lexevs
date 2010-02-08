@echo off
REM Encrypts the given password.
REM
REM Argument:
REM   -p,--password password for encryption.
REM
REM NOTE: The encrypted password will be placed @ lbAdmin/password.txt
REM
REM Example: PasswordEncryptor -p lexgrid
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime\lbRuntime.jar" org.LexGrid.LexBIG.admin.PasswordEncryptor %*