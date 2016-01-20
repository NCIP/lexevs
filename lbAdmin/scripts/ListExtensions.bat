@echo off
REM List registered extensions to the LexBIG runtime.
REM 
REM Options:
REM   -a,--all List all extensions (default, override by specifying other options).
REM   -i,--index List index extensions.
REM   -m,--match List match algorithm extensions.
REM   -s,--sort List sort algorithm extensions.
REM   -g,--generic List generic extensions.
REM 
REM  Example: ListExtensions -a
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.ListExtensions %*