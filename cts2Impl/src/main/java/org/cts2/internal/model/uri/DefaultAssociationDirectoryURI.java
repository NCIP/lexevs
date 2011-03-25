package org.cts2.internal.model.uri;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.CodedNodeSetImpl;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.cts2.association.AssociationDirectory;
import org.cts2.association.AssociationList;
import org.cts2.core.TargetExpression;
import org.cts2.core.types.SetOperator;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedAssociationDirectory;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedAssociationList;
import org.cts2.internal.model.uri.restrict.NonIterableBasedResolvingRestrictionHandler;
import org.cts2.service.core.EntityNameOrURI;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.AssociationDirectoryURI;
import org.cts2.uri.EntityDirectoryURI;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState.RestrictToCodeSystemVersionRestriction;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState.RestrictToTargetExpressionRestriction;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState.RestrictToTargetLiteralRestriction;

/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class DefaultAssociationDirectoryURI 
	extends AbstractNonIterableLexEvsBackedResolvingDirectoryURI<CodedNodeGraph,AssociationDirectoryURI> implements AssociationDirectoryURI {

	private CodedNodeGraph codedNodeGraph;
	private CodedNodeSet codedNodeSet;
	private BeanMapper beanMapper;
	private LexBIGService lbs;
	
	private AssociationDirectoryRestrictionState restrictionState;
	
	public DefaultAssociationDirectoryURI(
			CodedNodeGraph codedNodeGraph, 
			NonIterableBasedResolvingRestrictionHandler<CodedNodeGraph,AssociationDirectoryURI> restrictionHandler, 
			BeanMapper beanMapper){
		super(restrictionHandler);
		this.codedNodeGraph = codedNodeGraph;
		this.beanMapper = beanMapper;
		lbs = LexBIGServiceImpl.defaultInstance();
	}
 
	@SuppressWarnings("unchecked")
	@Override
	protected <O> O transform(
			CodedNodeGraph lexevsObject, Class<O> clazz) {
		try {
			if(clazz.equals(AssociationDirectory.class)){
				return (O) new ResolvedConceptReferencesIteratorBackedAssociationDirectory(lexevsObject, this.beanMapper);
			}
			if(clazz.equals(AssociationList.class)){
				return (O) new ResolvedConceptReferencesIteratorBackedAssociationList(lexevsObject, this.beanMapper);
			}
		} catch (LBException e) {
			//TODO: real cts2 exception here
			throw new IllegalStateException();
		}
		
		//TODO: real cts2 exception here
		throw new IllegalStateException();
	}
	@Override
	protected int doCount(ReadContext readContext) {
		return 0;
	}
	
	public EntityDirectoryURI getAllSourceAndTargetEntities(
			EntityDirectoryURI directory, QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public EntityDirectoryURI getPredicates(QueryControl queryControl, ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public EntityDirectoryURI getSourceEntities(QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public EntityDirectoryURI getTargetEntities(
			AssociationDirectoryURI directory, QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityDirectoryURI getTargetEntities(QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	public AssociationDirectoryURI restrictToCodeSystemVersion(
			NameOrURI codeSystemVersions) {
		RestrictToCodeSystemVersionRestriction restriction = new RestrictToCodeSystemVersionRestriction();
		restriction.setCodeSystemVersion(codeSystemVersions);
		
		return this.clone();
	}
	
	public AssociationDirectoryURI restrictToPredicate(EntityNameOrURI predicate) {
		NameAndValueList association = Constructors.createNameAndValueList(predicate.getEntityName().getName(), null);
		try {
			codedNodeGraph.restrictToAssociations(association, null);
		} catch (LBInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LBParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

	
	public AssociationDirectoryURI restrictToSourceEntity(EntityNameOrURI sourceEntity) {
		CodedNodeSet set = new CodedNodeSetImpl();
		ConceptReferenceList codeList = Constructors.createConceptReferenceList(sourceEntity.getEntityName().getName());
	
		try {
			set.restrictToCodes(codeList);
			codedNodeGraph.restrictToSourceCodes(set);
		} catch (LBInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LBParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this;
	}

	
	public AssociationDirectoryURI restrictToSourceOrTargetEntity(EntityNameOrURI entity) {
		CodedNodeSet set = new CodedNodeSetImpl();
		ConceptReferenceList codeList = Constructors.createConceptReferenceList(entity.getEntityName().getName());
		try {
			set.restrictToCodes(codeList);
			codedNodeGraph.restrictToCodes(set);
		} catch (LBInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LBParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this;
	}

	
	public AssociationDirectoryURI restrictToTargetEntity(EntityNameOrURI target) {
		CodedNodeSet set = new CodedNodeSetImpl();
		ConceptReferenceList codeList = Constructors.createConceptReferenceList(target.getEntityName().getName());
	
		try {
			set.restrictToCodes(codeList);
			codedNodeGraph.restrictToTargetCodes(set);
		} catch (LBInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LBParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this;
	}

	
	public AssociationDirectoryURI restrictToTargetExpression(TargetExpression target) {
		RestrictToTargetExpressionRestriction restriction = new RestrictToTargetExpressionRestriction();
		restriction.setTarget(target);
		
		return this.clone();
	}

	
	public AssociationDirectoryURI restrictToTargetLiteral(String target) {
		RestrictToTargetLiteralRestriction restriction = new RestrictToTargetLiteralRestriction();
		restriction.setTarget(target);
		
		return this.clone();
	}

	@Override
	protected CodedNodeGraph getOriginalState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AssociationDirectoryURI clone() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public AssociationDirectoryRestrictionState getRestrictionState() {
		return this.restrictionState;
	}

	@Override
	protected AssociationDirectoryURI createSetOperatedDirectoryURI(
			SetOperator setOperator, AssociationDirectoryURI directoryUri1,
			AssociationDirectoryURI directoryUri2) {
		// TODO Auto-generated method stub
		return null;
	}

}
