
package org.LexGrid.LexBIG.gui.codeSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.gui.restrictions.Restriction;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedAssociationQualifier;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedContext;
import org.LexGrid.naming.SupportedLanguage;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.SupportedPropertyQualifier;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.relations.Relations;

/**
 * Parent class for CodedNodeSets and CodedNodeGraphs.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public abstract class CodeSet {
	public static int curID = 0;

	public final static int UNION = 1;
	public final static int INTERSECTION = 2;
	public final static int DIFFERENCE = 3;
	public final static int RESTRICT_TO_CODES = 4;
	public final static int RESTRICT_TO_SOURCE_CODES = 5;
	public final static int RESTRICT_TO_TARGET_CODES = 6;

	public CodingSchemeRendering csr;
	public int id;
	public Operation operation_;

	public String[] sortOptions;

	public ArrayList<Restriction> restrictions;

	private CodingScheme codingScheme_; // coding scheme is cached here for use
										// as necessary.

	public CodeSet() {
		this.id = curID++;
		this.restrictions = new ArrayList<Restriction>();
	}

	public class Operation {
		CodeSet a;
		CodeSet b;
		int op;

		public Operation(CodeSet a, CodeSet b, int op) {
			this.a = a;
			this.b = b;
			this.op = op;
		}
	}

	public SortOptionList getSortOptions() {
		SortOptionList sol = new SortOptionList();
		if (sortOptions != null) {
			for (int i = 0; i < sortOptions.length; i++) {
				SortOption so = new SortOption();
				int j = sortOptions[i].indexOf(" -");

				so.setExtensionName(sortOptions[i].substring(0, j));
				so.setAscending(sortOptions[i].subSequence(j,
						sortOptions[i].length()).equals(" - Ascending"));
				sol.addEntry(so);
			}
		}

		return sol;
	}

	public String getNameForConstant(int operation) {
		if (operation == UNION) {
			return "union";
		} else if (operation == INTERSECTION) {
			return "intersection";
		} else if (operation == DIFFERENCE) {
			return "difference";
		} else if (operation == RESTRICT_TO_CODES) {
			return "restrict to codes";
		} else if (operation == RESTRICT_TO_SOURCE_CODES) {
			return "restrict to source codes";
		} else if (operation == RESTRICT_TO_TARGET_CODES) {
			return "restrict to target codes";
		} else {
			return null;
		}
	}

	public String[] getSupportedLanguages(LexBIGService lbs) throws LBException {
		if (csr != null) {
			SupportedLanguage[] sl = getCodingScheme(lbs).getMappings()
					.getSupportedLanguage();
			String[] result = new String[sl.length];
			for (int i = 0; i < result.length; i++) {
				result[i] = sl[i].getLocalId();
			}
			return sort(result);
		} else {
			// operation_ - need to merge the results.
			String[] temp = this.operation_.a.getSupportedLanguages(lbs);
			HashSet<String> set = new HashSet<String>();
			for (int i = 0; i < temp.length; i++) {
				set.add(temp[i]);
			}
			temp = this.operation_.b.getSupportedLanguages(lbs);
			for (int i = 0; i < temp.length; i++) {
				set.add(temp[i]);
			}
			return sort(set.toArray(new String[set.size()]));

		}
	}

	public String[] getSupportedAssociationQualifiers(LexBIGService lbs)
			throws LBException {
		if (csr != null) {
			SupportedAssociationQualifier[] sl = getCodingScheme(lbs)
					.getMappings().getSupportedAssociationQualifier();
			String[] result = new String[sl.length];
			for (int i = 0; i < result.length; i++) {
				result[i] = sl[i].getLocalId();
			}
			return sort(result);
		} else {
			// operation_ - need to merge the results.
			String[] temp = this.operation_.a
					.getSupportedAssociationQualifiers(lbs);
			HashSet<String> set = new HashSet<String>();
			for (int i = 0; i < temp.length; i++) {
				set.add(temp[i]);
			}
			temp = this.operation_.b.getSupportedAssociationQualifiers(lbs);
			for (int i = 0; i < temp.length; i++) {
				set.add(temp[i]);
			}
			return sort(set.toArray(new String[set.size()]));

		}
	}

	public String[] getSupportedRelations(LexBIGService lbs) throws LBException {
		if (csr != null) {
			Relations[] sl = getCodingScheme(lbs).getRelations();
			String[] result = new String[sl.length + 1];
			result[0] = "";
			for (int i = 0; i < sl.length; i++) {
				result[i + 1] = sl[i].getContainerName();
			}
			return sort(result);
		} else {
			// not applicable on a merge of multiple graphs. only set in the
			// base graph.
			return new String[] {};
		}
	}

	public String[] getSupportedPropertyQualifiers(LexBIGService lbs)
			throws LBException {
		if (csr != null) {
			SupportedPropertyQualifier[] sl = getCodingScheme(lbs)
					.getMappings().getSupportedPropertyQualifier();

			String[] result = new String[sl.length + 1];
			for (int i = 0; i < sl.length; i++) {
				result[i + 1] = sl[i].getLocalId();
			}

			// put an empty at the beginning - used in the gui combo
			result[0] = "";

			return sort(result);
		} else {
			// operation_ - need to merge the results.
			String[] temp = this.operation_.a
					.getSupportedPropertyQualifiers(lbs);
			HashSet<String> set = new HashSet<String>();
			for (int i = 0; i < temp.length; i++) {
				set.add(temp[i]);
			}
			temp = this.operation_.b.getSupportedPropertyQualifiers(lbs);
			for (int i = 0; i < temp.length; i++) {
				set.add(temp[i]);
			}
			return sort(set.toArray(new String[set.size()]));

		}
	}

	public String[] getSupportedCodeSystems(LexBIGService lbs)
			throws LBException {
		if (csr != null) {
			SupportedCodingScheme[] sl = getCodingScheme(lbs).getMappings()
					.getSupportedCodingScheme();
			String[] result = new String[sl.length];
			for (int i = 0; i < result.length; i++) {
				result[i] = sl[i].getLocalId();
			}
			return sort(result);
		} else {
			// operation_ - need to merge the results.
			String[] temp = this.operation_.a.getSupportedCodeSystems(lbs);
			HashSet<String> set = new HashSet<String>();
			for (int i = 0; i < temp.length; i++) {
				set.add(temp[i]);
			}
			temp = this.operation_.b.getSupportedCodeSystems(lbs);
			for (int i = 0; i < temp.length; i++) {
				set.add(temp[i]);
			}
			return sort(set.toArray(new String[set.size()]));

		}
	}
	
	public String[] getSupportedNamespaces(LexBIGService lbs)
	throws LBException {
	    if (csr != null) {
	        SupportedNamespace[] sn = getCodingScheme(lbs).getMappings()
	        .getSupportedNamespace();
	        String[] result = new String[sn.length];
	        for (int i = 0; i < result.length; i++) {
	            result[i] = sn[i].getLocalId();
	        }
	        return sort(result);
	    } else {
	        // operation_ - need to merge the results.
	        String[] temp = this.operation_.a.getSupportedNamespaces(lbs);
	        HashSet<String> set = new HashSet<String>();
	        for (int i = 0; i < temp.length; i++) {
	            set.add(temp[i]);
	        }
	        temp = this.operation_.b.getSupportedNamespaces(lbs);
	        for (int i = 0; i < temp.length; i++) {
	            set.add(temp[i]);
	        }
	        return sort(set.toArray(new String[set.size()]));

	    }
	}

	public String[] getSupportedAssociations(LexBIGService lbs)
			throws LBException {
		if (csr != null) {
			SupportedAssociation[] sl = getCodingScheme(lbs).getMappings()
					.getSupportedAssociation();
			String[] result = new String[sl.length];
			for (int i = 0; i < result.length; i++) {
				result[i] = sl[i].getLocalId();
			}
			return sort(result);
		} else {
			// operation_ - need to merge the results.
			String[] temp = this.operation_.a.getSupportedAssociations(lbs);
			HashSet<String> set = new HashSet<String>();
			for (int i = 0; i < temp.length; i++) {
				set.add(temp[i]);
			}
			temp = this.operation_.b.getSupportedAssociations(lbs);
			for (int i = 0; i < temp.length; i++) {
				set.add(temp[i]);
			}
			return sort(set.toArray(new String[set.size()]));

		}
	}

	public String[] getSupportedProperties(LexBIGService lbs)
			throws LBException {
		if (csr != null) {
			SupportedProperty[] sl = getCodingScheme(lbs).getMappings()
					.getSupportedProperty();
			String[] result = new String[sl.length + 1];
			for (int i = 0; i < sl.length; i++) {
				result[i] = sl[i].getLocalId();
			}

			result[result.length - 1] = "conceptCode";

			return sort(result);
		} else {
			// operation_ - need to merge the results.
			String[] temp = this.operation_.a.getSupportedProperties(lbs);
			HashSet<String> set = new HashSet<String>();
			for (int i = 0; i < temp.length; i++) {
				set.add(temp[i]);
			}
			temp = this.operation_.b.getSupportedProperties(lbs);
			for (int i = 0; i < temp.length; i++) {
				set.add(temp[i]);
			}
			return sort(set.toArray(new String[set.size()]));

		}
	}

	public String[] getSupportedSources(LexBIGService lbs) throws LBException {
		if (csr != null) {
			SupportedSource[] sl = getCodingScheme(lbs).getMappings()
					.getSupportedSource();

			String[] result = new String[sl.length];
			for (int i = 0; i < sl.length; i++) {
				result[i] = sl[i].getLocalId();
			}

			return sort(result);
		} else {
			// operation_ - need to merge the results.
			String[] temp = this.operation_.a.getSupportedSources(lbs);
			HashSet<String> set = new HashSet<String>();
			for (int i = 0; i < temp.length; i++) {
				set.add(temp[i]);
			}
			temp = this.operation_.b.getSupportedSources(lbs);
			for (int i = 0; i < temp.length; i++) {
				set.add(temp[i]);
			}
			return sort(set.toArray(new String[set.size()]));

		}
	}

	public String[] getSupportedContexts(LexBIGService lbs) throws LBException {
		if (csr != null) {
			SupportedContext[] sl = getCodingScheme(lbs).getMappings()
					.getSupportedContext();

			String[] result = new String[sl.length];
			for (int i = 0; i < sl.length; i++) {
				result[i] = sl[i].getLocalId();
			}

			return sort(result);
		} else {
			// operation_ - need to merge the results.
			String[] temp = this.operation_.a.getSupportedContexts(lbs);
			HashSet<String> set = new HashSet<String>();
			for (int i = 0; i < temp.length; i++) {
				set.add(temp[i]);
			}
			temp = this.operation_.b.getSupportedContexts(lbs);
			for (int i = 0; i < temp.length; i++) {
				set.add(temp[i]);
			}
			return sort(set.toArray(new String[set.size()]));

		}
	}

	public String[] getAssociationForwardAndReverseNames(LexBIGService lbs)
			throws LBException {
	    if(this.csr != null) {
    		ConvenienceMethods lbcm = new ConvenienceMethods(lbs);
    		return lbcm.getAssociationForwardAndReverseNames(this.csr
    				.getCodingSchemeSummary().getCodingSchemeURI(), Constructors
    				.createCodingSchemeVersionOrTagFromVersion(this.csr
    						.getCodingSchemeSummary().getRepresentsVersion()));
	    } else {
	        // operation_ - need to merge the results.
            String[] temp = this.operation_.a.getAssociationForwardAndReverseNames(lbs);
            HashSet<String> set = new HashSet<String>();
            for (int i = 0; i < temp.length; i++) {
                set.add(temp[i]);
            }
            temp = this.operation_.b.getAssociationForwardAndReverseNames(lbs);
            for (int i = 0; i < temp.length; i++) {
                set.add(temp[i]);
            }
            return sort(set.toArray(new String[set.size()]));
	    }
	}

	private CodingScheme getCodingScheme(LexBIGService lbs) throws LBException {
		if (codingScheme_ == null) {
			codingScheme_ = lbs.resolveCodingScheme(this.csr
					.getCodingSchemeSummary().getCodingSchemeURI(),
					Constructors
							.createCodingSchemeVersionOrTagFromVersion(this.csr
									.getCodingSchemeSummary()
									.getRepresentsVersion()));
		}
		return codingScheme_;

	}

	private String[] sort(String[] array) {
		Arrays.sort(array, new StringComparator());
		return array;
	}

}