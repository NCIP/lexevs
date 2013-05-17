package org.lexevs.graph.dao;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.constants.SystemVariables;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class LexEVSGraphDaoImpl extends AbstractExtendable implements LexEVSGraphDao, GenericExtension {

	private static final long serialVersionUID = 5199389242454319608L;
	private String name = "LexEVSGraphDao";
	private String description = "Graph database extension for Hierarchy Traversal";
	private String provider = "Mayo Foundation";
	private String version = "1.0";
	
	OrientDbGraphDao graph;
	
	public LexEVSGraphDaoImpl(){
		super();
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
		List<ConceptReference> refs = null;
		refs = new ArrayList<ConceptReference>();
		List<ODocument> docs = graph.getDocumentResultForSql(graph.getAllNodesForFocusCodeAndAssoc("C3262", "subClassOf"));
		for(ODocument o: docs){
		refs.add(graph.getConceptReferenceForVertex(o));
		}
		return refs;
	}

	@Override
	public List<ConceptReference> getAllIncomingRelationsForNode(
			ConceptReference node, String associationName) {
		List<ConceptReference> refs = null;
		try{
		refs = new ArrayList<ConceptReference>();
		List<OrientVertex> docs = graph.getVertexResultForSql(graph.getAllEdgesInForCode(node.getCode(), associationName));
		for(OrientVertex e: docs){
			ConceptReference ref = graph.getConceptReferenceForVertex(e.getRecord());
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
	@Override
	protected void doRegister(ExtensionRegistry registry,
			ExtensionDescription ed) throws LBParameterException {
		  registry.registerGenericExtension(ed);
	}
	
	@Override
	protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setDescription(description);
        ed.setExtensionBaseClass(GenericExtension.class.getName());
        ed.setExtensionClass(LexEVSGraphDaoImpl.class.getName());
        ed.setName(name);
        ed.setVersion(version);
        
        return ed;
	}

}
