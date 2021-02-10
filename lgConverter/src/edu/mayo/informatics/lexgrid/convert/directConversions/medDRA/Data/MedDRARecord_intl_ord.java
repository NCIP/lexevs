
package edu.mayo.informatics.lexgrid.convert.directConversions.medDRA.Data;

import java.io.Serializable;

/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public class MedDRARecord_intl_ord implements Serializable, DatabaseRecord{

	private static final long serialVersionUID = 1L;
	
	private String intl_ord_code;
	private String soc_code;

    private int[] validFieldIndices = {1,2};
    private int[] invalidFieldIndices = null;
    
	public String getIntl_ord_code() {
		return intl_ord_code;
	}

	public void setIntl_ord_code(String intl_ord_code) {
		this.intl_ord_code = intl_ord_code;
	}

	public String getSoc_code() {
		return soc_code;
	}

	public void setSoc_code(String soc_code) {
		this.soc_code = soc_code;
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