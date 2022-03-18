
package edu.mayo.informatics.lexgrid.convert.directConversions.medDRA.Data;

import java.io.Serializable;

/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public class MedDRARecord_soc_hlgt implements Serializable, DatabaseMapRecord{

	private static final long serialVersionUID = 1L;

	private String soc_code;
	private String hlgt_code;

    private int[] validFieldIndices = {1,2};
    private int[] invalidFieldIndices = null;
    
	public String getSoc_code() {
		return soc_code;
	}


	public void setSoc_code(String soc_code) {
		this.soc_code = soc_code;
	}


	public String getHlgt_code() {
		return hlgt_code;
	}


	public void setHlgt_code(String hlgt_code) {
		this.hlgt_code = hlgt_code;
	}


    @Override
    public String getSource() {
        return soc_code;
    }


    @Override
    public String getTarget() {
        return hlgt_code;
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