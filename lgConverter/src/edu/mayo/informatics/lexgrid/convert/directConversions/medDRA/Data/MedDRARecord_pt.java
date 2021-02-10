
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
public class MedDRARecord_pt implements Serializable, DatabaseEntityRecord{

	private static final long serialVersionUID = 1L;

	private String pt_code;
	private String pt_name;
	private String null_field;
	private String pt_soc_code;
	private String pt_whoart_code;
	private String pt_harts_code;
	private String pt_costart_sym;
	private String pt_icd9_code;
	private String pt_icd9cm_code;
	private String pt_icd10_code;
	private String pt_jart_code;

    private int[] validFieldIndices = {1, 2};
    private int[] invalidFieldIndices = {3,4,5,6,7,8,9,10,11};
	
    public String getPt_code() {
		return pt_code;
	}

	public void setPt_code(String pt_code) {
		this.pt_code = pt_code;
	}


	public String getPt_name() {
		return pt_name;
	}


	public void setPt_name(String pt_name) {
		this.pt_name = pt_name;
	}


	public String getNull_field() {
		return null_field;
	}


	public void setNull_field(String null_field) {
		this.null_field = null_field;
	}


	public String getPt_soc_code() {
		return pt_soc_code;
	}


	public void setPt_soc_code(String pt_soc_code) {
		this.pt_soc_code = pt_soc_code;
	}


	public String getPt_whoart_code() {
		return pt_whoart_code;
	}


	public void setPt_whoart_code(String pt_whoart_code) {
		this.pt_whoart_code = pt_whoart_code;
	}


	public String getPt_harts_code() {
		return pt_harts_code;
	}


	public void setPt_harts_code(String pt_harts_code) {
		this.pt_harts_code = pt_harts_code;
	}


	public String getPt_costart_sym() {
		return pt_costart_sym;
	}


	public void setPt_costart_sym(String pt_costart_sym) {
		this.pt_costart_sym = pt_costart_sym;
	}


	public String getPt_icd9_code() {
		return pt_icd9_code;
	}


	public void setPt_icd9_code(String pt_icd9_code) {
		this.pt_icd9_code = pt_icd9_code;
	}


	public String getPt_icd9cm_code() {
		return pt_icd9cm_code;
	}


	public void setPt_icd9cm_code(String pt_icd9cm_code) {
		this.pt_icd9cm_code = pt_icd9cm_code;
	}


	public String getPt_icd10_code() {
		return pt_icd10_code;
	}


	public void setPt_icd10_code(String pt_icd10_code) {
		this.pt_icd10_code = pt_icd10_code;
	}


	public String getPt_jart_code() {
		return pt_jart_code;
	}


	public void setPt_jart_code(String pt_jart_code) {
		this.pt_jart_code = pt_jart_code;
	}
    @Override
    public String getCode() {
        return this.pt_code;
    }

    @Override
    public String getName() {
        return pt_name;
    }


    // ------------
    @Override
    public List<Presentation> getPresentations() {
        List<Presentation> presentations = new ArrayList<Presentation>();

        presentations.add(MedDRARecord_Utils.createPresentation("T-1", this.pt_name, "PT", true));
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

        properties.add(MedDRARecord_Utils.createProperty("PRIMARY_SOC", this.pt_soc_code));
        
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