package edu.mayo.informatics.lexgrid.convert.directConversions.claml;

public class IndTerm {
    private String docCode_ = null;
    private String name_= null;
    private String definition_= null;
    private String synonym_= null;
    private String note_= null;
    private String linguisticNote_= null;
    
    private String xlsCode_= null;
    private String exactMatch_= null;
    private String xlsCode2_= null;
    private String exactMatch2_= null;
    
    IndTerm( )  {
    }
    
    IndTerm( String docCode, String name, String definition, String synonym, String note, String linguisticNote)  {
        docCode_ = docCode;
        name_ = name;
        definition_ = definition;
        synonym_ = synonym;
        note_ = note;
        linguisticNote_ = linguisticNote;
    }
    
    IndTerm( String xlsCode, String exactMatch, String xlsCode2, String exactMatch2)  {
        xlsCode_ = xlsCode;
        exactMatch_ = exactMatch;
        xlsCode2_ = xlsCode2;
        exactMatch2_ = exactMatch2;
    }
    
    
    public String getDocCode() {
        return docCode_;
    }

    public String getName() {
        return name_;
    }

    public String getDefinition() {
        return definition_;
    }

    public String getSynonym() {
        return synonym_;
    }

    public String getNote() {
        return note_;
    }
    
    public String getLinguisticNote() {
        return linguisticNote_;
    }

    public String getXlsCode() {
        return xlsCode_;
    }

    public String getExactMatch() {
        return exactMatch_;
    }
    
    public String getXlsCode2() {
        return xlsCode2_;
    }

    public String getExactMatch2() {
        return exactMatch2_;
    }

    public void setDocCode(String docCode) {
        this.docCode_ = docCode;
    }

    public void setName(String name) {
        this.name_ = name;
    }

    public void setDefinition(String definition) {
        this.definition_ = definition;
    }

    public void setSynonym(String synonym) {
        this.synonym_ = synonym;
    }

    public void setNote(String note) {
        this.note_ = note;
    }

    public void setLinguisticNote(String linguisticNote) {
        this.linguisticNote_ = linguisticNote;
    }
    
    public void setXlsCode(String xlsCode) {
        this.xlsCode_ = xlsCode;
    }

    public void setExactMatch(String exactMatch) {
        this.exactMatch_ = exactMatch;
    }
    
    public void setXlsCode2(String xlsCode2) {
        this.xlsCode2_ = xlsCode2;
    }

    public void setExactMatch2(String exactMatch2) {
        this.exactMatch2_ = exactMatch2;
    }
    
    public String toString()
    {
        StringBuffer termString = new StringBuffer();
        
        termString.append("\t");

        if(name_ != null)
        {
            termString.append(name_);
        }
        termString.append("\t");
        
        if(docCode_ != null)
        {
            termString.append(docCode_);
        }
        termString.append("\t");

        if(xlsCode_ != null)
        {
            termString.append(xlsCode_);
        }
        termString.append("\t");
        
        if(exactMatch_ != null)
        {
            termString.append(exactMatch_);
        }
        termString.append("\t");
        
        if(xlsCode2_ != null)
        {
            termString.append(xlsCode2_);
        }
        termString.append("\t");
        
        if(exactMatch2_ != null)
        {
            termString.append(exactMatch2_);
        }
        termString.append("\t");

        if(synonym_ != null)
        {
            termString.append(synonym_);
        }
        termString.append("\t");
        
        if(definition_ != null)
        {
            termString.append(definition_);
        }
        termString.append("\t");
        
        if(note_ != null)
        {
            termString.append(note_);
        }
        termString.append("\t");
        
        if(linguisticNote_ != null)
        {
            termString.append(linguisticNote_);
        }
        termString.append("\t");

        return termString.toString();
    }
    
}
