package org.cts2.internal.profile.query;

import org.cts2.core.Filter;
import org.cts2.map.MapVersionDirectory;
import org.cts2.map.MapVersionList;
import org.cts2.profile.query.MapVersionQueryService;
import org.cts2.service.core.NameOrURIList;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.MapVersionDirectoryURI;

public class LexEvsMapVersionQueryService extends
		AbstractBaseQueryService<MapVersionDirectoryURI> implements
		MapVersionQueryService {

	@Override
	public int count(MapVersionDirectoryURI directoryUri,
			ReadContext readContext) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MapVersionDirectoryURI restrict(MapVersionDirectoryURI restrictable,
			Filter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MapVersionDirectoryURI union(MapVersionDirectoryURI directoryUri1,
			MapVersionDirectoryURI directoryUri2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MapVersionDirectoryURI intersect(
			MapVersionDirectoryURI directoryUri1,
			MapVersionDirectoryURI directoryUri2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MapVersionDirectoryURI difference(
			MapVersionDirectoryURI directoryUri1,
			MapVersionDirectoryURI directoryUri2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MapVersionDirectory resolve(MapVersionDirectoryURI directory,
			QueryControl queryControl, ReadContext readContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MapVersionList resolveAsList(MapVersionDirectoryURI directory,
			QueryControl queryControl, ReadContext readContex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void restrictToCodeSystems(MapVersionDirectoryURI directory,
			NameOrURIList codeSystems) {
		// TODO Auto-generated method stub
		
	}

}
