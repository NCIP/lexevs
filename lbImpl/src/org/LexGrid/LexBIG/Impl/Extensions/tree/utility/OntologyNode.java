
package org.LexGrid.LexBIG.Impl.Extensions.tree.utility;

import java.util.List;

public class OntologyNode {
    
    private int ontology_node_child_count;
    
    private String ontology_node_id;
    
    private String ontology_node_name;
    
    private String ontology_node_ns; 

    private List<OntologyNode> children_nodes;

/**
     * @return the ontology_node_child_count
     */
public int getOntology_node_child_count() {
        return ontology_node_child_count;
    }

    /**
     * @param ontology_node_child_count the ontology_node_child_count to set
     */
    public void setOntology_node_child_count(int ontology_node_child_count) {
        this.ontology_node_child_count = ontology_node_child_count;
    }

    /**
     * @return the ontology_node_id
     */
    public String getOntology_node_id() {
        return ontology_node_id;
    }

    /**
     * @param ontology_node_id the ontology_node_id to set
     */
    public void setOntology_node_id(String ontology_node_id) {
        this.ontology_node_id = ontology_node_id;
    }

    /**
     * @return the ontology_node_name
     */
    public String getOntology_node_name() {
        return ontology_node_name;
    }

    /**
     * @param ontology_node_name the ontology_node_name to set
     */
    public void setOntology_node_name(String ontology_node_name) {
        this.ontology_node_name = ontology_node_name;
    }

    /**
     * @return the ontology_node_ns
     */
    public String getOntology_node_ns() {
        return ontology_node_ns;
    }

    /**
     * @param ontology_node_ns the ontology_node_ns to set
     */
    public void setOntology_node_ns(String ontology_node_ns) {
        this.ontology_node_ns = ontology_node_ns;
    }

    /**
     * @return the children_nodes
     */
    public List<OntologyNode> getChildren_nodes() {
        return children_nodes;
    }

    /**
     * @param children_nodes the children_nodes to set
     */
    public void setChildren_nodes(List<OntologyNode> children_nodes) {
        this.children_nodes = children_nodes;
    }

}