package org.lexevs.paging.codednodegraph;

import java.util.List;

import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao.TripleNode;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.paging.AbstractPageableIterator;

public class TripleUidIterator extends AbstractPageableIterator<String>{

	private String codingSchemeUri;
	private String codingSchemeVersion;
	private String relationsContainerName;
	private String associationPredicateName;
	private String entityCode;
	private String entityCodeNamespace;
	private GraphQuery graphQuery;
	private TripleNode tripleNode;

	public TripleUidIterator(
			String codingSchemeUri, 
			String codingSchemeVersion, 
			String relationsContainerName,
			String associationPredicateName,
			String entityCode,
			String entityCodeNamespace,
			GraphQuery graphQuery,
			TripleNode tripleNode,
			int pageSize) {
		super(pageSize);
		this.codingSchemeUri = codingSchemeUri;
		this.codingSchemeVersion = codingSchemeVersion;
		this.relationsContainerName = relationsContainerName;
		this.associationPredicateName = associationPredicateName;
		this.entityCode = entityCode;
		this.entityCodeNamespace = entityCodeNamespace;
		this.graphQuery = graphQuery;
		this.tripleNode = tripleNode;
	}
	@Override
	protected List<String> doPage(final int currentPosition, final int pageSize) {
		CodedNodeGraphService codedNodeGraphService =
			LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();

		if(tripleNode.equals(TripleNode.SUBJECT)) {
			return codedNodeGraphService.getTripleUidsContainingSubject(
					codingSchemeUri, 
					codingSchemeVersion, 
					relationsContainerName,
					associationPredicateName,
					entityCode,
					entityCodeNamespace,
					graphQuery,
					currentPosition, 
					pageSize);
		} else {
			return codedNodeGraphService.getTripleUidsContainingObject(
					codingSchemeUri, 
					codingSchemeVersion, 
					relationsContainerName,
					associationPredicateName,
					entityCode,
					entityCodeNamespace,
					graphQuery,
					currentPosition,
					pageSize);
		}
	}
}
