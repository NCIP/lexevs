package org.lexgrid.valuesets.admin;


import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.admin.Util;
import org.LexGrid.annotations.LgAdminFunction;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

public class RemoveAllValueSetDefinitions {

	@LgAdminFunction
	public static void main(String[] args) {
		try {
			new RemoveAllValueSetDefinitions().run(args);
		} catch (final Exception e) {
			Util.displayAndLogError("REQUEST FAILED !!!", e);
		}
	}

	public void run(String[] args)  {

		Util.displayMessage("REMOVING ALL VALUE SET DEFINITIONS. "
				+ "DO YOU REALLY WANT TO DO THIS? ('Y' to confirm, any other key to cancel)");
		char choice = 0;
		try {
			choice = Util.getConsoleCharacter();
		} catch (IOException e) {
			Util.displayMessage("Error Reading Input");
		}
		if (choice == 'Y' || choice == 'y'){
		final LexEVSValueSetDefinitionServices vss = new LexEVSValueSetDefinitionServicesImpl();
		List<String> uris = vss.listValueSetDefinitionURIs();
		for (String urn : uris) {
			try {
				vss.removeValueSetDefinition(URI.create(urn));
			} catch (LBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Util.displayMessage("ValueSetDefinition removed: " + urn);
		}
		}
	}
}
