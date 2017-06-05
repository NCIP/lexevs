package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedContext;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedSource;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.locator.LexEvsServiceLocator;


public class EntityToRVSTransformer {
    private static final String SOURCE_NAME = "Contributing_Source";
    private static final String DEFAULT_SOURCE = "NCI";
    private URI valueSetDefinitionURI;
    private String valueSetDefinitionRevisionId;
    private String vsVersion;
    
    final private String codingSchemeUri;
    final private String codingSchemeName;
    final private String csVersion;
    final private String association;
    final private AbsoluteCodingSchemeVersionReference ref;
    final private SupportedCodingScheme supportedScheme;
    
    private LexBIGService svc;
    private String baseUri;

    private LexEvsServiceLocator locator = LexEvsServiceLocator.getInstance();

    private ValueSetDefinitionService vsdService;
    private String owner;

    public EntityToRVSTransformer( 
            String association,
            String codingSchemeUri,
            String codingSchemeName,
            String csVersion, 
            AbsoluteCodingSchemeVersionReference ref, 
            LexBIGService svc,
            String baseURI, 
            String owner,
            SupportedCodingScheme suppScheme) {
        this.association = association;
        this.codingSchemeUri = codingSchemeUri;
        this.codingSchemeName= codingSchemeName;
        this.csVersion = csVersion;
        this.ref = ref;
        vsdService = locator.getDatabaseServiceManager().getValueSetDefinitionService();
        this.svc = svc;
        this.baseUri = baseURI;
        this.owner = owner;
        this.supportedScheme = suppScheme;
    }

    public EntityToRVSTransformer(
            String association,
            String codingSchemeUri,
            String codingSchemeName,
            String csVersion, 
            String vsVersion, 
            AbsoluteCodingSchemeVersionReference ref, 
            LexBIGService svc,
            String baseURI,
            String owner,
            SupportedCodingScheme suppScheme,
            URI valueSetDefinitionURI,
            String valueSetDefinitionRevisionId) {
        this.association = association;
        this.valueSetDefinitionURI = valueSetDefinitionURI;
        this.valueSetDefinitionRevisionId = valueSetDefinitionRevisionId;
        this.codingSchemeUri = codingSchemeUri;
        this.codingSchemeName = codingSchemeName;
        this.csVersion = csVersion;
        this.vsVersion = vsVersion;
        this.ref = ref;
        vsdService = locator.getDatabaseServiceManager().getValueSetDefinitionService();
        this.svc = svc;
        this.baseUri = baseURI;
        this.owner = owner;
        this.supportedScheme = suppScheme;
    }

    //Entity with more than one source will be processed into more than one definition
    //Assumption is that this source representation is always a flat list of values
    public List<CodingScheme> transformEntityToCodingSchemes(Entity entity, String sourceName) throws LBException{
      
        final String source = getDefaultSourceIfNull(sourceName);
        List<Property> props = entity.getPropertyAsReference();

       List<CodingScheme> schemes = new ArrayList<CodingScheme>();
       HashMap<String, String> definedSources = new HashMap<String, String>();
       List<Property> sourcelist = getPropertiesForPropertyName(props, source);
       
       sourcelist.stream().forEach(s -> definedSources.put(s.getValue().getContent(), 
               getPropertyQualifierValueForSource(s.getPropertyQualifierAsReference()) != null?
                       getPropertyQualifierValueForSource(s.getPropertyQualifierAsReference()): 
                           entity.getEntityDescription().getContent()));
       Entities vsEntities = getEntities(entity.getEntityCode());
      definedSources.forEach((x,y) -> {
        try {
            schemes.add(transform(entity,x,y,vsEntities));
        } catch (LBException e) {
            throw new RuntimeException("Source Asserted Resovled Value Set Load Failed", e);
        }
    }); 
      //No source has been declared. This must belong to the default source. 
      if(definedSources.size() == 0 || !definedSources.containsValue(DEFAULT_SOURCE)){
      schemes.add(transform(entity, "", entity.getEntityDescription().getContent(), vsEntities));
      }
       return schemes;
    }

    public CodingScheme transform(Entity entity, String source, String description,  Entities entities)
            throws LBException {
        String codingSchemeUri = createUri(baseUri, source, entity.getEntityCode());
        String codingSchemeVersion = csVersion == null ? "UNASSIGNED":
                csVersion;

        String codingSchemeName = description == null? entity.getEntityDescription().getContent(): description;

        CodingScheme cs = null;

        cs = new CodingScheme();

        cs.setCodingSchemeURI(codingSchemeUri);
        cs.setRepresentsVersion(codingSchemeVersion);
        if (entity.getEffectiveDate() != null)
            cs.setEffectiveDate(entity.getEffectiveDate());
        if (entity.getExpirationDate() != null)
            cs.setExpirationDate(entity.getExpirationDate());
        cs.setEntryState(entity.getEntryState());
        cs.setFormalName(codingSchemeName);
        cs.setCodingSchemeName(truncateDefNameforCodingSchemeName(codingSchemeName));
        cs.setIsActive(entity.getIsActive());
        cs.setMappings(createMappings(entity));
        cs.setOwner(entity.getOwner());
        //init properties
        cs.setProperties(new org.LexGrid.commonTypes.Properties());
        Source lexSource = new Source();
        lexSource.setContent(source.equals("")?DEFAULT_SOURCE:source);
        cs.setSource(new Source[]{lexSource});
        cs.setStatus(entity.getStatus());

        Property prop = new Property();
        prop.setPropertyType(AssertedValueSetDefinitionServices.GENERIC);
        prop.setPropertyName(AssertedValueSetDefinitionServices.RESOLVED_AGAINST_CODING_SCHEME_VERSION);
        Text txt = new Text();
        txt.setContent(ref.getCodingSchemeURN());
        prop.setValue(txt);
        PropertyQualifier pq = createPropertyQualifier(AssertedValueSetDefinitionServices.VERSION,
                ref.getCodingSchemeVersion());
        prop.getPropertyQualifierAsReference().add(pq);
        String csSourceName = getCodingSchemeName(ref.getCodingSchemeURN(), ref.getCodingSchemeVersion());
        if (csSourceName != null) {
            PropertyQualifier pQual = createPropertyQualifier(AssertedValueSetDefinitionServices.CS_NAME, csSourceName);
            prop.getPropertyQualifierAsReference().add(pQual);
        }
        cs.getProperties().addProperty(prop);

        cs.setEntities(entities);

        return cs;
    }

    private Mappings createMappings(Entity entity) {
        Mappings mappings = new Mappings();
        SupportedNamespace nmsp = new SupportedNamespace();
        nmsp.setContent(entity.getEntityCodeNamespace());
        nmsp.setEquivalentCodingScheme(codingSchemeName);
        SupportedCodingScheme scheme = supportedScheme;
        SupportedContext context = new SupportedContext();
        for(SupportedSource source : getSupportedSources(entity)){
            mappings.addSupportedSource(source);
        }
        mappings.addSupportedNamespace(nmsp);
        mappings.addSupportedCodingScheme(scheme);
        mappings.addSupportedContext(context);
        return mappings;
    }

    private List<SupportedSource> getSupportedSources(Entity entity) {
        List<SupportedSource> sources = new ArrayList<SupportedSource>();
        List<Property> props = entity.getPropertyAsReference();
        //props.stream().filter(p -> 
        for(Property p: props){
        p.getPropertyQualifierAsReference().stream().
        filter(pq -> pq.getPropertyQualifierName().equals("source")).
        map(PropertyQualifier::getValue).forEach(qual -> sources.add(getSupportedSource(qual)));
        }
        return sources;
    }

    private SupportedSource getSupportedSource(Text qual) {
       SupportedSource source = new SupportedSource();
       source.setContent(qual.getContent());
       source.setLocalId(qual.getContent());
        return null;
    }

    private String getCodingSchemeName(String codingSchemeURN, String codingSchemeVersion) throws LBParameterException {
        return locator.getSystemResourceService().getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeURN,
                codingSchemeVersion);
    }

    private String truncateDefNameforCodingSchemeName(String name){
        if (StringUtils.isNotEmpty(name) && name.length() > 50) {
            name = name.substring(0, 49);
        }
        return name;
    }
    
    public Entities getEntities(String topNodeCode) throws LBException {
        long start = System.currentTimeMillis();
        Entities newEntities = new Entities();
        CodedNodeGraph graph = svc.getNodeGraph(codingSchemeUri,
                Constructors.createCodingSchemeVersionOrTagFromVersion(csVersion), null);
        graph.restrictToAssociations(Constructors.createNameAndValueList(this.association), null);
        ResolvedConceptReferenceList refs = graph.resolveAsList(
                Constructors.createConceptReference(topNodeCode, codingSchemeUri), false, true, 1, 1, null, null, null,
                -1);
        AssociatedConceptList concepts = null;
        if( refs.getResolvedConceptReference(0) != null){
            concepts = refs.getResolvedConceptReference(0).getTargetOf().getAssociation(0).getAssociatedConcepts();
        }
        else{
            throw new RuntimeException("No values for value set with focus code of " + topNodeCode);
        }
                
        for (ResolvedConceptReference refer : concepts.getAssociatedConcept()) {
            newEntities.addEntity(refer.getEntity());
        }
        long end = System.currentTimeMillis();
        System.out.println("Resolution Time: " + (end - start));
        return newEntities;
    }
    
    // TODO Utility Class methods to break out later
    private PropertyQualifier createPropertyQualifier(String name, String value) {
        PropertyQualifier pq = new PropertyQualifier();
        pq.setPropertyQualifierName(name);
        Text pqtxt = new Text();
        pqtxt.setContent(value);
        pq.setValue(pqtxt);
        return pq;
    }

    private String getSupportedCodingSchemeNameForURI(CodingScheme cs, String URI) {
        for (SupportedCodingScheme scs : cs.getMappings().getSupportedCodingScheme()) {
            if (scs.getUri().equals(URI)) {
                return scs.getLocalId();
            }
        }
        return null;
    }

    protected String getDefaultSourceIfNull(String sourceName) {
        return sourceName == null?SOURCE_NAME: sourceName;
    }
    
    protected String createUri(String base, String source, String code){
        return base + (!source.equals("") || source != null ?source + "/":"") + code;
     }
    
    protected List<Property> getPropertiesForPropertyName(List<Property> props, String  name){
        return props.stream().filter(x -> x.getPropertyName().equals(name)).collect(Collectors.toList());
    }
    
    protected String getPropertyQualifierValueForSource(List<PropertyQualifier> quals){
        if(quals.stream().filter(pq -> pq.getPropertyQualifierName().equals("source")).findFirst().isPresent()){
            return quals.stream().filter(pq -> pq.getPropertyQualifierName().equals("source")).findFirst().get().getValue().getContent();
        }
        return null;
    }
    

}
