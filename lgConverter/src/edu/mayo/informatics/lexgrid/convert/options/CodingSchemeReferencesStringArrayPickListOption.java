
package edu.mayo.informatics.lexgrid.convert.options;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;

/**
 * The Class StringOption.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodingSchemeReferencesStringArrayPickListOption extends StringOption{
 
    /**
     * Instantiates a new string option.
     * 
     * @param optionName the option name
     */
    public CodingSchemeReferencesStringArrayPickListOption(String optionName, CodingSchemeRenderingList schemes) {
        super(optionName);
        this.setPickList(buildPickList(schemes));
 
    }
    
    private static List<String> buildPickList(CodingSchemeRenderingList schemes){
        List<String> returnList = new ArrayList<String>();
        for(CodingSchemeRendering csr : schemes.getCodingSchemeRendering()) {
            returnList.add(
                    buildOptionValue(
                            csr.getCodingSchemeSummary().getCodingSchemeURI(),
                            csr.getCodingSchemeSummary().getRepresentsVersion()));
        }
        
        return returnList;
    }
    
    public static String buildOptionValue(String uri, String version){  
        return uri + " - " + version;
    }
    
    public static AbsoluteCodingSchemeVersionReference getAbsoluteCodingSchemeVersionReference(String value, CodingSchemeRenderingList schemes) throws LBParameterException{
        List<String> possibilities = buildPickList(schemes);
        
        for(int i=0;i<possibilities.size();i++) {
            if(possibilities.get(i).equals(value)) {
                AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
                ref.setCodingSchemeURN(schemes.getCodingSchemeRendering(i).getCodingSchemeSummary().getCodingSchemeURI());
                ref.setCodingSchemeVersion(schemes.getCodingSchemeRendering(i).getCodingSchemeSummary().getRepresentsVersion());
            
                return ref;
            }
        }
        
        throw new LBParameterException("Could not find a coding scheme for value: ", value);
    }
}