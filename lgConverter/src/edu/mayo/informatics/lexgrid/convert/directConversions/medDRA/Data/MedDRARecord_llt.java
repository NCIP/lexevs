
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
public class MedDRARecord_llt implements Serializable, DatabaseEntityRecord, DatabaseMapRecord{

	private static final long serialVersionUID = 1L;
	
	private String llt_code;
	private String llt_name;
	private String pt_code;
	
	// Following fields not currently used by MedDRA
	private String llt_whoart_code;
	private String llt_harts_code;
	private String llt_costart_sym;
	private String llt_icd9_code;
	private String llt_icd9cm_code;
	private String llt_icd10_code;
	private String llt_currency;
	private String llt_jart_code;

    private int[] validFieldIndices = {1,2,3};
    private int[] invalidFieldIndices = {4,5,6,7,8,9,10,11};
    
    public String getInternalID() {
        return llt_code;
    }

	public String getLlt_code() {
		return llt_code;
	}


	public void setLlt_code(String llt_code) {
		this.llt_code = llt_code;
	}


	public String getLlt_name() {
		return llt_name;
	}


	public void setLlt_name(String llt_name) {
		this.llt_name = llt_name;
	}


	public String getPt_code() {
		return pt_code;
	}


	public void setPt_code(String pt_code) {
		this.pt_code = pt_code;
	}


	public String getLlt_whoart_code() {
		return llt_whoart_code;
	}


	public void setLlt_whoart_code(String llt_whoart_code) {
		this.llt_whoart_code = llt_whoart_code;
	}


	public String getLlt_harts_code() {
		return llt_harts_code;
	}


	public void setLlt_harts_code(String llt_harts_code) {
		this.llt_harts_code = llt_harts_code;
	}


	public String getLlt_costart_sym() {
		return llt_costart_sym;
	}


	public void setLlt_costart_sym(String llt_costart_sym) {
		this.llt_costart_sym = llt_costart_sym;
	}


	public String getLlt_icd9_code() {
		return llt_icd9_code;
	}


	public void setLlt_icd9_code(String llt_icd9_code) {
		this.llt_icd9_code = llt_icd9_code;
	}


	public String getLlt_icd9cm_code() {
		return llt_icd9cm_code;
	}


	public void setLlt_icd9cm_code(String llt_icd9cm_code) {
		this.llt_icd9cm_code = llt_icd9cm_code;
	}


	public String getLlt_icd10_code() {
		return llt_icd10_code;
	}


	public void setLlt_icd10_code(String llt_icd10_code) {
		this.llt_icd10_code = llt_icd10_code;
	}


	public String getLlt_currency() {
		return llt_currency;
	}


	public void setLlt_currency(String llt_currency) {
		this.llt_currency = llt_currency;
	}


	public String getLlt_jart_code() {
		return llt_jart_code;
	}


	public void setLlt_jart_code(String llt_jart_code) {
		this.llt_jart_code = llt_jart_code;
	}


    @Override
    public String getCode() {
        return this.llt_code;
    }

    @Override
    public String getName() {
        return this.llt_name;
    }

    @Override
    public List<Presentation> getPresentations() {
        List<Presentation> presentations = new ArrayList<Presentation>();

        presentations.add(MedDRARecord_Utils.createPresentation("T-1", this.llt_name, "LT", true));
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
    public String getSource() {
        return this.pt_code;
    }

    @Override
    public String getTarget() {
        return this.llt_code;
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