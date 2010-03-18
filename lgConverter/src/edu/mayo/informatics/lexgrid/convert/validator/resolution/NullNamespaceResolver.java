package edu.mayo.informatics.lexgrid.convert.validator.resolution;

import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.utility.DaoUtility;

import edu.mayo.informatics.lexgrid.convert.validator.error.NullNamespaceError;

public class NullNamespaceResolver extends AbstractResolver<Entity> {

    private String defaultNamespace;
    
    public NullNamespaceResolver(CodingScheme codingScheme) {
        this.defaultNamespace = getDefaultNamespaceFromCodingScheme(codingScheme);
    }

    protected String getDefaultNamespaceFromCodingScheme(CodingScheme codingScheme) {
        return codingScheme.getCodingSchemeName();
    }

    @Override
    protected List<String> doGetValidErrorCodes() {
        return
            DaoUtility.createList(String.class, NullNamespaceError.NULL_NAMESPACE_CODE);
    }

    @Override
    public void doResolveError(Entity errorObject) {
        errorObject.setEntityCodeNamespace(this.defaultNamespace);
    }
}
