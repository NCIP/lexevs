package org.lexevs.dao.database.operation.tools;

import static org.kohsuke.args4j.ExampleMode.ALL;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.locator.LexEvsServiceLocator;

public class ScriptProducingLauncher {
	
	@Option(name="-db", aliases={"--databaseType"}, required=true, usage="Target database type.")   
	private DatabaseType databaseType;
	
	@Option(name="-p", aliases={"--prefix"}, usage="Prefix to append to all tables.", metaVar="PREFIX") 
	private String prefix = "";
	
	@Option(name="-o", aliases={"--out"}, required=true, usage="Output directory.", metaVar="DIR") 
	private String out;
	
	@Option(name="-f", aliases={"--force"}, usage="Force output directory creation.") 
	private boolean force;
	
	@Argument
    private List<String> arguments = new ArrayList<String>();

	private void execute() {
		if(this.force) {
			new File(out).mkdir();
		}

		try {
			LexEvsServiceLocator.getInstance().
				getLexEvsDatabaseOperations().
					dumpSqlScripts(databaseType, out, prefix);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) throws Exception{
		new ScriptProducingLauncher().doMain(args);
	}
	
	public void doMain(String[] args) throws Exception{
		CmdLineParser parser = new CmdLineParser(this);
		parser.setUsageWidth(160);

		try {
			parser.parseArgument(args);	

			if( arguments.isEmpty()) {
                throw new CmdLineException("No argument is given");
			}

			this.execute();
		} catch(CmdLineException e) {
			System.err.println(e.getMessage());
            printUsage(parser);
            return;
        }
	}

	private void printUsage(CmdLineParser parser) {
		System.err.println("[options...] arguments...");

		parser.printUsage(System.err);
		System.err.println();

		System.err.println("  Example: [ ExportDDLScripts.bat | ExportDDLScripts.sh ] " + parser.printExample(ALL));
	}
}
