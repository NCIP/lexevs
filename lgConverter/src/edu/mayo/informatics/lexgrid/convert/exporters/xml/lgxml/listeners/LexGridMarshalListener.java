package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.listeners;

import java.util.Iterator;
import java.util.Vector;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.MarshalListener;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.LexGridConstants;


public class LexGridMarshalListener implements MarshalListener {
	Marshaller marshaller;
	CodedNodeSet cns;
	CodedNodeGraph cng;
	ResolvedConceptReferencesIterator rcri_iterator;
	ResolvedConceptReferenceList refList;
	Iterator<ResolvedConceptReference> blockIterator;
	ResolvedConceptReference curConRef;
	Entity curEntity;

	AssociationPredicate userAP = null;
	String stopToken = "$$TEST$$";
	public static String codingSchemeName = null;
	
	public static Vector<String> processed = new Vector<String>();
	
	private final int MAX_BLOCK_SIZE = 10; 
	private int blockSize = 10;
	private int start = 0;
	private int end = blockSize;
	private int max = 30;  // limit our output to the file just to keep it small
	private int entityIndex = start;
	
	
	
	public LexGridMarshalListener(Marshaller marshaller, CodedNodeSet cns, int pageSize) {
	    this.setBlockSize(pageSize);
		this.marshaller = marshaller;
		this.cns = cns;
	}
	
	public LexGridMarshalListener(Marshaller marshaller, CodedNodeGraph cng, int pageSize) {
	    this.setBlockSize(pageSize);
		this.marshaller = marshaller;
		this.cng = cng;
	}
	
	public LexGridMarshalListener(Marshaller marshaller, CodedNodeGraph cng, CodedNodeSet cns, int pageSize) {
	    this.setBlockSize(pageSize);
		this.marshaller = marshaller;
		this.cng = cng;
		this.cns = cns;
	}
	
	private void setBlockSize(int size) {
	    if(size > MAX_BLOCK_SIZE || size < 1 ) {
	        blockSize = MAX_BLOCK_SIZE;
	    } else {
	        blockSize = size;
	    }
        System.out.println("============ setting page size to: " + blockSize);	    
	}
	

	@Override
	public void postMarshal(Object arg0) {
		if(Entities.class.equals(arg0.getClass()) == true) {
			System.out.println("POST: found an Entities object");
		}
		
	}

	@Override
	public boolean preMarshal(Object arg0) {
		if(Entities.class.equals(arg0.getClass()) == true) {
			Entities entities = (Entities)arg0;
			System.out.println("PRE: entity count = " + entities.getEntityCount());
		}
		
		if((Entity.class.equals(arg0.getClass()) == true)) {
		    Entity temp = (Entity)arg0;
		    System.out.println("PRE: **************** marshalling entityCode=" + temp.getEntityCode());
			if(entityIndex >= max) {
				return false;
			}
		}
		
		if((Entity.class.equals(arg0.getClass()) == true) && ((Entity)arg0).getEntityCode().equals(LexGridConstants.MR_FLAG))  {
			
			// get groups of entity objects using CNS.
			if(cns != null) {
				try {
					rcri_iterator = cns.resolve(null, null, null, null, true);
					
					// will a  LBabcException break us out?
					boolean done = false;
					while(!done) {
						refList = rcri_iterator.next(blockSize);
//						refList = rcri_iterator.get(start, end);
						blockIterator = (Iterator<ResolvedConceptReference>) refList.iterateResolvedConceptReference();
						while(blockIterator.hasNext()) {
							curConRef = (ResolvedConceptReference)blockIterator.next();					
							curEntity = curConRef.getEntity();
							System.out.println("**************** entityCode=" + curEntity.getEntityCode());
						    this.marshaller.marshal(curEntity);
						    ++entityIndex;
						}			
						start = entityIndex;
						end = start + blockSize;
						System.out.println("new block: start=" + start + " end=" + end);
						// if(start >= numberRemaining) {
						if(rcri_iterator.hasNext() == false) {
							done = true;
						}
					}
										
				} catch (LBInvocationException e) {
					e.printStackTrace();
				} catch (LBParameterException e) {
					e.printStackTrace();
				} catch (LBResourceUnavailableException e) {
					e.printStackTrace();
				} catch (MarshalException e) {
					e.printStackTrace();
				} catch (ValidationException e) {
					e.printStackTrace();
				}
			}
			
			// lets try and prevent the FLAG entity from getting marshaled
			Entity entity = (Entity)arg0;
			System.out.println("PRE: don't marshal entity with code: " + entity.getEntityCode());
			return false;
		}
		else if(AssociationPredicate.class.equals(arg0.getClass())) 
		{
			AssociationPredicate ap = (AssociationPredicate)arg0;
			if(ap.getParent() != null && stopToken.equals((String)ap.getParent())) 
			{
				return true;
			}
			if (cng != null)
			{
				String curAssociationName = ap.getAssociationName();
				System.out.println("\n$$$$$$$$$$$$$$$   Marshalling " + curAssociationName + "    $$$$$$$$$$$$$$$$$$$\n");
				try 
				{
					ResolvedConceptReferenceList rcrl = cng.resolveAsList(null, true, false, -1, -1, null, null, null, null, -1);
					if (rcrl != null)
					{
						blockIterator = (Iterator<ResolvedConceptReference>) rcrl.iterateResolvedConceptReference();
						while(blockIterator.hasNext()) 
						{
							curConRef = (ResolvedConceptReference)blockIterator.next();	
							
							AssociationList asl = curConRef.getSourceOf();
							if ((userAP == null)||(!curAssociationName.equals(userAP.getAssociationName())))
							{
								System.out.println("STARTING ASSOCIATION PREDICATE.... with " + curAssociationName);
								userAP = ap;
							}

							processAssociationList(asl, userAP);
							userAP.setParent(new String(stopToken));
						}
					}
					
				} catch (LBInvocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LBParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				return true;
			}
			return false;
		}
		return true;
	}
	
	private void processAssociationList(AssociationList _asl, AssociationPredicate _asp) 
	{
		if(_asl == null) {return ;}
		if(_asp == null) {return ;}
		
		String curAssociationName = _asp.getAssociationName();
		System.out.println("**** inside processAssociationList with association=" + curAssociationName);
				
		if(curAssociationName == null){ return; }
		
		AssociationSource associationSource = null;
		Iterator<?> associationIterator = _asl.iterateAssociation();
		
		while(associationIterator.hasNext())
		{
			Association association = (Association) associationIterator.next();
			System.out.println("\tProcessing Association=" + association.getAssociationName());
			if(!association.getAssociationName().equals(curAssociationName)) 
			{
				// continue;
				System.out.println("\t####### no addition in this cycle");
			}
			
			// get the source
			int associatedConcepts = association.getAssociatedConcepts().getAssociatedConceptCount();
			System.out.println("\tAssociated Concepts=" + associatedConcepts);
			
			if (associatedConcepts > 0)
			{
				Vector<AssociationSource> associationSourceV = new Vector<AssociationSource>();
				
				Iterator associatedConceptsIterator	= association.getAssociatedConcepts().iterateAssociatedConcept();
				while(associatedConceptsIterator.hasNext())
				{
					AssociatedConcept source = (AssociatedConcept) associatedConceptsIterator.next();
					System.out.println("\tProcessing AssociatedConcept (source):" + source.getConceptCode());
				    
					if (codingSchemeName == null)
						codingSchemeName = source.getCodingSchemeName();
					
//					ConceptReference focus = Constructors.createConceptReference(source.getConceptCode(), codingSchemeName);
					ConceptReference focus = new ConceptReference();
					focus.setCode(source.getConceptCode());
					focus.setCodingSchemeName(codingSchemeName);
					ResolvedConceptReferenceList localRcrl = null;
					try 
					{
						System.out.println("Focus=" + focus);
						localRcrl = cng.resolveAsList(focus, true, false, -1, -1, null, null, null, null, -1);
					} 
					catch (LBInvocationException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					catch (LBParameterException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ResolvedConceptReference sourceRef = null;
					if (localRcrl != null)
					{
						Iterator<ResolvedConceptReference> innterIterator = (Iterator<ResolvedConceptReference>) localRcrl.iterateResolvedConceptReference();
						sourceRef = (ResolvedConceptReference)innterIterator.next();
					}
					
					if (sourceRef == null)
					{
						System.out.println("Failed to get Source Ref for " + source.getConceptCode());
						continue;
					}
					
					AssociationList targets = sourceRef.getSourceOf();
					if(targets == null) 
					{
						System.out.println("No Targets found for " + sourceRef.getConceptCode());
						continue;
					}
					else
					{
						associationSource = new AssociationSource();
						associationSource.setSourceEntityCodeNamespace(sourceRef.getCodeNamespace());
						associationSource.setSourceEntityCode(sourceRef.getConceptCode());
					
						Iterator<?> targetsIterator = targets.iterateAssociation();
						boolean targetsFound = false;
						while(targetsIterator.hasNext()) 
						{
							Association targetAssociation = (Association)targetsIterator.next();
							Iterator<?> associatedTargetsIterator = targetAssociation.getAssociatedConcepts().iterateAssociatedConcept();
							while(associatedTargetsIterator.hasNext()) 
							{
								AssociatedConcept target = (AssociatedConcept)associatedTargetsIterator.next();
								
								if (target == null)
									continue;
								
								AssociationTarget associationTarget = new AssociationTarget();
								associationTarget.setTargetEntityCodeNamespace(target.getCodeNamespace());
								associationTarget.setTargetEntityCode(target.getConceptCode());
								System.out.println("\t\t" + target.getConceptCode() + " with " + targetAssociation.getAssociationName());
								if(targetAssociation.getAssociationName().equals(curAssociationName)) 
								{
									targetsFound = true;
									associationSource.addTarget(associationTarget);
									System.out.println("\t\tAdding Target:" + associationTarget.getTargetEntityCode());
									NameAndValueList assocQuals = target.getAssociationQualifiers();
									if ((assocQuals != null)&&(assocQuals.getNameAndValueCount() > 0))
									{
										System.out.println("Processing Association Qualifiers now...");
										Iterator<?> associatedQualItr = assocQuals.iterateNameAndValue();
										while(associatedQualItr.hasNext()) 
										{
											NameAndValue nv = (NameAndValue) associatedQualItr.next();
											
											AssociationQualification qlf = new AssociationQualification();
											qlf.setAssociationQualifier(nv.getName());
											Text v = new Text();
											v.setContent(nv.getContent());
											qlf.setQualifierText(v);
											associationTarget.addAssociationQualification(qlf);
										}
									}
								}
							}
						}
						
						if (targetsFound)
							associationSourceV.add(associationSource);
					}
				
					if(associationSourceV.size() > 0) 
					{
						for (int vi=0; vi < associationSourceV.size();vi++)
						{
							System.out.println("\t Source[" + vi + "] ADDED TO PREDICATE");
							_asp.addSource(associationSourceV.elementAt(vi));
						}
						associationSourceV = new Vector<AssociationSource>();
					}
					
					System.out.println("\n--------> CALLING AGAIN targets =" + targets.getAssociation().length + " --------->\n");
					processAssociationList(targets, _asp);
				}
			}
		}
	}
}
