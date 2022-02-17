
package edu.mayo.informatics.lexgrid.convert.directConversions.medDRA.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Presentation;

import edu.mayo.informatics.lexgrid.convert.directConversions.medDRA.MedDRA2LGConstants;

/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public class MedDRARecord_soc implements Serializable, DatabaseEntityRecord, DatabaseMapRecord{

	private static final long serialVersionUID = 1L;
	
	private String soc_code;
	private String soc_name;
	private String soc_abbrev;
	private String soc_whoart_code;
	private String soc_harts_code;
	private String soc_costart_sym;
	private String soc_icd9_code;
	private String soc_icd9cm_code;
	private String soc_icd10_code;
	private String soc_jart_code;

    private String intlOrder = null;

    private int[] validFieldIndices = {1,2,3};
    private int[] invalidFieldIndices = {4,5,6,7,8,9,10};
    
	public String getSoc_code() {
		return soc_code;
	}

	public void setSoc_code(String soc_code) {
		this.soc_code = soc_code;
	}

	public String getSoc_name() {
		return soc_name;
	}

	public void setSoc_name(String soc_name) {
		this.soc_name = soc_name;
	}

	public String getSoc_abbrev() {
		return soc_abbrev;
	}

	public void setSoc_abbrev(String soc_abbrev) {
		this.soc_abbrev = soc_abbrev;
	}

	public String getSoc_whoart_code() {
		return soc_whoart_code;
	}

	public void setSoc_whoart_code(String soc_whoart_code) {
		this.soc_whoart_code = soc_whoart_code;
	}

	public String getSoc_harts_code() {
		return soc_harts_code;
	}

	public void setSoc_harts_code(String soc_harts_code) {
		this.soc_harts_code = soc_harts_code;
	}

	public String getSoc_costart_sym() {
		return soc_costart_sym;
	}

	public void setSoc_costart_sym(String soc_costart_sym) {
		this.soc_costart_sym = soc_costart_sym;
	}

	public String getSoc_icd9_code() {
		return soc_icd9_code;
	}

	public void setSoc_icd9_code(String soc_icd9_code) {
		this.soc_icd9_code = soc_icd9_code;
	}

	public String getSoc_icd9cm_code() {
		return soc_icd9cm_code;
	}

	public void setSoc_icd9cm_code(String soc_icd9cm_code) {
		this.soc_icd9cm_code = soc_icd9cm_code;
	}

	public String getSoc_icd10_code() {
		return soc_icd10_code;
	}

	public void setSoc_icd10_code(String soc_icd10_code) {
		this.soc_icd10_code = soc_icd10_code;
	}

	public String getSoc_jart_code() {
		return soc_jart_code;
	}

	public void setSoc_jart_code(String soc_jart_code) {
		this.soc_jart_code = soc_jart_code;
	}
	
	public void setIntlOrder(String order){
	    this.intlOrder = order;
	}

    @Override
    public String getCode() {
        return soc_code;
    }

    @Override
    public String getName() {
        return soc_name;
    }
    
    @Override
    public List<Presentation> getPresentations() {
        List<Presentation> presentations = new ArrayList<Presentation>();

        presentations.add(MedDRARecord_Utils.createPresentation("T-1", this.soc_name, "OS", true));
                
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
        
        properties.add(MedDRARecord_Utils.createProperty("P-1", this.intlOrder));
        properties.add(MedDRARecord_Utils.createProperty("P-2", this.soc_abbrev));

        return properties;
    }

    @Override
    public String getSource() {
       return MedDRA2LGConstants.TOP_NODE_SOC;
    }

    @Override
    public String getTarget() {
        return this.soc_code;
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