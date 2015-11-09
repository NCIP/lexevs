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
 #  -v,--verify
 #  	Basic verification that LexEVS is configured properly 
 #	    and basic systems are functioning.
 # 
 # Example: TestRunner -h
 #
java -Xmx1600m -XX:MaxPermSize=256m -cp ./lbTest.jar:../runtime-components/lexbig.jar:../runtime-components/extLib/*:./extlib/ant/ant-1.7.0.jar:./extlib/ant/ant-junit-1.7.0.jar:./extlib/ant/ant-trax-1.7.0.jar:./extlib/ant/ant-launcher-1.7.0.jar:./extlib/junit/junit-4.4.jar bvt.TestRunner $@