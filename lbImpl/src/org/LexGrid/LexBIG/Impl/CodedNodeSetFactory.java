
package org.LexGrid.LexBIG.Impl;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Impl.helpers.MappingCodingSchemeQueryRegistry;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.Relations;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;

/**
 * A factory for creating CodedNodeSet objects.
 */
public class CodedNodeSetFactory {

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
    public CodedNodeSet getCodedNodeSet(String codingScheme, CodingSchemeVersionOrTag versionOrTag, Boolean activeOnly, LocalNameList types)
    throws LBInvocationException, LBParameterException, LBResourceUnavailableException, LBException {
        
        String version = ServiceUtility.getVersion(codingScheme, versionOrTag);
        String uri = LexEvsServiceLocator.getInstance().getSystemResourceService().getUriForUserCodingSchemeName(codingScheme, version);

        Registry registry = LexEvsServiceLocator.getInstance().getRegistry();

        RegistryEntry entry = 
            registry.getCodingSchemeEntry(Constructors.createAbsoluteCodingSchemeVersionReference(uri, version));

        if(entry.getDbSchemaVersion().equals(VERSION_18) || entry.getDbSchemaVersion().equals(VERSION_17)){
            return new CodedNodeSetImpl(uri, versionOrTag, activeOnly, types);
        }

        if(entry.getDbSchemaVersion().equals(VERSION_20)){

            if(entry.getSupplementsUri() != null && entry.getSupplementsVersion() != null) {
                String parentUri = entry.getSupplementsUri();
                String parentVersion = entry.getSupplementsVersion();
                
                CodedNodeSetImpl supplement = new CodedNodeSetImpl(
                        parentUri, 
                        Constructors.createCodingSchemeVersionOrTagFromVersion(parentVersion), 
                        activeOnly, 
                        types);

                CodedNodeSetImpl parent = new CodedNodeSetImpl(uri, versionOrTag, activeOnly, types);
                
                return parent.union(supplement);
            } else {

                CodedNodeSet codingSchemeToReturn = new CodedNodeSetImpl(uri, versionOrTag, activeOnly, types);
                
               MappingExtension mappingExtension = 
                   (MappingExtension) LexBIGServiceImpl.defaultInstance().getGenericExtension("MappingExtension");
               
               if(mappingExtension.isMappingCodingScheme(codingScheme, versionOrTag)) {
                   CodingScheme cs = LexBIGServiceImpl.defaultInstance().resolveCodingScheme(uri, Constructors.createCodingSchemeVersionOrTagFromVersion(version));

                   for(Relations relations : cs.getRelations()) {
                       AbsoluteCodingSchemeVersionReference sourceRef = 
                           ServiceUtility.resolveCodingSchemeFromLocalName(
                               uri, 
                               version, 
                               relations.getSourceCodingScheme(),
                               relations.getSourceCodingSchemeVersion());
                       
                       AbsoluteCodingSchemeVersionReference targetRef = 
                           ServiceUtility.resolveCodingSchemeFromLocalName(
                               uri, 
                               version, 
                               relations.getTargetCodingScheme(),
                               relations.getTargetCodingSchemeVersion());
                       
                       if(sourceRef != null && targetRef !=  null) {
                           CodedNodeSetImpl sourceCodingScheme =
                               new CodedNodeSetImpl(
                                       sourceRef.getCodingSchemeURN(),
                                       Constructors.createCodingSchemeVersionOrTagFromVersion(sourceRef.getCodingSchemeVersion()),
                                       activeOnly,
                                       types);

                           CodedNodeSetImpl targetCodingScheme =
                               new CodedNodeSetImpl(
                                       targetRef.getCodingSchemeURN(),
                                       Constructors.createCodingSchemeVersionOrTagFromVersion(targetRef.getCodingSchemeVersion()),
                                       activeOnly,
                                       types);

                           CodedNodeSet combinedSet = sourceCodingScheme.union(targetCodingScheme);

                           combinedSet = combinedSet.restrictToCodes(
                                   MappingCodingSchemeQueryRegistry.
                                       defaultInstance().
                                           buildConceptReferenceList(uri, version));

                           codingSchemeToReturn = codingSchemeToReturn.union(combinedSet);
                       }
                   }
               } 
               
               return codingSchemeToReturn;
            }
        }

        throw new LBParameterException("Could not create a CodedNodeSet for CodingScheme: " + codingScheme);
    }

}