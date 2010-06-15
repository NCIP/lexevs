package org.LexGrid.LexBIG.Impl;


import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexEVSAuthoringService;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedAssociationQualifier;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;



public class LexEVSAuthoringServiceImpl implements LexEVSAuthoringService{
    
    CodingSchemeRendering[] codingSchemes;
    LexBIGService lbs;
    AuthoringService service;
    DatabaseServiceManager dbManager;
    LexEvsServiceLocator locator;
    
    public LexEVSAuthoringServiceImpl(){
        lbs = LexBIGServiceImpl.defaultInstance();
        locator = LexEvsServiceLocator.getInstance();
        dbManager = locator.getDatabaseServiceManager();
        service = dbManager.getAuthoringService();
    }
    @Override
    public Association createAssociation() throws LBException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createAssociationMapping(Relations relation, 
            AbsoluteCodingSchemeVersionReference sourceCodingScheme,
            AbsoluteCodingSchemeVersionReference targetCodingScheme, 
            AssociationSource[] associationSource, 
            String associationType,
            String relationsContainerName,
            Date effectiveDate,
            AssociationQualification[] associationQualifiers)
            throws LBException {

        if (!codingSchemeExists(codingSchemes, sourceCodingScheme)) {
            throw new LBResourceUnavailableException("Association source code system not found");
        }

        if (!codingSchemeExists(codingSchemes, targetCodingScheme)) {
            throw new LBResourceUnavailableException("Association target code system not found");
        }

        // We have a target and source for this mapping. Now we'll resolve the source
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion(sourceCodingScheme.getCodingSchemeVersion());
        CodingScheme scheme = lbs.resolveCodingScheme(sourceCodingScheme.getCodingSchemeURN(), csvt);

        Relations relations = createRelationsContainer(scheme,
                relationsContainerName,
                effectiveDate,
                sourceCodingScheme,
                targetCodingScheme, 
                true, 
                associationType,
                null);
        AssociationPredicate predicate = createAssociationPredicate(scheme, associationType);
        for(AssociationSource source: associationSource){
            
        //Here it should "createSource -- checking for source in existing coding scheme each time.
        predicate.addSource(source);
        }
        relations.addAssociationPredicate(predicate);
        scheme.addRelations(relations);
//        if(associationQualifiers != null && !supportedAssociationQualifiersExist(scheme, associationQualifiers)){
//            throw new LBException("This association qualifier does not exist in " + scheme.getCodingSchemeName()
//                    + " vocabulary");
//        }  
        service.loadRevision(scheme, "MappingRelease");
       
    }

    @Override
    public AssociationPredicate createAssociationPredicate(CodingScheme scheme,  String associationName) throws LBException {
        if (!supportedAssociationExists(scheme, associationName)) {
            throw new LBException("This association type does not exist in " + scheme.getCodingSchemeName()
                    + " vocabulary");
        }
        
        AssociationPredicate predicate = new AssociationPredicate();
        predicate.setAssociationName(associationName);
        return predicate;
    }

    @Override
    public AssociationSource createAssociationSource(Relations relations, AssociationPredicate predicate,
            AbsoluteCodingSchemeVersionReference sourceCodeSystemIdentifier, String sourceConceptCodeIdentifier)
            throws LBException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssociationTarget createAssociationTarget(Association Source) throws LBException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Properties createCodingSchemeProperties(CodingScheme scheme) throws LBException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Entities createEntities(CodingScheme scheme) throws LBException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Entity createEntity(Entities entities) throws LBException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Relations createRelationsContainer(CodingScheme scheme,
            String containerName,
            Date effectiveDate,
            AbsoluteCodingSchemeVersionReference sourceCodeSystemIdentifier,
            AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier, 
            boolean isMapping, 
            String associationType,
            Properties relationProperties) throws LBException {
      Relations relations = new Relations();
      relations.setContainerName(containerName);
      relations.setEffectiveDate(effectiveDate);
      return relations;
    }

    @Override
    public CodingScheme createCodingScheme() throws LBException {
        // TODO Auto-generated method stub
        return null;
    }
    
    public boolean codingSchemeExists(CodingSchemeRendering[] codingSchemes,
            AbsoluteCodingSchemeVersionReference codingScheme) {
        for (CodingSchemeRendering cr : codingSchemes) {
            CodingSchemeSummary summary = cr.getCodingSchemeSummary();
            if (summary.getCodingSchemeURI().equals(codingScheme.getCodingSchemeURN())
                    && summary.getRepresentsVersion().equals(codingScheme.getCodingSchemeVersion())) {
                return true;
            }

        }
        return false;
    }

    public boolean supportedAssociationExists(CodingScheme scheme, String associationName) {
        // Check for supported association and qualifiers.
        SupportedAssociation[] associations = scheme.getMappings().getSupportedAssociation();

        for (SupportedAssociation sa : associations) {
            if (sa.getContent().equals(associationName))
                return true;
        }
        return false;
    }

    // probably don't want to get this fine grained. Something to discuss.
    public boolean supportedAssociationQualifiersExist(CodingScheme scheme, AssociationQualification[] assocQualifiers) {
        SupportedAssociationQualifier[] qualifiers = scheme.getMappings().getSupportedAssociationQualifier();
        for (SupportedAssociationQualifier saq : qualifiers) {
            for (AssociationQualification aq : assocQualifiers) {
                if (saq.getContent().equals(aq.getAssociationQualifier())) {
                    return true;
                }
            }
        }
        return false;
    }
}
