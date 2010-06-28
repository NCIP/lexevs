package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.util;

public class Triple {
    
    private String sourceEntityCode;
    private String targetEntityCode;
    private String associationName;
    
    private Triple() { super(); }
    
    public Triple(String src, String tgt, String assocName ) { 
        super();
        this.setAssociationName(assocName);
        this.setSourceEntityCode(src);
        this.setTargetEntityCode(tgt);
    }

    public void setSourceEntityCode(String sourceEntityCode) {
        this.sourceEntityCode = sourceEntityCode;
    }

    public void setTargetEntityCode(String targetEntityCode) {
        this.targetEntityCode = targetEntityCode;
    }

    public void setAssociationName(String associationName) {
        this.associationName = associationName;
    }
    
    public String getSourceEntityCode() {
        return sourceEntityCode;
    }

    public String getTargetEntityCode() {
        return targetEntityCode;
    }

    public String getAssociationName() {
        return associationName;
    }
    
    public String toString() {
        String s = "src=" + this.getSourceEntityCode() + " tgt=" + this.getTargetEntityCode() + " assocName=" + this.getAssociationName();
        return s;
    }

    public boolean equals(Triple aTriple) {
        if(aTriple == null) {
            return false;
        }
        
        if(aTriple.getAssociationName().equals(this.associationName) && 
                aTriple.getSourceEntityCode().equals(this.sourceEntityCode) &&
                aTriple.getTargetEntityCode().equals(this.targetEntityCode)) {
            return true;
        }
        return false;
    }
}
