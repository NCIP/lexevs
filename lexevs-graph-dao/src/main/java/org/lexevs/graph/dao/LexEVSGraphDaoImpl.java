package org.lexevs.graph.dao;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.constants.SystemVariables;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;

public class LexEVSGraphDaoImpl implements LexEVSGraphDao, GenericExtension {

	private static final long serialVersionUID = 5199389242454319608L;
	private String name = "LexEVSGraphDao";
	private String description = "Graph database extension for Hierarchy Traversal";
	private String provider = "Mayo Foundation";
	private String version = "1.0";
	
	OrientDbGraphDao graph;
	
	public LexEVSGraphDaoImpl(){
		graph = getNewGraphInstance();
	}
	private OrientDbGraphDao getNewGraphInstance() {
		SystemVariables vars = LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables();
		String user = vars.getGraphdbUser();
		String password = vars.getGraphdbpwd();
		String path = vars.getGraphdbUrl();
		return new OrientDbGraphDao(user, password, path);
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	@Override
	public String getProvider() {
		// TODO Auto-generated method stub
		return provider;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return version;
	}

	@Override
	public List<ConceptReference> getTransitiveClosureForNode(
			ConceptReference nodes, String associationName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<ConceptReference> getAllIncomingRelationsForNode(
			ConceptReference node, String associationName) {
		List<ConceptReference> refs = null;
		try{
		refs = new ArrayList<ConceptReference>();
		List<OrientEdge> docs = graph.getResultForSql(graph.getAllEdgesInForCode(node.getCode(), associationName));
		for(OrientEdge e: docs){
			ConceptReference ref = graph.getConceptReferenceForJSON((ODocument) e.getOutVertex());
			refs.add(ref);
		}
		}
		finally{
		graph.close();
		}
		return refs;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LexEVSGraphDaoImpl gd = new LexEVSGraphDaoImpl();
		ConceptReference ref = new ConceptReference();
		ref.setCode("C14225");
		List<ConceptReference> refs = gd.getAllIncomingRelationsForNode(ref, "Gene_Found_In_Organism");
		int counter = 0;
		for(ConceptReference r: refs){
			System.out.println(r.getCode());
			counter++;
		}
		System.out.println("Number of concept references: " + counter);
		
	}

}
