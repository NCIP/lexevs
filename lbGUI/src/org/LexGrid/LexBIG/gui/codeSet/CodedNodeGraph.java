
package org.LexGrid.LexBIG.gui.codeSet;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.gui.restrictions.Association;
import org.LexGrid.LexBIG.gui.restrictions.CodeSystem;
import org.LexGrid.LexBIG.gui.restrictions.DirectionalName;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Holder class for constructing graphs.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CodedNodeGraph extends CodeSet {
	private static Logger log = Logger.getLogger("LB_GUI_LOGGER");

	public String relationName = "";
	public boolean resolveForward = true;
	public boolean resolveBackward;
	public String[] sortOptions_;
	public int resolveDepth = 1;
	public int maxToReturn = 500;
	public ConceptReference graphFocus;

	public CodedNodeGraph(CodingSchemeRendering csr) {
		super();
		this.csr = csr;
	}

	public CodedNodeGraph(CodedNodeGraph a, CodedNodeGraph b, int operation) {
		super();
		this.operation_ = new Operation(a, b, operation);
	}

	public CodedNodeGraph(CodedNodeGraph a, CodedNodeSet b, int operation) {
		super();
		this.operation_ = new Operation(a, b, operation);
	}

	@Override
	public String toString() {
		if (this.csr != null) {
			return id + " (CG) - "
					+ csr.getCodingSchemeSummary().getLocalName() + " "
					+ csr.getCodingSchemeSummary().getRepresentsVersion();
		} else {
			return this.id + " (CG) - " + this.operation_.a.id + " "
					+ getNameForConstant(this.operation_.op) + " "
					+ this.operation_.b.id;
		}
	}

	public String getFullDescription() {
		if (this.csr != null) {
			return "Coded Node Graph " + id + " - "
					+ csr.getCodingSchemeSummary().getLocalName() + " "
					+ csr.getCodingSchemeSummary().getRepresentsVersion();
		} else {
			return "Coded Node Graph " + this.id
					+ " Composed of Coded Node Graphss " + this.operation_.a.id
					+ " " + getNameForConstant(this.operation_.op) + " "
					+ this.operation_.b.id;
		}
	}

	public org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph getRealCodedNodeGraph(
			LexBIGService lbs) throws LBException {
		if (this.csr != null) {
			// build the real codedNodeSet
			org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph cng = lbs
					.getNodeGraph(csr.getCodingSchemeSummary()
							.getCodingSchemeURI(), Constructors
							.createCodingSchemeVersionOrTagFromVersion(csr
									.getCodingSchemeSummary()
									.getRepresentsVersion()), relationName);
			applyRestrictions(cng);

			// return it.
			return cng;
		} else {
			org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph cng;
			// must be an operation_
			if (this.operation_.op == UNION) {
				cng = ((CodedNodeGraph) this.operation_.a)
						.getRealCodedNodeGraph(lbs).union(
								((CodedNodeGraph) this.operation_.b)
										.getRealCodedNodeGraph(lbs));
			} else if (this.operation_.op == INTERSECTION) {
				cng = ((CodedNodeGraph) this.operation_.a)
						.getRealCodedNodeGraph(lbs).intersect(
								((CodedNodeGraph) this.operation_.b)
										.getRealCodedNodeGraph(lbs));
			} else if (this.operation_.op == RESTRICT_TO_CODES) {
				cng = ((CodedNodeGraph) this.operation_.a)
						.getRealCodedNodeGraph(lbs).restrictToCodes(
								((CodedNodeSet) this.operation_.b)
										.getRealCodedNodeSet(lbs));
			} else if (this.operation_.op == RESTRICT_TO_SOURCE_CODES) {
				cng = ((CodedNodeGraph) this.operation_.a)
						.getRealCodedNodeGraph(lbs).restrictToSourceCodes(
								((CodedNodeSet) this.operation_.b)
										.getRealCodedNodeSet(lbs));
			} else if (this.operation_.op == RESTRICT_TO_TARGET_CODES) {
				cng = ((CodedNodeGraph) this.operation_.a)
						.getRealCodedNodeGraph(lbs).restrictToTargetCodes(
								((CodedNodeSet) this.operation_.b)
										.getRealCodedNodeSet(lbs));
			} else {
				log.error("Invalid operation in the CodedNodeGraph");
				return null;
			}
			applyRestrictions(cng);
			return cng;
		}

	}

	private void applyRestrictions(
			org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph cng)
			throws LBInvocationException, LBParameterException {
		// play in all of the restrictions
		for (int i = 0; i < this.restrictions.size(); i++) {
			if (this.restrictions.get(i) instanceof Association) {
				Association a = (Association) this.restrictions.get(i);

				NameAndValueList assns = new NameAndValueList();
				NameAndValueList quals = new NameAndValueList();

				for (int j = 0; j < a.associations.length; j++) {
					assns.addNameAndValue(Constructors.createNameAndValue(
							a.associations[j], null));
				}

				// put in the qualifiers
				String qualVal = StringUtils
						.isNotBlank(a.associationQualifierValue) ? a.associationQualifierValue
						: null;

				for (int j = 0; j < a.associationQualifiers.length; j++) {
					quals.addNameAndValue(Constructors.createNameAndValue(
							a.associationQualifiers[j], qualVal));
				}

				cng.restrictToAssociations(assns, quals);
			} else if (this.restrictions.get(i) instanceof DirectionalName) {
				DirectionalName d = (DirectionalName) this.restrictions.get(i);

				NameAndValueList dns = new NameAndValueList();
				NameAndValueList quals = new NameAndValueList();

				for (int j = 0; j < d.directionalNames.length; j++) {
					dns.addNameAndValue(Constructors.createNameAndValue(
							d.directionalNames[j], null));
				}

				// put in the qualifiers
				String qualVal = StringUtils
						.isNotBlank(d.associationQualifierValue) ? d.associationQualifierValue
						: null;

				for (int j = 0; j < d.associationQualifiers.length; j++) {
					quals.addNameAndValue(Constructors.createNameAndValue(
							d.associationQualifiers[j], qualVal));
				}

				cng.restrictToDirectionalNames(dns, quals);
			} else if (this.restrictions.get(i) instanceof CodeSystem) {
				CodeSystem cs = (CodeSystem) this.restrictions.get(i);

				if (cs.type == CodeSystem.CODE_SYSTEM) {
					cng.restrictToCodeSystem(cs.codeSystem);
				} else if (cs.type == CodeSystem.SOURCE_CODE_SYSTEM) {
					cng.restrictToSourceCodeSystem(cs.codeSystem);
				} else if (cs.type == CodeSystem.TARGET_CODE_SYSTEM) {
					cng.restrictToTargetCodeSystem(cs.codeSystem);
				}
			}

		}
	}
}