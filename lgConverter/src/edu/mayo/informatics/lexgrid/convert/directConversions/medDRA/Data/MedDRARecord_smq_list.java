
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
public class MedDRARecord_smq_list implements Serializable, DatabaseEntityRecord, DatabaseMapRecord{

	private static final long serialVersionUID = 1L;
	
	private String smq_code;
	private String smq_name;
	private String smq_level;
	private String smq_description;
	private String smq_source;
	private String smq_note;
    private String MedDRA_version;
    private String status;
    private String smq_algorithm;

    private int[] validFieldIndices = {1,2,3,4,5,6,7,8,9};
    private int[] invalidFieldIndices = null;
    
	public String getSmq_code() {
		return smq_code;
	}

	public void setSmq_code(String smq_code) {
		this.smq_code = smq_code;
	}

	public String getSmq_name() {
		return smq_name;
	}

	public void setSmq_name(String smq_name) {
		this.smq_name = smq_name;
	}

	public String getSmq_level() {
		return smq_level;
	}

	public void setSmq_level(String smq_level) {
		this.smq_level = smq_level;
	}

	public String getSmq_description() {
		return smq_description;
	}

	public void setSmq_description(String smq_description) {
		this.smq_description = smq_description;
	}

	public String getSmq_source() {
		return smq_source;
	}

	public void setSmq_source(String smq_source) {
		this.smq_source = smq_source;
	}

	public String getSmq_note() {
		return smq_note;
	}

	public void setSmq_note(String smq_note) {
		this.smq_note = smq_note;
	}

	public String getMedDRA_version() {
		return MedDRA_version;
	}

	public void setMedDRA_version(String medDRA_version) {
		MedDRA_version = medDRA_version;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSmq_algorithm() {
		return smq_algorithm;
	}

	public void setSmq_algorithm(String smq_algorithm) {
		this.smq_algorithm = smq_algorithm;
	}

    @Override
    public String getCode() {
        return this.smq_code;
    }

    @Override
    public String getName() {
        return this.smq_name;
    }

    @Override
    public List<Presentation> getPresentations() {
        List<Presentation> presentations = new ArrayList<Presentation>();
        
        presentations.add(MedDRARecord_Utils.createPresentation("T-1", this.smq_name, "SMQ", true));
        
        
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
        
        
        comments.add(MedDRARecord_Utils.createComment("Name", this.smq_name));
        comments.add(MedDRARecord_Utils.createComment("Code", this.smq_code));
        comments.add(MedDRARecord_Utils.createComment("Level", this.smq_level));
        comments.add(MedDRARecord_Utils.createComment("Description", this.smq_description));
        comments.add(MedDRARecord_Utils.createComment("Source", this.smq_source));
        comments.add(MedDRARecord_Utils.createComment("Note", this.smq_note));
        comments.add(MedDRARecord_Utils.createComment("MedDRA Dictionary Version", this.MedDRA_version));
        comments.add(MedDRARecord_Utils.createComment("Status", this.status));
        comments.add(MedDRARecord_Utils.createComment("Algorithm", this.smq_algorithm));
        
        return comments;
    }

    @Override
    public List<Property> getProperties() {
        List<Property> properties = new ArrayList<Property>();
        
        return properties;
    }

    @Override
    public String getSource() {
        return MedDRA2LGConstants.TOP_NODE_SMQ;
    }

    @Override
    public String getTarget() {
        return this.smq_code;
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