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

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.admin.Util;
import org.LexGrid.annotations.LgAdminFunction;
import org.LexGrid.codingSchemes.CodingScheme;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;

/**
 * Remove the resolvedValueSet
 * 
 * <pre>
 * Example: java org.lexgrid.valuesets.admin.RemoveResolvedValueSet
 *   -l, &lt;id&gt; List of coding scheme versions to match when removing the ResolvedValueSet. The
 *       format is "csuri1::version1, csuri2::version2"
 *   -f Force remove of active schemes    
 * 
 * Note: If the URN and version values are unspecified, a
 * list of available coding schemes will be presented for
 * user selection.
 * 
 * Example: java -Xmx512m -cp lgRuntime.jar
 *  org.lexgrid.valuesets.admin.RemoveResolvedValueSet
 *    -l &quot;urn:oid:11.11.0.1::version1, GM_URI::version2&quot; -f
 * </pre>
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 */
@LgAdminFunction
public class RemoveResolvedValueSet {

	public static void main(String[] args) {
		try {
			new RemoveResolvedValueSet().run(args);
		} catch (Exception e) {
			Util.displayAndLogError("REQUEST FAILED !!!", e);
		}
	}

	public RemoveResolvedValueSet() {
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
		} catch (ParseException e) {
			Util.displayCommandOptions(
					"RemoveResolvedValueSet",
					options,
					"RemoveResolvedValueSet  -l \"CodingSchemeURI1::version1, CodingSchemeURI2::version2\" -f ",
					e);
			Util.displayMessage(Util.getPromptForSchemeHelp());
			return;
		}

		// Interpret provided values ...
		String csList = cl.getOptionValue("l");
		boolean force = cl.hasOption("f");

		AbsoluteCodingSchemeVersionReferenceList acsvl = getCodingSchemeVersions(csList);
		remove(acsvl, force);

	}

	public AbsoluteCodingSchemeVersionReferenceList getCodingSchemeVersions(
			String csv_list) {
		AbsoluteCodingSchemeVersionReferenceList acsvl = new AbsoluteCodingSchemeVersionReferenceList();
		if (csv_list != null) {
			List<String> codingSchemeList = Arrays.asList(csv_list.split(","));
			for (String codingSchemeWithVersion : codingSchemeList) {
				AbsoluteCodingSchemeVersionReference ref;
				String[] pair = codingSchemeWithVersion.split("::");
				if (pair.length == 2) {
					//AbsoluteCodingSchemeVersionReference ref = 
				    //        ServiceUtility.getAbsoluteCodingSchemeVersionReference(codingSchemeName, tagOrVersion, true);
					ref = Constructors
							.createAbsoluteCodingSchemeVersionReference(
									pair[0], pair[1]);
				} else {
					ref = Constructors
							.createAbsoluteCodingSchemeVersionReference(
									codingSchemeWithVersion, null);
				}
				acsvl.addAbsoluteCodingSchemeVersionReference(ref);
			}

		}
		return acsvl;
	}

	public void remove(
			AbsoluteCodingSchemeVersionReferenceList csVersionList,
			boolean force) throws Exception {
		// ResolvedValueSetDefinitionLoader loader =
		// (ResolvedValueSetDefinitionLoader)LexBIGServiceImpl.defaultInstance().getServiceManager(null).getLoader(ResolvedValueSetDefinitionLoader.NAME);
		LexEVSResolvedValueSetService resolved_vs_service = new LexEVSResolvedValueSetServiceImpl();
		for (CodingScheme cs : resolved_vs_service.listAllResolvedValueSets()) {
			AbsoluteCodingSchemeVersionReferenceList acsvl = resolved_vs_service
					.getListOfCodingSchemeVersionsUsedInResolution(cs);
			if (matches(csVersionList, acsvl)) {
				AbsoluteCodingSchemeVersionReference remove_acst = Constructors
						.createAbsoluteCodingSchemeVersionReference(
								cs.getCodingSchemeURI(),
								cs.getRepresentsVersion());
				remove(remove_acst, force);
			}
		}

	}

	boolean matches(AbsoluteCodingSchemeVersionReferenceList containedList,
			AbsoluteCodingSchemeVersionReferenceList sourceList) {
		boolean matched = true;
		for (AbsoluteCodingSchemeVersionReference contained_acsr : containedList
				.getAbsoluteCodingSchemeVersionReference()) {
			boolean found = false;
			for (AbsoluteCodingSchemeVersionReference source_acsr : sourceList
					.getAbsoluteCodingSchemeVersionReference()) {
				if (contained_acsr.getCodingSchemeURN().equalsIgnoreCase(
						source_acsr.getCodingSchemeURN())
						&& contained_acsr.getCodingSchemeVersion()
								.equalsIgnoreCase(
										source_acsr.getCodingSchemeVersion())) {
					found = true;
				}
			}
			if (found == false) {
				matched = false;
			}
		}
		return matched;
	}

	boolean getCodingSchemeStatus(String urn, String ver)
			throws LBInvocationException {
		CodingSchemeSummary css = null;
		boolean isActive = false;
		// Find in list of registered vocabularies ...
		LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
		if (urn != null && ver != null) {
			urn = urn.trim();
			ver = ver.trim();
			Enumeration<? extends CodingSchemeRendering> schemes = lbs
					.getSupportedCodingSchemes()
					.enumerateCodingSchemeRendering();
			while (schemes.hasMoreElements() && css == null) {
				CodingSchemeRendering rendering = schemes.nextElement();
				CodingSchemeSummary summary = rendering
						.getCodingSchemeSummary();
				if (urn.equalsIgnoreCase(summary.getCodingSchemeURI())
						&& ver.equalsIgnoreCase(summary.getRepresentsVersion())) {
					css = summary;
					isActive = rendering.getRenderingDetail()
							.getVersionStatus()
							.equals(CodingSchemeVersionStatus.ACTIVE);
				}
			}
		}
		return isActive;
	}

	void remove(AbsoluteCodingSchemeVersionReference acsvr, boolean force)
			throws Exception {
		// Continue and confirm the action (if not bypassed by force option)
		// ...
		Util.displayTaggedMessage("A matching resolved valueset coding scheme was found with urn: "+ acsvr.getCodingSchemeURN());
		LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance()
				.getServiceManager(null);
		boolean isActive = getCodingSchemeStatus(acsvr.getCodingSchemeURN(),
				acsvr.getCodingSchemeVersion());
		boolean confirmed = true;
		if (!force) {
			if (isActive) {
				Util.displayMessage("SCHEME IS ACTIVE! DEACTIVATE? ('Y' to confirm, any other key to cancel)");
				char choice = Util.getConsoleCharacter();
				if (confirmed = choice == 'Y' || choice == 'y')
					lbsm.deactivateCodingSchemeVersion(acsvr, null);
			}
			if (!isActive || confirmed) {
				Util.displayMessage("DELETE? ('Y' to confirm, any other key to cancel)");
				char choice = Util.getConsoleCharacter();
				confirmed = choice == 'Y' || choice == 'y';
			}
		} else
			lbsm.deactivateCodingSchemeVersion(acsvr, null);

		if (confirmed) {
			lbsm.removeCodingSchemeVersion(acsvr);
			Util.displayTaggedMessage("Resolved valueset coding scheme was removed.");
		} else {
			Util.displayTaggedMessage("Action cancelled by user");
		}
	}

	/**
	 * Return supported command options.
	 * 
	 * @return org.apache.commons.cli.Options
	 */
	private Options getCommandOptions() {
		Options options = new Options();
		Option o;



		o = new Option("l", "list", true, "List of coding schemes to use.");
		o.setArgName("id");
		o.setRequired(true);
		options.addOption(o);



		o = new Option("f", "force", false,
				"Force deactivation and removal without confirmation.");
		o.setRequired(false);
		options.addOption(o);

		return options;
	}

}
