
package org.LexGrid.LexBIG.Impl;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.pagedgraph.PagingCodedNodeGraphImpl;
import org.LexGrid.LexBIG.Impl.pagedgraph.UnionGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;

/**
 * A factory for creating CodedNodeGraph objects.
 */
public class CodedNodeGraphFactory {

    private static String VERSION_17 = "1.7";
    private static String VERSION_18 = "1.8";
    private static String VERSION_20 = "2.0";

    /**
     * Gets the coded node graph.
     * 
     * @param codingScheme the coding scheme
     * @param versionOrTag the version or tag
     * @param relationContainerName the relation container name
     * 
     * @return the coded node graph
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public CodedNodeGraph getCodedNodeGraph(String codingScheme, CodingSchemeVersionOrTag versionOrTag, String relationContainerName)
    throws LBParameterException {
        
        String version = ServiceUtility.getVersion(codingScheme, versionOrTag);
        String uri = LexEvsServiceLocator.getInstance().getSystemResourceService().getUriForUserCodingSchemeName(codingScheme, version);

        Registry registry = LexEvsServiceLocator.getInstance().getRegistry();

        RegistryEntry entry = 
            registry.getCodingSchemeEntry(Constructors.createAbsoluteCodingSchemeVersionReference(uri, version));

        if(entry.getDbSchemaVersion().equals(VERSION_18) || entry.getDbSchemaVersion().equals(VERSION_17)){
            try {
                return new CodedNodeGraphImpl(uri, version, relationContainerName);
            } catch (LBException e) {
                throw new RuntimeException(e);
            }

        }
        
        if(entry.getDbSchemaVersion().equals(VERSION_20)){
            if(entry.getSupplementsUri() != null && entry.getSupplementsVersion() != null) {
                String parentUri = entry.getSupplementsUri();
                String parentVersion = entry.getSupplementsVersion();
                
                PagingCodedNodeGraphImpl supplement = new PagingCodedNodeGraphImpl(
                        parentUri, 
                        parentVersion, 
                        relationContainerName);
                
                PagingCodedNodeGraphImpl parent = new PagingCodedNodeGraphImpl(uri, version, relationContainerName);
                
                return new UnionGraph(parent, supplement);
            } else {
                return new PagingCodedNodeGraphImpl(uri, version, relationContainerName);
            }
        }
        
        throw new LBParameterException("Could not create a CodedNodeGraph for CodingScheme: " + codingScheme);
    }
}