package org.lexevs.paging.codednodegraph;

import java.util.List;

import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao.IndividualDaoCallback;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao.TripleNode;
import org.lexevs.paging.AbstractPageableIterator;

public class TripleUidIterator extends AbstractPageableIterator<String>{

	private CodedNodeGraphDao codedNodeGraphDao;
	private String codingSchemeUid;
	private String associationPredicateUid;
	private String entityCode;
	private String entityCodeNamespace;
	private TripleNode tripleNode;

	public TripleUidIterator(
			CodedNodeGraphDao codedNodeGraphDao, 
			String codingSchemeUid, 
			String associationPredicateUid, 
			String entityCode,
			String entityCodeNamespace,
			TripleNode tripleNode,
			int pageSize) {
		super(pageSize);
		this.codedNodeGraphDao = codedNodeGraphDao;
		this.codingSchemeUid = codingSchemeUid;
		this.associationPredicateUid = associationPredicateUid;
		this.entityCode = entityCode;
		this.entityCodeNamespace = entityCodeNamespace;
		this.tripleNode = tripleNode;
	}
	@Override
	protected List<String> doPage(final int currentPosition, final int pageSize) {

		return codedNodeGraphDao.executeInTransaction(new IndividualDaoCallback<List<String>>(){

			@Override
			public List<String> execute() {
				if(tripleNode.equals(TripleNode.SUBJECT)) {
					return codedNodeGraphDao.getTripleUidsContainingSubject(
							codingSchemeUid, 
							associationPredicateUid, 
							entityCode,
							entityCodeNamespace,
							currentPosition, 
							pageSize);
				} else {
					return codedNodeGraphDao.getTripleUidsContainingObject(
							codingSchemeUid, 
							associationPredicateUid, 
							entityCode,
							entityCodeNamespace,
							currentPosition, 
							pageSize);
				}
			}
		});
	}
}
