
package edu.mayo.informatics.lexgrid.convert.directConversions.claml;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.NameNotFoundException;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.LexBIG.claml.ClaML;
import org.LexGrid.LexBIG.claml.Meta;
import org.LexGrid.LexBIG.claml.ModifiedBy;
import org.LexGrid.LexBIG.claml.ModifierClass;
import org.LexGrid.LexBIG.claml.Rubric;
import org.LexGrid.LexBIG.claml.RubricKind;
import org.LexGrid.LexBIG.claml.RubricKinds;
import org.LexGrid.LexBIG.claml.SubClass;
import org.LexGrid.LexBIG.claml.SuperClass;
import org.LexGrid.LexBIG.claml.UsageKind;
import org.LexGrid.LexBIG.claml.UsageKinds;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.custom.relations.RelationsUtil;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedAssociationQualifier;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedHierarchy;
import org.LexGrid.naming.SupportedLanguage;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.apache.commons.lang.StringUtils;

import edu.mayo.informatics.lexgrid.convert.directConversions.claml.config.ClaMLConfig;
import edu.mayo.informatics.lexgrid.convert.directConversions.claml.interfaces.DefaultRubricProcessorImpl;
import edu.mayo.informatics.lexgrid.convert.directConversions.claml.interfaces.ModifierClassRubricProcessorImpl;
import edu.mayo.informatics.lexgrid.convert.directConversions.claml.interfaces.RubricProcessor;

public class ClaML2LG {

		private LgMessageDirectorIF messages_;
		private ClaMLConfig config_;
		ClaML clamlXML_;

		public CodingScheme convertClaMLToEMF(URI clamlXML_URI, ClaMLConfig config, LgMessageDirectorIF messages)
				throws Exception {
			messages_ = messages;
			config_ = config;
					
			ClaMLXMLReader reader = new ClaMLXMLReader();
			clamlXML_ = reader.readClaMLXML(clamlXML_URI, config);
			
			CodingScheme clamlCS = new CodingScheme();
			clamlCS.setEntities(new Entities());
			
			clamlCS = buildCodingScheme(clamlCS);
			clamlCS = buildMappings(clamlCS);
			clamlCS = setUpRelations(clamlCS);
			clamlCS = buildConcepts(clamlCS);
			clamlCS = processRoots(clamlCS);
			
		
			return clamlCS;
		}
		
		private CodingScheme processRoots(CodingScheme clamlEMF) {
    
            for(String root : ClaMLUtils.getHierarchyRoots(clamlXML_)){
            
                this.processAssociation(clamlEMF, this.config_.getSubclassAssoc(), ClaMLConstants.ROOT, root);
            }
            
            SupportedHierarchy shier = new SupportedHierarchy();
            shier.setLocalId(ClaMLConstants.HIERARCHY_ID);
            shier.setAssociationNames(Arrays.asList(this.config_.getSubclassAssoc()));
            shier.setIsForwardNavigable(true);
            shier.setRootCode(ClaMLConstants.ROOT);

            clamlEMF.getMappings().addSupportedHierarchy(shier);
            
            return clamlEMF;
        }
		
		private CodingScheme setUpRelations(CodingScheme clamlCS){
			Relations relations = new Relations();
			relations.setContainerName("relations");
			clamlCS.addRelations(relations);
			
			clamlCS = createAssociation(clamlCS, relations, config_.getSubclassAssoc(), config_.getSubclassAssocReverse());
			clamlCS = createAssociation(clamlCS, relations, config_.getSuperclassAssoc(), config_.getSuperclassAssocReverse());
			clamlCS = createAssociation(clamlCS, relations, config_.getSubcodesAssoc(), config_.getSubcodesAssocReverse());
			clamlCS = createAssociation(clamlCS, relations, config_.getIncludesAssoc(), config_.getIncludesAssocReverse());
			clamlCS = createAssociation(clamlCS, relations, config_.getExcludesAssoc(), config_.getExcludesAssocReverse());
			clamlCS = createAssociation(clamlCS, relations, config_.getModifiesAssoc(), config_.getModifiesAssocReverse());
			
			return clamlCS;
		}
		
		private CodingScheme createAssociation(CodingScheme clamlCS, Relations relations, String forwardName, String reverseName){
			//Create the Association
			AssociationPredicate assoc = new AssociationPredicate();
			AssociationEntity assocEn = new AssociationEntity();
			assoc.setAssociationName(forwardName);
			assocEn.setEntityCode(forwardName);
			assocEn.setForwardName(forwardName);
			assocEn.setReverseName(reverseName);
			assocEn.setIsTransitive(true);
			assocEn.addEntityType(EntityTypes.ASSOCIATION.toString());
//			assoc.setInverse(reverseName);
//			assoc.setIsReverseFunctional(true);
//			assoc.setIsSymmetric(true);
			
			clamlCS.getEntities().addEntity(assocEn);
			
			//Add it to the Supported Mappings
			SupportedAssociation sa = new SupportedAssociation();
			sa.setLocalId(forwardName);
			clamlCS.getMappings().addSupportedAssociation(sa);
			
			RelationsUtil.subsume(relations, assoc);
			messages_.info("Loading association: " + forwardName);
			
			return clamlCS;
		}
		
		
		private CodingScheme buildCodingScheme(CodingScheme clamlCS){
			//LexGrid CodingScheme mapped to ClaML 'name' -- <Title date="2008-01-05" name="ICD-10" version="2008"/>
			clamlCS.setCodingSchemeName(clamlXML_.getTitle().getName());
			
			//LexGrid LocalName mapped to ClaML 'name' -- <Title date="2008-01-05" name="ICD-10" version="2008"/>
			clamlCS.addLocalName(clamlXML_.getTitle().getName());
			
			//LexGrid RepresentsVersion mapped to ClaML 'version' -- <Title date="2008-01-05" name="ICD-10" version="2008"/>
			clamlCS.setRepresentsVersion(clamlXML_.getTitle().getVersion());
			
			//LexGrid setApproxNumConcepts mapped to ClaML number of <Class> elements
			clamlCS.setApproxNumConcepts(new Long(clamlXML_.getClazz().size()));
			
			//LexGrid RegisteredName mapped to ClaML 'uid -- <Identifier authority="WHO" uid="to be added later"/>
			clamlCS.setCodingSchemeURI(clamlXML_.getIdentifier().get(0).getUid());
		
			//No ClaML mapping found
			//TODO find where ClaML XML keeps copyright info
			Text text = new Text();
			clamlCS.setCopyright(text);
			
			//LexGrid RepresentsVersion mapped to ClaML <Meta> tag with name="lang -- <Meta name="lang" value="en"/>
			try {
				clamlCS.setDefaultLanguage(
						getMetaAttributeValueFromName(clamlXML_.getMeta(), config_.getMetaLangName()));
			} catch (NameNotFoundException e) {
				//couldn't find the language in the ClaML XML -- set it to a default
				messages_.warn(
						"Couldn't find the Default Language specified in the ClaML XML -- setting to it to a default: " + 
						config_.getClamlDefaultLang());
				clamlCS.setDefaultLanguage(config_.getClamlDefaultLang());
			}
			
			//No ClaML mapping found
			//TODO find where ClaML XML keeps active info
			clamlCS.setIsActive(true);
					
			//LexGrid EntityDescription mapped to ClaML 'name' + 'version' -- <Title date="2008-01-05" name="ICD-10" version="2008"/>
			//TODO check to see if this is how we want to do this
			EntityDescription ed = new EntityDescription();
			ed.setContent(clamlXML_.getTitle().getName() + " " + clamlXML_.getTitle().getVersion());
			clamlCS.setEntityDescription(ed);
			
			//LexGrid CodingScheme mapped to ClaML 'name' -- <Title date="2008-01-05" name="ICD-10" version="2008"/>
			//TODO see if there is a better Formal Name
			clamlCS.setFormalName(clamlXML_.getTitle().getName());

			return clamlCS;
			
		}
		
		private CodingScheme buildMappings(CodingScheme clamlCS){
			Mappings mappingsCS = new Mappings();
		
			//Set the supported Properties with the ClaML <RubricKind>
			RubricKinds rubricKinds = clamlXML_.getRubricKinds();
			List<RubricKind> rubricKindList= rubricKinds.getRubricKind();
			
			for(RubricKind rubricKind : rubricKindList){
				SupportedProperty sp = new SupportedProperty();
				sp.setLocalId(rubricKind.getName());
				mappingsCS.addSupportedProperty(sp);
			}
					
			//Set a Supported Coding Scheme mapped to ClaML 'name'
			//<Title date="2008-01-05" name="ICD-10" version="2008"/>
			SupportedCodingScheme scs = new SupportedCodingScheme();
			scs.setLocalId(clamlXML_.getTitle().getName());
			scs.setUri(clamlXML_.getIdentifier().get(0).getUid());
			mappingsCS.addSupportedCodingScheme(scs);
					
			//LexGrid SupportedLanguage mapped to ClaML <Meta> tag with name="lang -- <Meta name="lang" value="en"/>
			SupportedLanguage sl = new SupportedLanguage();			
			try {
				sl.setLocalId(
						getMetaAttributeValueFromName(clamlXML_.getMeta(), config_.getMetaLangName()));
			} catch (NameNotFoundException e) {
				//couldn't find the language in the ClaML XML -- set it to a default
				messages_.warn(
						"Couldn't find the Default Language specified in the ClaML XML -- setting to it to a default: " + 
						config_.getClamlDefaultLang());
				sl.setLocalId(config_.getClamlDefaultLang());
			}
			mappingsCS.addSupportedLanguage(sl);
			
			//UsageKinds are mapped as AssociationQualifiers
			UsageKinds allUsageKinds = clamlXML_.getUsageKinds();
			List<UsageKind> usageKinds = allUsageKinds.getUsageKind();
			for(UsageKind usageKind : usageKinds){
				SupportedAssociationQualifier assocQual = new SupportedAssociationQualifier();
				assocQual.setLocalId(usageKind.getName());
				assocQual.setContent(usageKind.getMark());
				mappingsCS.addSupportedAssociationQualifier(assocQual);
			}
			
			
			clamlCS.setMappings(mappingsCS);
			return clamlCS;
		}
		/*
		private CodingScheme processModifiers(CodingScheme clamlCS){
//			Entities concepts = clamlCS.getEntities();
			
			//Load Modifiers as Concepts
			//TODO verify this...
			List<Modifier> modifierList = clamlXML_.getModifier();
			for(Modifier clamlModifier : modifierList){
				Entity conceptCS = new Entity();
				
				//Set the Concept code with ClaML 'code' -- <Modifier code="S20V90_4">
				conceptCS.setEntityCode(clamlModifier.getCode());	
				
				conceptCS.addEntityType(EntityTypes.CONCEPT.toString());
				
				//Process the Rubrics
				RubricProcessor rubricProcessor = new DefaultRubricProcessorImpl(clamlXML_, clamlCS, conceptCS, config_, messages_);
				
				List<Rubric> rubricList = clamlModifier.getRubric();
				
				for(Rubric rubric : rubricList){
					Property property = rubricProcessor.processRubric(rubric);
					//check to see what kind of property we got back -- checking with
					//'instanceof's probably isn't the best... but works.
					if(property instanceof Presentation){
						conceptCS.addPresentation((Presentation) property);
					} else if(property instanceof Definition){
						conceptCS.addDefinition((Definition) property);
					} else {
						conceptCS.addProperty(property);
					}
				} 
				
				if(conceptCS.getPresentation().length == 0){
					messages_.info("Adding a Default Presentation for Concept: " + conceptCS.getEntityCode());
					conceptCS.addPresentation(createDefaultPresentation(conceptCS));
				}
				
				//Process EntityDescription. Here, we want to get the Preferred Presentation.
				//If there's no Preferred Presentation... set Concept Code as EntityDescription.
				//:TODO Above logic is probably not what we want...
				List<Presentation> presentations = Arrays.asList(conceptCS.getPresentation());
				
				boolean foundPreferredPresentation = false;
				for(Presentation presentation : presentations){
					//Found a Preferred Presentation!
					if(presentation.getIsPreferred()){
					    EntityDescription ed = new EntityDescription();
					    ed.setContent(presentation.getValue().getContent());
						conceptCS.setEntityDescription(ed);
						foundPreferredPresentation = true;
						break;
					}
				}
				
				//We didn't find one... set the EntityDescription as the Concept Code
				if(!foundPreferredPresentation){
					if(conceptCS.getPresentation().length > 0){
						conceptCS.getPresentation()[0].setIsPreferred(true);
						EntityDescription ed = new EntityDescription();
						ed.setContent((conceptCS.getPresentation()[0]).getValue().getContent());
						conceptCS.setEntityDescription(ed);
					}	
				}
				
				if(StringUtils.isBlank(conceptCS.getEntityDescription().getContent())){
					messages_.warn("Found a blank Entity Description");
					EntityDescription ed = new EntityDescription();
					ed.setContent(" ");
					conceptCS.setEntityDescription(ed);
				}
				
				//Process the Concepts SubClasses and SuperClass
				List<SubClass> subClassList = clamlModifier.getSubClass();
				for(SubClass subClass : subClassList){
					String sourceCode = conceptCS.getEntityCode();
					String subclassCode = subClass.getCode();
					
					//Combine the SubClass Code with the Modifier Code
					//<Modifier code="S20Y70_4">
					//<SubClass code=".0"/>
					//to get the actual SubClass Code - S20Y70_4.0
					String targetCode = conceptCS.getEntityCode() + subclassCode;
					
					processAssociation(clamlCS, config_.getSubclassAssoc(),
							sourceCode, targetCode);
					
					//processAssociation(clamlCS, config_.getSuperclassAssoc(),
					//		targetCode, sourceCode);
				}
				
				
				//add the Concept
				clamlCS.getEntities().addEntity(conceptCS);
				
			}
			
			return clamlCS;
		}
*/
		private Entity processConceptProperties(CodingScheme schemeCS, Entity conceptCS, org.LexGrid.LexBIG.claml.Class clamlClass){
			RubricProcessor rubricProcessor = new DefaultRubricProcessorImpl(clamlXML_, schemeCS, conceptCS, config_, messages_);

			List<Rubric> rubricList = clamlClass.getRubric();
			
			for(Rubric rubric : rubricList){
				Property property = rubricProcessor.processRubric(rubric);
				if(StringUtils.isBlank(property.getValue().getContent())){
                    continue;
                }
				//check to see what kind of property we got back -- checking with
				//'instanceof's probably isn't the best... but works.
				if(property instanceof Presentation){
					conceptCS.addPresentation((Presentation) property);
				} else if(property instanceof Definition){
					conceptCS.addDefinition((Definition) property);
				} else {
				    conceptCS.addProperty(property);
				}
			} 
			
			
			if(conceptCS.getPresentation().length == 0){
				messages_.info("Adding a Default Presentation for Concept: " + conceptCS.getEntityCode());
				conceptCS.addPresentation(createDefaultPresentation(conceptCS));
			}
			return conceptCS;
		}
		
		private Entity processModifierProperties(CodingScheme schemeCS, Entity conceptCS, ModifierClass clamlClass){
			RubricProcessor rubricProcessor = new DefaultRubricProcessorImpl(clamlXML_, schemeCS, conceptCS, config_, messages_);

			List<Rubric> rubricList = clamlClass.getRubric();
			
			for(Rubric rubric : rubricList){
				Property property = rubricProcessor.processRubric(rubric);
				//check to see what kind of property we got back -- checking with
				//'instanceof's probably isn't the best... but works.
				if(property instanceof Presentation){
					conceptCS.addPresentation((Presentation) property);
				} else if(property instanceof Definition){
					conceptCS.addDefinition((Definition) property);
				} else {
					conceptCS.addProperty(property);
				}
			} 
			
			if(conceptCS.getPresentation().length == 0){
				messages_.info("Adding a Default Presentation for Concept: " + conceptCS.getEntityCode());
				conceptCS.addPresentation(createDefaultPresentation(conceptCS));
			}
			return conceptCS;
		}
		
		private Presentation createDefaultPresentation(Entity conceptCS){
			Presentation presentation = new Presentation();
			
			Text text = new Text();
			text.setContent(conceptCS.getEntityDescription().getContent());
			presentation.setValue(text);
			presentation.setIsPreferred(true);
			presentation.setPropertyId("P-1");
			presentation.setPropertyName("defaultPresentation");
			return presentation;
		}
				
		private CodingScheme buildConcepts(CodingScheme clamlCS){
			
			//Add all of the <Class> elements as concepts
			List<org.LexGrid.LexBIG.claml.Class> classList = clamlXML_.getClazz();
			for(org.LexGrid.LexBIG.claml.Class clamlClass : classList){
				//add the Concept
			    Entity concept = processConcept(clamlCS, clamlClass);
				
				clamlCS.getEntities().addEntity(concept);
	
				//process its modifiers
				List<Entity> modifiedConcepts = processModifiedConcepts(concept, clamlClass, clamlCS);

				for (Entity c : modifiedConcepts) {
				    
				    clamlCS.getEntities().addEntity(c);
				}
			}
					
			//Load ModifiersClasses as Concepts
			//TODO verify this...
			List<ModifierClass> modifierClasList = clamlXML_.getModifierClass();
			/*
			for(ModifierClass clamlModifierClass : modifierClasList){
			    Entity concept = processModifier(clamlCS, clamlModifierClass);
				clamlCS.getEntities().addEntity(concept);
			}
			 */
			
			return clamlCS;
		}

		private List<Entity> processModifiedConcepts(Entity concept, org.LexGrid.LexBIG.claml.Class clamlClass, CodingScheme clamlEMF){
            List<Entity> returnList = new ArrayList<Entity>();
            List<ModifiedBy> modifiedBy = clamlClass.getModifiedBy();

            for(ModifiedBy modified : modifiedBy){  

                List<ModifierClass> modifierClasses = this.getModifierClassesByCode(modified.getCode());

                for(ModifierClass modifierClass : modifierClasses){
                    returnList.add(this.modifyConcept(concept, modifierClass, clamlEMF));   
                    /*
                    Property prop = CommontypesFactory.eINSTANCE.createProperty();
                    prop.setPropertyName(this.config_.getModifiedByProperty());

                    Text text = CommontypesFactory.eINSTANCE.createText();
                    prop.setValue(text);
                    prop.setPropertyId(modifierClass.getCode());

                    if(isModifierOptional(modified)){
                        PropertyQualifier qual = CommontypesFactory.eINSTANCE.createPropertyQualifier();
                        qual.setPropertyQualifierName("optional");

                        Text qualText = CommontypesFactory.eINSTANCE.createText();
                        qualText.setValue("true");
                        qual.setValue(qualText);

                        prop.getPropertyQualifier().add(qual);
                    }

                    concept.getProperty().add(prop);
                    */
                }
            }
            return returnList;
        }
		
		private boolean isModifierOptional(ModifiedBy modifiedBy){
			List<Meta> metaList = modifiedBy.getMeta();
			for(Meta meta : metaList){
				if(meta.getName().equals("usage") &&
				meta.getValue().equals("optional")){
					return true;
				}
			}
			return false;
		}
		
		private Entity cloneConcept(Entity concept){
		    Entity clone = new Entity();

			clone.setStatus(concept.getStatus());
			clone.setEntityDescription(concept.getEntityDescription());
			clone.setEntityCode(concept.getEntityCode());
			clone.setEntityType(concept.getEntityType());
			clone.setIsActive(concept.getIsActive());
			clone.setIsAnonymous(concept.getIsAnonymous());
			clone.setIsDefined(concept.getIsDefined());
			
			clone.setEffectiveDate(concept.getEffectiveDate());
			clone.setEntityCodeNamespace(concept.getEntityCodeNamespace());
			clone.setEntryState(concept.getEntryState());
			clone.setExpirationDate(concept.getExpirationDate());
			clone.setOwner(concept.getOwner());
			clone.setStatus(concept.getStatus());

			for(Presentation pre : clonePresentationList(Arrays.asList(concept.getPresentation()))) {
			    clone.addPresentation(pre);
			}

			for(Property pro: clonePropertyList(Arrays.asList(concept.getProperty()))) {
			    clone.addProperty(pro);
			}
			
			return clone;
		}
		
		private List<Presentation> clonePresentationList(List<Presentation> presentationList){
			List<Presentation> returnList = new ArrayList<Presentation>();
			for(Presentation pres : presentationList){
				returnList.add(clonePresentation(pres));
			}
			return returnList;
		}
		
		private List<Property> clonePropertyList(List<Property> propertyList){
			List<Property> returnList = new ArrayList<Property>();
			for(Property prop : propertyList){
				returnList.add(cloneConceptProperty(prop));
			}
			return returnList;
		}
		

		private Presentation clonePresentation(Presentation presentation){
			Presentation prop = new Presentation();
			prop.setEffectiveDate(presentation.getEffectiveDate());
			prop.setDegreeOfFidelity(presentation.getDegreeOfFidelity());
			prop.setIsPreferred(presentation.getIsPreferred());
			prop.setLanguage(presentation.getLanguage());
			prop.setMatchIfNoContext(presentation.getMatchIfNoContext());
			prop.setPropertyId(presentation.getPropertyId());
			prop.setPropertyName(presentation.getPropertyName());
			prop.setRepresentationalForm(presentation.getRepresentationalForm());
			
			Text text = new Text();
			if(presentation.getValue() != null){
				text.setContent(new String(presentation.getValue().getContent()));
			} else{
				System.out.println("Null prop1 value");
			}
			
			prop.setValue(text);
			
			return prop;
		}
	
		
		private Property cloneConceptProperty(Property presentation){
			Property prop = new Property();
			prop.setLanguage(presentation.getLanguage());
			prop.setPropertyId(presentation.getPropertyId());
			prop.setPropertyName(presentation.getPropertyName());
			
			Text text = new Text();
			
			if(presentation.getValue() != null){
				text.setContent(new String(presentation.getValue().getContent()));
			} else{
				System.out.println("Null prop2 value");
			}
			prop.setValue(text);
			
			return prop;
		}
		
		private Entity modifyConcept(Entity concept, ModifierClass modifierClass, CodingScheme clamlCS){
		    Entity modifiedConcept = cloneConcept(concept);	

			modifiedConcept.setEntityCode(concept.getEntityCode() + appendDotOnModifierCode(modifierClass.getCode()));
			
			Presentation pres = getPreferredPresentation(modifierClass, clamlCS);
			
			List<Presentation> presentations = Arrays.asList(modifiedConcept.getPresentation());
			
			for(Presentation conceptPres : presentations){
				if(conceptPres.getIsPreferred()){
					Text text;
					text = new Text();
					text.setContent(conceptPres.getValue().getContent() + ", " + pres.getValue().getContent());
					conceptPres.setValue(text);
				}
			}
			
			EntityDescription ed = new EntityDescription();
			ed.setContent(modifiedConcept.getEntityDescription().getContent() + ", " + pres.getValue().getContent());
			modifiedConcept.setEntityDescription(ed);
			
			this.processAssociation(clamlCS, config_.getSubclassAssoc(), concept.getEntityCode(), modifiedConcept.getEntityCode());
			
			return modifiedConcept;
		}
		
		private String appendDotOnModifierCode(String code){
			if(code.charAt(0) != '.'){
				code = "." + code;
			}
			return code;
		}
		
		
		private Presentation getPreferredPresentation(ModifierClass modifierClass, CodingScheme clamlCS){
			RubricProcessor rubricProcessor = new ModifierClassRubricProcessorImpl(clamlXML_, clamlCS, modifierClass, config_, messages_);
			List<Rubric> rubrics = modifierClass.getRubric();
			for(Rubric rubric : rubrics){
				Property prop = rubricProcessor.processRubric(rubric);
				if(prop instanceof Presentation){
					Presentation pres = (Presentation)prop;
					if(pres.getIsPreferred() == true){
						return pres;
					}
				}
			}
			return null;
		}
		
		/*
		 * ClaML has <Meta> tags that can be used to specify metadata about the Coding Scheme. 
		 * They are basically name/value pairs.
		 * For Example: 	
		 * 
		 * <Meta name="TopLevelSort" value="I II III IV V VI VII VIII IX X XI XII XIII XIV XV XVI XVII XVIII XIX XX XXI XXII"/>
		 * <Meta name="lang" value="en"/>
		 * 
		 * Because there can be more than one <Meta> tag, this method allows us to specify a 'name'
		 * and return its 'value'.
		 * 
		 * Example = passing in 'lang' would return 'en'.
		 */
		private String getMetaAttributeValueFromName(List<Meta> meta, String attributeName) throws NameNotFoundException {
			for(Meta currentMeta : meta){
				String foundName = currentMeta.getName();
				if(foundName.equals(attributeName)){
					return currentMeta.getValue();
				}
			 }
			
			//if it doesn't find it, throw an Exception
			throw new NameNotFoundException("Could not find a Meta Element with value: " + attributeName);
		}
		
		private Entity processConcept(CodingScheme schemeCS, org.LexGrid.LexBIG.claml.Class clamlClass){
		    Entity concept = new Entity();
			
			//Set the Concept code with ClaML 'code' -- <Class code="I47" kind="category">
			concept.setEntityCode(clamlClass.getCode());
			
			concept.addEntityType(EntityTypes.CONCEPT.toString());
			
			//Process the Concept's Properties
			concept = processConceptProperties(schemeCS, concept, clamlClass);
			
			//Process EntityDescription. Here, we want to get the Preferred Presentation.
			//If there's no Preferred Presentation... set Concept Code as EntityDescription.
			//:TODO Above logic is probably not what we want...
			List<Presentation> presentations = Arrays.asList(concept.getPresentation());
			
			boolean foundPreferredPresentation = false;
			for(Presentation presentation : presentations){
				//Found a Preferred Presentation!
				if(presentation.getIsPreferred()){
				    EntityDescription ed = new EntityDescription();
				    ed.setContent(presentation.getValue().getContent());
					concept.setEntityDescription(ed);
					foundPreferredPresentation = true;
					break;
				}
			}
			
			//We didn't find one... set the EntityDescription as the Concept Code
			if(!foundPreferredPresentation){
			    EntityDescription ed = new EntityDescription();
			    ed.setContent(concept.getEntityCode());
				concept.setEntityDescription(ed);
			}
			
			//Process the Concepts SubClasses and SuperClass
			List<SubClass> subClassList = clamlClass.getSubClass();
			for(SubClass subClass : subClassList){
				String sourceCode = concept.getEntityCode();
				String targetCode = subClass.getCode();
				processAssociation(schemeCS, config_.getSubclassAssoc(),
						sourceCode, targetCode);
				
				//processAssociation(schemeEMF, config_.getSuperclassAssoc(),
				//		targetCode, sourceCode);
			}
			List<SuperClass> superClassList = clamlClass.getSuperClass();
			for(SuperClass superClass : superClassList){
				String sourceCode = concept.getEntityCode();
				String targetCode = superClass.getCode();
				processAssociation(schemeCS, config_.getSuperclassAssoc(),
						sourceCode, targetCode);
				
				//processAssociation(schemeEMF, config_.getSubclassAssoc(),
				//		sourceCode, targetCode);
			}
			
			
			
			return concept;
		}
		/*
		private Entity processModifier(CodingScheme schemeCS, ModifierClass modifier){
		    Entity concept = new Entity();
			
			//Set the Concept code with ClaML 'code' -- <Class code="I47" kind="category">
			concept.setEntityCode(modifier.getModifier() + modifier.getCode());
			
			concept.addEntityType(EntityTypes.CONCEPT.toString());
			
			//Process the Concept's Properties
			concept = processModifierProperties(schemeCS, concept, modifier);
			
			//Process EntityDescription. Here, we want to get the Preferred Presentation.
			//If there's no Preferred Presentation... set Concept Code as EntityDescription.
			//:TODO Above logic is probably not what we want...
			List<Presentation> presentations = Arrays.asList(concept.getPresentation());
			
			boolean foundPreferredPresentation = false;
			for(Presentation presentation : presentations){
				//Found a Preferred Presentation!
				if(presentation.getIsPreferred()){
				    EntityDescription ed = new EntityDescription();
				    ed.setContent(presentation.getValue().getContent());
					concept.setEntityDescription(ed);
					foundPreferredPresentation = true;
					break;
				}
			}
			
			//We didn't find one... set the EntityDescription as the Concept Code
			if(!foundPreferredPresentation){
			    EntityDescription ed = new EntityDescription();
			    ed.setContent(concept.getEntityCode());
				concept.setEntityDescription(ed);
			}
			
			//Process the Concepts SubClasses and SuperClass
			List<SubClass> subClassList = modifier.getSubClass();
			for(SubClass subClass : subClassList){
				String sourceCode = concept.getEntityCode();
				String targetCode = subClass.getCode();
				processAssociation(schemeCS, config_.getSubclassAssoc(),
						sourceCode, targetCode);
				
				//processAssociation(schemeEMF, config_.getSuperclassAssoc(),
				//		targetCode, sourceCode);
			}
			SuperClass superClass = modifier.getSuperClass();
			String sourceCode = concept.getEntityCode();
			String targetCode = superClass.getCode();
			processAssociation(schemeCS, config_.getSuperclassAssoc(),
					sourceCode, targetCode);

			//processAssociation(schemeEMF, config_.getSubclassAssoc(),
			//		sourceCode, targetCode);


			return concept;
		}
*/
		private void processAssociation(CodingScheme schemeCS, String association,
				String sourceCode, String targetCode) {
			this.processAssociation(schemeCS, association, sourceCode, targetCode, new ArrayList<AssociationQualification>());
		}
		
		private void processAssociation(CodingScheme schemeCS, String association,
				String sourceCode, String targetCode, AssociationQualification qualifier) {
			List<AssociationQualification> qualList = new ArrayList<AssociationQualification>();
			qualList.add(qualifier);
			this.processAssociation(schemeCS, association, sourceCode, targetCode, qualList);
		}
		
		private void processAssociation(CodingScheme schemeCS, String association,
			String sourceCode, String targetCode, List<AssociationQualification> qualifiers){
		AssociationPredicate associationCS = (AssociationPredicate)RelationsUtil.resolveAssociationPredicates(schemeCS, association).get(0);

		AssociationSource as = new AssociationSource();
		as.setSourceEntityCode(sourceCode);
		as.setSourceEntityCodeNamespace(schemeCS.getCodingSchemeName());
		as = RelationsUtil.subsume(associationCS, as);
		
		AssociationTarget at = new AssociationTarget();
		at.setTargetEntityCode(targetCode);
		at.setTargetEntityCodeNamespace(schemeCS.getCodingSchemeName());
		if(qualifiers != null && qualifiers.size() > 0){
			for (AssociationQualification aq : qualifiers) {
			    at.addAssociationQualification(aq);
			}
		}
		RelationsUtil.subsume(as, at);		
	}
	
	private List<ModifierClass> getModifierClassesByCode(String modifierCode){
		List<ModifierClass> returnList = new ArrayList<ModifierClass>();
		List<ModifierClass> modifiers = this.clamlXML_.getModifierClass();
		for(ModifierClass modifier : modifiers){
			if(modifier.getModifier().equals(modifierCode)){
				returnList.add(modifier);
			}
		}
		return returnList;
	}		
}