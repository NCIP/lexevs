
package edu.mayo.informatics.lexgrid.convert.directConversions.medDRA.Data;

import java.io.Serializable;

/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public class MedDRARecord_hlt_pt implements Serializable, DatabaseMapRecord{

	private static final long serialVersionUID = 1L;
	
	private String hlt_code;
	private String pt_code;

    private int[] validFieldIndices = {1,2};
    private int[] invalidFieldIndices = null;
	
	public String getHlt_code() {
		return hlt_code;
	}

	public void setHlt_code(String hlt_code) {
		this.hlt_code = hlt_code;
	}

	public String getPt_code() {
		return pt_code;
	}

	public void setPt_code(String pt_code) {
		this.pt_code = pt_code;
	}

    @Override
    public String getSource() {
        return hlt_code;
    }

    @Override
    public String getTarget() {
        return pt_code;
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