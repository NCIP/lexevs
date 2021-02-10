
package edu.mayo.informatics.lexgrid.convert.directConversions.medDRA.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Presentation;

/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public class MedDRARecord_meddra_history implements Serializable, DatabaseEntityRecord{

	private static final long serialVersionUID = 1L;
	
	private String term_code;
	private String term_name;
	private String term_addition_version;
	private String term_type;
	private String llt_currency;
	private String action;

    private int[] validFieldIndices = null;
    private int[] invalidFieldIndices = {1,2,3,4,5,6};
    
	public String getTerm_code() {
		return term_code;
	}


	public void setTerm_code(String term_code) {
		this.term_code = term_code;
	}


	public String getTerm_name() {
		return term_name;
	}


	public void setTerm_name(String term_name) {
		this.term_name = term_name;
	}


	public String getTerm_addition_version() {
		return term_addition_version;
	}


	public void setTerm_addition_version(String term_addition_version) {
		this.term_addition_version = term_addition_version;
	}


	public String getTerm_type() {
		return term_type;
	}


	public void setTerm_type(String term_type) {
		this.term_type = term_type;
	}


	public String getLlt_currency() {
		return llt_currency;
	}


	public void setLlt_currency(String llt_currency) {
		this.llt_currency = llt_currency;
	}


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}
    
    @Override
    public boolean fieldsValid() throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
        return MedDRARecord_Utils.fieldsValid(this, this.validFieldIndices);
    }
    
    @Override
    public String toString(){
        return MedDRARecord_Utils.recordToString(this, this.validFieldIndices, this.invalidFieldIndices);
    }


    @Override
    public String getCode() {
        // TODO Auto-generated method stub
        return this.term_code;
    }


    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return this.term_name;
    }


    @Override
    public List<Presentation> getPresentations() {
       List<Presentation> presentations = new ArrayList<Presentation>();
        return presentations;
    }


    @Override
    public List<Definition> getDefinitions() {
        List<Definition> definitions = new ArrayList<Definition>();
        return definitions;
    }


    @Override
    public List<Comment> getComments() {
        List<Comment> comments = new ArrayList<Comment>();
        return comments;
    }


    @Override
    public List<Property> getProperties() {
        List<Property> properties = new ArrayList<Property>();
        Property property = MedDRARecord_Utils.createProperty("EDIT_ACTION", this.action);
        PropertyQualifier pq  = MedDRARecord_Utils.createPropertyQualifier("term_name", this.term_name);
        property.getPropertyQualifierAsReference().add(pq);
        pq  = MedDRARecord_Utils.createPropertyQualifier("term_addition_version", this.term_addition_version);
        property.getPropertyQualifierAsReference().add(pq);
        pq  = MedDRARecord_Utils.createPropertyQualifier("term_type", this.term_type);
        property.getPropertyQualifierAsReference().add(pq);
        pq  = MedDRARecord_Utils.createPropertyQualifier("llt_currency", this.llt_currency);
        property.getPropertyQualifierAsReference().add(pq);
        properties.add(property);

        return properties;
    }    
}