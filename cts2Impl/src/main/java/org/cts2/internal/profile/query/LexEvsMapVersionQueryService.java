package org.cts2.internal.profile.query;

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
	public MapVersionDirectory resolve(MapVersionDirectoryURI directory,
			QueryControl queryControl, ReadContext readContext) {
		return directory.get(queryControl, readContext,
				MapVersionDirectory.class);
	}

	@Override
	public MapVersionList resolveAsList(MapVersionDirectoryURI directory,
			QueryControl queryControl, ReadContext readContext) {
		return directory.get(queryControl, readContext, MapVersionList.class);
	}

	@Override
	public MapVersionDirectoryURI restrictToCodeSystems(MapVersionDirectoryURI directory,
			NameOrURIList codeSystems) {
		// TODO Auto-generated method stub
		return null;
	}

}
