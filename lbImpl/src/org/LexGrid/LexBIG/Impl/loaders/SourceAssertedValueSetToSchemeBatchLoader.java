package org.LexGrid.LexBIG.Impl.loaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.SourceAssertedVStoCodingSchemeLoader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;

import com.hp.hpl.jena.sparql.util.Loader;

import edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets.EntityToRVSTransformer;
import edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets.EntityToVSDTransformer;

public class SourceAssertedValueSetToSchemeBatchLoader {
    
    private CodedNodeGraphService codedNodeGraphDao;
    private EntityService entityService;
    private CodingSchemeService csService;
    private SystemResourceService resourceService;
    private LexBIGService lbsvc;
    private String codingSchemeName;
    private String codingSchemeUri;
    private String codingSchemeVersion;
    private String associationName;
    private boolean targetToSource;
    private EntityToRVSTransformer transformer;
    private ValueSetDefinitionService valueSetDefinitionService;
    private LgMessageDirectorIF messages;
    

    
    public SourceAssertedValueSetToSchemeBatchLoader(String codingScheme, String version, 
            String associationName, boolean targetToSource, String baseUri, String owner) throws LBParameterException{
        codedNodeGraphDao = LexEvsServiceLocator.getInstance().
                getDatabaseServiceManager().getCodedNodeGraphService();
        entityService = LexEvsServiceLocator.getInstance().
                getDatabaseServiceManager().getEntityService();
        resourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
        this.codingSchemeUri = resourceService.getUriForUserCodingSchemeName(codingScheme, version);
        valueSetDefinitionService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getValueSetDefinitionService();
        csService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodingSchemeService();
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
    
    public void run(String sourceName) throws LBException{
        processEntitiesToCodingScheme(getEntitiesForAssociation(associationName, codingSchemeUri, codingSchemeVersion), sourceName);
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
    
    public void processEntitiesToCodingScheme(List<Node> nodes, String sourceName) throws LBException{
       for(Node n: nodes){
           Entity e = getEntityByCodeAndNamespace(codingSchemeUri, 
                   codingSchemeVersion, n.getEntityCode(), n.getEntityCodeNamespace());
           List<CodingScheme> schemes = transformer.transformEntityToCodingSchemes(e, sourceName);
           SourceAssertedVStoCodingSchemeLoader loader = (SourceAssertedVStoCodingSchemeLoader) LexBIGServiceImpl.
                   defaultInstance().getServiceManager(null).getLoader("SourceAssertedVStoCodingSchemeLoader");
           for(CodingScheme s : schemes){
               loader.load(s);
               AbsoluteCodingSchemeVersionReference ref = Constructors.
                       createAbsoluteCodingSchemeVersionReference(s.getCodingSchemeURI(),s.getRepresentsVersion());
               LexBIGServiceImpl.
               defaultInstance().getServiceManager(null).activateCodingSchemeVersion(ref);
               LexBIGServiceImpl.
               defaultInstance().getServiceManager(null).setVersionTag(ref, "PRODUCTION");
//               uriToCodingSchemeNameMap.put(s.getCodingSchemeURI(), s.getCodingSchemeName());
           }
       }
//       indexResolvedValueSetCodingSchemes();
    }
    
//    private void indexResolvedValueSetCodingSchemes(){
//        uriToCodingSchemeNameMap.forEach((x,y) -> indexUsingMap(x,y));
//    }
//
//    private void indexUsingMap(String x, String y) {
//        ProcessRunner loader = new IndexLoaderImpl();
//        AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference(x, y);
//        try {
//            loader.runProcess(ref, null);
//        } catch (LBParameterException e) {
//               System.out.println("Indexing failed for value set coding scheme " + y + ":" + x);
//        }
//    }

    public static void main(String[] args) {
      try {
        new SourceAssertedValueSetToSchemeBatchLoader("NCI_Thesaurus", "17.02d", "Concept_In_Subset", true, "http://evs.nci.nih.gov/valueset/", "NCI").run("Contributing_Source");
    } catch (LBParameterException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (LBException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    }

}
