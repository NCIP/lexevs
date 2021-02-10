
package edu.mayo.informatics.lexgrid.convert.directConversions.medDRA.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Presentation;

/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public class MedDRARecord_hlt implements Serializable, DatabaseEntityRecord{

	private static final long serialVersionUID = 1L;
	
	private String hlt_code;
	private String hlt_name;
	private String hlt_whoart_code;
	private String hlt_harts_code;
	private String hlt_costart_sym;
	private String hlt_icd9_code;
	private String hlt_icd9cm_code;
	private String hlt_icd10_code;
	private String hlt_jart_code;

    private int[] validFieldIndices = {1,2};
    private int[] invalidFieldIndices = null;
    
	public String getHlt_code() {
		return hlt_code;
	}

	public void setHlt_code(String hlt_code) {
		this.hlt_code = hlt_code;
	}

	public String getHlt_name() {
		return hlt_name;
	}

	public void setHlt_name(String hlt_name) {
		this.hlt_name = hlt_name;
	}

	public String getHlt_whoart_code() {
		return hlt_whoart_code;
	}

	public void setHlt_whoart_code(String hlt_whoart_code) {
		this.hlt_whoart_code = hlt_whoart_code;
	}

	public String getHlt_harts_code() {
		return hlt_harts_code;
	}

	public void setHlt_harts_code(String hlt_harts_code) {
		this.hlt_harts_code = hlt_harts_code;
	}

	public String getHlt_costart_sym() {
		return hlt_costart_sym;
	}

	public void setHlt_costart_sym(String hlt_costart_sym) {
		this.hlt_costart_sym = hlt_costart_sym;
	}

	public String getHlt_icd9_code() {
		return hlt_icd9_code;
	}

	public void setHlt_icd9_code(String hlt_icd9_code) {
		this.hlt_icd9_code = hlt_icd9_code;
	}

	public String getHlt_icd9cm_code() {
		return hlt_icd9cm_code;
	}

	public void setHlt_icd9cm_code(String hlt_icd9cm_code) {
		this.hlt_icd9cm_code = hlt_icd9cm_code;
	}

	public String getHlt_icd10_code() {
		return hlt_icd10_code;
	}

	public void setHlt_icd10_code(String hlt_icd10_code) {
		this.hlt_icd10_code = hlt_icd10_code;
	}

	public String getHlt_jart_code() {
		return hlt_jart_code;
	}

	public void setHlt_jart_code(String hlt_jart_code) {
		this.hlt_jart_code = hlt_jart_code;
	}


    @Override
    public String getCode() {
        return this.hlt_code;
    }

    @Override
    public String getName() {
        return hlt_name;
    }

    @Override
    public List<Presentation> getPresentations() {
        List<Presentation> presentations = new ArrayList<Presentation>();

        presentations.add(MedDRARecord_Utils.createPresentation("T-1", this.hlt_name, "HT", true));
        
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
        
        return properties;
    }
    
    @Override
    public boolean fieldsValid() throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
        return MedDRARecord_Utils.fieldsValid(this, this.validFieldIndices);
    }
    
    @Override
    public String toString(){
        return MedDRARecord_Utils.recordToString(this, this.validFieldIndices, this.invalidFieldIndices);
    }    
}