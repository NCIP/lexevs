package org.cts.test;

import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.SortDescriptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.SortContext;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Query.Filter;
import org.LexGrid.LexBIG.Extensions.Query.Sort;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;
import org.LexGrid.codingSchemes.CodingScheme;

public class LexBIGServiceNoOp implements LexBIGService{

	private static final long serialVersionUID = 1L;

	@Override
	public CodedNodeSet getCodingSchemeConcepts(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return null;
	}

	@Override
	public CodedNodeSet getCodingSchemeConcepts(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, boolean activeOnly)
			throws LBException {
		return null;
	}

	@Override
	public Filter getFilter(String name) throws LBException {
		return null;
	}

	@Override
	public ExtensionDescriptionList getFilterExtensions() {
		return null;
	}

	@Override
	public GenericExtension getGenericExtension(String name) throws LBException {
		return null;
	}

	@Override
	public ExtensionDescriptionList getGenericExtensions() {
		return null;
	}

	@Override
	public HistoryService getHistoryService(String codingScheme)
			throws LBException {
		return null;
	}

	@Override
	public Date getLastUpdateTime() throws LBInvocationException {
		return null;
	}

	@Override
	public ModuleDescriptionList getMatchAlgorithms() {
		return null;
	}

	@Override
	public CodedNodeGraph getNodeGraph(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String relationContainerName)
			throws LBException {
		return null;
	}

	@Override
	public CodedNodeSet getNodeSet(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, LocalNameList entityTypes)
			throws LBException {
		return null;
	}

	@Override
	public LexBIGServiceManager getServiceManager(Object credentials)
			throws LBException {
		return null;
	}

	@Override
	public LexBIGServiceMetadata getServiceMetadata() throws LBException {
		return null;
	}

	@Override
	public Sort getSortAlgorithm(String name) throws LBException {
		return null;
	}

	@Override
	public SortDescriptionList getSortAlgorithms(SortContext context) {
		return null;
	}

	@Override
	public CodingSchemeRenderingList getSupportedCodingSchemes()
			throws LBInvocationException {
		return null;
	}

	@Override
	public CodingScheme resolveCodingScheme(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return null;
	}

	@Override
	public String resolveCodingSchemeCopyright(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return null;
	}

}
