
package edu.mayo.informatics.lexgrid.convert.directConversions.claml.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.claml.ClaML;
import org.LexGrid.LexBIG.claml.Fragment;
import org.LexGrid.LexBIG.claml.Include;
import org.LexGrid.LexBIG.claml.Label;
import org.LexGrid.LexBIG.claml.ListItem;
import org.LexGrid.LexBIG.claml.Para;
import org.LexGrid.LexBIG.claml.Reference;
import org.LexGrid.LexBIG.claml.Rubric;
import org.LexGrid.LexBIG.claml.RubricKind;
import org.LexGrid.LexBIG.claml.SubClass;
import org.LexGrid.LexBIG.claml.Table;
import org.LexGrid.LexBIG.claml.Term;
import org.LexGrid.LexBIG.claml.UsageKind;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.custom.relations.RelationsUtil;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;

import edu.mayo.informatics.lexgrid.convert.directConversions.claml.config.ClaMLConfig;

public class DefaultRubricProcessorImpl implements RubricProcessor{

	ClaML clamlXML_;
	CodingScheme schemeCS_;
	Entity conceptCS_;
	LgMessageDirectorIF messages_;
	ClaMLConfig config_;
	
	
	public DefaultRubricProcessorImpl(ClaML clamlXML, CodingScheme schemeCS, Entity conceptCS, ClaMLConfig config, LgMessageDirectorIF messages){
		this.clamlXML_ = clamlXML;
		this.schemeCS_ = schemeCS;
		this.conceptCS_ = conceptCS;
		this.messages_ = messages;
		this.config_ = config;
	}
	
	public Property processRubric(Rubric rubric) {
		Property cp = null;	
		
		//Get the Kind of the Rubric
		RubricKind kind = (RubricKind)rubric.getKind();
		String kindName = kind.getName();
		if(config_.getDefinitions().contains(kindName)) {
			cp = new Definition();	
		} else if(config_.getPresentations().contains(kindName)){
			Presentation presentation = new Presentation();
			//check if its the Preferred Presentation
			if(kindName.equals(config_.getPreferrredPresentation())){
				presentation.setIsPreferred(true);
			} else {
				presentation.setIsPreferred(false);
			}
			cp = presentation;
		} else {
			//if it can't find a specific Property to set it as,
			//set it as the generic Property
			cp = new Property();
		}
		
		cp.setPropertyId(rubric.getId());
		cp.setPropertyName(kind.getName());
		
		List<Label> labelList = rubric.getLabel();
		if(labelList.size() > 1){
			messages_.warn("Found more than one Label for a Rubric - " +
					"this loader only supports one Label per Rubric. " +
					"Any labels after the first one will be ignored.");
		}
		cp = processLabel(cp, labelList.get(0));
		
		return cp;
	}
	
	private Property processLabel(Property cp, Label label){
		String description = "";
		
			List<Object>contentList = label.getContent();
			for(Object content : contentList){
				if(content instanceof String){
					description = description + (String)content;
				}
				if(content instanceof Para){
					Para para = (Para)content;
					List<Object>paraList = para.getContent();
					for(Object paraContent : paraList){
						if(paraContent instanceof String){
							description = description + (String)paraContent;
						}
						if(paraContent instanceof Reference){
							processReference(cp, (Reference)paraContent);
						}
						if(paraContent instanceof Term){
							processTerm((Term)paraContent);
						}
					}
				}
				if(content instanceof Fragment){
					Fragment fragment = (Fragment)content;
					List<Object> fragmentList = fragment.getContent();
					for(Object fragmentContent : fragmentList){
						if(fragmentContent instanceof Reference){
							processReference(cp, (Reference)fragmentContent);
						}
						if(fragmentContent instanceof Term){
							processTerm((Term)fragmentContent);
						}
						if(fragmentContent instanceof String){
							description = description + (String)fragmentContent;
						}
					}
				}
				if(content instanceof Reference){
					processReference(cp, (Reference)content);
				}
				if(content instanceof org.LexGrid.LexBIG.claml.List){
					org.LexGrid.LexBIG.claml.List list = (org.LexGrid.LexBIG.claml.List)content;
					List<ListItem> listItemList = list.getListItem();
					for(ListItem listItem : listItemList){
						List<Object> listItemContentList = listItem.getContent();
						if(listItemContentList instanceof Reference){
							processReference(cp, (Reference)listItemContentList);
						}
						if(listItemContentList instanceof Table){
							
						}
						if(listItemContentList instanceof org.LexGrid.LexBIG.claml.List){

						}
						if(listItemContentList instanceof Para){

						}	
						if(listItemContentList instanceof Term){
							processTerm((Term)listItemContentList);
						}	
						if(listItemContentList instanceof Include){
							
						}	
						
					}
				}
			}
			Text text = new Text();
			text.setContent(description);
			
			cp.setValue(text);
			cp.setLanguage("en");
		
		return cp;
	}
	private void processReference(Property property, Reference reference){
		String association = property.getPropertyName();

		try {
			List<AssociationPredicate> associations = RelationsUtil.
			resolveAssociationPredicates(this.schemeCS_, association);
			
			//If the Association isn't found as a supported association, warn and return.
			if(associations.size() == 0){
				messages_.debug("Can't process Association: " + association + ". No association" +
						" of that type was found. Concept reference is: " + this.conceptCS_.getEntityCode());
				return;
			}

			AssociationPredicate associationCS = (AssociationPredicate)associations.get(0);
			
			AssociationSource ai = new AssociationSource();
			ai.setSourceEntityCode(conceptCS_.getEntityCode());
			ai.setSourceEntityCodeNamespace(conceptCS_.getEntityCodeNamespace());
			ai = RelationsUtil.subsume(associationCS, ai);

			//If the Reference has a 'code' attribute, use that, it
			//is the best way to resolve the Referenced Code.
			String referenceCode = reference.getCode();
			
			ArrayList<String> targetCodes = new ArrayList<String>();
			
			//If the Reference Code indicates a range of codes, such
			//as J44.-, we want to include J44 and all of its subclasses.
			if(referenceCode != null){
				if(referenceCode.endsWith(".-")){
					targetCodes.addAll(
							this.getAllSubConceptsFromRangeTerm(referenceCode));
				} else {
					//if its just a normal code (Like J44), just add the code. 
					targetCodes.add(referenceCode);
				}
			}

			//Check the content of the Reference to look for codes
			String referenceContent = reference.getContent();

			if(referenceContent != null){
				if(referenceContent.endsWith(".-")){
					targetCodes.addAll(
							this.getAllSubConceptsFromRangeTerm(referenceContent));
				} else {
					//if its just a normal code (Like J44), just add the code.
					targetCodes.add(referenceContent);
				}
			}
			
			//loop through and create the associations
			for(String target : targetCodes){
				AssociationTarget at = new AssociationTarget();
				
				//Add associationQualifiers
				at = addAssociationQualifiers(at, reference);
				at.setTargetEntityCode(target);
				at.setTargetEntityCodeNamespace(conceptCS_.getEntityCodeNamespace());
				RelationsUtil.subsume(ai, at);
			}
		} catch (Exception e) {
			messages_.warn("Concept: " + conceptCS_.getEntityCode() + " - Association: " + association + " Error: " + e.getMessage());
		}
	}
	
	private AssociationTarget addAssociationQualifiers(AssociationTarget target, Reference reference){
		//Get any Association Qualifiers ('usage' attributes)
		//First the 'class' attribute
		String classQualifier = reference.getClazz();
		if(classQualifier != null){
			AssociationQualification qual = new AssociationQualification();
			qual.setAssociationQualifier(config_.getClassAssociationQualifier());
			
			Text text = new Text();
			text.setContent(classQualifier);
			qual.setQualifierText(text);
			target.addAssociationQualification(qual);
		}
		
		//Now check the 'usage' attribute
		UsageKind usageQualifier = (UsageKind)reference.getUsage();
		if(usageQualifier != null){
			AssociationQualification qual = new AssociationQualification();
			qual.setAssociationQualifier(config_.getUsageAssociationQualifier());
			
			Text text = new Text();
			text.setContent(usageQualifier.getName());
			qual.setQualifierText(text);
			target.addAssociationQualification(qual);
		}
		
		return target;
	}
	
	private void processTerm(Term term){
		
	}
	
	/**
	 * Gets all the SubConcepts given a '.-" range String.
	 * 
	 * NOTE: This does not return the base concept - for example,
	 * in '75.-', code '75' is not returned, but '75.1', '75.2' is.
	 * @param range
	 * @return
	 */
	private ArrayList<String> getAllSubConceptsFromRangeTerm(String range){
		ArrayList<String> returnCodes = new ArrayList<String>();
		
		//remove the ".-" characters
		String searchCode = range.replace(".-", "");
		
		//Search through the ClaML file to find the subclass
		//to add as associations
		//:TODO find a better way to do this
		List<org.LexGrid.LexBIG.claml.Class> classes = clamlXML_.getClazz();
		for(org.LexGrid.LexBIG.claml.Class clamlClass : classes){
			String code = clamlClass.getCode();
			if(code.equals(searchCode)){
				//Just add the sub classes, not the parrent class
				List<SubClass> subClasses = clamlClass.getSubClass();
				for(SubClass subClass : subClasses){
					returnCodes.add(subClass.getCode());
				}
				//we've found what we need -- return.
				return returnCodes;
			}
		}
		//return the empty list if nothing is found
		return returnCodes;
	}
	

}