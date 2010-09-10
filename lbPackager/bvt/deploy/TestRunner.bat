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
REM      for view in a standard web browser; this is the default
REM      if no other option is specified.
REM 
REM Example: TestRunner -h
REM
java -Xmx1000m -cp .\lbTest.jar;..\runtime\lbPatch.jar;..\runtime\lbRuntime.jar;.\extlib\ant\ant.jar;.\extlib\ant\ant-junit.jar;.\extlib\ant\ant-trax.jar;.\extlib\ant\ant-launcher.jar;.\extlib\junit\junit-4.4.jar bvt.TestRunner %*
