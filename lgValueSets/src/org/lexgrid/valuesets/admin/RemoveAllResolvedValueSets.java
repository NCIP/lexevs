
package org.lexgrid.valuesets.admin;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.admin.Util;
import org.LexGrid.annotations.LgAdminFunction;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;

@LgAdminFunction
public class RemoveAllResolvedValueSets {

	public static void main(String[] args) {
		try {
			new RemoveAllResolvedValueSets().run(args);
		} catch (Exception e) {
			Util.displayAndLogError("REQUEST FAILED !!!", e);
		}
	}

	public RemoveAllResolvedValueSets() {
		super();
	}

/**
	 * Primary entry point for the program.
	 * 
	 * @throws Exception
	 */
public void run(String[] args) throws Exception {
		
		Util.displayMessage("REMOVING ALL RESOLVED VALUE SETS. "
				+ "DO YOU REALLY WANT TO DO THIS? ('Y' to confirm, any other key to cancel)");
		char choice = Util.getConsoleCharacter();
		if (choice == 'Y' || choice == 'y'){
		LexEVSResolvedValueSetService resolved_vs_service = new LexEVSResolvedValueSetServiceImpl();
		for (CodingScheme cs : resolved_vs_service.listAllResolvedValueSets()) {
			AbsoluteCodingSchemeVersionReference remove_acst = Constructors
					.createAbsoluteCodingSchemeVersionReference(cs.getCodingSchemeURI(), cs.getRepresentsVersion());
			remove(remove_acst, true);
		}
		}
	}

	public void remove(AbsoluteCodingSchemeVersionReference acsvr, boolean force) throws Exception {
		LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);
		lbsm.deactivateCodingSchemeVersion(acsvr, null);
		lbsm.removeCodingSchemeVersion(acsvr);
		Util.displayAndLogMessage("Resolved valueset [URN=" + acsvr.getCodingSchemeURN() + ", Version="
				+ acsvr.getCodingSchemeVersion() + "] was removed.");
	}

}