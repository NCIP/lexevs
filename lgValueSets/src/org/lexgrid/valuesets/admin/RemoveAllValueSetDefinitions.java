package org.lexgrid.valuesets.admin;


import java.net.URI;
import java.util.List;

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

	public void run(String[] args) throws Exception {

		final LexEVSValueSetDefinitionServices vss = new LexEVSValueSetDefinitionServicesImpl();
		List<String> uris = vss.listValueSetDefinitionURIs();
		for (String urn : uris) {
			vss.removeValueSetDefinition(URI.create(urn));
			Util.displayMessage("ValueSetDefinition removed: " + urn);
		}
	}
}
