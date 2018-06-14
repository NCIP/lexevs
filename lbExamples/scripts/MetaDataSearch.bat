@echo off
REM Example attempting to query metadata stored for a code system.
REM HL7 RIM sample database must be loaded for this example.
REM Use the LoadSampleMetaDataData.bat to load the required 
REM code system and metadata.
REM
REM Example: MetaDataSearch "test string"
REM Example "test string" input: localName,  codingSchemeURI     
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.MetaDataSearch %*