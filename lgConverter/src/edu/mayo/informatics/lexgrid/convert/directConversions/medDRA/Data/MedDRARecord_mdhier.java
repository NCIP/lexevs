
package edu.mayo.informatics.lexgrid.convert.directConversions.medDRA.Data;

import java.io.Serializable;

/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public class MedDRARecord_mdhier implements Serializable, DatabaseRecord{

	private static final long serialVersionUID = 1L;
	
	private String pt_code;
	private String hlt_code;
	private String hlgt_code;
	private String soc_code;
	private String pt_name;
	private String hlt_name;
	private String hlgt_name;
	private String soc_name;
	private String soc_abbrev;
	private String null_field;
	private String pt_soc_code;
	private String primary_soc_fg;

    private int[] validFieldIndices = null;
    private int[] invalidFieldIndices = {1,2,3,4,5,6,7,8,9,10,11,12};
    
	public String getPt_code() {
		return pt_code;
	}

	public void setPt_code(String pt_code) {
		this.pt_code = pt_code;
	}

	public String getHlt_code() {
		return hlt_code;
	}

	public void setHlt_code(String hlt_code) {
		this.hlt_code = hlt_code;
	}

	public String getHlgt_code() {
		return hlgt_code;
	}

	public void setHlgt_code(String hlgt_code) {
		this.hlgt_code = hlgt_code;
	}

	public String getSoc_code() {
		return soc_code;
	}

	public void setSoc_code(String soc_code) {
		this.soc_code = soc_code;
	}

	public String getPt_name() {
		return pt_name;
	}

	public void setPt_name(String pt_name) {
		this.pt_name = pt_name;
	}

	public String getHlt_name() {
		return hlt_name;
	}

	public void setHlt_name(String hlt_name) {
		this.hlt_name = hlt_name;
	}

	public String getHlgt_name() {
		return hlgt_name;
	}

	public void setHlgt_name(String hlgt_name) {
		this.hlgt_name = hlgt_name;
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

	public String getPrimary_soc_fg() {
		return primary_soc_fg;
	}

	public void setPrimary_soc_fg(String primary_soc_fg) {
		this.primary_soc_fg = primary_soc_fg;
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