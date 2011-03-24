package org.cts2.profile.read;

import org.cts2.map.MapVersion;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;

public interface MapVersionReadService extends BaseReadService<MapVersion> {
	public MapVersion read(NameOrURI mapVersion, QueryControl queryContro,
			ReadContext readContext);

	public boolean exists(NameOrURI mapVersion, ReadContext context);

	public MapVersion readMapVersionForMap(NameOrURI map, NameOrURI tag,
			QueryControl queryControl, ReadContext context);

	public MapVersion existsMapVersionForMap(NameOrURI map, NameOrURI tag,
			ReadContext context);

	public boolean existsExternalId(NameOrURI map, String externalIdentifer,
			ReadContext context);

	public MapVersion getMapVersionByExternalId(NameOrURI map,
			String externalIdentifier, QueryControl queryControl,
			ReadContext context);
	
}
