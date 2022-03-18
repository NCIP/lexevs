
package edu.mayo.informatics.lexgrid.convert.directConversions.medDRA.Data;

import java.io.Serializable;

/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public class MedDRARecord_smq_content implements Serializable, DatabaseMapRecord{

	private static final long serialVersionUID = 1L;
	
	private String smq_code;
	private String term_code;
	private String term_level;
	private String term_scope;
	private String term_category;
	private String term_weight;
    private String term_status;
    private String term_addition_version;
    private String term_last_modified_version;

    private int [] validFieldIndices = {1,2,3,4,5,6,7,8,9};
    private int [] invalidFieldIndices = null;
    
    public String getSmq_code() {
		return smq_code;
	}

	public void setSmq_code(String smq_code) {
		this.smq_code = smq_code;
	}

	public String getTerm_code() {
		return term_code;
	}

	public void setTerm_code(String term_code) {
		this.term_code = term_code;
	}

	public String getTerm_level() {
		return term_level;
	}

	public void setTerm_level(String term_level) {
		this.term_level = term_level;
	}

	public String getTerm_scope() {
		return term_scope;
	}

	public void setTerm_scope(String term_scope) {
		this.term_scope = term_scope;
	}

	public String getTerm_category() {
		return term_category;
	}

	public void setTerm_category(String term_category) {
		this.term_category = term_category;
	}

	public String getTerm_weight() {
		return term_weight;
	}

	public void setTerm_weight(String term_weight) {
		this.term_weight = term_weight;
	}

	public String getTerm_status() {
		return term_status;
	}

	public void setTerm_status(String term_status) {
		this.term_status = term_status;
	}

	public String getTerm_addition_version() {
		return term_addition_version;
	}

	public void setTerm_addition_version(String term_addition_version) {
		this.term_addition_version = term_addition_version;
	}

	public String getTerm_last_modified_version() {
		return term_last_modified_version;
	}

	public void setTerm_last_modified_version(String term_last_modified_version) {
		this.term_last_modified_version = term_last_modified_version;
	}

    @Override
    public String getSource() {
        return this.smq_code;
    }

    @Override
    public String getTarget() {
        return this.term_code;
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