
package org.LexGrid.LexBIG.Impl.namespace;

import java.io.Serializable;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;

public interface NamespaceHandler extends Serializable {

    public AbsoluteCodingSchemeVersionReference 
        getCodingSchemeForNamespace(
                String codingSchemeUri, 
                String version, 
                String namespace) throws LBParameterException;
    
    public String 
        getCodingSchemeNameForNamespace(
            String codingSchemeUri, 
            String version, 
            String namespace) throws LBParameterException;
    
    public List<String> 
        getNamespacesForCodingScheme(
                String codingSchemeUri, 
                String version,
                String codingSchemeNameOfSearchCodingScheme) throws LBParameterException;
}