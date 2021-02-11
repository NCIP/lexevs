
package org.LexGrid.LexBIG.Impl.bugs;


import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import junit.framework.TestCase;

public class GForge22296 extends TestCase 
{
    public void testConceptWithParanthesis() throws LBException 
    {
    	LexBIGService service = ServiceHolder.instance().getLexBIGService();

        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion("1.0");
        
        try 
        {
            CodedNodeSet nodeSet = service.getCodingSchemeConcepts("Automobiles", versionOrTag);
            
            nodeSet.restrictToCodes(Constructors.createConceptReferenceList("C0011(5564)"));
            
            ResolvedConceptReferenceList resolvedCodeList = nodeSet.resolveToList(null, null, null, 1);
            
            ResolvedConceptReference resolvedCode = resolvedCodeList.getResolvedConceptReference()[0];
            
            assertNotNull(resolvedCode);
        
        } catch (LBInvocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (LBParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}