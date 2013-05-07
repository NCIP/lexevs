=======================================
LexEVS Build/Configuration Verification
=======================================

Used to either run a short configuration verification or
a full JUnit test of LexEVS.

Options:
Usage: TestRunner(.sh/.bat)
  -b, --brief
  	Run the LexBIG test suite and produce a text report with
  	overall statistics and details for failed tests only.
  -f, --full
  	Run the LexBIG test suite and produce an itemized list of
	all tests with indication of success/failure.
  -x,--xml
  	Run the LexBIG test suite and produce a report with extensive
  	information for each test case in xml format.
  -h,--html
  	Run the LexBIG test suite and produce a report suitable
     for view in a standard web browser; this is the default
     if no other option is specified.
  -v,--verify
  	Basic verification that LexEVS is configured properly 
	and basic systems are functioning.
	
	
The (-v,--verify) option will provide a short verification that
the system is configured and running properly, able to connect
to the underlying database, and load and query content. It is
designed to run in a few minutes.

The remaining options will run the entire LexEVS JUnit Suite. Expect
at least a 30 minute run time and a substantial system load during
the test execution. 
NOTE: During the JUnit test execution, immediate feedback will not
be given. For instance, connection/configuration problems will not
necessarily halt test execution. For a verification of the
system configuration, please use the (-v,--verify) option.