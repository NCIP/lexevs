package org.lexevs.cts2.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.LexGrid.commonTypes.types.descriptors.EntityTypesDescriptor;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.relations.AssociationEntity;
import org.apache.commons.lang.StringUtils;
import org.hibernate.type.EntityType;
import org.lexevs.cts2.BaseService;

public class CodeSystemQueryOperationImpl implements CodeSystemQueryOperation {

	@Override
	public AssociationEntity getAssociationTypeDetails(String codingSchemeName,
			CodingSchemeVersionOrTag versionOrTag, String associationName) {
		try {
			LexBIGServiceImpl.defaultInstance().getCodingSchemeConcepts(codingSchemeName, versionOrTag);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public CodingScheme getCodeSystemDetails(String codingSchemeName,
			CodingSchemeVersionOrTag versionOrTag) {
		try {
			return LexBIGServiceImpl.defaultInstance().resolveCodingScheme(codingSchemeName, versionOrTag);
		} catch (LBInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LBParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Entity getConceptDetails(String codingSchemeName,
			CodingSchemeVersionOrTag versionOrTag, String code, String namespace) {
		try {
			LocalNameList lnl = new LocalNameList();
			lnl.addEntry(EntityTypes.CONCEPT.toString());
			CodedNodeSet cns = LexBIGServiceImpl.defaultInstance().getNodeSet(codingSchemeName, versionOrTag, lnl);
			ResolvedConceptReferencesIterator conceptIterator = cns.resolve(null, null, null, null, true);
			while (conceptIterator.hasNext()) {
				ResolvedConceptReference conceptRef = conceptIterator.next();
				if (StringUtils.equals(code, conceptRef.getCode()) && StringUtils.equals(namespace, conceptRef.getCodeNamespace()))
					return conceptRef.getEntity();
			}
			
			
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<SupportedAssociation> listAssociationTypes(String codingSchemeName, CodingSchemeVersionOrTag versionOrTag) {
		List<SupportedAssociation> associationList = new ArrayList<SupportedAssociation>();
		CodingScheme cs = this.getCodeSystemDetails(codingSchemeName, versionOrTag);
		return cs.getMappings().getSupportedAssociationAsReference();
	}

	@Override
	public Iterator<ResolvedConceptReference> listCodeSystemConcepts(
			CodedNodeSet cns) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodingSchemeRenderingList listCodeSystems(
			CodingSchemeSummary queryByExample) {
		CodingSchemeRenderingList csList = new CodingSchemeRenderingList();
		try {
			for (CodingSchemeRendering csr : LexBIGServiceImpl
					.defaultInstance().getSupportedCodingSchemes()
					.getCodingSchemeRendering()) {
				CodingSchemeSummary css = csr.getCodingSchemeSummary();
				if (css.getCodingSchemeURI() != null
						&& StringUtils.equals(css.getCodingSchemeURI(),
								queryByExample.getCodingSchemeURI()) == false)
					continue;

				if (css.getRepresentsVersion() != null
						&& StringUtils.equals(css.getRepresentsVersion(),
								queryByExample.getRepresentsVersion()) == false)
					continue;

				if (css.getFormalName() != null
						&& StringUtils.equals(css.getFormalName(),
								queryByExample.getFormalName()) == false)
					continue;

				if (css.getLocalName() != null
						&& StringUtils.equals(css.getLocalName(),
								queryByExample.getLocalName()) == false)
					continue;
				
				csList.addCodingSchemeRendering(csr);
			}

		} catch (LBInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return csList;
	}
}
