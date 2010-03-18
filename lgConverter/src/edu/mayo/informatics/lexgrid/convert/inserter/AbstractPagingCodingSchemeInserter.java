package edu.mayo.informatics.lexgrid.convert.inserter;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entities;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

public abstract class AbstractPagingCodingSchemeInserter extends AbstractCodingSchemeInserter implements PagingCodingSchemeInserter{

    public void insertCodingScheme(CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException {
        loadNonPagedItems(codingScheme);
        
        String uri = codingScheme.getCodingSchemeURI();
        String version = codingScheme.getRepresentsVersion();
        
        pageEntities(uri, version, codingScheme.getEntities());
        for(Relations relations : codingScheme.getRelations()) {
            pageRelations(uri, version, relations);
        }
    }
    
    protected abstract void loadNonPagedItems(CodingScheme codingScheme);
    
    protected abstract void pageEntities(String codingSchemeUri, String codingSchemeVersion, Entities entities);
    
    protected abstract void pageRelations(String codingSchemeUri, String codingSchemeVersion, Relations relations);
}
