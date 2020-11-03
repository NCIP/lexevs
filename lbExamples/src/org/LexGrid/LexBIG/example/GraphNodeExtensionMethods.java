package org.LexGrid.LexBIG.example;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.Direction;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.graph.NodeGraphResolutionExtensionImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;

public class GraphNodeExtensionMethods{
    NodeGraphResolutionExtensionImpl   ngr;

    
    public void run(String uri) throws LBException{
        
        ngr = (NodeGraphResolutionExtensionImpl) LexBIGServiceImpl
                .defaultInstance()
                .getGenericExtension("NodeGraphResolution");
        ngr.init(uri);
        CodedNodeSet set = LexBIGServiceImpl.defaultInstance().getCodingSchemeConcepts("NCI Thesaurus", null);
        set.restrictToCodes(Constructors.createConceptReferenceList("C61410"));
        List<ResolvedConceptReference> list = ngr.getAssociatedConcepts(
                set, 
                Direction.SOURCE_OF,
                -1, 
                Constructors.createNameAndValueList("association", "subClassOf"));
        list.forEach(x -> System.out.println(
                "Code: " + x.getCode() + 
                " Description: " + 
                        x.getEntityDescription().getContent()));
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Example: ScoreTerm \"some term to evaluate\"");
            System.out.println("Example: ScoreTerm \"some term to evaluate\" 25%");
            return;
        }
        String uri = args[0];
        if (args.length  == 1) {
       try {
        new GraphNodeExtensionMethods().run(uri);
    } catch (LBException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
        }
    }

}
