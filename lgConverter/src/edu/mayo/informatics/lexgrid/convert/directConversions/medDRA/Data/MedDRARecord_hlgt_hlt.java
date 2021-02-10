
package edu.mayo.informatics.lexgrid.convert.directConversions.medDRA.Data;

import java.io.Serializable;

/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public class MedDRARecord_hlgt_hlt implements Serializable, DatabaseMapRecord{

	private static final long serialVersionUID = 1L;
	
	private String hlgt_code;
	private String hlt_code;

    private int[] validFieldIndices = {1,2};
    private int[] invalidFieldIndices = null;
    
	public String getHlgt_code() {
		return hlgt_code;
	}

	public void setHlgt_code(String hlgt_code) {
		this.hlgt_code = hlgt_code;
	}


	public String getHlt_code() {
		return hlt_code;
	}


	public void setHlt_code(String hlt_code) {
		this.hlt_code = hlt_code;
	}

	
    @Override
    public String getSource() {
        return hlgt_code;
    }

    @Override
    public String getTarget() {
        return hlt_code;
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