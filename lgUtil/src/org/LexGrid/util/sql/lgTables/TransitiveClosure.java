
package org.LexGrid.util.sql.lgTables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.util.sql.DBUtility;

/**
 * This class is used to process the transitive closure of the association table
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 */
public class TransitiveClosure {

    Connection connection;
    SQLTableUtilities sqlTblUtils;
    String codingScheme;
    LgMessageDirectorIF md;
    
    /**
     * private constructor only used for testing the algorithm within this class
     */
    private TransitiveClosure() {
        
    }

    /**
     * 
     * @param connection
     * @param sqlTblUtils
     * @param codingScheme
     * @param md
     */
    public TransitiveClosure(Connection connection, SQLTableUtilities sqlTblUtils, String codingScheme,
            LgMessageDirectorIF md) {
        this.connection = connection;
        this.sqlTblUtils = sqlTblUtils;
        this.codingScheme = codingScheme;
        this.md= md;

    }

    
    
    public void computeTransitivityTable() throws SQLException {
        SQLTableConstants stc_ = sqlTblUtils.getSQLTableConstants();

        // now, the fun part...
        PreparedStatement transitiveAssociationsStmt = connection.prepareStatement("Select "
                + stc_.containerNameOrContainerDC + ", " + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + ", "
                + stc_.entityCodeOrAssociationId + " from " + stc_.getTableName(SQLTableConstants.ASSOCIATION)
                + " Where " + SQLTableConstants.TBLCOL_ISTRANSITIVE + " = ? AND " + stc_.codingSchemeNameOrId + " = ?");

        DBUtility.setBooleanOnPreparedStatment(transitiveAssociationsStmt, 1, new Boolean(true));
        transitiveAssociationsStmt.setString(2, codingScheme);

        ArrayList<StringTriple> transitiveAssociationsTriple = new ArrayList<StringTriple>();

        ResultSet results = transitiveAssociationsStmt.executeQuery();
        while (results.next()) {
            StringTriple temp = new StringTriple();
            temp.container = results.getString(stc_.containerNameOrContainerDC);
            temp.code = results.getString(stc_.entityCodeOrAssociationId);
            temp.namespace = results.getString(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE);
            transitiveAssociationsTriple.add(temp);
        }
        results.close();
        transitiveAssociationsStmt.close();
        
        PreparedStatement allSourceAndTargetCodeStmt = connection.prepareStatement("SELECT "
                + stc_.sourceCSIdOrEntityCodeNS + ", " + stc_.sourceEntityCodeOrId + " FROM "
                + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + " WHERE "
                + stc_.codingSchemeNameOrId + " = ? and " + stc_.containerNameOrContainerDC + " = ? and "
                + stc_.entityCodeOrAssociationId + " = ? " + " UNION " + "SELECT " + stc_.targetCSIdOrEntityCodeNS
                + ", " + stc_.targetEntityCodeOrId + " FROM "
                + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + " WHERE "
                + stc_.codingSchemeNameOrId + " = ? and " + stc_.containerNameOrContainerDC + " = ? and "
                + stc_.entityCodeOrAssociationId + " = ? ");
        
        PreparedStatement getAllRelations = connection.prepareStatement("Select " + stc_.sourceCSIdOrEntityCodeNS
                + ", " + stc_.sourceEntityCodeOrId + ", " + stc_.targetCSIdOrEntityCodeNS + ", "
                + stc_.targetEntityCodeOrId + " from "
                + stc_.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + " where "
                + stc_.codingSchemeNameOrId + " = ? and " + stc_.containerNameOrContainerDC + " = ? and "
                + stc_.entityCodeOrAssociationId + " = ?");

        PreparedStatement insertTransitiveStmt = connection.prepareStatement(stc_
                .getInsertStatementSQL(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE));


        try {
            for (StringTriple assocTriple: transitiveAssociationsTriple) { 
                // get all the unique source and target codes for this
                // relationship - these become the vertices of the directed graph
                md.info("ComputeTransitive - Processing " + assocTriple.code);
                int i=1;
                allSourceAndTargetCodeStmt.setString(i++, codingScheme);
                allSourceAndTargetCodeStmt.setString(i++, assocTriple.container);
                allSourceAndTargetCodeStmt.setString(i++, assocTriple.code);
                allSourceAndTargetCodeStmt.setString(i++, codingScheme);
                allSourceAndTargetCodeStmt.setString(i++, assocTriple.container);
                allSourceAndTargetCodeStmt.setString(i++, assocTriple.code);

                results = allSourceAndTargetCodeStmt.executeQuery();

                List<StringTriple> nodeList = new ArrayList<StringTriple>();
                Map<StringTriple, Integer> nodeMap = new HashMap<StringTriple, Integer>();

                 
                while (results.next()) {
                    StringTriple temp = new StringTriple();
                    temp.namespace = results.getString(1);
                    temp.code = results.getString(2);                   
                    nodeList.add(temp);
                }
                results.close();
                
                for (int j = 0; j < nodeList.size(); j++) {
                    nodeMap.put(nodeList.get(j), j);
                }

                
                //We now get all the source target paths to build an adjacency matrix

                StringTriple sourceTriple = new StringTriple();
                StringTriple targetTriple = new StringTriple();
               
                getAllRelations.setString(1, codingScheme);
                getAllRelations.setString(2, assocTriple.container);
                getAllRelations.setString(3, assocTriple.code);
                results = getAllRelations.executeQuery();
                
                // Build the adjacency matrix
                //Refer to Warshall's algorithm for an explanation at
                //http://datastructures.itgo.com/graphs/transclosure.htm
                int max = nodeList.size();
                boolean adjacencyMatrix[][] = new boolean[max][max];

                while (results.next()) {
                    sourceTriple.namespace = results.getString(stc_.sourceCSIdOrEntityCodeNS);
                    sourceTriple.code = results.getString(stc_.sourceEntityCodeOrId);
                    targetTriple.namespace = results.getString(stc_.targetCSIdOrEntityCodeNS);
                    targetTriple.code = results.getString(stc_.targetEntityCodeOrId);
                    Object srcIndexObj = nodeMap.get(sourceTriple);
                    Object tgtIndexObj = nodeMap.get(targetTriple);
                    if (srcIndexObj != null && tgtIndexObj != null) {
                        int srcIndex = (Integer) srcIndexObj;
                        int tgtIndex = (Integer) tgtIndexObj;
                        adjacencyMatrix[srcIndex][tgtIndex] = true;
                    }
                }
                results.close();
                
                //Compute transitive closure of adjacency matrix
                boolean path[][] = warshallsAlgorithmForTransitiveClosure(adjacencyMatrix);
                //Insert the closure into the transitive table
                insertTransitiveClosure(assocTriple, insertTransitiveStmt, path, nodeList);


            }
        } finally {
            getAllRelations.close();
            insertTransitiveStmt.close();
            allSourceAndTargetCodeStmt.close();
        }

    }

    /**
     * Insert the results in the closureMatrix into the transitive table
     * @param assocTriple
     * @param insertTransitiveStmt
     * @param closureMatrix
     * @param nodeList
     * @throws SQLException
     */
    private void insertTransitiveClosure(StringTriple assocTriple, PreparedStatement insertTransitiveStmt, boolean[][] closureMatrix, List<StringTriple> nodeList) throws SQLException {
       
        int max= closureMatrix.length;
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                if (closureMatrix[i][j]) {
                    StringTriple srcTriple= nodeList.get(i);
                    StringTriple tgtTriple= nodeList.get(j);
                    try {
                        int k = 1;
                        insertTransitiveStmt.setString(k++, codingScheme);
                        insertTransitiveStmt.setString(k++, assocTriple.container);
                        insertTransitiveStmt.setString(k++, assocTriple.namespace);
                        insertTransitiveStmt.setString(k++, assocTriple.code);
                        insertTransitiveStmt.setString(k++, srcTriple.namespace);
                        insertTransitiveStmt.setString(k++, srcTriple.code);
                        insertTransitiveStmt.setString(k++, tgtTriple.namespace);
                        insertTransitiveStmt.setString(k++, tgtTriple.code);

                        insertTransitiveStmt.execute();
                       
                    } catch (SQLException e) {
                         md.debug(e.getMessage());
                        // assume an exception means that it is a duplicate
                        // error. ignore.
                        // cheaper to do this (in theory) than check ahead of
                        // time - duplicates should
                        // be abnormal, not the rule. And we have a cache now.
                    }
                }
            }
        }

               
    }

    
    
    
    


    /**
     * Implements Warshall's transitive closure algorithm
     * reference: http://datastructures.itgo.com/graphs/transclosure.htm
     */
    
    boolean[][] warshallsAlgorithmForTransitiveClosure(boolean adjmat[][]) {
        int max = adjmat.length;
       
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                if (adjmat[i][j]){
                    for (int k = 0; k < max; k++){
                        if (adjmat[j][k])
                            adjmat[i][k] = true;
                    }
                }
            }
        }
        return adjmat;
    }
    
    boolean[][] originalwarshallsAlgorithmForTransitiveClosure(boolean adjmat[][]) {
        int max = adjmat.length;
        boolean path[][] = new boolean[max][max];
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++)
                path[i][j] = adjmat[i][j];
        }

        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                if (path[i][j]){
                    for (int k = 0; k < max; k++){
                        if (path[j][k])
                            path[i][k] = true;
                    }
                }
            }
        }
        return path;
    }
    
    
    
    
    
    private class StringTriple {
        String container;
        String namespace;
        String code;

        public boolean equals(Object anObject) {
            if (this == anObject) {
                return true;
            }
            if (anObject instanceof StringTriple) {
                StringTriple other = (StringTriple) anObject;
                if (container == null) {
                    if (other.container != null)
                        return false;
                } else if (!container.equals(other.container))
                    return false;

                if (namespace == null) {
                    if (other.namespace != null)
                        return false;
                } else if (!namespace.equals(other.namespace))
                    return false;

                if (code == null) {
                    if (other.code != null)
                        return false;
                } else if (!code.equals(other.code))
                    return false;

                return true;
            }

            return false;
        }

        public int hashCode() {
            String str = container + namespace + code;
            return str.hashCode();
        }
        
        public String toString() {
            return "container= "+container +" namespace= "+ namespace+" code= "+code;
            
        }

    }
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        TransitiveClosure tc= new TransitiveClosure();
        tc.testTransitiveClosure();

    }

    void testTransitiveClosure() {
        List<String> nodeList = new ArrayList<String>();
        Map<String, Integer> nodeMap = new HashMap<String, Integer>();

        nodeList.add("A");
        nodeList.add("B");
        nodeList.add("C");
        nodeList.add("D");
        nodeList.add("E");

        for (int i = 0; i < nodeList.size(); i++) {
            nodeMap.put(nodeList.get(i), i);
        }

        int max = nodeList.size();
        boolean adjacencyMatrix[][] = new boolean[max][max];

        // Initialize adjacency matrix
        String[] connections = { "AB", "BC", "CD", "DE" };
        for (String str : connections) {
            String srcStr = str.substring(0, 1);
            String tgtStr = str.substring(1);
            Object srcIndexObj = nodeMap.get(srcStr);
            Object tgtIndexObj = nodeMap.get(tgtStr);
            if (srcIndexObj != null && tgtIndexObj != null) {
                int srcIndex = (Integer) srcIndexObj;
                int tgtIndex = (Integer) tgtIndexObj;
                adjacencyMatrix[srcIndex][tgtIndex] = true;
            }

        }

        boolean closureMatrix[][] = warshallsAlgorithmForTransitiveClosure(adjacencyMatrix);

        // Print the closure
        System.out.println("Printing closure");
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                if (closureMatrix[i][j]) {
                    System.out.println(nodeList.get(i) + nodeList.get(j));
                }
            }
        }

    }    
    

}