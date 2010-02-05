@echo off
REM Loads sample content for reference by the example programs
REM (to be run from the examples directory).
REM
REM Usage: LoadSampleData
REM
call ..\admin\LoadOWL.bat -a -t SAMPLE -in .\resources\sample.owl
call ..\admin\LoadUMLSSemnet.bat -a -t SAMPLE -in .\resources\semnet