@echo off
REM Loads the UMLS Semantic Network, provided as a collection of files in a
REM single directory.  The following files are expected to be provided from the
REM National Library of Medicine (NLM) distribution:
REM   - LICENSE.txt (text from distribution terms and conditions)
REM   - SRFIL.txt (File Description)
REM   - SRFIL.txt (Field Description)
REM   - SRDEF.txt (Basic information about the Semantic Types and Relations)
REM   - SRSTR.txt (Structure of the Network)
REM   - SRSTRE1.txt (Fully inherited set of Relations (UIs))
REM   - SRSTRE2.txt (Fully inherited set of Relations (Names))
REM   - SU.txt (Unit Record)
REM These files can be downloaded from the NLM web site at
REM http://semanticnetwork.nlm.nih.gov/Download/index.html.
REM
REM Options:
REM   -in,--input <uri> URI or path of the directory containing the NLM files
REM   -v, --validate <int> Perform validation of the candidate
REM         resource without loading data.  If specified, the '-a' and '-t'
REM         options are ignored.  Supported levels of validation include:
REM         0 = Verify the existence of each required file
REM   -a, --activate ActivateScheme on successful load; if unspecified the
REM         vocabulary is loaded but not activated.
REM   -t, --tag <id> An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign.
REM   -il,--InheritanceLevel <int> If specified, indicates the extent of inherited relationships to import.  
REM			0 = none; 1 = all; 2 = all except is_a (default).
REM 		All direct relationships are imported, regardless of option.
REM
REM Example: LoadUMLSSemnet -in "file:///path/to/directory/" -a
REM          LoadUMLSSemnet -in "file:///path/to/directory/" -v 0
REM
java -Xmx1300m -Djava.awt.headless=true -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.admin.LoadUMLSSemnet %*