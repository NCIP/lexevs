package org.cts2.internal.profile;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;

public class ProfileUtils {

	public static CodedNodeSet unionAll(LexBIGService lexBigService) throws LBException {
		CodedNodeSet cns = null;
		for(CodingSchemeRendering csr : lexBigService.getSupportedCodingSchemes().getCodingSchemeRendering()){
			CodedNodeSet newCns = lexBigService.getNodeSet(
					csr.getCodingSchemeSummary().getCodingSchemeURI(),
					Constructors.createCodingSchemeVersionOrTagFromVersion(csr.getCodingSchemeSummary().getRepresentsVersion()),
					null);
			
			if(cns == null){
				cns = newCns;
			} else {
				cns = cns.union(newCns);
			}
		}
		
		return cns;
	}
}
