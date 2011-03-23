package org.cts2.internal.model.uri;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
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
import org.cts2.core.Directory;
import org.cts2.core.Filter;
import org.cts2.core.FilterComponent;
import org.cts2.core.TargetExpression;
import org.cts2.entity.EntityDirectory;
import org.cts2.entity.EntityList;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedAssociationDirectory;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedAssociationList;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedEntityDirectory;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedEntityList;
import org.cts2.internal.model.uri.restrict.AssociationRestrictionHandler;
import org.cts2.internal.model.uri.restrict.EntityDescriptionRestrictionHandler;
import org.cts2.service.core.EntityNameOrURI;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.AssociationDirectoryURI;
import org.cts2.uri.EntityDirectoryURI;

/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class DefaultAssociationDirectoryURI extends AbstractNonIterableLexEvsBackedResolvingDirectoryURI<CodedNodeGraph,AssociationDirectoryURI> implements AssociationDirectoryURI {

	private CodedNodeGraph codedNodeGraph;
	private CodedNodeSet codedNodeSet;
	private BeanMapper beanMapper;
	private LexBIGService lbs;
	
	public DefaultAssociationDirectoryURI(CodedNodeGraph codedNodeGraph, AssociationRestrictionHandler restrictionHandler, BeanMapper beanMapper){
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
	
	public EntityDirectoryURI doGetAllSourceAndTargetEntities(
			EntityDirectoryURI directory, QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public EntityDirectoryURI doGetPredicates(AssociationDirectoryURI directory,
			QueryControl queryControl, ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public EntityDirectoryURI doGetSourceEntities(
			AssociationDirectoryURI directory, QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public EntityDirectoryURI doGetTargetEntities(
			AssociationDirectoryURI directory, QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}


	public AssociationDirectoryURI doRestrictToCodeSystemVersion(
			NameOrURI codeSystemVersions) {
		((AssociationRestrictionHandler) this.getRestrictionHandler())
				.restrictToCodeSystemVersions(codeSystemVersions);
		return this;
	}
	
	public AssociationDirectoryURI doRestrictToPredicate(
			AssociationDirectoryURI directory, EntityNameOrURI predicate) {
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

	
	public AssociationDirectoryURI doRestrictToSourceEntity(
			AssociationDirectoryURI directory, EntityNameOrURI sourceEntity) {
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

	
	public AssociationDirectoryURI doRestrictToSourceOrTargetEntity(
			AssociationDirectoryURI directory, EntityNameOrURI entity) {
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

	
	public AssociationDirectoryURI doRestrictToTargetEntity(
			AssociationDirectoryURI directory, EntityNameOrURI target) {
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

	
	public AssociationDirectoryURI doRestrictToTargetExpression(
			AssociationDirectoryURI directory, TargetExpression target) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public AssociationDirectoryURI doRestrictToTargetLiteral(
			AssociationDirectoryURI directory, String target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssociationDirectoryURI restrict(Filter filter) {
		// TODO Auto-generated method stub
		return null;
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

}
