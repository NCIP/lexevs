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
package org.LexGrid.LexBIG.gui.codeSet;

import java.util.ArrayList;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.gui.restrictions.Anonymous;
import org.LexGrid.LexBIG.gui.restrictions.HavingProperties;
import org.LexGrid.LexBIG.gui.restrictions.MatchingCode;
import org.LexGrid.LexBIG.gui.restrictions.MatchingDesignation;
import org.LexGrid.LexBIG.gui.restrictions.MatchingProperties;
import org.LexGrid.LexBIG.gui.restrictions.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Holder class for constructing coded node sets. Stores all of the options that
 * the user chooses, and then "plays" all of the choices into the LexBIG API.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CodedNodeSet extends CodeSet {
	private static Logger log = LogManager.getLogger("LB_GUI_LOGGER");

	public CodedNodeSet(CodingSchemeRendering csr) {
		super();
		this.csr = csr;
	}

	public CodedNodeSet(CodedNodeSet a, CodedNodeSet b, int operation) {
		super();
		this.operation_ = new Operation(a, b, operation);
	}

	public org.LexGrid.LexBIG.LexBIGService.CodedNodeSet getRealCodedNodeSet(
			LexBIGService lbs) throws LBException {
		if (this.csr != null) {
			// build the real codedNodeSet
			org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns =
				lbs.getNodeSet(
			        csr.getCodingSchemeSummary().getCodingSchemeURI(),
					Constructors.createCodingSchemeVersionOrTagFromVersion(
					    csr.getCodingSchemeSummary().getRepresentsVersion()),
					null);
			applyRestrictions(cns);

			// return it.
			return cns;
		} else {
			org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns;
			// must be an operation_
			if (this.operation_.op == UNION) {
				cns = ((CodedNodeSet) this.operation_.a).getRealCodedNodeSet(
						lbs).union(
						((CodedNodeSet) this.operation_.b)
								.getRealCodedNodeSet(lbs));
			} else if (this.operation_.op == INTERSECTION) {
				cns = ((CodedNodeSet) this.operation_.a).getRealCodedNodeSet(
						lbs).intersect(
						((CodedNodeSet) this.operation_.b)
								.getRealCodedNodeSet(lbs));
			} else if (this.operation_.op == DIFFERENCE) {
				cns = ((CodedNodeSet) this.operation_.a).getRealCodedNodeSet(
						lbs).difference(
						((CodedNodeSet) this.operation_.b)
								.getRealCodedNodeSet(lbs));
			} else {
				log.error("Invalid operation in the CodedNodeSet");
				return null;
			}
			applyRestrictions(cns);
			return cns;
		}

	}

	@Override
	public String toString() {
		if (this.csr != null) {
			return id + " (CS) - "
					+ csr.getCodingSchemeSummary().getLocalName() + " "
					+ csr.getCodingSchemeSummary().getRepresentsVersion();
		} else {
			return this.id + " (CS) - " + this.operation_.a.id + " "
					+ getNameForConstant(this.operation_.op) + " "
					+ this.operation_.b.id;
		}
	}

	public String getFullDescription() {
		if (this.csr != null) {
			return "Coded Node Set " + id + " - "
					+ csr.getCodingSchemeSummary().getLocalName() + " "
					+ csr.getCodingSchemeSummary().getRepresentsVersion();
		} else {
			return "Coded Node Set " + this.id
					+ " Composed of Coded Node Sets " + this.operation_.a.id
					+ " " + getNameForConstant(this.operation_.op) + " "
					+ this.operation_.b.id;
		}
	}

	private void applyRestrictions(
			org.LexGrid.LexBIG.LexBIGService.CodedNodeSet cns)
			throws LBInvocationException, LBParameterException {
		// play in all of the restrictions
		for (int i = 0; i < this.restrictions.size(); i++) {
			if (this.restrictions.get(i) instanceof MatchingDesignation) {
				MatchingDesignation md = (MatchingDesignation) this.restrictions
						.get(i);
				cns.restrictToMatchingDesignations(md.matchText,
						md.searchDesignationOption, md.matchAlgorithm,
						md.language);
			} else if (this.restrictions.get(i) instanceof MatchingProperties) {
				MatchingProperties mp = (MatchingProperties) this.restrictions
						.get(i);
				cns.restrictToMatchingProperties(Constructors
						.createLocalNameList(mp.properties
								.toArray(new String[mp.properties.size()])),
						mp.propertyTypes
								.toArray(new PropertyType[mp.propertyTypes
										.size()]),
						Constructors.createLocalNameList(mp.sources
								.toArray(new String[mp.sources.size()])),
						Constructors.createLocalNameList(mp.usageContexts
								.toArray(new String[mp.usageContexts.size()])),
						Constructors
								.createNameAndValueList(mp.propertyQualifier,
										mp.propertyQualifierValue),
						mp.matchText, mp.matchAlgorithm, mp.language);
			} else if (this.restrictions.get(i) instanceof MatchingCode) {
				MatchingCode mc = (MatchingCode) this.restrictions.get(i);

				cns
						.restrictToCodes(Constructors
								.createConceptReferenceList(
										stringToArray(mc.codes), this.csr
												.getCodingSchemeSummary()
												.getCodingSchemeURI()));
			} else if (this.restrictions.get(i) instanceof Status) {
				Status s = (Status) this.restrictions.get(i);

				cns.restrictToStatus(s.activeOption,
						stringToArray(s.conceptStatus));
			} else if (this.restrictions.get(i) instanceof Anonymous) {
			    Anonymous a = (Anonymous) this.restrictions.get(i);

                cns.restrictToAnonymous(a.anonymousOption);
            }

			else if (this.restrictions.get(i) instanceof HavingProperties) {
				HavingProperties hp = (HavingProperties) this.restrictions
						.get(i);
				cns.restrictToProperties(Constructors
						.createLocalNameList(hp.properties
								.toArray(new String[hp.properties.size()])),
						hp.propertyTypes
								.toArray(new PropertyType[hp.propertyTypes
										.size()]),
						Constructors.createLocalNameList(hp.sources
								.toArray(new String[hp.sources.size()])),
						Constructors.createLocalNameList(hp.usageContexts
								.toArray(new String[hp.usageContexts.size()])),
						Constructors
								.createNameAndValueList(hp.propertyQualifier,
										hp.propertyQualifierValue));
			}
		}
	}

	private String[] stringToArray(String in) {
		ArrayList<String> items = new ArrayList<String>();
		int startPos = 0;
		int breakPos = 0;

		breakPos = in.indexOf(",", startPos);
		if (breakPos == -1) {
			// no commas
			if (in.trim().length() > 0) {
				items.add(in.trim());
			}
		}

		while (breakPos != -1) {
			items.add(in.substring(startPos, breakPos).trim());
			startPos = breakPos + 1;
			if (startPos > in.length()) {
				break;
			}
			breakPos = in.indexOf(",", startPos);
		}
		if (startPos < in.length()) {
			items.add(in.substring(startPos, in.length()).trim());
		}

		return items.toArray(new String[items.size()]);
	}
}