
package org.LexGrid.util;

import java.lang.reflect.Method;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;

/**
 * The Class PrintUtility.
 * 
 * @author <a href="https://cabig-kc.nci.nih.gov/Vocab/KC/index.php/LexBig_and_LexEVS">The LexEVS Team</a>
 */
public class PrintUtility {
	
	/**
	 * Prints the.
	 * 
	 * @param assocs the assocs
	 */
	public static void print(AssociationList assocs){
		print(assocs, 0);
	}

	/**
	 * Prints the.
	 * 
	 * @param assocs the assocs
	 * @param depth the depth
	 */
	public static void print(AssociationList assocs, int depth){
		for(Association assoc : assocs.getAssociation()){
			System.out.println(buildPrefix(depth) + "Association: " + assoc.getAssociationName() + " Container: " + assoc.getRelationsContainerName());
			for(AssociatedConcept concept : assoc.getAssociatedConcepts().getAssociatedConcept()){
				print(concept, depth+1);
			}
		}	
	}
	
	/**
	 * Prints the.
	 * 
	 * @param ref the ref
	 */
	public static void print(ResolvedConceptReference ref){
		print(ref, 0);
	}
	
	public static void print(AssociatedConcept ref){
        print(ref, 0);
    }
	
	/**
	 * Prints the.
	 * 
	 * @param ref the ref
	 * @param depth the depth
	 */
	private static void print(AssociatedConcept ref, int depth){
		String description;
		if(ref.getEntityDescription() == null) {
			description = "NOT AVAILABLE";
		} else {
			description = ref.getEntityDescription().getContent();
		}
		System.out.println(buildPrefix(depth) + "Code: " + ref.getCode() + ", Description: " + description + " Hash: " + ref.hashCode());
		print(ref.getAssociationQualifiers(), "Qualifiers", depth + 1);
		
		if(ref.getSourceOf() != null){
			print(ref.getSourceOf(), depth+1);
		}
		if(ref.getTargetOf() != null){
			print(ref.getTargetOf(), depth+1);
		}
	}
	
	   private static void print(ResolvedConceptReference ref, int depth){
	        String description;
	        if(ref.getEntityDescription() == null) {
	            description = "NOT AVAILABLE";
	        } else {
	            description = ref.getEntityDescription().getContent();
	        }
	        System.out.println(buildPrefix(depth) + "Code: " + ref.getCode() + ", Description: " + description + " Hash: " + ref.hashCode());

	        if(ref.getSourceOf() != null){
	            print(ref.getSourceOf(), depth+1);
	        }
	        if(ref.getTargetOf() != null){
	            print(ref.getTargetOf(), depth+1);
	        }
	    }
	
	private static void print(NameAndValueList nameAndValueList, String label, int depth){
	    if(nameAndValueList != null) {
	        System.out.println(buildPrefix(depth, false) + label + ":");
	        for(NameAndValue nv : nameAndValueList.getNameAndValue()) {
	            System.out.println(buildPrefix(depth + 1, false) + "/ Name: " + nv.getName());
	            System.out.println(buildPrefix(depth + 1, false) + "\\ Value: " + nv.getContent());
	        }
	    }  
	}
	
	/**
	 * Prints the.
	 * 
	 * @param list the list
	 */
	public static void print(ResolvedConceptReferenceList list){
		for(ResolvedConceptReference ref : list.getResolvedConceptReference()){
			print(ref);
		}
	}
	
	/**
	 * Prints the.
	 * 
	 * @param summary the summary
	 */
	public static void print(CodingSchemeSummary summary){
		System.out.println("CodingScheme: " + summary.getLocalName());
		System.out.println(" - Formal Name: " + summary.getFormalName());
		System.out.println(" - Version: " + summary.getRepresentsVersion());
	}
	
	/**
	 * Prints the.
	 * 
	 * @param csrl the csrl
	 */
	public static void print(CodingSchemeRenderingList csrl){
		for(CodingSchemeRendering rendering : csrl.getCodingSchemeRendering()){
			print(rendering.getCodingSchemeSummary());
		}
	}
	
	/**
	 * Prints the.
	 * 
	 * @param ed the ed
	 */
	public static void print(ExtensionDescription ed){
		System.out.println("Extension Name: " + ed.getName());
		System.out.println(" - Description: " + ed.getDescription());
		System.out.println(" - Extension Base Class: " + ed.getExtensionBaseClass());
		System.out.println(" - Extension Implementing Class: " + ed.getExtensionClass());
	}
	
	/**
	 * Prints the.
	 * 
	 * @param edl the edl
	 */
	public static void print(ExtensionDescriptionList edl){
		for(ExtensionDescription description : edl.getExtensionDescription()){
			print(description);
		}
	}
	
	/**
	 * Prints the.
	 * 
	 * @param cs the cs
	 */
	public static void print(CodingScheme cs){
		System.out.println("CodingScheme: " + cs.getLocalName());
		System.out.println(" - Formal Name: " + cs.getFormalName());
		System.out.println(" - Version: " + cs.getRepresentsVersion());
		
		if(cs.getEntities() != null) {
			System.out.println("Entities:");
			for(Entity entity : cs.getEntities().getEntity()) {
				print(entity);
			}
		}
	}
	
	/**
	 * Prints the.
	 * 
	 * @param entity the entity
	 */
	public static void print(Entity entity){
		print(entity, 0);
	}
	
	private static void print(Entity entity, int depth){
        System.out.println(buildPrefix(depth) + "Code: " + entity.getEntityCode());
        
        if(entity.getEntityDescription() != null) {
            System.out.println(buildPrefix(depth) + " - Description: " + entity.getEntityDescription().getContent());
        }
    }
	
	/**
	 * Prints the.
	 * 
	 * @param obj the obj
	 */
	public static void print(Object obj){
		System.out.println("Result is: " + obj.toString());
	}
	
	public static void print(ResolvedConceptReferencesIterator itr){
	    try {
            while(itr.hasNext()){
                print(itr.next());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
	
	/**
	 * Prints the.
	 * 
	 * @param cns the cns
	 */
	public static void print(CodedNodeSet cns){
		try {
		    print(cns.resolve(null,null,null,null,false));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void print(CodedNodeGraph cng) {
        print(cng, null, null);
    }
	
	/**
	 * Prints the.
	 * 
	 * @param cng the cng
	 */
	public static void print(CodedNodeGraph cng, ConceptReference focus, SortOptionList sorts) {

		ResolvedConceptReferenceList rcrl;
		System.out.println("-----------------");
		System.out.println("Resolving Forward");
		System.out.println("-----------------");
		try {
			rcrl = cng.resolveAsList(focus, true, false, -1, -1, null, null, sorts,
					-1);
			ResolvedConceptReference[] rcrArray = rcrl
			.getResolvedConceptReference();
			for (ResolvedConceptReference rcr : rcrArray) {
				print(rcr);
			}

			System.out.println("------------------");
			System.out.println("Resolving Backward");
			System.out.println("------------------");
				rcrl = cng.resolveAsList(focus, false, true, -1, -1, null, null, sorts,
						-1);
				rcrArray = rcrl
				.getResolvedConceptReference();
				for (ResolvedConceptReference rcr : rcrArray) {
					print(rcr);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
			
		}
	
	/**
	 * Prints the.
	 * 
	 * @param lbsm the lbsm
	 */
	public static void print(LexBIGServiceConvenienceMethods lbsm) {

		Method[] methods = lbsm.getClass().getDeclaredMethods();
		for (Method m : methods) {
			System.out.println(m.getName());
		}
	}
	
	/**
	 * Prints the.
	 * 
	 * @param lbsm the lbsm
	 */
	public static void print(LexBIGServiceManager lbsm) {

		Method[] methods = lbsm.getClass().getDeclaredMethods();
		for (Method m : methods) {
			System.out.println(m.getName());
		}
	}
	
	/**
	 * Builds the prefix.
	 * 
	 * @param depth the depth
	 * 
	 * @return the string
	 */
	private static String buildPrefix(int depth){
		return buildPrefix(depth, true);
	}
	
	private static String buildPrefix(int depth, boolean buildArrows){
        String prefix = "";
        for(int i=0;i<depth;i++){
            String padding;
            if(buildArrows) {
                padding = " -> ";
            } else {
                padding = "    ";
            }
            
            prefix = prefix + padding;
        }
        return prefix;
    }
}