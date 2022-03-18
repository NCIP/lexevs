
package bvt;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Runs the test suite by invoking the Ant launcher.
 * <pre>
 * Usage: java TestRunner
 *  -b, --brief
 *  	Run the LexBIG test suite and produce a text report with
 *  	overall statistics and details for failed tests only.
 *  -f, --full
 *  	Run the LexBIG test suite and produce an itemized list of
 *		all tests with indication of success/failure.
 *  -x,--xml
 *  	Run the LexBIG test suite and produce a report with extensive
 *  	information for each test case in xml format.
 *  -h,--html
 *  	Run the LexBIG test suite and produce a report suitable
 *      for view in a standard web browser.
 * 
 * Example: java -Xmx512m -cp lgRuntime.jar:lgTest.jar
 * org.LexGrid.LexBIG.test.TestRunner -h
 * </pre>
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 */
public class TestRunner {

	public static void main(String[] args) {
		try {
			new TestRunner().run(args);
		} catch (Exception e) {
			System.out.println("[LexBIG] *** Launch failed: "
				+ e.toString());
		}
	}

	public TestRunner() {
		super();
	}

	/**
	 * Primary entry point for the program.
	 * 
	 * @throws Exception
	 */
	public void run(String[] args) throws Exception {
		
		// Parse the command line ...
		CommandLine cl = null;
		Options options = getCommandOptions();
		try {
			cl = new BasicParser().parse(options, args);
			if (cl.getOptions().length == 0)
				throw new ParseException("Report format not specified.");
		} catch (ParseException e) {
			System.out.println("");
			System.out.println(e.getMessage());
			System.out.println("");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(80, "TestRunner", "", options, "", true);
			System.out.println("");
			System.out.println("Example: TestRunner -h");
			return;
		}

		// Interpret provided values ...
		boolean isBriefRequested = cl.hasOption("b");
		boolean isFullRequested = cl.hasOption("f");
		boolean isHtmlRequested = cl.hasOption("h");
		boolean isXmlRequested = cl.hasOption("x");
		boolean isVerifyRequested = cl.hasOption("v");

		// Launch the requested tests ...
		if (isBriefRequested)
			org.apache.tools.ant.launch.Launcher.main(
				new String[] {"-buildfile", "TestRunner.xml", "bvt-brief"});
		if (isFullRequested)
			org.apache.tools.ant.launch.Launcher.main(
				new String[] {"-buildfile", "TestRunner.xml", "bvt-full"});
		if (isHtmlRequested)
			org.apache.tools.ant.launch.Launcher.main(
				new String[] {"-buildfile", "TestRunner.xml", "bvt-html"});
		if (isXmlRequested)
			org.apache.tools.ant.launch.Launcher.main(
				new String[] {"-buildfile", "TestRunner.xml", "bvt-xml"});
		if (isVerifyRequested)
			org.apache.tools.ant.launch.Launcher.main(
				new String[] {"-buildfile", "TestRunner.xml", "verify"});
		
	}

	/**
	 * Return supported command options.
	 * @return org.apache.commons.cli.Options
	 */
	private Options getCommandOptions() {
		Options options = new Options();
		Option o;

		o = new Option("b", "brief", false,
				"Run the LexBIG test suite and produce a text report " +
				"with overall statistics and details for failed " +
				"tests only.");
		o.setRequired(false);
		options.addOption(o);

		o = new Option("f", "full", false,
				"Run the LexBIG test suite and produce an itemized " +
				"list of all tests with indication of success/failure.");
		o.setRequired(false);
		options.addOption(o);

		o = new Option("x", "xml", false,
				"Run the LexBIG test suite and produce a report with " +
				"extensive information for each test case in xml format.");
		o.setRequired(false);
		options.addOption(o);

		o = new Option("h", "html", false,
				"Run the LexBIG test suite and produce a report " +
				"suitable for view in a standard web browser.");
		o.setRequired(false);
		options.addOption(o);
		
		o = new Option("v", "verify", false,
				"Basic verification that LexEVS is configured properly " +
				"and basic systems are functioning.");
		o.setRequired(false);
		options.addOption(o);

		return options;
	}

}