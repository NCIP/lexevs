package org.cts2.internal.profile.read;

import org.cts2.map.MapVersion;
import org.cts2.profile.read.MapVersionReadService;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;

public class LexEvsMapVersionReadService extends
		AbstractBaseReadService<MapVersion> implements MapVersionReadService {

	@Override
	public MapVersion read(NameOrURI mapVersion, QueryControl queryContro,
			ReadContext readContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(NameOrURI mapVersion, ReadContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MapVersion readMapVersionForMap(NameOrURI map, NameOrURI tag,
			QueryControl queryControl, ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MapVersion existsMapVersionForMap(NameOrURI map, NameOrURI tag,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsExternalId(NameOrURI map, String externalIdentifer,
			ReadContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MapVersion getMapVersionByExternalId(NameOrURI map,
			String externalIdentifier, QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

}
