package org.lexevs.graph.load.connect;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.access.association.model.graphdb.GraphDbTriple;

import com.orientechnologies.orient.core.db.ODatabase.STATUS;
import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.db.graph.OGraphDatabasePool;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.intent.OIntentMassiveInsert;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

public class OrientDbGraphDbConnect implements GraphDataBaseConnect {
	
	private OGraphDatabase orientDB = null;
	
	ODocument source;
	ODocument target;
	ODocument edge;
	
	public OrientDbGraphDbConnect(String user, String password, String dbPath){
		orientDB = new OGraphDatabase("local:" + dbPath);
		if(orientDB.exists() && orientDB.isClosed()){
			orientDB.open(user, password);}
		else{
			//automatically opens the database
			createDatabase(dbPath);
			}
	}
	
	public boolean verifyDatabase(){
		return orientDB.exists();
	}

	public ODocument getSource() {
		return source;
	}


	public void setSource(ODocument source) {
		this.source = source;
	}


	public ODocument getTarget() {
		return target;
	}


	public void setTarget(ODocument target) {
		this.target = target;
	}


	public ODocument getEdge() {
		return edge;
	}


	public void setEdge(ODocument edge) {
		this.edge = edge;
	}

	

	
	
	private void createDatabase(String dbPath) {
		orientDB = new OGraphDatabase("local:" + dbPath);
		orientDB.create();
		orientDB.declareIntent(new OIntentMassiveInsert());
	}
	
	//initialize vertices and edges and reuse them
	//to save memory
	public void initVerticesAndEdge(){
		source = orientDB.createVertex();
		target = orientDB.createVertex();
		edge = orientDB.createEdge(source, target);
	}


	@Override
	public OGraphDatabase openForWrite(String dbPath) {
		File dir = new File(dbPath);
		if(!dir.exists()){
			System.out.println("Database does not exist");
			return null;
		}
		orientDB = new OGraphDatabase("local:" + dbPath);
		return orientDB.open("admin",  "admin");
	}
	
	@Override
	public OGraphDatabase openForRead(String dbPath) {
		File dir = new File(dbPath);
		if(!dir.exists()){
			System.out.println("Database does not exist");
			return null;
		}
		return getGraphDbFromPool(dbPath, "reader", "reader");
	}
	
	@Override
	public OGraphDatabase getGraphDbFromPool(String dbPath, String user, String password) {
		return OGraphDatabasePool.global().acquire("local:" + dbPath, user, password);
	}

	@Override
	public void close(OGraphDatabase database) {
		database.close();
	}
	
	public boolean close(){
		boolean closed = false;
		orientDB.close();
		if(orientDB.isClosed())
			{closed = true;}
		return closed;
	}
	
	public void commit(){
		orientDB.commit();
	}

	@Override
	public void delete(String dbPath) {
		File dir = new File(dbPath);
		if (dir.exists()) {
			System.out.println("exists,,,,, deleting");
			try {
				FileUtils.deleteDirectory(dir);
			} catch (Exception e) {
				System.out.println("Unable to delete old version of database: "	+ dbPath);
				throw new RuntimeException(e);
			}
		}
	}


	
	@Override
	public OClass createVertexTable(String table, List<String> fieldnames) {
		return createTable("vertex", table, fieldnames);
	}

	@Override
	public OClass createEdgeTable(String table, List<String> fieldnames) {
		return createTable("edge", table, fieldnames);
	}
	
	protected OClass createTable(String type, String table,
			List<String> fieldnames){
		OClass tableId = null;
		OSchema schema = orientDB.getMetadata().getSchema();
		if(type.equals("edge")){
			tableId = orientDB.createEdgeType(table);
			createTableProperties(schema, table, fieldnames);
		}
		else{
			tableId = orientDB.createVertexType(table);
			createTablePropertiesAndIndexes(schema, table, fieldnames);
		}
		return tableId;
	}
	
	public void createTablePropertiesAndIndexes(OSchema schema, String table, List<String> fieldnames){
		OClass oClass = schema.getClass(table);
		for (int j = 0; j < fieldnames.size(); j++) {
			oClass.createProperty(fieldnames.get(j), OType.STRING);
		}
		oClass.createIndex("conceptCode",OClass.INDEX_TYPE.UNIQUE, "code");
		schema.save();	
		
	}
	
	public void createTableProperties(OSchema schema, String table, List<String> fieldnames){
		OClass oClass = schema.getClass(table);
		for (int j = 0; j < fieldnames.size(); j++) {
			oClass.createProperty(fieldnames.get(j), OType.STRING);
		}
		schema.save();	
	}
	
	@Override	
	public void storeGraphTriple(GraphDbTriple triple, String vertexTableName, String edgeTableName){
		sourceSet(triple, vertexTableName);
		targetSet(triple, vertexTableName);
		edgeSet(triple, edgeTableName);
		addVertexOut(source, edge);
		addVertexIn(target, edge);
	}
	
	@SuppressWarnings("unchecked")
	public ODocument getVertexForCode(String code, String vertexTableName){
		String sql = "select from " + vertexTableName + " where code = " + "\"" + code + "\"";
		List<ODocument> docs= (List<ODocument>)orientDB
				.query(new OSQLSynchQuery<ODocument>(sql));
		if(docs.size() > 0)
			return docs.get(0);
			else{
				return null;
			}
	}
	
	public void addVertexOut(ODocument od, ODocument eDoc){
		String sql = "update " + od.getIdentity() + " add out = "
				+ eDoc.getIdentity();
		orientDB.command(new OCommandSQL(sql)).execute();
	}
	
	public void addVertexIn(ODocument od, ODocument eDoc){
		String sql = "update " + od.getIdentity() + " add in = "
				+ eDoc.getIdentity();
		orientDB.command(new OCommandSQL(sql)).execute();
	}
	
	
	
	private void sourceSet(GraphDbTriple triple, String vertexTableName){
		String code = triple.getSourceEntityCode();
		ODocument tempsource = getVertexForCode(code, vertexTableName);
		if(tempsource == null){
		source.reset();
		source.setClassName(vertexTableName);
		source.getIdentity().reset();
		source.field("code", triple.getSourceEntityCode());
		source.field("namespace", triple.getSourceEntityNamespace());
		source.field("uri", triple.getSourceSchemeUri());
		source.field("version", triple.getSourceSchemeVersion());
		source.field("description", triple.getSourceDescription());
		source.save();
		}
		else{
			source = tempsource;
		}
	}
	
	public boolean recordVertexIfNotRecorded(Triple triple, String vertexTableName){
		
		if(getVertexForCode(triple.getSourceEntityCode() != null?
				triple.getSourceEntityCode():
					triple.getTargetEntityCode(), vertexTableName) == null){
		return false;
	}
		else{return true;}
	}
	
	private void targetSet(GraphDbTriple triple, String vertexTableName){
		String code = triple.getTargetEntityCode();
		ODocument temptarget = getVertexForCode(code, vertexTableName);
		if(temptarget == null){
		target.reset();
		target.setClassName(vertexTableName);
		target.getIdentity().reset();
		target.field("code", triple.getTargetEntityCode());
		target.field("namespace", triple.getTargetEntityNamespace());
		target.field("uri", triple.getTargetSchemeUri());
		target.field("version", triple.getTargetSchemeVersion());
		target.field("predicateId", triple.getAssociationPredicateId());
		target.field("description", triple.getTargetDescription());
		target.save();}
		else{
			target = temptarget;
		}
	}
	private void edgeSet(GraphDbTriple triple, String edgeTableName){
		edge.reset();
		edge.setClassName(edgeTableName);
		edge.getIdentity().reset();
		edge.field("predicateId", triple.getAssociationPredicateId());
		edge.field("predicateName", triple.getAssociationName());
		edge.field("entityAssnEntityGuid", triple.getEntityAssnsGuid());
		edge.field("anonymousStatus",triple.getAnonymousStatus());
		edge.field("associationInstanceId", triple.getAssociationInstanceId());
		edge.field("in", source);
		edge.field("out", target);
		edge.save();
	}
	
		

	
	public List<String> getFieldNamesForVertex(){
		return  Arrays.asList("code", "namespace", "uri",
				"version", "predicateId", "description",
				"predicateName");
	}
	
	public List<String> getFieldNamesForEdge(){
		return  Arrays.asList( "predicateId", "predicateName", "associationInstanceId",
				"anonymousStatus", "entityAssnEntityGuid");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OrientDbGraphDbConnect db = null;
		// TODO Auto-generated method stub
//		String sourceUri = "1.11.111.11111.1";
//		String sourceVersion = "1.0";
//		String targetUri = "1.11.111.11111.1";
//		String targetVersion = "1.0";
//		String associationName = "subClassOf";
//		try{
		db = new OrientDbGraphDbConnect("admin", "admin", "/Users/m029206/software/orientdb-1.3.0/databases/thesGraph");
//
//		//db.createDatabase("/Users/m029206/software/orientdb-1.3.0/databases/testGraph");
////		//OGraphDatabase database = db.getGraphDbFromPool("/Users/m029206/software/orientdb-1.3.0/databases/testGraph", "admin", "admin");
//		System.out.println("database exists?: " + db);
//		List<String> vertexFieldList = db.getFieldNamesForVertex();
//		List<String> edgeFieldList = db.getFieldNamesForEdge();
//		OClass vertexTable = db.createVertexTable("vertexTable", vertexFieldList);
//		OClass edgeTable = db.createEdgeTable("edgeTable", edgeFieldList);
//		db.initVerticesAndEdge();
//		List<TriplePlus> triples = db.generateTriplesPlus(10000);
//		for(TriplePlus t: triples){
//			db.storeTriple(t, vertexTable.getName(), edgeTable.getName());
//		}
//		System.out.println("is closed? " + db.close());
		db.close();
		db.delete("/Users/m029206/software/orientdb-1.3.0/databases/thesGraph");
//		db.delete("/Users/m029206/software/orientdb-1.3.0/databases/testGraph");
//		System.out.println("database exists after delete?: " + db.orientDB.exists());
//		}
//			finally{
//			if(db != null){
//				db.close();
//				db.delete("/Users/m029206/software/orientdb-1.3.0/databases/testGraph");
//			}
//			}
	
    db.verifyDatabase();
	}


	@Override
	public Object storeVertex(String table, String entityCode,
			String entityNamespace) {
		// TODO Auto-generated method stub
		return null;
	}









}
