/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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
		Util.displayTaggedMessage("Resolved valueset [URN=" + acsvr.getCodingSchemeURN() + ", Version="
				+ acsvr.getCodingSchemeVersion() + "] was removed.");
	}

}
