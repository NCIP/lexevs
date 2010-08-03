package org.lexevs.cts2.query;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.apache.commons.lang.StringUtils;

public class AssociationQueryOperationImpl implements AssociationQueryOperation {

	@Override
	public boolean computeSubsumptionRelationship(String codingSystemName,
			CodingSchemeVersionOrTag versionOrTag, String associationtype,
			ConceptReference parentCode, ConceptReference childCode) {
		try {
			if (StringUtils.equals(parentCode.getCodeNamespace(), childCode
					.getCodeNamespace()) == false
					|| StringUtils.equals(parentCode.getCodingSchemeName(),
							childCode.getCodingSchemeName()) == false) {
				throw new LBParameterException(
						"Does not support different coding systems subsumes");
			} else {
				CodedNodeGraph cng = LexBIGServiceImpl.defaultInstance()
						.getNodeGraph(codingSystemName, versionOrTag, null);
				return cng.areCodesRelated(Constructors.createNameAndValue(
						associationtype, null), parentCode, childCode, false);
			}
		} catch (LBParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LBInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LBResourceUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public ResolvedConceptReference determineTransitiveConceptRelationship(
			String codingSystemUri, CodingSchemeVersionOrTag versionOrTag,
			String relationContainerName, String associationName,
			String sourceCode, String sourceNS, String targetCode, String targetNS) {
		
			LexBIGServiceConvenienceMethods lbscm;
			try {
				lbscm = (LexBIGServiceConvenienceMethods) LexBIGServiceImpl
						.defaultInstance().getGenericExtension(
								"LexBIGServiceConvenienceMethods");
				return lbscm.getNodesPath(codingSystemUri, versionOrTag, relationContainerName, associationName, sourceCode, sourceNS, targetCode, targetNS);
			} catch (LBParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LBInvocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return null;
	}

	@Override
	public void getAssociationDetails() {
		// TODO Auto-generated method stub

	}

	@Override
	public ResolvedConceptReferenceList listAssociations(
			String codingSystemName, CodingSchemeVersionOrTag versionOrTag,
			String namespace, String code, String associationName,
			boolean isBackward, int depth, int maxToReturn) {
		ResolvedConceptReferenceList list = new ResolvedConceptReferenceList();

		try {
			CodedNodeGraph cng = LexBIGServiceImpl.defaultInstance()
					.getNodeGraph(codingSystemName, versionOrTag, null);
			ConceptReference conRef = new ConceptReference();
			conRef.setCode(code);
			conRef.setCodeNamespace(namespace);
			conRef.setCodingSchemeName(codingSystemName);
			conRef.setConceptCode(code);

			if (StringUtils.isEmpty(associationName) == false) {
				cng = cng.restrictToAssociations(Constructors
						.createNameAndValueList(associationName), null);
			}
			return cng.resolveAsList(conRef, !(isBackward), isBackward, -1,
					depth, null, null, null, null, maxToReturn);
		} catch (LBParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LBInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LBResourceUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
