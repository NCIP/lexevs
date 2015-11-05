@echo off
REM Runs the test suite by invoking the Ant launcher.
REM
REM Usage: TestRunner
REM  -b, --brief
REM  	Run the LexBIG test suite and produce a text report with
REM  	overall statistics and details for failed tests only.
REM  -f, --full
REM  	Run the LexBIG test suite and produce an itemized list of
REM		all tests with indication of success/failure.
REM  -x,--xml
REM  	Run the LexBIG test suite and produce a report with extensive
REM  	information for each test case in xml format.
REM  -h,--html
REM  	Run the LexBIG test suite and produce a report suitable
REM     for view in a standard web browser; this is the default
REM     if no other option is specified.
REM  -v,--verify
REM  	Basic verification that LexEVS is configured properly 
REM	    and basic systems are functioning.
REM
REM Example: TestRunner -h
REM
java -Xmx1600m -XX:MaxPermSize=256m -cp .\lbTest.jar;..\runtime\lbPatch.jar;..\runtime-components\lexbig.jar;.\runtime-components\* bvt.TestRunner %*
