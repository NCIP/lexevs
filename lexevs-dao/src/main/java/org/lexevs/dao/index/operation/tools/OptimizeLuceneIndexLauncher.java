package org.lexevs.dao.index.operation.tools;

import static org.kohsuke.args4j.ExampleMode.ALL;

import java.io.PrintStream;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.lexevs.locator.LexEvsServiceLocator;

public class OptimizeLuceneIndexLauncher {

	@Option(name="-h", aliases={"--help"}, usage="Prints usage information.") 
	private boolean help;

	private void execute() {
		try {
			LexEvsServiceLocator.getInstance().
				getIndexServiceManager().
					getEntityIndexService().
						optimizeCommonIndex();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) throws Exception{
		new OptimizeLuceneIndexLauncher().doMain(args);
	}
	
	public void doMain(String[] args) throws Exception{
		CmdLineParser parser = new CmdLineParser(this);
		parser.setUsageWidth(160);

		try {
			parser.parseArgument(args);	

			if(help) {
				printUsage(parser, System.out);
                System.out.println("Optimizes the Common Lucene Index.");
			}

			this.execute();
		} catch(CmdLineException e) {
			System.err.println(e.getMessage());
            printUsage(parser, System.err);
            return;
        }
	}

	private void printUsage(CmdLineParser parser, PrintStream printStream) {
		printStream.println("[options...] arguments...");

		parser.printUsage(printStream);
		printStream.println();

		printStream.println("  Example: [ OptimizeLuceneIndex.bat | OptimizeLuceneIndex.sh ] " + parser.printExample(ALL));
	}
}
