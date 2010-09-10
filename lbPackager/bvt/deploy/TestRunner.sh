@echo off
 # Runs the test suite by invoking the Ant launcher.
 #
 # Usage: TestRunner
 #  -b, --brief
 #  	Run the LexBIG test suite and produce a text report with
 #  	overall statistics and details for failed tests only.
 #  -f, --full
 #  	Run the LexBIG test suite and produce an itemized list of
 #		all tests with indication of success/failure.
 #  -x,--xml
 #  	Run the LexBIG test suite and produce a report with extensive
 #  	information for each test case in xml format.
 #  -h,--html
 #  	Run the LexBIG test suite and produce a report suitable
 #      for view in a standard web browser: this is the default
 #      if no other option is specified.
 # 
 # Example: TestRunner -h
 #
java -Xmx1000m -cp ./lbTest.jar:../runtime/lbPatch.jar:../runtime/lbRuntime.jar:./extlib/ant/ant.jar:./extlib/ant/ant-junit.jar:./extlib/ant/ant-trax.jar:./extlib/ant/ant-launcher.jar:./extlib/junit/junit-4.4.jar bvt.TestRunner $@