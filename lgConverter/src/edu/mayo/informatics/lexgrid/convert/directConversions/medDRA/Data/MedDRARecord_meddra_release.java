
package edu.mayo.informatics.lexgrid.convert.directConversions.medDRA.Data;

import java.io.Serializable;

/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public class MedDRARecord_meddra_release implements Serializable, DatabaseRecord{

	private static final long serialVersionUID = 1L;
	
	public String version;
	public String language;
	private String null_field1;
	private String null_field2;
	private String null_field3;

    private int[] validFieldIndices = {1,2};
    private int[] invalidFieldIndices = {3,4,5};
    
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getNull_field1() {
		return null_field1;
	}

	public void setNull_field1(String null_field1) {
		this.null_field1 = null_field1;
	}

	public String getNull_field2() {
		return null_field2;
	}

	public void setNull_field2(String null_field2) {
		this.null_field2 = null_field2;
	}

	public String getNull_field3() {
		return null_field3;
	}

	public void setNull_field3(String null_field3) {
		this.null_field3 = null_field3;
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