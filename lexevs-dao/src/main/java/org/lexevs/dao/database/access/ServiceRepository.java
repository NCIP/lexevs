package org.lexevs.dao.database.access;

import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao;
import org.lexevs.dao.database.access.codednodeset.CodedNodeSetDao;

public class ServiceRepository {

	private CodedNodeSetDao codedNodeSetDao;
	
	private CodedNodeGraphDao codedNodeGraphDao;

	public CodedNodeSetDao getCodedNodeSetDao() {
		return codedNodeSetDao;
	}

	public void setCodedNodeSetDao(CodedNodeSetDao codedNodeSetDao) {
		this.codedNodeSetDao = codedNodeSetDao;
	}

	public CodedNodeGraphDao getCodedNodeGraphDao() {
		return codedNodeGraphDao;
	}

	public void setCodedNodeGraphDao(CodedNodeGraphDao codedNodeGraphDao) {
		this.codedNodeGraphDao = codedNodeGraphDao;
	}
}
