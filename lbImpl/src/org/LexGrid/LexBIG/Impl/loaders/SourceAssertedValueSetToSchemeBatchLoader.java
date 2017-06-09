package org.LexGrid.LexBIG.Impl.loaders;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.SourceAssertedVStoCodingSchemeLoader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedCodingScheme;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.LexEvsResourceManagingService;
import org.lexevs.system.service.SystemResourceService;

import edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets.EntityToRVSTransformer;

public class SourceAssertedValueSetToSchemeBatchLoader {
    private LexEvsResourceManagingService service = new LexEvsResourceManagingService();
    private CodedNodeGraphService codedNodeGraphDao;
    private EntityService entityService;
    private SystemResourceService resourceService;
    private LexBIGService lbsvc;
    private String codingSchemeName;
    private String codingSchemeUri;
    private String codingSchemeVersion;
    private String associationName;
    private boolean targetToSource;
    private EntityToRVSTransformer transformer;
    

    
    public SourceAssertedValueSetToSchemeBatchLoader(String codingScheme, String version, 
            String associationName, boolean targetToSource, String baseUri, String owner) throws LBParameterException{
        codedNodeGraphDao = LexEvsServiceLocator.getInstance().
                getDatabaseServiceManager().getCodedNodeGraphService();
        entityService = LexEvsServiceLocator.getInstance().
                getDatabaseServiceManager().getEntityService();
        resourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
        this.codingSchemeUri = resourceService.getUriForUserCodingSchemeName(codingScheme, version);
        lbsvc = LexBIGServiceImpl.defaultInstance();

        this.codingSchemeVersion = version;
        CodingSchemeVersionOrTag versionOrTag = Constructors.createCodingSchemeVersionOrTagFromVersion(this.codingSchemeVersion);
        try {
            codingSchemeName = lbsvc.resolveCodingScheme(codingSchemeUri, versionOrTag ).getCodingSchemeName();
        } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.associationName = associationName;
        this.targetToSource = targetToSource;
        AbsoluteCodingSchemeVersionReference ref = Constructors.
                createAbsoluteCodingSchemeVersionReference(codingSchemeUri, version);
        SupportedCodingScheme supCS = new SupportedCodingScheme();
        supCS.setContent(codingSchemeName);
        supCS.setIsImported(true);
        supCS.setLocalId(codingSchemeName);
        supCS.setUri(codingSchemeUri);
        this.transformer = new EntityToRVSTransformer(associationName, 
                codingSchemeUri, codingSchemeName, version, ref , lbsvc, baseUri, owner, supCS);
    }
    
    public void run(String sourceName) throws LBException, InterruptedException{
        synchronized (service) {
        processEntitiesToCodingScheme(getEntitiesForAssociation(associationName, codingSchemeUri, codingSchemeVersion), sourceName);
        }
    }
    
    
    
    protected List<Node> getEntitiesForAssociation(String association, String codingSchemeUri,String version){
       String associationPredicateUid = this.getPredicateGuidForValueSetRelation(association, codingSchemeUri, version);
       return codedNodeGraphDao.getDistinctTargetTriples(codingSchemeUri, version, associationPredicateUid);
    }
    
    protected String getPredicateGuidForValueSetRelation(String associationName, String codingSchemeUri,
            String codingSchemeVersion){
        List<String> list = new ArrayList<String>();
        list.add(associationName);
        List<String> uids = codedNodeGraphDao.getAssociationPredicateUidsForNames(
                codingSchemeUri, codingSchemeVersion, null, list);
        if( uids == null || uids.size() < 1){
            throw new RuntimeException("No association is asserted for :" 
        + associationName + " in " + codingSchemeUri + ":" + codingSchemeVersion);
        }
        return uids.get(0);
    }
    
    
    protected Entity getEntityByCodeAndNamespace(String codingSchemeUri, String version, String code, String namespace){
        Entity entity = entityService.getEntity(codingSchemeUri, version, code, namespace);
        return entity;
    }
    
    public void processEntitiesToCodingScheme(List<Node> nodes, String sourceName) throws LBException, InterruptedException {
        for (Node n : nodes) {
            Entity e = getEntityByCodeAndNamespace(codingSchemeUri, codingSchemeVersion, n.getEntityCode(),
                    n.getEntityCodeNamespace());
            List<CodingScheme> schemes = transformer.transformEntityToCodingSchemes(e, sourceName);

            for (CodingScheme s : schemes) {

                    SourceAssertedVStoCodingSchemeLoader loader = (SourceAssertedVStoCodingSchemeLoader) LexBIGServiceImpl
                            .defaultInstance().getServiceManager(null)
                            .getLoader("SourceAssertedVStoCodingSchemeLoader");
                    loader.load(s);
                    while(loader.getStatus().getEndTime() == null){
                        Thread.sleep(2000);
                    }               
                    if(!loader.getStatus().getErrorsLogged()){
                    LexBIGServiceImpl.defaultInstance().getServiceManager(null).activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);
                    LexBIGServiceImpl.defaultInstance().getServiceManager(null).setVersionTag(loader.getCodingSchemeReferences()[0],"PRODUCTION");
                    System.out.println("Loaded and activiated resolved value set scheme for: " + s.getCodingSchemeURI() + " :" +
                    s.getCodingSchemeName());
                    }
                    else{
                    System.out.println("Error loading value set: " + s.getCodingSchemeURI() + " :" +
                           s.getCodingSchemeName());   
                    }
                }
        }
    }
    


    public static void main(String[] args) {
      try {
        new SourceAssertedValueSetToSchemeBatchLoader("NCI_Thesaurus", "17.02d", "Concept_In_Subset", true, "http://evs.nci.nih.gov/valueset/", "NCI").run("Contributing_Source");
    } catch (LBParameterException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (LBException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    }

}
