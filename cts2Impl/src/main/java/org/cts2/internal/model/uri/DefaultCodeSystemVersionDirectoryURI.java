package org.cts2.internal.model.uri;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.cts2.core.Directory;
import org.cts2.core.FilterComponent;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.CodeSystemVersionDirectoryURI;

public class DefaultCodeSystemVersionDirectoryURI extends AbstractResolvingDirectoryURI<CodeSystemVersionDirectoryURI> implements CodeSystemVersionDirectoryURI{
	
	private CodingSchemeRenderingList codingSchemeRenderingList;
	
	private BeanMapper beanMapper;

	public DefaultCodeSystemVersionDirectoryURI(
			CodingSchemeRenderingList codingSchemeRenderingList,
			BeanMapper beanMapper) {
		super();
		this.codingSchemeRenderingList = codingSchemeRenderingList;
		this.beanMapper = beanMapper;
	}

	@Override
	protected void doRestrict(FilterComponent filterComponent) {
		// TODO Auto-generated method stub
	}

	@Override
	protected int doCount(ReadContext readContext) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected <D extends Directory<?>> D doGet(
			NameOrURI format,
			Long maxToReturn, 
			ReadContext readContext, 
			Class<D> resolveClass) {
		return this.beanMapper.map(this.codingSchemeRenderingList, resolveClass);
	}
}
