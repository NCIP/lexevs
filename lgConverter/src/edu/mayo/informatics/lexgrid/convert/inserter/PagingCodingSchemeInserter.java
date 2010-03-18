package edu.mayo.informatics.lexgrid.convert.inserter;

public interface PagingCodingSchemeInserter extends CodingSchemeInserter {

    public void setEntityPageSize(int pageSize);
    
    public void setAssociationInstancePageSize(int pageSize);
}
