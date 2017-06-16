package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedConceptDomain;
import org.LexGrid.naming.SupportedSource;
import org.apache.commons.lang.StringUtils;
import org.lexevs.locator.LexEvsServiceLocator;


public class EntityToRVSTransformer {

    private static final String DEFAULT_SOURCE = "NCI";
    
    final private String codingSchemeUri;
    final private String codingSchemeName;
    final private String csVersion;
    final private String association;
    final private AbsoluteCodingSchemeVersionReference ref;
    final private SupportedCodingScheme supportedScheme;
    
    private LexBIGService svc;
    private String baseUri;

    private LexEvsServiceLocator locator = LexEvsServiceLocator.getInstance();

    private String propertyName;

    public EntityToRVSTransformer( 
            String association,
            String codingSchemeUri,
            String codingSchemeName,
            String csVersion, 
            AbsoluteCodingSchemeVersionReference ref, 
            LexBIGService svc,
            String baseURI, 
            String owner,
            SupportedCodingScheme suppScheme,
            String conceptDomainIndicator) {
        this.association = association;
        this.codingSchemeUri = codingSchemeUri;
        this.codingSchemeName= codingSchemeName;
        this.csVersion = csVersion;
        this.ref = ref;
        this.svc = svc;
        this.baseUri = baseURI;
        this.supportedScheme = suppScheme;
        this.propertyName = conceptDomainIndicator;
    }

    //Entity with more than one source will be processed into more than one definition
    //Assumption is that this source representation is always a flat list of values
    public List<CodingScheme> transformEntityToCodingSchemes(Entity entity, String sourceName) throws LBException {

        final String source = AssertedValueSetServices.getDefaultSourceIfNull(sourceName);
        List<Property> props = entity.getPropertyAsReference();

        List<CodingScheme> schemes = new ArrayList<CodingScheme>();
        if (!AssertedValueSetServices.isPublishableValueSet(entity)) {
            return schemes;
        }
        HashMap<String, String> definedSources = new HashMap<String, String>();
        List<Property> sourcelist = AssertedValueSetServices.getPropertiesForPropertyName(props, source);

        sourcelist.stream().forEach(s -> definedSources.put(s.getValue().getContent(),
                AssertedValueSetServices.getPropertyQualifierValueForSource(s.getPropertyQualifierAsReference()) != null
                        ? AssertedValueSetServices.getPropertyQualifierValueForSource(
                                s.getPropertyQualifierAsReference())
                        : entity.getEntityDescription().getContent()));
        Entities vsEntities = getEntities(entity.getEntityCode());
        definedSources.forEach((x, y) -> {
            try {
                schemes.add(transform(entity, x, y, vsEntities));
            } catch (LBException e) {
                throw new RuntimeException("Source Asserted Resolved Value Set Load Failed", e);
            }
        });
        // No source has been declared. This must belong to the default source.
        if (definedSources.size() == 0) {
            schemes.add(transform(entity, null, entity.getEntityDescription().getContent(), vsEntities));
        }
        return schemes;
    }



    public CodingScheme transform(Entity entity, String source, String description,  Entities entities)
            throws LBException {
        String codingSchemeUri = AssertedValueSetServices.createUri(baseUri, source, entity.getEntityCode());
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
        lexSource.setContent(source == null?DEFAULT_SOURCE:source);
        cs.setSource(new Source[]{lexSource});
        cs.setStatus(entity.getStatus());

        Property prop = new Property();
        prop.setPropertyType(AssertedValueSetServices.GENERIC);
        prop.setPropertyName(AssertedValueSetServices.RESOLVED_AGAINST_CODING_SCHEME_VERSION);
        Text txt = new Text();
        txt.setContent(ref.getCodingSchemeURN());
        prop.setValue(txt);
        PropertyQualifier pq = AssertedValueSetServices.createPropertyQualifier(AssertedValueSetServices.VERSION,
                ref.getCodingSchemeVersion());
        prop.getPropertyQualifierAsReference().add(pq);

        if (codingSchemeName != null) {
            PropertyQualifier pQual = AssertedValueSetServices.createPropertyQualifier(AssertedValueSetServices.CS_NAME, codingSchemeName);
            prop.getPropertyQualifierAsReference().add(pQual);
        }
        cs.getProperties().addProperty(prop);

        cs.setEntities(entities);

        return cs;
    }

    private Mappings createMappings(Entity entity) {
        Mappings mappings = new Mappings();
        SupportedCodingScheme scheme = supportedScheme;
        SupportedConceptDomain domain = AssertedValueSetServices.getSupportedConceptDomain(entity, propertyName, codingSchemeUri);
        for(SupportedSource source : getSupportedSources(entity)){
            mappings.addSupportedSource(source);
        }
        mappings.addSupportedNamespace(AssertedValueSetServices.
                createSupportedNamespace(entity.getEntityCodeNamespace(), 
                        codingSchemeName, codingSchemeUri));
        mappings.addSupportedCodingScheme(scheme);
        mappings.addSupportedConceptDomain(domain);
        return mappings;
    }

    protected List<SupportedSource> getSupportedSources(Entity entity) {
        List<SupportedSource> sources = new ArrayList<SupportedSource>();
        List<Property> props = entity.getPropertyAsReference();
        for(Property p: props){
        p.getPropertyQualifierAsReference().stream().
        filter(pq -> pq.getPropertyQualifierName().equals(AssertedValueSetServices.SOURCE)).
        map(PropertyQualifier::getValue).forEach(qual -> sources.add(AssertedValueSetServices.createSupportedSource(qual.getContent(), codingSchemeUri)));
        }
        return sources;
    }

    protected String truncateDefNameforCodingSchemeName(String name){
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
                Constructors.createConceptReference(topNodeCode, codingSchemeUri), 
                false, true, 1, 1, null, null, null,-1);
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
}
