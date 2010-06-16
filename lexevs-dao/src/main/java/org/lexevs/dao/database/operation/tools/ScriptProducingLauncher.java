package org.lexevs.dao.database.operation.tools;

import java.io.IOException;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.locator.LexEvsServiceLocator;

public class ScriptProducingLauncher {
	
	@Option(name="-db", required=true)   
	private String databaseType;
	
	@Option(name="-prefix") 
	private String prefix = "";
	
	@Option(name="-out", required=true) 
	private String out;
	
	private void execute() {
		try {
			LexEvsServiceLocator.getInstance().
				getLexEvsDatabaseOperations().
					dumpSqlScripts(DatabaseType.toDatabaseType(databaseType), out, prefix);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) throws Exception{
		ScriptProducingLauncher launcher = new ScriptProducingLauncher();
		CmdLineParser parser = new CmdLineParser(launcher);
		parser.parseArgument(args);	

		launcher.execute();
	}
}
