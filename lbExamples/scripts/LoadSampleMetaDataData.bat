@echo off
REM Loads sample content for reference by the MetaData example programs
REM (to be run from the examples directory).
REM
REM Usage: LoadSampleMetaDataData
REM
call ..\admin\LoadHL7.bat -a -t SAMPLE -in .\resources\rimSample.mdb -lp .\resources\HL7Prefs.xml