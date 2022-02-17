
package org.lexevs.paging.codednodegraph;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.annotations.LgClientSideSafe;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao.TripleNode;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.database.utility.DaoUtility.SortContainer;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.paging.AbstractPageableIterator;

@LgClientSideSafe
public class TripleUidIterator extends AbstractPageableIterator<String>{

	private static final long serialVersionUID = 5700989835363272357L;
	
	private String codingSchemeUri;
	private String codingSchemeVersion;
	private String relationsContainerName;
	private String associationPredicateName;
	private String entityCode;
	private String entityCodeNamespace;
	private GraphQuery graphQuery;
	private TripleNode tripleNode;
	private SortOptionList sortAlgorithms;

	public TripleUidIterator(
			String codingSchemeUri, 
			String codingSchemeVersion, 
			String relationsContainerName,
			String associationPredicateName,
			String entityCode,
			String entityCodeNamespace,
			GraphQuery graphQuery,
			TripleNode tripleNode,
			SortOptionList sortAlgorithms,
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
		this.sortAlgorithms = sortAlgorithms;
	}
	@Override
	protected List<String> doPage(final int currentPosition, final int pageSize) {
		CodedNodeGraphService codedNodeGraphService =
			LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();

		SortContainer sortContainer = DaoUtility.mapSortOptionListToSort(sortAlgorithms);
		
		if(tripleNode.equals(TripleNode.SUBJECT)) {
			return codedNodeGraphService.getTripleUidsContainingSubject(
					codingSchemeUri, 
					codingSchemeVersion, 
					relationsContainerName,
					associationPredicateName,
					entityCode,
					entityCodeNamespace,
					graphQuery,
					sortContainer.getSorts(),
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
					sortContainer.getSorts(),
					currentPosition,
					pageSize);
		}
	}
}