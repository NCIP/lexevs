package org.lexevs.cts2.query;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.relations.AssociationEntity;
import org.apache.commons.lang.StringUtils;

public class CodeSystemQueryOperationImpl implements CodeSystemQueryOperation {
	public CodeSystemQueryOperationImpl() {
		
	}

	@Override
	public AssociationEntity getAssociationTypeDetails(String codingSchemeName,
			CodingSchemeVersionOrTag versionOrTag, String associationName) {
		try {
			String sacodingSchemeName = null;
			String code = null;
			String namespace = null;
			for(SupportedAssociation assoc : this.listAssociationTypes(codingSchemeName, versionOrTag)) {
				if(assoc.getContent().equals(associationName)) {
					if (assoc.getCodingScheme() == null)
						sacodingSchemeName = codingSchemeName;
					else
						sacodingSchemeName = assoc.getCodingScheme();
					if (assoc.getEntityCode() == null) 
						code = associationName;
					else
						code = assoc.getEntityCode();
					if (assoc.getEntityCodeNamespace() == null)
						namespace = codingSchemeName;
					else
						namespace = assoc.getEntityCodeNamespace();
				}
			}
			
			CodedNodeSet cns = LexBIGServiceImpl.defaultInstance().getNodeSet(sacodingSchemeName, versionOrTag, Constructors.createLocalNameList("association"));
		
			cns = cns.restrictToCodes(Constructors.createConceptReferenceList(code, namespace, sacodingSchemeName));
			ResolvedConceptReferencesIterator resolvedConList = cns.resolve(null, null, null);
			if (resolvedConList.hasNext()) {
				Object entity = cns.resolve(null, null, null).next().getEntity();
				if (entity instanceof AssociationEntity)
					return (AssociationEntity)entity;
				else
					throw new LBException("Now valid Association Entity found");
			}
		} catch (LBException e) {
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
			e.printStackTrace();
		} catch (LBParameterException e) {
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
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<SupportedAssociation> listAssociationTypes(String codingSchemeName, CodingSchemeVersionOrTag versionOrTag) {
		CodingScheme cs = this.getCodeSystemDetails(codingSchemeName, versionOrTag);
		return cs.getMappings().getSupportedAssociationAsReference();
	}

	@Override
	public ResolvedConceptReferencesIterator listCodeSystemConcepts(
			String codingSchemeName, CodingSchemeVersionOrTag versionOrTag, LocalNameList entityTypes, SortOptionList sortOptionList) {
		try {
			CodedNodeSet cns = LexBIGServiceImpl.defaultInstance().getNodeSet(codingSchemeName, versionOrTag, entityTypes);
			return cns.resolve(sortOptionList, null, null, null);
		} catch (LBException e) {
			e.printStackTrace();
		}
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
				if (queryByExample.getCodingSchemeURI() != null
						&& StringUtils.equals(css.getCodingSchemeURI(),
								queryByExample.getCodingSchemeURI()) == false)
					continue;

				if (queryByExample.getRepresentsVersion() != null
						&& StringUtils.equals(css.getRepresentsVersion(),
								queryByExample.getRepresentsVersion()) == false)
					continue;

				if (queryByExample.getFormalName() != null
						&& StringUtils.equals(css.getFormalName(),
								queryByExample.getFormalName()) == false)
					continue;

				if (queryByExample.getLocalName() != null
						&& StringUtils.equals(css.getLocalName(),
								queryByExample.getLocalName()) == false)
					continue;
				
				csList.addCodingSchemeRendering(csr);
			}

		} catch (LBInvocationException e) {
			e.printStackTrace();
		}
		return csList;
	}
}
