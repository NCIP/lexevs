
package org.LexGrid.LexBIG.example;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entity;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;



public class GetResolvedValueSetData {
    private String message = "Enter the number of the Value Set Definition to use, then <Enter> :";
    public void run() {
       org.LexGrid.codingSchemes.CodingScheme scheme = Util.promptForResolvedValueSetDefinition(message);
       System.out.println("*RETURNING A SUMMARY OF THE SELECTED RESOLVED VALUE SET*:");
       System.out.println("Resolved Value Set Name: " + scheme.getCodingSchemeName());
       System.out.println("Resolved Value Set URI: " + scheme.getCodingSchemeURI());
       System.out.println("Resolved Value Set Version: "+ scheme.getRepresentsVersion() + "\n");
       System.out.println("*USING RESOLVED VALUE SET URI \"" + scheme.getCodingSchemeURI() + "\" TO GET THE SAME CODING SCHEME*");
       System.out.println("Getting properties from \"" + scheme.getCodingSchemeURI() + "\"");
       LexEVSResolvedValueSetService service = new LexEVSResolvedValueSetServiceImpl();
       
       
        CodingScheme rvsScheme = null;
        try {
            rvsScheme = service.getResolvedValueSetForValueSetURI(new URI(scheme.getCodingSchemeURI()));
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        for(Property p: rvsScheme.getProperties().getProperty()){
            if(p.getPropertyName().equals(LexEVSValueSetDefinitionServices.RESOLVED_AGAINST_CODING_SCHEME_VERSION)){
            
            System.out.println("Accomplished by finding the property: " + p.getPropertyName() + "for coding Scheme at URI: ");
            System.out.println(p.getValue().getContent() + "\n");
            for(PropertyQualifier pq: p.getPropertyQualifier()){
                System.out.println(pq.getPropertyQualifierName() + ": " + pq.getValue().getContent());
            }
            }
        }
        System.out.println("\n*GETTING THE ENTITIES OF THE SCHEME*");
        ResolvedConceptReferenceList list = service.getValueSetEntitiesForURI(rvsScheme.getCodingSchemeURI());
        for(ResolvedConceptReference ref : list.getResolvedConceptReference()){
            System.out.println("Entity Description: " + ref.getEntityDescription().getContent());
            System.out.println("Entity Code: " + ref.getCode());
        }
        
        System.out.println("\n*GETTING ALL VALUESETS CONTAINIING A PARTICULAR ENTITY*");
        ConceptReference ref = new ConceptReference();
        ref.setCode("005");
        ref.setCodingSchemeName("Automobiles");
        List<CodingScheme> codingSchemes = service.getResolvedValueSetsForConceptReference(ref);
        for(CodingScheme s: codingSchemes){
        System.out.println("Resolved Value Set Name: " + s.getCodingSchemeName());
        System.out.println("Resolved Value Set URI: "  + s.getCodingSchemeURI());
        }
        
        System.out.println("\n*GETTING ALL CODING SCHEME VERSIONS IN A RESOLVED VALUE SET*");
        AbsoluteCodingSchemeVersionReferenceList acsvr = service.getListOfCodingSchemeVersionsUsedInResolution(scheme);
        for(AbsoluteCodingSchemeVersionReference abrefs :acsvr.getAbsoluteCodingSchemeVersionReference()){
            System.out.println("Coding Scheme Id: " + abrefs.getCodingSchemeURN());
            System.out.println("Coding Scheme Version: " + abrefs.getCodingSchemeVersion());
        }
    }

       
    
    
    

    /**
     * @param args
     */
    public static void main(String[] args) {
        new GetResolvedValueSetData().run();

    }

}